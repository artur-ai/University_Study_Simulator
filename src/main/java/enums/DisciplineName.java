package enums;

public enum DisciplineName {
    BASICS_OF_PROGRAMMING("Основи програмування"),
    OOP("ООП"),
    ALGORITHMS_AND_DATA_STRUCTURES("Алгоритми та структури даних");

    private final String displayName;

    DisciplineName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
