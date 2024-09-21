import manager.InMemoryTaskManager;
import status.Status;
import tasks.*;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        Task task = new Task("Переезд", "в 2 часа", Status.NEW);
        taskManager.addNewTask(task);

        Task task2 = new Task("Покупки", "Открылся новый магазин", Status.IN_PROGRESS);
        taskManager.addNewTask(task2);

        Epic epic = new Epic("Приготовить ужин", "Купить продукты");
        taskManager.addNewEpic(epic);

        Subtask subtask = new Subtask("Купить овощи", "Огурцы, картошка", Status.IN_PROGRESS,
                epic.getId());
        taskManager.addNewSubtask(subtask);

        Subtask subtask2 = new Subtask("Купить мясо", "Филе куриное", Status.DONE, epic.getId());
        taskManager.addNewSubtask(subtask2);

        Subtask subtask3 = new Subtask("Список дел", "накормить кота", Status.DONE, epic.getId());
        taskManager.addNewSubtask(subtask3);

        Epic epic2 = new Epic("Поиграть в компьютер", "Сначала выполнить дела");
        taskManager.addNewEpic(epic2);

        taskManager.getTaskByID(task.getId());
        taskManager.getTaskByID(task.getId());
        taskManager.getTaskByID(task2.getId());
        System.out.println("История:");
        System.out.println(taskManager.getHistory());
        taskManager.getEpicByID(epic.getId());
        taskManager.getEpicByID(epic2.getId());
        taskManager.getEpicByID(epic2.getId());
        System.out.println("История:");
        System.out.println(taskManager.getHistory());
        taskManager.deleteTaskById(task2.getId());
        System.out.println("История:");
        System.out.println(taskManager.getHistory());
        taskManager.deleteEpicById(epic.getId());
        System.out.println("История:");
        System.out.println(taskManager.getHistory());
        printAllTasks(taskManager);




    }
        
    private static void printAllTasks(InMemoryTaskManager taskManager) {
        System.out.println("Задачи:");
        for (Task tasks : taskManager.getTasks()) {
            System.out.println(tasks);
        }
        System.out.println("Эпики:");
        for (Epic epics : taskManager.getEpics()) {
            System.out.println(epics);

            for (Subtask subtasks : taskManager.getEpicSubtasks(epics)) {
                System.out.println("--> " + subtasks);
            }
        }
        System.out.println("Подзадачи:");
        for (Subtask subtasks : taskManager.getSubtasks()) {
            System.out.println(subtasks);
        }

        System.out.println("История:");
        for (Task tasks : taskManager.getHistory()) {
            System.out.println(tasks);
        }
    }
}
