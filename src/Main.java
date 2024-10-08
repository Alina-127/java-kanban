import manager.InMemoryTaskManager;
import status.Status;
import tasks.*;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        Task task = new Task("Переезд", "в 2 часа", Status.NEW);
        taskManager.addNewTask(task);

        Task newTask = new Task("Покупки", "Открылся новый магазин", Status.IN_PROGRESS);
        taskManager.addNewTask(newTask);

        Epic epic = new Epic("Приготовить ужин", "Купить продукты");
        taskManager.addNewEpic(epic);

        Subtask subtask = new Subtask("Купить овощи", "Огурцы, картошка", Status.IN_PROGRESS,
                epic.getId());
        taskManager.addNewSubtask(subtask);

        Subtask newSubtask = new Subtask("Купить мясо", "Филе куриное", Status.DONE, epic.getId());
        taskManager.addNewSubtask(newSubtask);

        Epic newEpic = new Epic("Поиграть в компьютер", "Сначала выполнить дела");
        taskManager.addNewEpic(newEpic);

        Subtask subtask2 = new Subtask("Список дел", "накормить кота", Status.DONE, newEpic.getId());
        taskManager.addNewSubtask(subtask2);

        Task task2 = new Task(1, "Переезд", "в 2 часа", Status.DONE);
        taskManager.updateTask(task2);

        Task newTask2 = new Task(2, "Покупки", "Открылся новый магазин", Status.DONE);
        taskManager.updateTask(newTask2);

        Subtask subtaskUpdate = new Subtask(4, "Купить овощи", "Огурцы, картошка",
                Status.NEW, epic.getId());
        taskManager.updateSubtask(subtaskUpdate);

        Subtask newSubtaskUpdate = new Subtask(5, "Купить мясо", "Филе куриное", Status.NEW,
                epic.getId());
        taskManager.updateSubtask(newSubtaskUpdate);

        Subtask subtask2Update = new Subtask(7, "Список дел", "накормить кота", Status.IN_PROGRESS,
                newEpic.getId());
        taskManager.updateSubtask(subtask2Update);
        System.out.println(taskManager.getTaskByID(1));

        taskManager.deleteTaskById(2);
        taskManager.deleteEpicById(3);
        printAllTasks(taskManager);
//        System.out.println(taskManager.getTasks());
//        System.out.println(taskManager.getEpics());
//        System.out.println(taskManager.getSubtasks());
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
