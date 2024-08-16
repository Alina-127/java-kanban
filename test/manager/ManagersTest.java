package manager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import status.Status;
public class ManagersTest {
   @Test //убедитесь, что утилитарный класс всегда возвращает проинициализированные и
   // готовые к работе экземпляры менеджеров;
    public void getDefaultShouldInitializeInMemoryTaskManager() {
       Assertions.assertInstanceOf(InMemoryTaskManager.class, Managers.getDefault(), "Ошибка, значение " +
               "не является экземпляром менеджера");
   }
    @Test //убедитесь, что утилитарный класс всегда возвращает проинициализированные и
    // готовые к работе экземпляры менеджеров;
    public void getDefaultHistoryShouldInitializeInMemoryHistoryManager(){
        Assertions.assertInstanceOf(InMemoryHistoryManager.class,Managers.getDefaultHistory(),"Ошибка, " +
                "значение не является экземпляром менеджера");
    }

}
