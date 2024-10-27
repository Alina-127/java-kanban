package tasks;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import status.Status;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SubtaskTest {
    @Test
    public void subtaskShouldReturnEqualsId() {
        Subtask subtask1 = new Subtask(12, "Купить молоко", "В 2 часа дня", Status.DONE, Duration.ofMinutes(32),
                LocalDateTime.of(2000,2,10,14,0));
        Subtask subtask2 = new Subtask(12, "Переезд", "в 2 часа", Status.DONE,Duration.ofMinutes(29),
                LocalDateTime.of(2009,2,10,14,0));

        Assertions.assertEquals(subtask2, subtask1,
                "Ошибка, экземпляры класса Subtask равны друг другу, если равен их id;");
    }

    @Test
    public void toStringSubtask_shouldReturnSubtaskComponentsInString() {
        Epic epic = new Epic("Приготовить ужин", "Купить продукты");
        Subtask subtask = new Subtask("Купить овощи", "Огурцы, картошка", Status.IN_PROGRESS,
                Duration.ofMinutes(30),
                LocalDateTime.of(2010,2,1,14,0), epic.getId());
        assertNotNull(subtask.toString(), "Вывод не совпадает");
    }

}
