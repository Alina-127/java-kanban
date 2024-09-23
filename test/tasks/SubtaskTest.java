package tasks;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import status.Status;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SubtaskTest {
    @Test
    public void subtaskShouldReturnEqualsId() {
        Subtask subtask1 = new Subtask(12, "Купить молоко", "В 2 часа дня", Status.DONE);
        Subtask subtask2 = new Subtask(12, "Переезд", "в 2 часа", Status.DONE);

        Assertions.assertEquals(subtask2, subtask1,
                "Ошибка, экземпляры класса Subtask равны друг другу, если равен их id;");
    }

    @Test
    public void toStringSubtask_shouldReturnSubtaskComponentsInString() {
        Epic epic = new Epic("Приготовить ужин", "Купить продукты");
        Subtask subtask = new Subtask("Купить овощи", "Огурцы, картошка", Status.IN_PROGRESS,
                epic.getId());
        assertNotNull(subtask.toString(), "Вывод не совпадает");
    }

}
