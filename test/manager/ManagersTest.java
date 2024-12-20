package manager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ManagersTest {
   @Test
    public void getDefaultShouldInitializeInMemoryTaskManager() {
       Assertions.assertInstanceOf(InMemoryTaskManager.class, Managers.getDefault(), "Ошибка, значение " +
               "не является экземпляром менеджера");
   }

    @Test
    public void getDefaultHistoryShouldInitializeInMemoryHistoryManager() {
        Assertions.assertInstanceOf(InMemoryHistoryManager.class,Managers.getDefaultHistory(),"Ошибка, " +
                "значение не является экземпляром менеджера");
    }

}
