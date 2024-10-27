package manager;

import tasks.AbstractTask;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    List<Task> getTasks();

    List<Epic> getEpics();

    List<Subtask> getSubtasks();

    List<Subtask> getEpicSubtasks(Epic epic);

    ArrayList<AbstractTask> getPrioritizedTasks();

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

    List<AbstractTask> getHistory();
}
