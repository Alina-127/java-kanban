package tasks;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import status.Status;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TaskTest {
    @Test
    public void taskShouldReturnEqualsId() {
        Task task1 = new Task(12, "Купить молоко", "В 2 часа дня", Status.DONE);
        Task task2 = new Task(12, "Переезд", "в 2 часа", Status.DONE);

        Assertions.assertEquals(task2, task1,
                "Ошибка, экземпляры класса Task равны друг другу, если равен их id;");
    }

    @Test
    public void toStringEpic() {
        Task task = new Task("Переезд", "в 2 часа", Status.NEW);
        assertNotNull(task.toString(), "Вывод не совпадает");
    }
}

