package entity;

public class Student {
    private final String name;
    private boolean hasSubmittedWork;
    private boolean hasComputer;

    public Student(String name, boolean hasComputer) {
        this.name = name;
        this.hasComputer = hasComputer;
        this.hasSubmittedWork = false;
    }

    public String getName() {
        return name;
    }

    public boolean hasSubmittedWork() {
        return hasSubmittedWork;
    }

    public boolean hasComputer() {
        return hasComputer;
    }

    public void submitWork() {
        this.hasSubmittedWork = true;
    }

    public void setHasComputer(boolean hasComputer) {
        this.hasComputer = hasComputer;
    }

    @Override
    public String toString() {
        return name;
    }
}