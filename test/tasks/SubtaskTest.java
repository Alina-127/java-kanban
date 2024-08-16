package tasks;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import status.Status;

public class SubtaskTest {
    @Test //проверьте, что наследники класса Task равны друг другу, если равен их id;
    public void subtaskShouldReturnEqualsId() {
        Subtask subtask1 = new Subtask(12, "Купить молоко", "В 2 часа дня", Status.DONE);
        Subtask subtask2 = new Subtask(12, "Переезд", "в 2 часа", Status.DONE);

        Assertions.assertEquals(subtask2, subtask1,
                "Ошибка, экземпляры класса Subtask равны друг другу, если равен их id;");
    }
}
