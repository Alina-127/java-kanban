package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import status.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefaultTaskManager();
    }

    @BeforeEach
    void updateTaskManager() {
        taskManager = new FileBackedTaskManager(new File("src/resources/fileTasks.csv"));
        File file = taskManager.getFile();
        try {
            if (file.isFile()) {
                FileWriter writer = new FileWriter(file);
                writer.write("");
                writer.close();
            }
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void shouldBeGetSubtasksAfterLoadFromFile() {
        Epic epic = new Epic("Название задачи 1", "Описание задачи 1");
        taskManager.addNewEpic(epic);
        Subtask subtask = new Subtask("Название подзадачи 1", "Описание задачи 1", Status.NEW,
                Duration.ofMinutes(15),
                LocalDateTime.of(2000,12,10,18,50),epic.getId());
        Subtask subtask2 = new Subtask("Название подзадачи 2", "Описание задачи 2", Status.NEW,
                Duration.ofMinutes(30),
                LocalDateTime.of(2010,2,1,13,50),epic.getId());
        taskManager.addNewSubtask(subtask);
        taskManager.addNewSubtask(subtask2);
        FileBackedTaskManager loadFile = FileBackedTaskManager.loadFromFile(Managers.file);
        assertEquals(2, loadFile.getSubtasks().size());
    }

    @Test
    public void shouldBeGetEpicAfterLoadFromFile() {
        Epic epic = new Epic("Название задачи 1", "Описание задачи 1");
        taskManager.addNewEpic(epic);
        FileBackedTaskManager loadFile = FileBackedTaskManager.loadFromFile(Managers.file);
        assertEquals(1, loadFile.getEpics().size());
    }

    @Test
    public void shouldBeGetTasksAfterLoadFromFile() {
        Task task = new Task("Переезд", "в 2 часа", Status.NEW,Duration.ofMinutes(10),
                LocalDateTime.of(2008,12,1,18,0));
        taskManager.addNewTask(task);
        Task task2 = new Task("Танцы", "в 4 часа", Status.IN_PROGRESS,Duration.ofMinutes(15),
                LocalDateTime.of(2000,12,10,18,50));
        taskManager.addNewTask(task2);
        FileBackedTaskManager loadFile = FileBackedTaskManager.loadFromFile(Managers.file);
        assertEquals(2, loadFile.getTasks().size());
    }

    @Test
    void shouldBeEmptyTasksAfterFromFile() {
        FileBackedTaskManager loadFile = FileBackedTaskManager.loadFromFile(Managers.file);
        assertEquals(0, loadFile.getTasks().size());
    }

}
