package tasks;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import status.Status;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TaskTest {
    @Test
    public void taskShouldReturnEqualsId() {
        Task task1 = new Task(12, "Купить молоко", "В 2 часа дня", Status.DONE,
                Duration.ofMinutes(32),
                LocalDateTime.of(2000,2,10,14,0));
        Task task2 = new Task(12, "Переезд", "в 2 часа", Status.DONE, Duration.ofMinutes(32),
                LocalDateTime.of(2000,8,25,14,0));

        Assertions.assertEquals(task2, task1,
                "Ошибка, экземпляры класса Task равны друг другу, если равен их id;");
    }

    @Test
    public void toStringTask_shouldReturnTaskComponentsInString() {
        Task task = new Task("Переезд", "в 2 часа", Status.NEW,Duration.ofMinutes(32),
                LocalDateTime.of(2000,2,10,14,0));
        assertNotNull(task.toString(), "Вывод не совпадает");
    }
}

