package service;

import entity.*;
import enums.ActivityType;
import enums.DisciplineName;

public class DisciplineService {

    public Discipline createDiscipline(DisciplineName name, Group group) {
        return new Discipline(name, group);
    }

    public void addActivity(Discipline discipline, ActivityType type, int hours, Teacher teacher) {
        Activity activity = new Activity(type, hours, teacher);
        discipline.addActivity(activity);
    }

    public void removeActivity(Discipline discipline, ActivityType type) {
        discipline.removeActivity(type);
    }

    public void changeTeacher(Discipline discipline, ActivityType activityType, Teacher newTeacher) {
        if (newTeacher.isBusy() && newTeacher.getCurrentDiscipline() != discipline) {
            throw new IllegalStateException(
                    "Викладач " + newTeacher.getName() + " зайнятий іншою дисципліною: " +
                            newTeacher.getCurrentDiscipline().getName().getDisplayName());
        }
        discipline.changeActivityTeacher(activityType, newTeacher);
    }

    public void startDiscipline(Discipline discipline) {
        discipline.start();
    }

    public void conductActivity(Discipline discipline, ActivityType type) {
        discipline.conductActivity(type);
    }

    public void submitStudentWork(Student student) {
        student.submitWork();
    }
}
