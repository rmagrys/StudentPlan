package dtoConverters;

import com.student_plan.dto.LectureDto;
import com.student_plan.dto.LectureParamsDto;
import com.student_plan.entity.Lecture;

import java.time.LocalDate;

public class LectureDtoConverter {

    public static LectureDto toDto(Lecture lecture){

        LectureDto lectureDto = new LectureDto();

        lectureDto.setId(lecture.getId());
        lectureDto.setLectureName(lecture.getLectureName());
        lectureDto.setDate(lecture.getDate());

        return lectureDto;
    }

    public static Lecture toEntity(LectureDto lectureDto){

        Lecture lecture = new Lecture();

        lecture.setId(lectureDto.getId());
        lecture.setLectureName(lectureDto.getLectureName());
        lecture.setDate(lectureDto.getDate());

        return lecture;
    }

    public static Lecture toEntityWithParams(LectureParamsDto lectureParamsDto, LocalDate date){

        Lecture lecture = new Lecture();

        lecture.setId(lectureParamsDto.getId());
        lecture.setLectureName(lectureParamsDto.getLectureName());
        lecture.setDate(date);

        return lecture;
    }
}
