import manager.FileBackedTaskManager;
import manager.Managers;
import status.Status;
import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
            FileBackedTaskManager taskManager = Managers.getDefaultTaskManager();

            Task task = new Task("Переезд", "в 2 часа", Status.NEW, Duration.ofMinutes(45),
                    LocalDateTime.of(2008,12,13,14,20));
            taskManager.addNewTask(task);
            Task task2 = new Task("Переезд", "в 2 часа", Status.NEW, Duration.ofMinutes(45),
                    LocalDateTime.of(2022,12,13,14,20));
            taskManager.addNewTask(task2);
            Task task3 = new Task("Переезд", "в 2 часа", Status.NEW, Duration.ofMinutes(45),
                    LocalDateTime.of(2001,12,13,14,20));
            taskManager.addNewTask(task3);

            System.out.println(taskManager.getPrioritizedTasks());

            Epic epic = new Epic("Приготовить ужин", "Купить продукты");
            taskManager.addNewEpic(epic);

            Subtask subtask = new Subtask("Купить овощи", "Огурцы", Status.IN_PROGRESS,
                    Duration.ofMinutes(30),
                    LocalDateTime.of(2002,7,15,10,0),
                    epic.getId());
            taskManager.addNewSubtask(subtask);

            Subtask subtask2 = new Subtask("Купить мясо", "Филе куриное", Status.NEW,
                    Duration.ofMinutes(15),
                    LocalDateTime.of(2000,12,10,17,40), epic.getId());
            taskManager.addNewSubtask(subtask2);

            FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(Managers.file);

            System.out.println("Tasks: " + loadedManager.getTasks());
            System.out.println("Epics: " + loadedManager.getEpics());
            System.out.println("Subtasks: " + loadedManager.getSubtasks());
    }
}
