package tasks;

import java.util.Comparator;

public class TaskComparator implements Comparator<AbstractTask> {

    @Override
    public int compare(AbstractTask task1, AbstractTask task2) {
        if (task1 == null && task2 == null) {
            return 0; // Оба равны
        }
        if (task1 == null) {
            return 1; // task1 считается "больше" null
        }
        if (task2 == null) {
            return -1; // task2 считается "больше" null
        }

        if (task1.getStartTime().isBefore(task2.getStartTime())) {
            return 1;
        } else if (task1.getStartTime().isAfter(task2.getStartTime())) {
            return -1;
        }
        return 0;
    }
//    public int compare(AbstractTask task1, AbstractTask task2) {
//        if (task1.getStartTime().isBefore(task2.getStartTime()))  {
//            return 1;
//        } else if (task1.getStartTime().isAfter(task2.getStartTime())) {
//            return -1;
//        }
//        return 0;
//    }
}
