package tasks;

import status.Status;
import status.TypesOfTasks;

public class Subtask extends AbstractTask {
    private int subId;

    public Subtask(int id, String name, String description, Status status, int subId) {
        super(id, name, description, status);
        this.subId = subId;
    }

    //для тестирования
    public Subtask(String name, String description, Status status, int subId) {
        super(name, description, status);
        this.subId = subId;
    }

    public Subtask(int id, String name, String description, Status status) {
        super(id, name, description, status);
    }


    public int getSubId() {
        return subId;
    }

    public void setSubId(int subId) {
        this.subId = subId;
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
                ", subId=" + subId +
                '}';
    }
}
