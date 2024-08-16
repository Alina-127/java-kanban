package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;

public interface TaskManager {
    ArrayList<Task> getTasks();

    ArrayList<Epic> getEpics();

    ArrayList<Subtask> getSubtasks();

    ArrayList<Subtask> getEpicSubtasks(Epic epic);

    void deleteTasks();

    void deleteEpics();

    void deleteSubtasks();

    Task getTaskByID(int id);

    Epic getEpicByID(int id);

    Subtask getSubtaskByID(int id);

    int getNewId();

    Task addNewTask(Task newTask);

    Epic addNewEpic(Epic newEpic);

    Subtask addNewSubtask(Subtask newSubtask);

    Task updateTask(Task updatedTask);

    Epic updateEpic(Epic updateEpic);

    Subtask updateSubtask(Subtask updateSubtask);

    Task deleteTaskById(Integer id);

    Epic deleteEpicById(Integer id);

    Subtask deleteSubtaskById(Integer id);

    ArrayList<Task> getHistory();
}
