package ModelCreator;

import com.student_plan.dto.LectureParamsDto;
import com.student_plan.entity.Lecture;

import java.time.LocalDate;

public class LectureModelCreator {

   public static Lecture createLecture(final String lectureName, final LocalDate date){

        return  Lecture
                    .builder()
                        .lectureName(lectureName)
                        .date(date)
                    .build();
    }

   public static LectureParamsDto createLectureParamsDto(final String lectureName, final String day,
                                                   final String month, final String year){

        return LectureParamsDto
                .builder()
                    .lectureName(lectureName)
                    .day(day)
                    .month(month)
                    .year(year)
                .build();
    }
}


