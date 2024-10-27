package tasks;

import status.Status;
import status.TypesOfTasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends AbstractTask {
    private int subId;

    public Subtask(int id, String name, String description, Status status, Duration duration, LocalDateTime startTime,
                   int subId) {
        super(id, name, description, status, duration, startTime);
        this.subId = subId;

    }

    //для тестирования
    public Subtask(String name, String description, Status status, Duration duration, LocalDateTime startTime, int subId) {
        super(name, description, status, duration, startTime);
        this.subId = subId;
    }

    public Subtask(int id, String name, String description, Status status, Duration duration, LocalDateTime startTime) {
        super(id, name, description, status, duration, startTime);
    }


    public int getSubId() {
        return subId;
    }

    @Override
    public TypesOfTasks getType() {
        return TypesOfTasks.SUBTASK;
    }



    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", duration=" + duration.toMinutesPart() +
                ", startTime=" + startTime.format(formatter) +
                ", subId=" + subId +
                '}';
    }
}
