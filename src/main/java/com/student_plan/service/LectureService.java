package com.student_plan.service;


import com.student_plan.entity.Lecture;
import com.student_plan.expections.NotFoundException;
import com.student_plan.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepository;

    public List<Lecture> getAllLectures(){
        return lectureRepository.findAll();
    }

    public Lecture getLectureById(long id){
        return lectureRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Lecture [id="+id+"] not found"));
    }

    public Lecture saveNewLecture(Lecture lecture){
        return lectureRepository.save(lecture);
    }

    public void deleteLectureById(long id){
        lectureRepository.findById(id).orElseThrow(()-> new NotFoundException("Lecture [id="+id+"] not found"));
        lectureRepository.delete(getLectureById(id));
    }

    public Lecture updateLecture(long lectureId, String lectureName){
        Lecture lectureToUpdate = lectureRepository
                .findById(lectureId)
                .orElseThrow(() -> new NotFoundException("Lecture [id="+lectureId+"] not found"));

        updateLectureValues(lectureName,lectureToUpdate);
        return lectureRepository.save(lectureToUpdate);
    }

    private void updateLectureValues(String lectureName, Lecture lectureToUpdate) {
        if(lectureName != null)
            lectureToUpdate.setLectureName(lectureName);
    }
}
