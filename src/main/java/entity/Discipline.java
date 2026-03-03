package entity;

import enums.ActivityType;
import enums.DisciplineName;
import events.DisciplineEvent;
import events.DisciplineListener;

import java.util.*;

public class Discipline {
    private static final int MIN_HOURS = 64;

    private final DisciplineName name;
    private final Group group;
    private final List<Activity> activities;
    private final List<Teacher> teachers;
    private final List<DisciplineListener> listeners;
    private boolean active;

    public Discipline(DisciplineName name, Group group) {
        this.name = name;
        this.group = group;
        this.activities = new ArrayList<>();
        this.teachers = new ArrayList<>();
        this.listeners = new ArrayList<>();
        this.active = false;
    }

    public DisciplineName getName() {
        return name;
    }

    public Group getGroup() {
        return group;
    }

    public List<Activity> getActivities() {
        return Collections.unmodifiableList(activities);
    }

    public List<Teacher> getTeachers() {
        return Collections.unmodifiableList(teachers);
    }

    public boolean isActive() {
        return active;
    }

    public void addListener(DisciplineListener listener) {
        listeners.add(listener);
    }

    public void removeListener(DisciplineListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(DisciplineEvent event) {
        for (DisciplineListener listener : listeners) {
            listener.onEvent(event);
        }
    }

    public void addActivity(Activity activity) {
        Set<ActivityType> allowed = DisciplineConfig.getAllowedActivities(name);
        if (!allowed.contains(activity.getType())) {
            throw new IllegalArgumentException(
                    "Активність " + activity.getType().getDisplayName() +
                            " не дозволена для дисципліни " + name.getDisplayName());
        }
        activities.add(activity);
        notifyListeners(new DisciplineEvent(DisciplineEvent.Type.ACTIVITY_ADDED,
                "Додано активність: " + activity));
    }

    public void removeActivity(ActivityType type) {
        activities.removeIf(a -> a.getType() == type);
        notifyListeners(new DisciplineEvent(DisciplineEvent.Type.ACTIVITY_REMOVED,
                "Видалено активність: " + type.getDisplayName()));
    }

    public void changeActivityTeacher(ActivityType type, Teacher newTeacher) {
        activities.stream()
                .filter(a -> a.getType() == type)
                .forEach(a -> {
                    Teacher old = a.getTeacher();
                    a.setTeacher(newTeacher);
                    if (!teachers.contains(newTeacher)) {
                        addTeacher(newTeacher);
                    }
                    notifyListeners(new DisciplineEvent(DisciplineEvent.Type.TEACHER_CHANGED,
                            "Викладач " + type.getDisplayName() + " змінено: " + old.getName() + " → " + newTeacher.getName()));
                });
    }

    public void addTeacher(Teacher teacher) {
        int maxTeachers = group.getSubgroups().size() + 1;
        if (teachers.size() >= maxTeachers) {
            throw new IllegalStateException("Перевищено максимальну кількість викладачів (" + maxTeachers + ")");
        }
        if (teacher.isBusy() && teacher.getCurrentDiscipline() != this) {
            throw new IllegalStateException(
                    "Викладач " + teacher.getName() + " вже веде дисципліну: " +
                            teacher.getCurrentDiscipline().getName().getDisplayName());
        }
        teachers.add(teacher);
        teacher.assignToDiscipline(this);
    }

    public void removeTeacher(Teacher teacher) {
        if (teachers.size() <= 1) {
            throw new IllegalStateException("Дисципліна повинна мати щонайменше одного викладача");
        }
        teachers.remove(teacher);
        teacher.releaseDiscipline();
    }

    public int getTotalHours() {
        return activities.stream().mapToInt(Activity::getHours).sum();
    }

    public boolean hasEnoughHours() {
        return getTotalHours() >= MIN_HOURS;
    }

    public void start() {
        if (!DisciplineConfig.isCourseAllowed(name, group.getCourseYear())) {
            throw new IllegalStateException(
                    "Дисципліна '" + name.getDisplayName() + "' не може вивчатися на " +
                            group.getCourseYear() + " курсі");
        }
        if (group.hasStudiedDiscipline(name)) {
            throw new IllegalStateException(
                    "Група '" + group.getName() + "' вже вивчала дисципліну '" + name.getDisplayName() + "'");
        }
        if (!group.allStudentsHaveComputers()) {
            throw new IllegalStateException(
                    "Не всі студенти мають комп'ютер/ноутбук. Вивчення дисципліни неможливе.");
        }
        if (!hasEnoughHours()) {
            throw new IllegalStateException(
                    "Недостатньо аудиторних годин: " + getTotalHours() + " (мінімум " + MIN_HOURS + ")");
        }
        if (teachers.isEmpty()) {
            throw new IllegalStateException("Дисципліна не має жодного викладача");
        }
        this.active = true;
        group.markDisciplineAsStudied(name);
        notifyListeners(new DisciplineEvent(DisciplineEvent.Type.STARTED,
                "Дисципліна '" + name.getDisplayName() + "' розпочата для групи " + group.getName()));
    }

    public void conductActivity(ActivityType type) {
        if (!active) {
            throw new IllegalStateException("Дисципліна не активна");
        }
        Activity activity = activities.stream()
                .filter(a -> a.getType() == type)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Активність " + type.getDisplayName() + " не налаштована для цієї дисципліни"));

        // Validate prerequisites
        validateActivityPrerequisites(type);

        notifyListeners(new DisciplineEvent(DisciplineEvent.Type.ACTIVITY_CONDUCTED,
                type.getDisplayName() + " проведено. Викладач: " + activity.getTeacher().getName()));
    }

    private void validateActivityPrerequisites(ActivityType type) {
        if (type == ActivityType.MODULE_CONTROL || type == ActivityType.EXAM) {
            boolean anySubmitted = group.getStudents().stream().anyMatch(Student::hasSubmittedWork);
            if (!anySubmitted) {
                throw new IllegalStateException(
                        "Жоден студент не здав роботи – проведення " + type.getDisplayName() + " неможливе");
            }
        }
        if (type == ActivityType.CREDIT) {
            if (name != DisciplineName.BASICS_OF_PROGRAMMING) {
                throw new IllegalStateException("Залік передбачений тільки для 'Основ програмування'");
            }
        }
    }

    @Override
    public String toString() {
        return name.getDisplayName() + " [Група: " + group.getName() +
                ", Годин: " + getTotalHours() + ", Активний: " + active + "]";
    }
}