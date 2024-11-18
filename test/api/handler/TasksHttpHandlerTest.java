package api.handler;

import api.adapters.DurationAdapter;
import api.adapters.LocalDateTimeAdapter;
import api.server.HttpTaskServer;
import com.google.gson.*;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TasksHttpHandlerTest {
   InMemoryTaskManager taskManager = new InMemoryTaskManager();
    // передаём его в качестве аргумента в конструктор HttpTaskServer
    HttpTaskServer taskServer = new HttpTaskServer(taskManager);

    protected Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    public TasksHttpHandlerTest() throws IOException {
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
    public void getTask_shouldReturnCode() throws IOException, InterruptedException {
        Task task = new Task("Переезд", "в 2 часа", Status.NEW, Duration.ofMinutes(45),
                LocalDateTime.of(2008, 12, 13, 14, 20));
        taskManager.addNewTask(task);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
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
    public void postTask_shouldReturnTask() throws IOException, InterruptedException {
        Task task = new Task(1,"Test 2", "Testing task 2",
                Status.NEW, Duration.ofMinutes(45),
                LocalDateTime.of(2008, 12, 13, 14, 20));
        // конвертируем её в JSON
        String taskJson = gson.toJson(task);
        System.out.println(taskJson);
        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(taskManager);
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Task> tasksFromManager = taskManager.getTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void postTask_shouldReturn500() throws RuntimeException, IOException, InterruptedException {
            Task task = new Task("Переезд", "в 2 часа", Status.NEW, Duration.ofMinutes(45),
                    LocalDateTime.of(2008, 12, 13, 14, 20));
            taskManager.addNewTask(task);
            Task task2 = new Task("Переезд", "в 2 часа", Status.NEW, Duration.ofMinutes(45),
                    LocalDateTime.of(2008, 12, 13, 14, 20));
            String taskJson = gson.toJson(task2);

            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create("http://localhost:8080/tasks");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .header("Content-Type", "application/json") // Добавляем заголовок
                    .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Проверяем код ответа
            assertEquals(500, response.statusCode(), "Ожидался статус 500");
    }

    @Test
    public void postTask_shouldReturn406() throws RuntimeException, IOException, InterruptedException {
        Task task = new Task("Переезд", "в 2 часа", Status.NEW, Duration.ofMinutes(45),
                LocalDateTime.of(2008, 12, 13, 14, 20));
        taskManager.addNewTask(task);
        String taskJson = gson.toJson(null);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
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
    public void postUpdateTaskById_shouldReturn201() throws RuntimeException, IOException, InterruptedException {
        Task task = new Task("Переезд", "в 2 часа", Status.NEW, Duration.ofMinutes(45),
                LocalDateTime.of(2008, 12, 13, 14, 20));
        taskManager.addNewTask(task);
        Task task2 = new Task(task.getId(),"Шопинг", "в 2 часа", Status.NEW, Duration.ofMinutes(45),
                LocalDateTime.of(2009, 12, 13, 14, 20));
        String taskJson = gson.toJson(task2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json") // Добавляем заголовок
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Проверяем код ответа
        assertEquals(201, response.statusCode(), "Ожидался статус 201");
    }

    @Test
    public void postTaskById_shouldReturn201() throws RuntimeException, IOException, InterruptedException {
        Task task2 = new Task(1,"Шопинг", "в 2 часа", Status.NEW, Duration.ofMinutes(45),
                LocalDateTime.of(2009, 12, 13, 14, 20));

        String taskJson = gson.toJson(task2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json") // Добавляем заголовок
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Проверяем код ответа
        assertEquals(201, response.statusCode(), "Ожидался статус 201");
    }

    @Test
    public void getById_shouldReturnTask() throws IOException, InterruptedException {
        // Сначала добавим задачу
        Task task = new Task("Переезд", "в 2 часа", Status.NEW, Duration.ofMinutes(45),
                LocalDateTime.of(2008, 12, 13, 14, 20));
        taskManager.addNewTask(task);
        // Теперь получим задачу по ID
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + task.getId());
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

        Task returnedTask = gson.fromJson(jsonResponse, Task.class);
        assertEquals("Переезд", returnedTask.getName(), "Name должно совпадать");
        assertEquals("в 2 часа", returnedTask.getDescription(), "Description должно совпадать");
    }

    @Test
    public void getById_shouldReturnNotFound() throws IOException, InterruptedException {
        // Попробуем получить задачу, которая не существует
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/999");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int responseCode = response.statusCode();
        assertEquals(404, responseCode, "Ожидался статус 404");
    }

    @Test
    public void deleteById_shouldReturnNull() throws IOException, InterruptedException {
        // Сначала добавим задачу
        Task task = new Task("Переезд", "в 2 часа", Status.NEW, Duration.ofMinutes(45),
                LocalDateTime.of(2008, 12, 13, 14, 20));
        taskManager.addNewTask(task);
        // Теперь получим задачу по ID
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + task.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .header("Content-Type", "application/json;charset=utf-8")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Проверяем код ответа
        assertEquals(204, response.statusCode(), "Ожидался статус 200");

        assertNull(taskManager.getTaskByID(task.getId()), "Задачи должны быть пустыми");
    }
}



