package manager;

import tasks.Task;
import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {
    private final ArrayList<Task> history = new ArrayList<>();
    private Node<Task> head;
    private Node<Task> tail;

    private final HashMap<Integer,Node<Task>> historyTasks = new HashMap<>();

    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node<Task> element = head;
        while (element != null) {
            tasks.add(element.getValues());
            element = element.getNext();
        }
        return tasks;
    }

    public void linkLast(Task task) {
        if (historyTasks.containsKey(task.getId())) {
            removeNode(historyTasks.get(task.getId()));
        }
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(tail, task, null);
        tail = newNode;
        historyTasks.put(task.getId(), newNode);
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.setNext(newNode);
        }
    }

    public void removeNode(Node<Task> node) {
        if (node != null) {
            Node<Task> newNext = node.getNext();
            Node<Task> newPrevious = node.getPrevious();
            node.setValues(null);
            if (head == node && tail == node) {
                head = null;
                tail = null;
            } else if (head == node && tail != node) {
                head = newNext;
                head.setPrevious(null);
            } else if (head != node && tail == node) {
                tail = newPrevious;
                tail.setNext(null);
            } else {
                newNext.setPrevious(newPrevious);
                newPrevious.setNext(newNext);
            }

        }
    }

    @Override
    public void add(Task task) {
            if (task != null) {
                linkLast(task);
            }
    }

    @Override
    public void remove(int id) {
        removeNode(historyTasks.get(id));
    }

    @Override
    public ArrayList<Task> getHistory() {
        return getTasks();
    }
}
