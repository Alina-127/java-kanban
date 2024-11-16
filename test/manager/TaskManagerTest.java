package manager;

import org.junit.jupiter.api.Test;
import status.Status;
import tasks.AbstractTask;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest <T extends TaskManager> {
    protected T taskManager;

    @Test
    public void addNewTaskAndGetById_shouldReturnTask() {
        Task task = new Task("Переезд", "в 2 часа", Status.NEW, Duration.ofMinutes(32),
                LocalDateTime.of(2000,2,10,14,0));
        final int taskId = taskManager.addNewTask(task).getId();

        final Task savedTask = taskManager.getTaskByID(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.getFirst(), "Задачи не совпадают.");

    }

    @Test
    public void addNewEpicAndGetById_shouldReturnEpic() {
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

    @Test
    public void addNewSubtaskAndGetById_shouldReturnSubtask() {
        Epic epic = new Epic("Приготовить ужин", "Купить продукты");
        taskManager.addNewEpic(epic);
        Subtask subtask = new Subtask("Купить овощи", "Огурцы, картошка", Status.IN_PROGRESS,
                Duration.ofMinutes(32),
                LocalDateTime.of(2000,2,10,12,50), epic.getId());

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
    public void addNewSubtaskWithStartTimeIsAfterEpic_shouldReturnNewStartTimeInEpic() {
        Epic epic = new Epic("Приготовить ужин", "Купить продукты");
        taskManager.addNewEpic(epic);
        Subtask subtask = new Subtask("Купить овощи", "Огурцы, картошка", Status.IN_PROGRESS,
                Duration.ofMinutes(32),
                LocalDateTime.of(2003,2,10,12,50), epic.getId());
        taskManager.addNewSubtask(subtask);
        Subtask subtask2 = new Subtask("Купить овощи", "Огурцы, картошка", Status.IN_PROGRESS,
                Duration.ofMinutes(32),
                LocalDateTime.of(2000,2,10,12,50), epic.getId());
        taskManager.addNewSubtask(subtask2);
        assertEquals(epic.getStartTime(), subtask2.getStartTime(), "StartTime не совпадают.");
    }

    @Test
    public void updateTask_shouldReturnTaskId() {
        Task task = new Task("Переезд", "в 2 часа", Status.NEW, Duration.ofMinutes(32),
                LocalDateTime.of(2000,2,10,14,0));
        taskManager.addNewTask(task);
        Task task2 = new Task(task.getId(),"Прогулка", "в 4 часа", Status.DONE, Duration.ofMinutes(32),
                LocalDateTime.of(2000,8,10,16,0));
        taskManager.updateTask(task2);
        assertEquals(task, task2, "Задачи не совпадают.");
    }

    @Test
    public void updateTaskIfNull_shouldReturnNull() {
        Task task = new Task(1, "Переезд", "в 2 часа", Status.NEW,  Duration.ofMinutes(32),
                LocalDateTime.of(2000,2,10,14,00));
        taskManager.addNewTask(task);
        Task task2 = new Task(2, "Прогулка", "в 4 часа", Status.DONE,  Duration.ofMinutes(32),
                LocalDateTime.of(2001,2,10,16,0));
        assertNull(taskManager.updateTask(task2), "Задачи совпадают.");
    }

    @Test
    public void updateEpic_shouldReturnEpicId() {
        Epic epic = new Epic("Приготовить ужин", "Купить продукты");
        taskManager.addNewEpic(epic);
        Epic epic2 = new Epic(epic.getId(),"Переезд", "в 2 часа");
        taskManager.updateEpic(epic2);
        assertEquals(epic, epic2, "Задачи не совпадают.");
    }

    @Test
    public void updateEpicWithSubtask_shouldEpicEqual() {
        Epic epic = new Epic("Приготовить ужин", "Купить продукты");
        taskManager.addNewEpic(epic);
        Subtask subtask = new Subtask("Купить овощи", "Огурцы, картошка", Status.IN_PROGRESS,
                Duration.ofMinutes(32),
                LocalDateTime.of(2000,2,10,12,50), epic.getId());
        Subtask subtask2 = new Subtask("Купить машину", "Марк2", Status.DONE,  Duration.ofMinutes(80),
                LocalDateTime.of(2021,5,10,18,50), epic.getId());
        taskManager.addNewSubtask(subtask2);
        Epic epic2 = new Epic(epic.getId(),"Переезд", "в 2 часа");
        taskManager.updateEpic(epic2);
        assertEquals(epic, epic2, "Задачи не совпадают.");
    }

    @Test
    public void updateEpicIfNull_shouldReturnNull() {
        Epic epic = new Epic(1,"Приготовить ужин", "Купить продукты");
        taskManager.addNewEpic(epic);
        Epic epic2 = new Epic(2,"Переезд", "в 2 часа");
        assertNull(taskManager.updateEpic(epic2), "Задачи не совпадают.");
    }

    @Test
    public void updateSubtask_shouldReturnSubtaskId() {
        Epic epic = new Epic("Приготовить ужин", "Купить продукты");
        taskManager.addNewEpic(epic);
        Subtask subtask = new Subtask("Купить овощи", "Огурцы, картошка", Status.IN_PROGRESS,
                Duration.ofMinutes(32),
                LocalDateTime.of(2000,2,10,12,50), epic.getId());
        taskManager.addNewSubtask(subtask);
        Subtask subtask2 = new Subtask(subtask.getId(),"Переезд", "в 2 часа", Status.NEW,
                Duration.ofMinutes(32),
                LocalDateTime.of(2000,2,10,14,0), epic.getId());
        taskManager.updateSubtask(subtask2);
        assertEquals(subtask, subtask2, "Задачи не совпадают.");
    }

    @Test
    public void updateSubtaskIfNull_shouldReturnNull() {
        Epic epic = new Epic("Приготовить ужин", "Купить продукты");
        taskManager.addNewEpic(epic);
        Subtask subtask = new Subtask(1,"Купить овощи", "Огурцы, картошка", Status.IN_PROGRESS,
                Duration.ofMinutes(32),
                LocalDateTime.of(2000,2,10,12,50), epic.getId());
        taskManager.addNewSubtask(subtask);
        Subtask subtask2 = new Subtask(2,"Переезд", "в 2 часа", Status.NEW, Duration.ofMinutes(32),
                LocalDateTime.of(2007,2,8,14,0), epic.getId());
        assertNull(taskManager.updateSubtask(subtask2), "Задачи не совпадают.");
    }

    @Test
    public void deleteByIdTask() {
        Task task = new Task("Переезд", "в 2 часа", Status.NEW, Duration.ofMinutes(32),
                LocalDateTime.of(2000,2,10,14,0));
        taskManager.addNewTask(task);
        Task task2 = new Task("Прогулка", "в 4 часа", Status.DONE, Duration.ofMinutes(2),
                LocalDateTime.of(2020,5,10,16,0));
        taskManager.addNewTask(task2);
        taskManager.deleteTaskById(task.getId());
        final List<Task> tasks = taskManager.getTasks();
        assertEquals(1, tasks.size(), "Задачи по id не удаляются");
    }

    @Test
    public void deleteByIdEpic() {
        Epic epic = new Epic("Приготовить ужин", "Купить продукты");
        taskManager.addNewEpic(epic);
        Subtask subtask = new Subtask("Купить овощи", "Огурцы, картошка", Status.IN_PROGRESS,
                Duration.ofMinutes(24),
                LocalDateTime.of(2000,2,10,12,50), epic.getId());
        taskManager.addNewSubtask(subtask);
        Epic epic2 = new Epic("Прогулка", "в 4 часа");
        taskManager.addNewEpic(epic2);
        taskManager.deleteEpicById(epic.getId());
        final List<Epic> epics = taskManager.getEpics();
        assertEquals(1, epics.size(), "Задачи по id не удаляются");
    }

    @Test
    public void deleteByIdSubtask() {
        Epic epic = new Epic("Приготовить ужин", "Купить продукты");
        taskManager.addNewEpic(epic);
        Subtask subtask = new Subtask("Купить овощи", "Огурцы, картошка", Status.IN_PROGRESS,  Duration.ofMinutes(32),
                LocalDateTime.of(2000,2,10,12,50), epic.getId());
        taskManager.addNewSubtask(subtask);
        Subtask subtask2 = new Subtask("Переезд", "в 2 часа", Status.NEW,  Duration.ofMinutes(72),
                LocalDateTime.of(2010,2,10,14,0), epic.getId());
        taskManager.addNewSubtask(subtask2);
        taskManager.deleteSubtaskById(subtask.getId());
        final List<Subtask> subtasks = taskManager.getSubtasks();
        assertEquals(1, subtasks.size(), "Задачи по id не удаляются");
    }

    @Test
    public void deleteAllTasks_shouldReturnEmpty() {
        Task task = new Task("Переезд", "в 2 часа", Status.NEW,  Duration.ofMinutes(30),
                LocalDateTime.of(2000,2,10,14,0));
        taskManager.addNewTask(task);
        Task task2 = new Task("Прогулка", "в 4 часа", Status.DONE,  Duration.ofMinutes(54),
                LocalDateTime.of(2000,2,10,16,0));
        taskManager.addNewTask(task2);
        taskManager.deleteTasks();
        final List<Task> tasks = taskManager.getTasks();
        assertTrue(tasks.isEmpty(), "Задачи по id не удаляются");
    }

    @Test
    public void deleteAllEpics_shouldReturnEmpty() {
        Epic epic = new Epic("Приготовить ужин", "Купить продукты");
        taskManager.addNewEpic(epic);
        Epic epic2 = new Epic("Прогулка", "в 4 часа");
        taskManager.addNewEpic(epic2);
        taskManager.deleteEpics();
        final List<Epic> epics = taskManager.getEpics();
        assertTrue(epics.isEmpty(), "Задачи по id не удаляются");
    }

    @Test
    public void deleteAllSubtasks_shouldReturnEmpty() {
        Epic epic = new Epic("Приготовить ужин", "Купить продукты");
        taskManager.addNewEpic(epic);
        Subtask subtask = new Subtask("Купить овощи", "Огурцы, картошка", Status.IN_PROGRESS,  Duration.ofMinutes(32),
                LocalDateTime.of(2000,2,10,12,50), epic.getId());
        taskManager.addNewSubtask(subtask);
        Subtask subtask2 = new Subtask("Переезд", "в 2 часа", Status.NEW,  Duration.ofMinutes(32),
                LocalDateTime.of(2006,2,8,14,0), epic.getId());
        taskManager.addNewSubtask(subtask2);
        taskManager.deleteSubtasks();
        final List<Subtask> subtasks = taskManager.getSubtasks();
        assertTrue(subtasks.isEmpty(), "Задачи по id не удаляются");
    }

    @Test
    public void addTasks_shouldReturnTaskComponents() {
        Task task = new Task("Переезд", "в 2 часа", Status.NEW,  Duration.ofMinutes(32),
                LocalDateTime.of(2000,2,10,14,0));
        taskManager.addNewTask(task);
        final List<Task> tasks = taskManager.getTasks();
        Task task2 = tasks.getFirst();
        assertEquals(task.getId(), task2.getId(), "Задачи по id не совпадают");
        assertEquals(task.getName(), task2.getName(), "Задачи по name не совпадают");
        assertEquals(task.getDescription(), task2.getDescription(), "Задачи по description не совпадают");
        assertEquals(task.getStatus(), task2.getStatus(), "Задачи по status не совпадают");
        assertEquals(task.getDuration(), task2.getDuration(), "Задачи по duration не совпадают");
        assertEquals(task.getStartTime(), task2.getStartTime(), "Задачи по startTime не совпадают");
    }

    @Test
    public void historyIfOneTask__shouldReturnEqualTasks() {
        Task task = new Task("Переезд", "в 2 часа", Status.NEW, Duration.ofMinutes(32),
                LocalDateTime.of(2000,2,10,14,0));
        taskManager.addNewTask(task);
        final ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(taskManager.getTaskByID(task.getId()));
        assertEquals(tasks,taskManager.getHistory(),"История не сохранилась");
    }

    @Test
    public void removeRepeatIfTaskIsHead_shouldReturnOneTaskInHistory() {
        Task task = new Task("Переезд", "в 2 часа", Status.NEW, Duration.ofMinutes(32),
                LocalDateTime.of(2000,2,10,14,0));
        taskManager.addNewTask(task);
        Task task2 = new Task( "Концерт", "в 2 часа", Status.NEW, Duration.ofMinutes(56),
                LocalDateTime.of(2000,5,15,14,0));
        taskManager.addNewTask(task2);
        Task task3 = new Task( "Концерт", "в 4 часа", Status.NEW, Duration.ofMinutes(48),
                LocalDateTime.of(2000,7,15,16,0));
        taskManager.addNewTask(task3);
        final ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(taskManager.getTaskByID(task.getId()));
        taskManager.getTaskByID(task.getId());
        tasks.add(taskManager.getTaskByID(task2.getId()));
        tasks.add(taskManager.getTaskByID(task3.getId()));
        assertEquals(tasks,taskManager.getHistory(),"История не сохранилась");
    }

    @Test
    public void removeRepeatTasks_shouldReturnOneTaskInHistory() {
        Task task = new Task("Переезд", "в 2 часа", Status.NEW, Duration.ofMinutes(32),
                LocalDateTime.of(2000,2,10,14,0));
        taskManager.addNewTask(task);
        Task task2 = new Task( "Концерт", "в 2 часа", Status.NEW, Duration.ofMinutes(32),
                LocalDateTime.of(2007,2,18,14,0));
        taskManager.addNewTask(task2);
        Task task3 = new Task( "Концерт", "в 4 часа", Status.NEW, Duration.ofMinutes(32),
                LocalDateTime.of(2004,5,10,16,0));
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
    public void getEpic_shouldReturnTheSameSubtasks() {
        Epic epic = new Epic("Приготовить ужин", "Купить продукты");
        taskManager.addNewEpic(epic);
        Subtask subtask = new Subtask("Купить овощи", "Огурцы, картошка", Status.IN_PROGRESS,
                Duration.ofMinutes(32),
                LocalDateTime.of(2000,2,10,14,0), epic.getId());
        taskManager.addNewSubtask(subtask);
        Subtask subtask2 = new Subtask("Купить молочку", "Йогурт, ряженка", Status.IN_PROGRESS,
                Duration.ofMinutes(7),
                LocalDateTime.of(2001,3,17,21,0), epic.getId());
        taskManager.addNewSubtask(subtask2);
        final ArrayList<AbstractTask> tasks = new ArrayList<>();
        tasks.add(subtask);
        tasks.add(subtask2);
        assertEquals(tasks,taskManager.getEpicSubtasks(epic),"Subtasks не совпадают");
    }

    @Test
    public void toStringTaskManager_shouldReturnTaskManagerComponentsInString() {
        Task task = new Task("Переезд", "в 2 часа", Status.NEW, Duration.ofMinutes(32),
                LocalDateTime.of(2000,2,10,14,0));
        taskManager.addNewTask(task);
        Epic epic = new Epic("Приготовить ужин", "Купить продукты");
        taskManager.addNewEpic(epic);
        Subtask subtask = new Subtask("Купить овощи", "Огурцы, картошка", Status.IN_PROGRESS,
                Duration.ofMinutes(32),
                LocalDateTime.of(2020,2,19,14,50), epic.getId());
        taskManager.addNewSubtask(subtask);
        assertNotNull(taskManager.toString(), "Вывод не совпадает");
    }

    @Test
    public void addTaskAndSubtask_shouldReturnPrioritisedTaskAndSubtask() {
        Task task = new Task("Переезд", "в 2 часа", Status.NEW, Duration.ofMinutes(32),
                LocalDateTime.of(2000,2,10,14,0));
        taskManager.addNewTask(task);
        Epic epic = new Epic("Приготовить ужин", "Купить продукты");
        taskManager.addNewEpic(epic);
        Subtask subtask = new Subtask("Купить овощи", "Огурцы, картошка", Status.IN_PROGRESS,
                Duration.ofMinutes(32),
                LocalDateTime.of(2020,2,19,14,50), epic.getId());
        taskManager.addNewSubtask(subtask);
        assertEquals(2,taskManager.getPrioritizedTasks().size(),"Задачи не сохранились по приоритету");
    }

    @Test
    public void testException() {
        assertThrows(RuntimeException.class, () -> {
            Task task = new Task("Переезд", "в 2 часа", Status.NEW, Duration.ofMinutes(32),
                    LocalDateTime.of(2000,2,10,14,0));
            taskManager.addNewTask(task);
            Task task2 = new Task("Переезд", "в 2 часа", Status.NEW, Duration.ofMinutes(32),
                    LocalDateTime.of(2000,2,10,14,10));
            taskManager.addNewTask(task2);
        }, "Задачи с пересечением должны приводить к исключению");
    }
}

