package events;

@FunctionalInterface
public interface DisciplineListener {
    void onEvent(DisciplineEvent event);
}