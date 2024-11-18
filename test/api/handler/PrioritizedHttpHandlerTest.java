package api.handler;

import api.adapters.DurationAdapter;
import api.adapters.LocalDateTimeAdapter;
import api.server.HttpTaskServer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import status.Status;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrioritizedHttpHandlerTest {
    InMemoryTaskManager taskManager = new InMemoryTaskManager();
    // передаём его в качестве аргумента в конструктор HttpTaskServer
    HttpTaskServer taskServer = new HttpTaskServer(taskManager);

    protected Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    public PrioritizedHttpHandlerTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        taskManager.deleteTasks();
        taskManager.deleteSubtasks();
        taskManager.deleteEpics();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void getTask_shouldReturnCode() throws IOException, InterruptedException {
        Task task = new Task("Переезд", "в 2 часа", Status.NEW, Duration.ofMinutes(45),
                LocalDateTime.of(2008, 12, 13, 14, 20));
        taskManager.addNewTask(task);
        Task task3 = new Task("Переезд", "в 2 часа", Status.NEW, Duration.ofMinutes(45),
                LocalDateTime.of(2001,12,13,14,20));
        taskManager.addNewTask(task3);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .header("Content-Type", "application/json;charset=utf-8")
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        // отправляем запрос и получаем ответ от сервера
        HttpResponse<String> response = client.send(request, handler);
        // проверяем код ответа
        assertEquals(200, response.statusCode());
    }
}

