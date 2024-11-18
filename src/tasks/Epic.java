package tasks;

import status.TypesOfTasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends AbstractTask {
    private ArrayList<Subtask> subtasks = new ArrayList<>();

    public LocalDateTime getEndTimeEpic() {
        return endTimeEpic;
    }

    public void setEndTimeEpic(LocalDateTime endTimeEpic) {
        this.endTimeEpic = endTimeEpic;
    }

    private LocalDateTime endTimeEpic;


    public Epic(int id, String name, String description, Duration duration, LocalDateTime startTime) {
        super(id, name, description, duration, startTime);
    }

    public Epic(String name, String description) {
        super(name, description);

    }

    public Epic(int id, String name, String description) {
        super(id, name, description);
    }

    public ArrayList<Subtask> getSubtasks() {
        if (subtasks == null) {
            return new ArrayList<>(); // Возвращаем пустой список, если подзадачи не инициализированы
        }
        return subtasks;
    }

    public void setSubtasks(ArrayList<Subtask> subtask) {
        this.subtasks = subtask;
    }

    public void addSubtasks(Subtask subtask) {
        subtasks.add(subtask);
    }

    public void removeSubtasks(Subtask subtask) {
        subtasks.remove(subtask);
    }

    public void clearSubtasks() {
        if (subtasks != null) {
            subtasks.clear();
        }
    }

    @Override
    public TypesOfTasks getType() {
        return TypesOfTasks.EPIC;
    }

    @Override
    public Duration getDuration() {
        int sumDuration = 0;
        for (Subtask sub: getSubtasks()) {
            sumDuration = sumDuration + sub.getDuration().toMinutesPart();
        }
        return Duration.ofMinutes(sumDuration);
    }

    private int getDurationToStr() {
        int sumDuration = 0;
        for (Subtask sub: getSubtasks()) {
            sumDuration = sumDuration + sub.getDuration().toMinutesPart();
        }
        return sumDuration;
    }

    private String getStartTimeToStr() {
        if (getStartTime() == null) {
            return "0";
        }
        return getStartTime().format(formatter);
    }


    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", duration=" + getDurationToStr() +
                ", startTime=" + getStartTimeToStr() +
                ", subtasksList=" + getSubtasks() +
                '}';
    }

}
