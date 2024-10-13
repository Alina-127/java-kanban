package tasks;

import status.TypesOfTasks;

import java.util.ArrayList;

public class Epic extends AbstractTask {
    private ArrayList<Subtask> subtasks = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(int id, String name, String description) {
        super(id, name, description);

    }

    public ArrayList<Subtask> getSubtasks() {
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
        subtasks.clear();
    }

    @Override
    public TypesOfTasks getType() {
        return TypesOfTasks.EPIC;
    }


    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", subtasksList=" + subtasks +
                '}';
    }
}
