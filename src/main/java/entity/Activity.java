package entity;

import enums.ActivityType;

public class Activity {
    private final ActivityType type;
    private int hours;
    private Teacher teacher;

    public Activity(ActivityType type, int hours, Teacher teacher) {
        this.type = type;
        this.hours = hours;
        this.teacher = teacher;
    }

    public ActivityType getType() { return type; }
    public int getHours() { return hours; }
    public Teacher getTeacher() { return teacher; }

    public void setHours(int hours) {
        if (hours <= 0) throw new IllegalArgumentException("Кількість годин має бути більше 0");
        this.hours = hours;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    @Override
    public String toString() {
        return type.getDisplayName() + " (" + hours + " год, викладач: " + teacher.getName() + ")";
    }
}
