package tasks;

import java.util.Comparator;

public class TaskComparator implements Comparator<AbstractTask> {

    @Override
    public int compare(AbstractTask task1, AbstractTask task2) {
        if (task1.getStartTime().isBefore(task2.getStartTime()))  {
            return 1;
        } else if (task1.getStartTime().isAfter(task2.getStartTime())) {
            return -1;
        }
        return 0;
    }
}
