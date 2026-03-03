package events;

public class DisciplineEvent {
    public enum Type {
        STARTED,
        ACTIVITY_ADDED,
        ACTIVITY_REMOVED,
        ACTIVITY_CONDUCTED,
        TEACHER_CHANGED
    }

    private final Type type;
    private final String message;

    public DisciplineEvent(Type type, String message) {
        this.type = type;
        this.message = message;
    }

    public Type getType() { return type; }
    public String getMessage() { return message; }
}
