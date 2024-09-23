package manager;

import tasks.Epic;
import status.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.*;


public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    static int id = 1;

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasks(Epic epic) {
        return epic.getSubtasks();
    }

    @Override
    public void deleteTasks() {
        tasks.clear();
    }

    @Override
    public void deleteEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubtasks();
            epic.setStatus(Status.NEW);
        }
    }

    @Override
    public Task getTaskByID(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public Epic getEpicByID(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public Subtask getSubtaskByID(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }


    @Override
    public int getNewId() {
        return id++;
    }

    @Override
    public Task addNewTask(Task newTask) {
        int newId = getNewId();
        newTask.setId(newId);
        tasks.put(newTask.getId(), newTask);
        return newTask;
    }

    @Override
    public Epic addNewEpic(Epic newEpic) {
        int newId = getNewId();
        newEpic.setId(newId);
        epics.put(newEpic.getId(), newEpic);
        updateEpicStatus(newEpic);
        return newEpic;
    }

    @Override
    public Subtask addNewSubtask(Subtask newSubtask) {
        int newId = getNewId();
        newSubtask.setId(newId);
        Epic newEpic = epics.get(newSubtask.getSubId());
        newEpic.addSubtasks(newSubtask);
        subtasks.put(newSubtask.getId(), newSubtask);
        transferSubIntoEpic(newSubtask);
        return newSubtask;
    }

    @Override
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

    @Override
    public Epic updateEpic(Epic updateEpic) {
        Integer epicId = updateEpic.getId();
        ArrayList<Subtask> updateSub = new ArrayList<>();
        if (epics.containsKey(epicId)) {
            for (Subtask subtask : epics.get(epicId).getSubtasks()) {
                updateSub.add(subtask);
            }
            epics.put(epicId, updateEpic);
            updateEpic.setSubtasks(updateSub);
            updateEpicStatus(updateEpic);
            return updateEpic;
        } else {
            System.out.println("Эпик с таким id не найден");
            return null;
        }
    }

    @Override
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

    @Override
    public Task deleteTaskById(Integer id) {
        Task task = tasks.get(id);
        tasks.remove(id);
        historyManager.remove(id);
        return task;// возвращаем значение, чтобы спросить у пользователя действительно ли он хочет это удалить.
    }

    @Override
    public Epic deleteEpicById(Integer id) {
        Epic epic = epics.get(id);
        epics.remove(id);
        historyManager.remove(id);
        for (Subtask subtask : epic.getSubtasks()) {
            subtasks.remove(subtask.getId());
        }
        epic.clearSubtasks();
        return epic;// возвращаем значение, чтобы спросить у пользователя действительно ли он хочет это удалить.
    }

    @Override
    public Subtask deleteSubtaskById(Integer id) {
        Subtask subtask = subtasks.get(id);
        Epic epic = epics.get(subtask.getSubId());
        subtasks.remove(id);
        historyManager.remove(id);
        epic.removeSubtasks(subtask);
        updateEpicStatus(epic);
        return subtask; // возвращаем значение, чтобы спросить у пользователя действительно ли он хочет это удалить.
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }

    // трансфер для передачи подзадачи в эпик, чтобы в дальнейшем проверить статус
    private void transferSubIntoEpic(Subtask newSubtask) {
        Epic epic = epics.get(newSubtask.getSubId());
        updateEpicStatus(epic);
    }

    // проверка и изменение сатуса эпик
    private void updateEpicStatus(Epic epic) {
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
