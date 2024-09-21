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

public class InMemoryHistoryManagerTest {
    //public TaskManager taskManager;
    HistoryManager historyManager;
    Task task1 = new Task(1, "Переезд", "в 2 часа", Status.NEW);
    Task task2 = new Task(2, "Концерт", "в 2 часа", Status.NEW);
    Task task3 = new Task(3, "Концерт", "в 4 часа", Status.NEW);
    @BeforeEach
    public void beforeEach() {
        //taskManager = Managers.getDefault();
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    public void addHistory() {
        historyManager.add(new Task("Переезд", "в 2 часа", Status.NEW));
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    public void addTasks() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        historyManager.add(task1);
        historyManager.add(task2);
        assertEquals(tasks, historyManager.getHistory(), "Неверное количество элементов в истории ");
    }

    @Test
    public void addTaskNull() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        historyManager.add(null);
        assertTrue(historyManager.getHistory().isEmpty(), "Неверное добавление элементов в историю");
    }

    @Test
    public void removeIfOne(){
        historyManager.add(task1);
        historyManager.remove(1);
        assertTrue(historyManager.getHistory().isEmpty(), "Неверное удаление элементов по id");
    }

    @Test
    public void removeIfHead(){
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
    public void removeIfTail(){
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
    public void remove(){
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
    public void aadTheSameTask(){
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        historyManager.add(task1);
        historyManager.add(task1);
        assertEquals(tasks, historyManager.getHistory(),"Неверное добавление одинаковых элементов");
    }
}
