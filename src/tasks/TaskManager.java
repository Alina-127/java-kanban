package tasks;

import java.util.ArrayList;
import java.util.HashMap;


public class TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    static int id = 1;

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public ArrayList<Subtask> getEpicSubtasks(Epic epic) {
        return epic.getSubtasks();
    }

    public void deleteTasks() {
        tasks.clear();
    }

    public void deleteEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void deleteSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubtasks();
            epic.setStatus(Status.NEW);
        }
    }

    public Task getTaskByID(int id) {
        return tasks.get(id);
    }

    public Epic getEpicByID(int id) {
        return epics.get(id);
    }

    public Subtask getSubtaskByID(int id) {
        return subtasks.get(id);
    }


    public int getNewId() {
        return id++;
    }

    public Task addNewTask(Task newTask) {
        int newId = getNewId();
        newTask.setId(newId);
        tasks.put(newTask.getId(), newTask);
        return newTask;
    }

    public Epic addNewEpic(Epic newEpic) {
        int newId = getNewId();
        newEpic.setId(newId);
        epics.put(newEpic.getId(), newEpic);
        updateEpicStatus(newEpic);
        return newEpic;
    }

    public Subtask addNewSubtask(Subtask newSubtask) {
        int newId = getNewId();
        newSubtask.setId(newId);
        Epic newEpic = epics.get(newSubtask.getSubId());
        newEpic.addSubtasks(newSubtask);
        subtasks.put(newSubtask.getId(), newSubtask);
        transferSubIntoEpic(newSubtask);
        return newSubtask;
    }

    public Task updateTask(Task updatedTask) {
        Integer taskId = updatedTask.getId();
        if (tasks.containsKey(taskId)) {
            deleteTaskById(taskId);
            tasks.put(taskId, updatedTask);
            return updatedTask;
        } else {
            System.out.println("Задача с таким id не найдена");
            return null;
        }
    }

    public Epic updateEpic(Epic updateEpic) {
        Integer EpicId = updateEpic.getId();
        ArrayList<Subtask> updateSub = new ArrayList<>();
        if (epics.containsKey(EpicId)) {
            for (Subtask subtask : epics.get(EpicId).getSubtasks()) {
                updateSub.add(subtask);
            }
            epics.put(EpicId, updateEpic);
            updateEpic.setSubtasks(updateSub);
            updateEpicStatus(updateEpic);
            return updateEpic;
        } else {
            System.out.println("Эпик с таким id не найден");
            return null;
        }
    }

    public Subtask updateSubtask(Subtask updateSubtask) {
        Integer subtaskId = updateSubtask.getId();
        Integer epicId = updateSubtask.getSubId();
        if (subtasks.containsKey(subtaskId)) {
            Subtask oldSubtask = subtasks.get(subtaskId);
            subtasks.put(subtaskId, updateSubtask);
            Epic epic = epics.get(epicId);
            ArrayList<Subtask> newSubtaskList = epic.getSubtasks();
            newSubtaskList.remove(oldSubtask);
            newSubtaskList.add(updateSubtask);
            epic.setSubtasks(newSubtaskList);
            subtasks.put(subtaskId, updateSubtask);
            transferSubIntoEpic(updateSubtask);
            return updateSubtask;
        } else {
            System.out.println("Подзадача с таким id не найдена");
            return null;
        }
    }

    public Task deleteTaskById(Integer id) {
        Task task = tasks.get(id);
        tasks.remove(id);
        return task;// возвращаем значение, чтобы спросить у пользователя действительно ли он хочет это удалить.
    }

    public Epic deleteEpicById(Integer id) {
        Epic epic = epics.get(id);
        epics.remove(id);
        for (Subtask subtask : epic.getSubtasks()) {
            subtasks.remove(subtask.getId());
        }
        epic.clearSubtasks();
        return epic;// возвращаем значение, чтобы спросить у пользователя действительно ли он хочет это удалить.
    }

    public Subtask deleteSubtaskById(Integer id) {
        Subtask subtask = subtasks.get(id);
        Epic epic = epics.get(subtask.getSubId());
        subtasks.remove(id);
        epic.removeSubtasks(subtask);
        updateEpicStatus(epic);
        return subtask; // возвращаем значение, чтобы спросить у пользователя действительно ли он хочет это удалить.
    }

    // трансфер для передачи подзадачи в эпик, чтобы в дальнейшем проверить статус
    public void transferSubIntoEpic(Subtask newSubtask) {
        Epic epic = epics.get(newSubtask.getSubId());
        updateEpicStatus(epic);
    }

    // проверка и изменение сатуса эпик
    public void updateEpicStatus(Epic epic) {
        int numberOfNew = 0;
        int numberOfDone = 0;
        ArrayList<Subtask> newSubList = epic.getSubtasks();

        for (Subtask subtask : newSubList) {
            if (subtask.getStatus() == Status.NEW) {
                numberOfNew++;
            } else if (subtask.getStatus() == Status.DONE) {
                numberOfDone++;
            }
        }
        if (newSubList.isEmpty()) {
            epic.setStatus(Status.NEW);
        } else {
            if (numberOfNew == newSubList.size()) {
                epic.setStatus(Status.NEW);
            } else if (numberOfDone == newSubList.size()) {
                epic.setStatus(Status.DONE);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }
    }


    @Override
    public String toString() {
        return "TaskManager{" +
                "tasks=" + tasks +
                ", epics=" + epics +
                ", subtasks=" + subtasks +
                '}';
    }

}
