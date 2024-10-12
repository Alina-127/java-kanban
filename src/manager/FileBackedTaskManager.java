package manager;

import status.Status;
import status.TypesOfTasks;
import tasks.AbstractTask;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.nio.file.Files;
import java.util.List;



public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    @Override
    public Task addNewTask(Task task) {
        Task newTask = super.addNewTask(task);
        save();
        return newTask;
    }

    @Override
    public Epic addNewEpic(Epic epic) {
        Epic newEpic = super.addNewEpic(epic);
        save();
        return newEpic;
    }

    @Override
    public Subtask addNewSubtask(Subtask subtask) {
        Subtask newSubtask = super.addNewSubtask(subtask);
        save();
        return newSubtask;
    }

    @Override
    public Task updateTask(Task updatedTask) {
        Task task = super.updateTask(updatedTask);
        save();
        return task;
    }

    @Override
    public Epic updateEpic(Epic updateEpic) {
        Epic epic = super.updateEpic(updateEpic);
        save();
        return epic;
    }

    @Override
    public Subtask updateSubtask(Subtask updateSubtask) {
        Subtask subtask = super.updateSubtask(updateSubtask);
        save();
        return subtask;
    }

    @Override
    public Task deleteTaskById(Integer id) {
        super.deleteTaskById(id);
        save();
        return getTaskByID(id);
    }

    @Override
    public Epic deleteEpicById(Integer id) {
        super.deleteEpicById(id);
        save();
        return getEpicByID(id);
    }

    @Override
    public Subtask deleteSubtaskById(Integer id) {
        super.deleteSubtaskById(id);
        save();
        return getSubtaskByID(id);
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }

    public void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            List<Task> tasks = getTasks();
            List<Epic> epics = getEpics();
            List<Subtask> subtasks = getSubtasks();
            bw.write("id,type,name,description,status,epic");
            bw.newLine();

            for (Task task: tasks) {
                bw.write(taskToString(task));
            }
            for (Epic epic: epics) {
                bw.write(taskToString(epic));
            }
            for (Subtask subtask: subtasks) {
                bw.write(taskToString(subtask));
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager taskManager = new FileBackedTaskManager(file);
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            boolean isFirstLine = true;
            for (String line : lines) {
                String[] content = line.split("\n");
                for (String lin : content) {
                    if (isFirstLine) {
                        isFirstLine = false;
                        continue;
                    }
                    String[] contents = lin.split(",");
                    int id = Integer.parseInt(contents[0]);
                    String taskType = contents[1];
                    String name = contents[2];
                    String description = contents[3];
                    Status status = getStatus(contents[4]);
                    int idOfEpic = 0;
                    if (taskType.equals("SUBTASK")) {
                        idOfEpic = Integer.parseInt(contents[5]);
                    }
                    if (taskType.equals("TASK")) {
                        Task task = new Task(id, name, description, status);
                        taskManager.tasks.put(id, task);
                    } else if (taskType.equals("SUBTASK")) {
                        Subtask subtask = new Subtask(id, name, description, status, idOfEpic);
                        taskManager.subtasks.put(id, subtask);
                        taskManager.epics.get(idOfEpic).addSubtasks(subtask);
                        taskManager.updateEpicStatus(taskManager.epics.get(idOfEpic));
                    } else if (taskType.equals("EPIC")) {
                        Epic epic = new Epic(id, name, description);
                        taskManager.epics.put(id, epic);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return taskManager;
    }

    public static String taskToString(AbstractTask task) {
        String taskToString = null;
        if (TypesOfTasks.TASK == task.getType()) {
            taskToString = String.format("%d,%s,%s,%s,%s\n", task.getId(), task.getType(), task.getName(),
                    task.getDescription(), task.getStatus().toString());
        } else if (TypesOfTasks.EPIC == task.getType()) {
            Epic epic = (Epic) task;
            taskToString = String.format("%d,%s,%s,%s,%s\n", epic.getId(), epic.getType(), epic.getName(),
                    epic.getDescription(), epic.getStatus().toString());
        } else if (TypesOfTasks.SUBTASK == task.getType()) {
            Subtask subtask = (Subtask) task;
            taskToString = String.format("%d,%s,%s,%s,%s,%d\n", subtask.getId(), subtask.getType(),
                    subtask.getName(), subtask.getDescription(), subtask.getStatus().toString(), subtask.getSubId());
        }
        return taskToString;
    }

    public File getFile() {
        return file;
    }
}
