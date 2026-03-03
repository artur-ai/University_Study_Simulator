package entity;

import enums.DisciplineName;

import java.util.*;

public class Group {
    private final String name;
    private final int courseYear;
    private final List<Student> students;
    private final List<Subgroup> subgroups;
    private final Set<DisciplineName> completedDisciplines;

    public Group(String name, int courseYear, List<Student> students) {
        this.name = name;
        this.courseYear = courseYear;
        this.students = new ArrayList<>(students);
        this.subgroups = new ArrayList<>();
        this.completedDisciplines = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public int getCourseYear() {
        return courseYear;
    }

    public List<Student> getStudents() {
        return Collections.unmodifiableList(students);
    }

    public List<Subgroup> getSubgroups() {
        return Collections.unmodifiableList(subgroups);
    }

    public void addSubgroup(Subgroup subgroup) {
        subgroups.add(subgroup);
    }

    public boolean hasStudiedDiscipline(DisciplineName disciplineName) {
        return completedDisciplines.contains(disciplineName);
    }

    public void markDisciplineAsStudied(DisciplineName disciplineName) {
        completedDisciplines.add(disciplineName);
    }

    public boolean allStudentsHaveComputers() {
        return students.stream().allMatch(Student::hasComputer);
    }

    @Override
    public String toString() {
        return name + " (" + courseYear + " курс, " + students.size() + " студентів)";
    }
}