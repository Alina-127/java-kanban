package manager;

import tasks.Task;

public class Node <T> {
        Node <T> previous;
        Node <T> next;
        Task values;
        public Node(Node <T> previous, Task values, Node <T> next) {
            this.values = values;
            this.next = next;
            this.previous = previous;
        }
    public Node<T> getPrevious() {
        return previous;
    }

    public Node<T> getNext() {
        return next;
    }

    public Task getValues() {
        return values;
    }

    public void setPrevious(Node<T> previous) {
        this.previous = previous;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }

    public void setValues(Task values) {
        this.values = values;
    }
}
