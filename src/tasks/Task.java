package tasks;

import status.Status;
import status.TypesOfTasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task extends AbstractTask {
    public Task(String name, String description, Status status, Duration duration, LocalDateTime startTime) {
        super(name, description, status, duration, startTime);
    }

    // для тестирования
    public Task(int id, String name, String description, Status status, Duration duration, LocalDateTime startTime) {
        super(id, name, description, status, duration, startTime);
    }

    @Override
    public TypesOfTasks getType() {
        return TypesOfTasks.TASK;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", duration=" + duration.toMinutesPart() +
                ", startTime=" + startTime.format(formatter) +
                '}';
    }

}

