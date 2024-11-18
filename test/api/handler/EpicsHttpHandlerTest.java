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
import tasks.Epic;
import tasks.Subtask;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EpicsHttpHandlerTest {
    InMemoryTaskManager taskManager = new InMemoryTaskManager();
    // передаём его в качестве аргумента в конструктор HttpTaskServer
    HttpTaskServer taskServer = new HttpTaskServer(taskManager);

    protected Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    public EpicsHttpHandlerTest() throws IOException {
    }

    @BeforeEach
    public void setUp(){
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
    public void getEpics_shouldReturnCode() throws IOException, InterruptedException {
        Epic epic = new Epic("Приготовить ужин", "Купить продукты");
        taskManager.addNewEpic(epic);

        Subtask subtask = new Subtask("Купить овощи", "Огурцы", Status.IN_PROGRESS,
                Duration.ofMinutes(30),
                LocalDateTime.of(2002,7,15,10,0),
                epic.getId());
        taskManager.addNewSubtask(subtask);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
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
    @Test
    public void postEpic_shouldReturnSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 2", "Купить продукты");
        String taskJson = gson.toJson(epic);
        System.out.println(taskJson);
        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(taskManager);
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Epic> epicsFromManager = taskManager.getEpics();

        assertNotNull(epicsFromManager, "Задачи не возвращаются");
        assertEquals(1, epicsFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", epicsFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void postEpic_shouldReturn406() throws RuntimeException, IOException, InterruptedException {
        Epic epic = new Epic("Приготовить ужин", "Купить продукты");
        taskManager.addNewEpic(epic);

        String taskJson = gson.toJson(null);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json") // Добавляем заголовок
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Проверяем код ответа
        assertEquals(406, response.statusCode(), "Ожидался статус 406");
    }

    @Test
    public void getById_shouldReturnEpic() throws IOException, InterruptedException {
        // Сначала добавим задачу
        Epic epic = new Epic("Приготовить ужин", "Купить продукты");
        taskManager.addNewEpic(epic);

        // Теперь получим задачу по ID
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epic.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .header("Content-Type", "application/json;charset=utf-8")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Проверяем код ответа
        assertEquals(200, response.statusCode(), "Ожидался статус 200");

        // Проверяем, что возвращается корректный JSON
        String jsonResponse = response.body();
        assertNotNull(jsonResponse, "Тело не должно быть пустым");
    }

    @Test
    public void getById_shouldReturnNotFound() throws IOException, InterruptedException {
        // Попробуем получить задачу, которая не существует
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/999");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int responseCode = response.statusCode();
        assertEquals(404, responseCode, "Ожидался статус 404");
    }

    @Test
    public void getSubtaskById_shouldReturnSubtask() throws IOException, InterruptedException {
        // Сначала добавим задачу
        Epic epic = new Epic("Приготовить ужин", "Купить продукты");
        taskManager.addNewEpic(epic);
        Subtask subtask = new Subtask("Купить овощи", "Огурцы", Status.IN_PROGRESS,
                Duration.ofMinutes(30),
                LocalDateTime.of(2002,7,15,10,0),
                epic.getId());
        taskManager.addNewSubtask(subtask);

        // Теперь получим задачу по ID
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epic.getId() + "/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .header("Content-Type", "application/json;charset=utf-8")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Проверяем код ответа
        assertEquals(200, response.statusCode(), "Ожидался статус 200");

        // Проверяем, что возвращается корректный JSON
        String jsonResponse = response.body();
        assertNotNull(jsonResponse, "Тело не должно быть пустым");
    }

    @Test
    public void deleteById_shouldReturnNull() throws IOException, InterruptedException {
        // Сначала добавим задачу
        Epic epic = new Epic("Приготовить ужин", "Купить продукты");
        taskManager.addNewEpic(epic);

        // Теперь получим задачу по ID
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epic.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .header("Content-Type", "application/json;charset=utf-8")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Проверяем код ответа
        assertEquals(204, response.statusCode(), "Ожидался статус 200");

        assertNull(taskManager.getEpicByID(epic.getId()), "Задачи должны быть пустыми");
    }
}
