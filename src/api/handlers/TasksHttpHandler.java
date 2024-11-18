package api.handlers;

import api.adapters.DurationAdapter;
import api.adapters.LocalDateTimeAdapter;
import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.InMemoryTaskManager;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class TasksHttpHandler implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    protected InMemoryTaskManager taskManager;
    protected Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    private BaseHttpHandler baseSend = new BaseHttpHandler();

    public TasksHttpHandler(InMemoryTaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Началась обработка /tasks запроса от клиента.");
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET: {
                String json = gson.toJson(taskManager.getTasks());
                baseSend.sendText(exchange,json,200);
                break;
            }
            case POST: {
                exchange.sendResponseHeaders(handlePost(exchange),0);
                if (exchange.getResponseCode() == 201) {
                    try (OutputStream os = exchange.getResponseBody()) {
                        String response = "Задача успешно добавлена";
                        os.write(response.getBytes());
                    }
                } else {
                    try (OutputStream os = exchange.getResponseBody()) {
                        String response = "Возникла ошибка";
                        os.write(response.getBytes());
                    }
                }
                break;
            }
            case GET_ID: {
                if (handleGetById(exchange).equals("Такой задачи нет")) {
                    baseSend.sendText(exchange,handleGetById(exchange),404);
                } else {
                    baseSend.sendText(exchange,handleGetById(exchange),200);
                }
                break;
            }
            case DELETE_ID: {
                exchange.sendResponseHeaders(handleDelete(exchange),-1);
                break;
            }
            default:
                baseSend.sendText(exchange, "Такого метода не существует", 505);
        }
    }

    protected Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");

        if (pathParts.length == 2 && pathParts[1].equals("tasks")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET;
            }
            if (requestMethod.equals("POST")) {
                return Endpoint.POST;
            }
        }
        if (pathParts.length == 3 && pathParts[1].equals("tasks")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_ID;
            }
            if (requestMethod.equals("DELETE")) {
                return Endpoint.DELETE_ID;
            }
        }
        return Endpoint.UNKNOWN;
    }

    protected int handlePost(HttpExchange exchange) {
        try {
            InputStream inputStream = exchange.getRequestBody();
            String json = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            Task postTask = gson.fromJson(json, Task.class);

            if (postTask == null) {
                return 406; // Неверный запрос
            }

            if (postTask.getId() == 0) {
                taskManager.addNewTask(postTask);
            } else {
                if (taskManager.getTaskByID(postTask.getId()) == null) {
                    try {
                        taskManager.addNewTask(postTask);
                    } catch (RuntimeException exception) {
                        taskManager.deleteTaskById(postTask.getId());
                        return 406;
                    }

                } else {
                    taskManager.updateTask(postTask);
                }
            }
            System.out.println(taskManager);
            return 201;
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
            return 500; // Внутренняя ошибка сервера
        }
    }

    protected String handleGetById(HttpExchange exchange) {
            String path = exchange.getRequestURI().getPath();
            String[] splitStrings = path.split("/");
            String id = splitStrings[2];
            String json = gson.toJson(taskManager.getTaskByID(Integer.parseInt(id)));
            if (taskManager.getTaskByID(Integer.parseInt(id)) == null) {
                return "Такой задачи нет";
            } else {
                return json;
            }
    }

    protected int handleDelete(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        String[] splitStrings = path.split("/");
        String id = splitStrings[2];
        if (taskManager.getTaskByID(Integer.parseInt(id)) == null) {
            return 404;
        } else {
            taskManager.deleteTaskById(Integer.parseInt(id));
            return 204;
        }
    }

}
