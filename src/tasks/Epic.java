package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Subtask> subtasksList = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(int id, String name, String description) {
        super(id, name, description);

    }

    public ArrayList<Subtask> getSubtasksList() {
        return subtasksList;
    }

    public void setSubtasksList(ArrayList<Subtask> subtasksList) {
        this.subtasksList = subtasksList;
    }

    public void addSubtasksList(Subtask subtask) {
        subtasksList.add(subtask);
    }

    public void removeSubtasksList(Subtask subtask) {
        subtasksList.remove(subtask);
    }

    public void clearSubtasksList() {
        subtasksList.clear();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", subtasksList=" + subtasksList +
                '}';
    }
}
