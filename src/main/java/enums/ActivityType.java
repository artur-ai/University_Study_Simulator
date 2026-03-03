package enums;

public enum ActivityType {
    LECTURE("Лекція"),
    LAB_WORK("Лабораторна робота"),
    COURSE_WORK("Курсова робота"),
    MODULE_CONTROL("Модульна контрольна робота"),
    EXAM("Екзамен"),
    CREDIT("Залік");

    private final String displayName;

    ActivityType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
