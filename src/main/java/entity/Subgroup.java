package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Subgroup {
    private static final int MIN_STUDENTS = 10;
    private final String name;
    private final List<Student> students;

    public Subgroup(String name, List<Student> students) {
        if (students.size() < MIN_STUDENTS) {
            throw new IllegalArgumentException(
                    "Підгрупа '" + name + "' має менше " + MIN_STUDENTS + " студентів (" + students.size() + ")");
        }
        this.name = name;
        this.students = new ArrayList<>(students);
    }

    public String getName() { return name; }
    public List<Student> getStudents() { return Collections.unmodifiableList(students); }
    public int size() { return students.size(); }

    @Override
    public String toString() {
        return name + " (" + students.size() + " студентів)";
    }
}
