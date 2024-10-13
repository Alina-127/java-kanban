package tasks;

import status.Status;
import status.TypesOfTasks;


public abstract class AbstractTask {
    protected String name;
    protected String description;
    protected int id;
    protected Status status;
    protected TypesOfTasks type;


    public AbstractTask(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    // для тестирования
    public AbstractTask(int id, String name, String description, Status status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    // для наследования в эпик
    public AbstractTask(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    // для наследования в эпик
    public AbstractTask(String name, String description) {
        this.name = name;
        this.description = description;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


    public abstract TypesOfTasks getType();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractTask task = (AbstractTask) o;
        return id == task.id;
//        && Objects.equals(name, task.name) && Objects.equals(description, task.description) &&
//                Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        int hash = 17; // объявляем и инициализируем переменную hash
        if (name != null) { // проверяем значение первого поля
            hash = name.hashCode(); // вычисляем хеш первого поля
        }
        if (description != null) { // проверяем значение второго поля
            hash = hash + description.hashCode(); // вычисляем хеш второго поля и общий хеш
        }
        return hash;
    }
}
