package tasks;

public class Subtask extends Task {
    protected int subId;

    public Subtask(int id, String name, String description, Status status, int subId) {
        super(id, name, description, status);
        this.subId = subId;
    }

    //для тестирования
    public Subtask(String name, String description, Status status, int subId) {
        super(name, description, status);
        this.subId = subId;
    }


    public int getSubId() {
        return subId;
    }

    public void setSubId(int subId) {
        this.subId = subId;
    }


    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", subId=" + subId +
                '}';
    }
}
