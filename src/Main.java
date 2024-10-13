import manager.FileBackedTaskManager;
import manager.Managers;
import status.Status;
import tasks.*;

public class Main {
    public static void main(String[] args) {
            FileBackedTaskManager taskManager = Managers.getDefaultTaskManager();

            Task task = new Task("Переезд", "в 2 часа", Status.NEW);
            taskManager.addNewTask(task);

            Task task2 = new Task("Покупки", "Открылся новый магазин", Status.NEW);
            taskManager.addNewTask(task2);

            Epic epic = new Epic("Приготовить ужин", "Купить продукты");
            taskManager.addNewEpic(epic);

            Subtask subtask = new Subtask("Купить овощи", "Огурцы", Status.IN_PROGRESS,
                    epic.getId());
            taskManager.addNewSubtask(subtask);

            Subtask subtask2 = new Subtask("Купить мясо", "Филе куриное", Status.NEW, epic.getId());
            taskManager.addNewSubtask(subtask2);

            FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(Managers.file);

            System.out.println("Tasks: " + loadedManager.getTasks());
            System.out.println("Epics: " + loadedManager.getEpics());
            System.out.println("Subtasks: " + loadedManager.getSubtasks());
    }
}
