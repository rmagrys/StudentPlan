package ModelCreator;

import com.student_plan.entity.Lecture;
import com.student_plan.entity.StudentLecture;
import com.student_plan.entity.User;

public class StudentLectureModelCreator {

    public static StudentLecture createStudentLecture(Lecture lecture, User user, boolean presence){


        return StudentLecture
                .builder()
                    .lecture(lecture)
                    .user(user)
                    .presence(presence)
                .build();
    }
}
