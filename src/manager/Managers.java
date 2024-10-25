package manager;

import java.io.File;

public class Managers {
    public static final File file = new File("src/resources/fileTasks.csv");

    public static InMemoryTaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static FileBackedTaskManager getDefaultTaskManager() {
        return new FileBackedTaskManager(file);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
