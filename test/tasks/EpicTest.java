package tasks;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class EpicTest {
    @Test //проверьте, что наследники класса Task равны друг другу, если равен их id;
    public void EpicShouldReturnEqualsId() {
        Epic epic1 = new Epic(12, "Купить молоко", "В 2 часа дня");
        Epic epic2 = new Epic(12, "Переезд", "в 2 часа");

        Assertions.assertEquals(epic2, epic1,
                "Ошибка, экземпляры класса Subtask равны друг другу, если равен их id;");
    }

    @Test
    public void toStringEpic() {
        Epic epic = new Epic("Приготовить ужин", "Купить продукты");
        assertNotNull(epic.toString(), "Вывод не совпадает");
    }
}
