package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import status.Status;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {
    HistoryManager historyManager;
    Task task1 = new Task(1, "Переезд", "в 2 часа", Status.NEW);
    Task task2 = new Task(2, "Концерт", "в 2 часа", Status.NEW);
    Task task3 = new Task(3, "Концерт", "в 4 часа", Status.NEW);

    @BeforeEach
    public void beforeEach() {
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    public void addHistory_shouldNotBeAnEmptyHistory() {
        historyManager.add(new Task("Переезд", "в 2 часа", Status.NEW));
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    public void addTasks_shouldAddTasksToHistory() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        historyManager.add(task1);
        historyManager.add(task2);
        assertEquals(tasks, historyManager.getHistory(), "Неверное количество элементов в истории ");
    }

    @Test
    public void addTask_shouldReturnNullIfTaskIsEmpty() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        historyManager.add(null);
        assertTrue(historyManager.getHistory().isEmpty(), "Неверное добавление элементов в историю");
    }

    @Test
    public void remove_ifOneTask_shouldReturnNull() {
        historyManager.add(task1);
        historyManager.remove(1);
        assertTrue(historyManager.getHistory().isEmpty(), "Неверное удаление элементов по id");
    }

    @Test
    public void remove_ifTaskIsHead_shouldReturnNull() {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task2);
        tasks.add(task3);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(1);
        assertEquals(tasks, historyManager.getHistory(),"Неверное удаление элементов по id head");

    }

    @Test
    public void remove_ifTaskIsTail_shouldReturnNull() {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(3);
        assertEquals(tasks, historyManager.getHistory(),"Неверное удаление элементов по id tail");

    }

    @Test
    public void remove_inTheMiddle_shouldReturnNull() {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task3);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(2);
        assertEquals(tasks, historyManager.getHistory(),"Неверное удаление элементов по id в середине");

    }

    @Test
    public void add_TheSameTask_shouldEquals() {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        historyManager.add(task1);
        historyManager.add(task1);
        assertEquals(tasks, historyManager.getHistory(),"Неверное добавление одинаковых элементов");
    }
}
