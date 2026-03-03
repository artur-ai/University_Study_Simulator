package entity;

public class Teacher {
    private final String name;
    private Discipline currentDiscipline;

    public Teacher(String name) {
        this.name = name;
        this.currentDiscipline = null;
    }

    public String getName() {
        return name;
    }

    public boolean isBusy() {
        return currentDiscipline != null;
    }

    public Discipline getCurrentDiscipline() {
        return currentDiscipline;
    }

    public void assignToDiscipline(Discipline discipline) {
        this.currentDiscipline = discipline;
    }

    public void releaseDiscipline() {
        this.currentDiscipline = null;
    }

    @Override
    public String toString() {
        return name;
    }
}
