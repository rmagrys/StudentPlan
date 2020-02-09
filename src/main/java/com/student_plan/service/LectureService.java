package com.student_plan.service;


import com.student_plan.entity.Lecture;
import com.student_plan.entity.Type;
import com.student_plan.entity.User;
import com.student_plan.expections.BadRequestException;
import com.student_plan.expections.NotDeletedException;
import com.student_plan.expections.NotFoundException;
import com.student_plan.repository.LectureRepository;
import com.student_plan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepository;
    private final UserRepository userRepository;

    public List<Lecture> getAllLectures(){
        return lectureRepository.findAll();
    }

    public Optional<Lecture> getLectureById(long id){
        return lectureRepository
                .findById(id);
    }

    public Lecture saveNewLecture(@Valid Lecture lecture){
        return lectureRepository.save(lecture);
    }

    public void deleteLectureById(long id){
       Lecture lecture =  lectureRepository
                .findById(id)
                .orElseThrow(() ->
                    new NotFoundException("Lecture [id=" + id + "] not found")
                );

        if(!isLectureTookPlace(lecture)) {
             lectureRepository.deleteById(id);
        } else {
            throw new NotDeletedException("This Lecture actually took place, cannot be deleted");
        }
    }

    private boolean isLectureTookPlace(@Valid Lecture lecture) {
       LocalDate lectureDate = lecture.getDate();
       LocalDate actualDate = LocalDate.now();

       return lectureDate.isBefore(actualDate);

    }

    public Lecture updateLecture(long lectureId, String lectureName, @Valid LocalDate date) {
        Lecture lectureToUpdate = lectureRepository
                .findById(lectureId)
                .orElseThrow(() ->
                    new NotFoundException("Lecture [id=" + lectureId + "] not found")
                );

        updateLectureValues(lectureName,lectureToUpdate,date);

        return lectureRepository.save(lectureToUpdate);
    }

    private void updateLectureValues(String lectureName, Lecture lectureToUpdate, LocalDate date) {
        if(lectureName != null)
            lectureToUpdate.setLectureName(lectureName);
        if(date != null)
            lectureToUpdate.setDate(date);
    }

    public Long registerLecturerToLecture(long lectureId, long userId) {
        Lecture lecture = lectureRepository
                .findById(lectureId)
                .orElseThrow(() ->
                        new NotFoundException("Lecture [id=" + lectureId + "] not found")
                );

        User user = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new NotFoundException("User [id=" + userId + "] not found")
                );

        if(!isUserRoleLecturer(user) && isLectureOccupied(lecture)) {
            throw new BadRequestException("Cannot update lecturer to lecture");
        } else {
            return saveStatus(user, lecture);
        }
    }

    private boolean isLectureOccupied(Lecture lecture) {
        return lectureRepository.countLectureByIdAndLecturerIsNull(lecture.getId()) > 0;
    }

    private Long saveStatus(User user, @Valid Lecture lecture) {
         lecture.setLecturer(user);

        return lectureRepository.save(lecture).getId();

    }

    private boolean isUserRoleLecturer(User user) {
       return user.getType().equals(Type.LECTURER);
    }
}
