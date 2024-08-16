package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import status.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InMemoryHistoryManagerTest {
    public TaskManager taskManager;
    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
    }

    @Test
    public void addHistory() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        historyManager.add(new Task("Переезд", "в 2 часа", Status.NEW));
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    public void addListOf10Tasks(){
        for(int i = 0; i < 15; i++){
            taskManager.addNewTask(new Task("Переезд", "в 2 часа", Status.NEW));
        }
        List<Task> tasks = taskManager.getTasks();
        for (Task task : tasks) {
            taskManager.getTaskByID(task.getId());
        }

        List<Task> historyTasks = taskManager.getHistory();
        assertEquals(10, historyTasks.size(), "Неверное количество элементов в истории ");
    }

    @Test
    public void getHistoryOldTaskAfterUpdate(){
        Task task1 = new Task("Переезд", "в 2 часа", Status.NEW);
        taskManager.addNewTask(task1);
        taskManager.getTaskByID(task1.getId());
        final List<Task> tasks = new ArrayList<>();
        tasks.add(taskManager.getTaskByID(task1.getId()));
        Task task2 = new Task(task1.getId(),"Прогулка", "в 4 часа", Status.DONE);
        taskManager.updateTask(task2);
        taskManager.getTaskByID(task2.getId());
        final List<Task> historyTasks = taskManager.getHistory();
        Task oldTaskHistory = historyTasks.getFirst();
        Task oldTask = tasks.getFirst();
        assertEquals(oldTask.getId(), oldTaskHistory.getId(), "Задачи по id не совпадают");
        assertEquals(oldTask.getName(), oldTaskHistory.getName(), "Задачи по name не совпадают");
        assertEquals(oldTask.getDescription(), oldTaskHistory.getDescription(), "Задачи по description не совпадают");
        assertEquals(oldTask.getStatus(), oldTaskHistory.getStatus(), "Задачи по status не совпадают");
    }

    @Test
    public void getHistoryOldEpicAfterUpdate(){
        Epic epic1 = new Epic("Приготовить ужин", "Купить продукты");
        taskManager.addNewEpic(epic1);
        taskManager.getEpicByID(epic1.getId());
        final List<Task> epics = new ArrayList<>();
        epics.add(taskManager.getEpicByID(epic1.getId()));
        Epic epic2 = new Epic(epic1.getId(),"Прогулка", "в 4 часа");
        taskManager.updateEpic(epic2);
        taskManager.getEpicByID(epic2.getId());
        final List<Task> historyEpics = taskManager.getHistory();
        Task oldEpicHistory = historyEpics.getFirst();
        Task oldEpic = epics.getFirst();
        assertEquals(oldEpic.getId(), oldEpicHistory.getId(), "Задачи по id не совпадают");
        assertEquals(oldEpic.getName(), oldEpicHistory.getName(), "Задачи по name не совпадают");
        assertEquals(oldEpic.getDescription(), oldEpicHistory.getDescription(), "Задачи по description не совпадают");
        assertEquals(oldEpic.getStatus(), oldEpicHistory.getStatus(), "Задачи по status не совпадают");
    }

    @Test
    public void getHistoryOldSubtaskAfterUpdate(){
        Epic epic1 = new Epic("Приготовить ужин", "Купить продукты");
        taskManager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("Приготовить крылышки", "Купить мёд и соевый соус", Status.IN_PROGRESS,
                epic1.getId());
        taskManager.addNewSubtask(subtask1);
        taskManager.getSubtaskByID(subtask1.getId());
        final List<Task> subtasks = new ArrayList<>();
        subtasks.add(taskManager.getSubtaskByID(subtask1.getId()));
        Subtask subtask2 = new Subtask(subtask1.getId(),"Прогулка", "в 4 часа", Status.NEW,
                epic1.getId() );
        taskManager.updateSubtask(subtask2);
        taskManager.getSubtaskByID(subtask2.getId());
        final List<Task> historySubtasks = taskManager.getHistory();
        Task oldSubtaskHistory = historySubtasks.getFirst();
        Task oldSubtask = subtasks.getFirst();
        assertEquals(oldSubtask.getId(), oldSubtaskHistory.getId(), "Задачи по id не совпадают");
        assertEquals(oldSubtask.getName(), oldSubtaskHistory.getName(), "Задачи по name не совпадают");
        assertEquals(oldSubtask.getDescription(), oldSubtaskHistory.getDescription(), "Задачи по description не совпадают");
        assertEquals(oldSubtask.getStatus(), oldSubtaskHistory.getStatus(), "Задачи по status не совпадают");
    }
}
