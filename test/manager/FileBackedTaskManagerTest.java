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

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        Subtask subtask = new Subtask("Название подзадачи 1", "Описание задачи 1", Status.NEW,epic.getId());
        Subtask subtask2 = new Subtask("Название подзадачи 2", "Описание задачи 2", Status.NEW,epic.getId());
        taskManager.addNewSubtask(subtask);
        taskManager.addNewSubtask(subtask2);
        FileBackedTaskManager loadFile = FileBackedTaskManager.loadFromFile(Managers.file);
        assertEquals(2, loadFile.getSubtasks().size());
    }

    @Test
    public void shouldBeGetTasksAfterLoadFromFile() {
        Task task = new Task("Переезд", "в 2 часа", Status.NEW);
        taskManager.addNewTask(task);
        Task task2 = new Task("Танцы", "в 4 часа", Status.IN_PROGRESS);
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
