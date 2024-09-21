package manager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import status.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {
    public TaskManager taskManager;
    @BeforeEach
    public void beforeEach(){
        taskManager = Managers.getDefault();
    }
    @Test //проверьте, что InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id
    public void addNewTaskAndGetById(){
        Task task = new Task("Переезд", "в 2 часа", Status.NEW);
        final int taskId = taskManager.addNewTask(task).getId();

        final Task savedTask = taskManager.getTaskByID(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.getFirst(), "Задачи не совпадают.");

    }

    @Test //проверьте, что InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id
    public void addNewEpicAndGetById(){
        Epic epic = new Epic("Приготовить ужин", "Купить продукты");

        final int epicId = taskManager.addNewEpic(epic).getId();

        final Epic savedEpic = taskManager.getEpicByID(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");

        final List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.getFirst(), "Задачи не совпадают.");
    }

    @Test //проверьте, что InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id
    public void addNewSubtaskAndGetById(){
        Epic epic = new Epic("Приготовить ужин", "Купить продукты");
        taskManager.addNewEpic(epic);
        Subtask subtask = new Subtask("Купить овощи", "Огурцы, картошка", Status.IN_PROGRESS,
                epic.getId());

        final int subtaskId = taskManager.addNewSubtask(subtask).getId();

        final Subtask savedSubtask = taskManager.getSubtaskByID(subtaskId);

        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask, savedSubtask, "Задачи не совпадают.");

        final List<Subtask> subtasks = taskManager.getSubtasks();

        assertNotNull(subtasks, "Задачи не возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtask, subtasks.getFirst(), "Задачи не совпадают.");
    }
    @Test
    public void updateTaskShouldReturnTaskId(){
        Task task = new Task("Переезд", "в 2 часа", Status.NEW);
        taskManager.addNewTask(task);
        Task task2 = new Task(task.getId(),"Прогулка", "в 4 часа", Status.DONE);
        taskManager.updateTask(task2);
        assertEquals(task, task2, "Задачи не совпадают.");
    }

    @Test
    public void updateTaskIfNull(){
        Task task = new Task(1, "Переезд", "в 2 часа", Status.NEW);
        taskManager.addNewTask(task);
        Task task2 = new Task(2,"Прогулка", "в 4 часа", Status.DONE);
        assertNull(taskManager.updateTask(task2), "Задачи не совпадают.");
    }

    @Test
    public void updateEpicShouldReturnEpicId(){
        Epic epic = new Epic("Приготовить ужин", "Купить продукты");
        taskManager.addNewEpic(epic);
        Epic epic2 = new Epic(epic.getId(),"Переезд", "в 2 часа");
        taskManager.updateEpic(epic2);
        assertEquals(epic, epic2, "Задачи не совпадают.");
    }

    @Test
    public void updateEpicWithSubtask(){
        Epic epic = new Epic("Приготовить ужин", "Купить продукты");
        taskManager.addNewEpic(epic);
        Subtask subtask = new Subtask("Купить овощи", "Огурцы, картошка", Status.IN_PROGRESS,
                epic.getId());
        Subtask subtask2 = new Subtask("Купить машину", "Марк2", Status.DONE,
                epic.getId());
        taskManager.addNewSubtask(subtask2);
        Epic epic2 = new Epic(epic.getId(),"Переезд", "в 2 часа");
        taskManager.updateEpic(epic2);
        assertEquals(epic, epic2, "Задачи не совпадают.");
    }

    @Test
    public void updateEpicIfNull(){
        Epic epic = new Epic(1,"Приготовить ужин", "Купить продукты");
        taskManager.addNewEpic(epic);
        Epic epic2 = new Epic(2,"Переезд", "в 2 часа");
        assertNull(taskManager.updateEpic(epic2), "Задачи не совпадают.");
    }
    @Test
    public void updateSubtaskShouldReturnSubtaskId(){
        Epic epic = new Epic("Приготовить ужин", "Купить продукты");
        taskManager.addNewEpic(epic);
        Subtask subtask = new Subtask("Купить овощи", "Огурцы, картошка", Status.IN_PROGRESS,
                epic.getId());
        taskManager.addNewSubtask(subtask);
        Subtask subtask2 = new Subtask(subtask.getId(),"Переезд", "в 2 часа", Status.NEW,epic.getId());
        taskManager.updateSubtask(subtask2);
        assertEquals(subtask, subtask2, "Задачи не совпадают.");
    }
    @Test
    public void updateSubtaskIfNull(){
        Epic epic = new Epic("Приготовить ужин", "Купить продукты");
        taskManager.addNewEpic(epic);
        Subtask subtask = new Subtask(1,"Купить овощи", "Огурцы, картошка", Status.IN_PROGRESS,
                epic.getId());
        taskManager.addNewSubtask(subtask);
        Subtask subtask2 = new Subtask(2,"Переезд", "в 2 часа", Status.NEW,epic.getId());
        assertNull(taskManager.updateSubtask(subtask2), "Задачи не совпадают.");
    }

    @Test
    public void deleteByIdTask(){
        Task task = new Task("Переезд", "в 2 часа", Status.NEW);
        taskManager.addNewTask(task);
        Task task2 = new Task("Прогулка", "в 4 часа", Status.DONE);
        taskManager.addNewTask(task2);
        taskManager.deleteTaskById(task.getId());
        final List<Task> tasks = taskManager.getTasks();
        assertEquals(1, tasks.size(), "Задачи по id не удаляются");
    }

    @Test
    public void deleteByIdEpic(){
        Epic epic = new Epic("Приготовить ужин", "Купить продукты");
        taskManager.addNewEpic(epic);
        Subtask subtask = new Subtask("Купить овощи", "Огурцы, картошка", Status.IN_PROGRESS,
                epic.getId());
        taskManager.addNewSubtask(subtask);
        Epic epic2 = new Epic("Прогулка", "в 4 часа");
        taskManager.addNewEpic(epic2);
        taskManager.deleteEpicById(epic.getId());
        final List<Epic> epics = taskManager.getEpics();
        assertEquals(1, epics.size(), "Задачи по id не удаляются");
    }

    @Test
    public void deleteByIdSubtask(){
        Epic epic = new Epic("Приготовить ужин", "Купить продукты");
        taskManager.addNewEpic(epic);
        Subtask subtask = new Subtask("Купить овощи", "Огурцы, картошка", Status.IN_PROGRESS,
                epic.getId());
        taskManager.addNewSubtask(subtask);
        Subtask subtask2 = new Subtask("Переезд", "в 2 часа", Status.NEW,epic.getId());
        taskManager.addNewSubtask(subtask2);
        taskManager.deleteSubtaskById(subtask.getId());
        final List<Subtask> subtasks = taskManager.getSubtasks();
        assertEquals(1, subtasks.size(), "Задачи по id не удаляются");
    }

    @Test
    public void deleteAllTasks(){
        Task task = new Task("Переезд", "в 2 часа", Status.NEW);
        taskManager.addNewTask(task);
        Task task2 = new Task("Прогулка", "в 4 часа", Status.DONE);
        taskManager.addNewTask(task2);
        taskManager.deleteTasks();
        final List<Task> tasks = taskManager.getTasks();
        assertTrue(tasks.isEmpty(), "Задачи по id не удаляются");
    }

    @Test
    public void deleteAllEpics(){
        Epic epic = new Epic("Приготовить ужин", "Купить продукты");
        taskManager.addNewEpic(epic);
        Epic epic2 = new Epic("Прогулка", "в 4 часа");
        taskManager.addNewEpic(epic2);
        taskManager.deleteEpics();
        final List<Epic> epics = taskManager.getEpics();
        assertTrue(epics.isEmpty(), "Задачи по id не удаляются");
    }

    @Test
    public void deleteAllSubtasks(){
        Epic epic = new Epic("Приготовить ужин", "Купить продукты");
        taskManager.addNewEpic(epic);
        Subtask subtask = new Subtask("Купить овощи", "Огурцы, картошка", Status.IN_PROGRESS,
                epic.getId());
        taskManager.addNewSubtask(subtask);
        Subtask subtask2 = new Subtask("Переезд", "в 2 часа", Status.NEW,epic.getId());
        taskManager.addNewSubtask(subtask2);
        taskManager.deleteSubtasks();
        final List<Subtask> subtasks = taskManager.getSubtasks();
        assertTrue(subtasks.isEmpty(), "Задачи по id не удаляются");
    }

    @Test
    public void createdTasks(){
        Task task = new Task("Переезд", "в 2 часа", Status.NEW);
        taskManager.addNewTask(task);
        final List<Task> tasks = taskManager.getTasks();
        Task task2 = tasks.getFirst();
        assertEquals(task.getId(), task2.getId(), "Задачи по id не совпадают");
        assertEquals(task.getName(), task2.getName(), "Задачи по name не совпадают");
        assertEquals(task.getDescription(), task2.getDescription(), "Задачи по description не совпадают");
        assertEquals(task.getStatus(), task2.getStatus(), "Задачи по status не совпадают");
    }

    @Test
    public void historyTaskOne(){
        Task task = new Task("Переезд", "в 2 часа", Status.NEW);
        taskManager.addNewTask(task);
        final ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(taskManager.getTaskByID(task.getId()));
        assertEquals(tasks,taskManager.getHistory(),"История не сохранилась");
    }

    @Test
    public void historyTaskHead(){
        Task task = new Task("Переезд", "в 2 часа", Status.NEW);
        taskManager.addNewTask(task);
        Task task2 = new Task( "Концерт", "в 2 часа", Status.NEW);
        taskManager.addNewTask(task2);
        Task task3 = new Task( "Концерт", "в 4 часа", Status.NEW);
        taskManager.addNewTask(task3);
        final ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(taskManager.getTaskByID(task.getId()));
        taskManager.getTaskByID(task.getId());
        tasks.add(taskManager.getTaskByID(task2.getId()));
        tasks.add(taskManager.getTaskByID(task3.getId()));
        assertEquals(tasks,taskManager.getHistory(),"История не сохранилась");
    }

    @Test
    public void historyTask(){
        Task task = new Task("Переезд", "в 2 часа", Status.NEW);
        taskManager.addNewTask(task);
        Task task2 = new Task( "Концерт", "в 2 часа", Status.NEW);
        taskManager.addNewTask(task2);
        Task task3 = new Task( "Концерт", "в 4 часа", Status.NEW);
        taskManager.addNewTask(task3);
        final ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(taskManager.getTaskByID(task.getId()));
        tasks.add(taskManager.getTaskByID(task2.getId()));
        taskManager.getTaskByID(task2.getId());
        tasks.add(taskManager.getTaskByID(task3.getId()));
        assertEquals(tasks,taskManager.getHistory(),"История не сохранилась");
        taskManager.getTaskByID(task2.getId());
        assertNotEquals(tasks,taskManager.getHistory(),"История не сохранилась");
    }

    @Test
    public void getEpicTheSameSubtasks(){
        Epic epic = new Epic("Приготовить ужин", "Купить продукты");
        taskManager.addNewEpic(epic);
        Subtask subtask = new Subtask("Купить овощи", "Огурцы, картошка", Status.IN_PROGRESS,
                epic.getId());
        taskManager.addNewSubtask(subtask);
        Subtask subtask2 = new Subtask("Купить молочку", "Йогурт, ряженка", Status.IN_PROGRESS,
                epic.getId());
        taskManager.addNewSubtask(subtask2);
        final ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(subtask);
        tasks.add(subtask2);
        assertEquals(tasks,taskManager.getEpicSubtasks(epic),"Subtasks не совпадают");
    }

    @Test
    public void toStringTaskManager(){
        Task task = new Task("Переезд", "в 2 часа", Status.NEW);
        taskManager.addNewTask(task);
        Epic epic = new Epic("Приготовить ужин", "Купить продукты");
        taskManager.addNewEpic(epic);
        Subtask subtask = new Subtask("Купить овощи", "Огурцы, картошка", Status.IN_PROGRESS,
                epic.getId());
        taskManager.addNewSubtask(subtask);
        assertNotNull(taskManager.toString(), "Вывод не совпадает");
    }
    }
