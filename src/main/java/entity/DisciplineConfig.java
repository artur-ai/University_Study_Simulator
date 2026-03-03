package entity;

import enums.ActivityType;
import enums.DisciplineName;

import java.util.Set;

public class DisciplineConfig {
    private static final int MAX_ALLOWED_COURSE = 2;

    public static Set<Integer> getAllowedCourses(DisciplineName name) {
        return switch (name) {
            case BASICS_OF_PROGRAMMING -> Set.of(1);
            case OOP -> Set.of(1, 2);
            case ALGORITHMS_AND_DATA_STRUCTURES -> Set.of(2);
        };
    }

    public static Set<ActivityType> getAllowedActivities(DisciplineName name) {
        return switch (name) {
            case BASICS_OF_PROGRAMMING -> Set.of(
                    ActivityType.LECTURE, ActivityType.LAB_WORK,
                    ActivityType.MODULE_CONTROL, ActivityType.EXAM, ActivityType.CREDIT
            );
            case OOP -> Set.of(
                    ActivityType.LECTURE, ActivityType.LAB_WORK,
                    ActivityType.COURSE_WORK, ActivityType.MODULE_CONTROL, ActivityType.EXAM
            );
            case ALGORITHMS_AND_DATA_STRUCTURES -> Set.of(
                    ActivityType.LECTURE, ActivityType.LAB_WORK,
                    ActivityType.COURSE_WORK, ActivityType.MODULE_CONTROL, ActivityType.EXAM
            );
        };
    }

    public static boolean isCourseAllowed(DisciplineName name, int courseYear) {
        return courseYear <= MAX_ALLOWED_COURSE && getAllowedCourses(name).contains(courseYear);
    }
}
