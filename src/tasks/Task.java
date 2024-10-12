package tasks;

import status.Status;
import status.TypesOfTasks;

public class Task extends AbstractTask{
    public Task(String name, String description, Status status) {
        super(name, description, status);
    }

    // для тестирования
    public Task(int id, String name, String description, Status status) {
        super(id, name, description, status);
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
                '}';
    }
}

