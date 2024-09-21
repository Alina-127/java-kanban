package tasks;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import status.Status;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SubtaskTest {
    @Test //проверьте, что наследники класса Task равны друг другу, если равен их id;
    public void subtaskShouldReturnEqualsId() {
        Subtask subtask1 = new Subtask(12, "Купить молоко", "В 2 часа дня", Status.DONE);
        Subtask subtask2 = new Subtask(12, "Переезд", "в 2 часа", Status.DONE);

        Assertions.assertEquals(subtask2, subtask1,
                "Ошибка, экземпляры класса Subtask равны друг другу, если равен их id;");
    }
    @Test
    public void toStringEpic(){
        Epic epic = new Epic("Приготовить ужин", "Купить продукты");
        Subtask subtask = new Subtask("Купить овощи", "Огурцы, картошка", Status.IN_PROGRESS,
                epic.getId());
        assertNotNull(subtask.toString(), "Вывод не совпадает");
    }

//    @Test
//    public void setId(){
//        Subtask subtask1 = new Subtask(12, "Купить молоко", "В 2 часа дня", Status.DONE);
//        subtask1.setId(23);
//        assertEquals(23,subtask1.getId(),"id не совпадают");
//    }
}
