package manager;

import exception.ManagerSaveException;
import status.Status;
import status.TypesOfTasks;
import tasks.AbstractTask;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;



public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

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

    private void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            List<Task> tasks = getTasks();
            List<Epic> epics = getEpics();
            List<Subtask> subtasks = getSubtasks();
            bw.write("id,type,name,description,status,duration,startTime,epic");
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
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
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
                    if (taskType.equals("SUBTASK") && contents.length < 6) {
                        idOfEpic = Integer.parseInt(contents[5]);
                    } else if (taskType.equals("SUBTASK") && contents.length > 6) {
                        idOfEpic = Integer.parseInt(contents[7]);
                    }
                    if (taskType.equals("TASK")) {
                        Duration duration = Duration.ofMinutes(Long.parseLong(contents[5]));
                        LocalDateTime startTime = LocalDateTime.parse(contents[6],formatter);
                        Task task = new Task(id, name, description, status, duration, startTime);
                        taskManager.tasks.put(id, task);
                    } else if (taskType.equals("SUBTASK")) {
                        Duration duration = Duration.ofMinutes(Long.parseLong(contents[5]));
                        LocalDateTime startTime = LocalDateTime.parse(contents[6],formatter);
                        Subtask subtask = new Subtask(id, name, description, status, duration, startTime, idOfEpic);
                        taskManager.subtasks.put(id, subtask);
                        taskManager.epics.get(idOfEpic).addSubtasks(subtask);
                        taskManager.updateEpicStatus(taskManager.epics.get(idOfEpic));
                    } else if (taskType.equals("EPIC")) {
                        if (contents.length < 6) {
                            Epic epic = new Epic(id, name, description);
                            taskManager.epics.put(id, epic);
                        } else {
                            Duration duration = Duration.ofMinutes(Long.parseLong(contents[5]));
                            LocalDateTime startTime = LocalDateTime.parse(contents[6],formatter);
                            Epic epic = new Epic(id, name, description, duration, startTime);
                            taskManager.epics.put(id, epic);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
        return taskManager;
    }

    public static String taskToString(AbstractTask task) {
        String taskToString = null;
        if (TypesOfTasks.TASK == task.getType()) {
            taskToString = String.format("%d,%s,%s,%s,%s,%d,%s\n", task.getId(), task.getType(), task.getName(),
                    task.getDescription(), task.getStatus().toString(),task.getDuration().toMinutesPart(),
                    task.getStartTime().format(formatter));
        } else if (TypesOfTasks.EPIC == task.getType()) {
            Epic epic = (Epic) task;
            if (epic.getStartTime() == null) {
                taskToString = String.format("%d,%s,%s,%s,%s\n", epic.getId(), epic.getType(), epic.getName(),
                        epic.getDescription(), epic.getStatus().toString());
            } else {
                taskToString = String.format("%d,%s,%s,%s,%s,%d,%s\n", epic.getId(), epic.getType(), epic.getName(),
                        epic.getDescription(), epic.getStatus().toString(), epic.getDuration().toMinutesPart(),
                        epic.getStartTime().format(formatter));
            }
        } else if (TypesOfTasks.SUBTASK == task.getType()) {
            Subtask subtask = (Subtask) task;
            taskToString = String.format("%d,%s,%s,%s,%s,%d,%s,%d\n", subtask.getId(), subtask.getType(),
                    subtask.getName(), subtask.getDescription(), subtask.getStatus().toString(),
                    subtask.getDuration().toMinutesPart(), subtask.getStartTime().format(formatter),
                    subtask.getSubId());
        }
        return taskToString;
    }

    public File getFile() {
        return file;
    }
}

