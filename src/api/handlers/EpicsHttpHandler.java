package api.handlers;

import api.adapters.DurationAdapter;
import api.adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.InMemoryTaskManager;
import tasks.Epic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class EpicsHttpHandler implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    protected InMemoryTaskManager taskManager;
    protected Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    private BaseHttpHandler baseSend = new BaseHttpHandler();
    public EpicsHttpHandler(InMemoryTaskManager taskManager) {
        this.taskManager = taskManager;;
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Началась обработка /epics запроса от клиента.");
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET: {
                String json = gson.toJson(taskManager.getEpics());
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
                if (handleGetById(exchange).equals("Такого эпика нет")) {
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
            case GET_ID_SUB: {
                if (handleGetByIdSubtasks(exchange).equals("Такого эпика нет")) {
                    baseSend.sendText(exchange,handleGetByIdSubtasks(exchange),404);
                } else {
                    baseSend.sendText(exchange,handleGetByIdSubtasks(exchange),200);
                }
                break;
            }
            default:
                baseSend.sendText(exchange, "Такого метода не существует", 505);
        }
    }




    protected Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");

        if (pathParts.length == 2 && pathParts[1].equals("epics")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET;
            }
            if (requestMethod.equals("POST")) {
                return Endpoint.POST;
            }
        }
        if (pathParts.length == 3 && pathParts[1].equals("epics")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_ID;
            }
            if (requestMethod.equals("DELETE")) {
                return Endpoint.DELETE_ID;
            }
        }
        if (pathParts.length == 4 && pathParts[1].equals("epics")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_ID_SUB;
            }
        }
        return Endpoint.UNKNOWN;
    }

    protected int handlePost(HttpExchange exchange) throws IOException {
            InputStream inputStream = exchange.getRequestBody();
            String json = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            Epic postEpic = gson.fromJson(json, Epic.class);

            if (postEpic == null) {
                return 406; // Неверный запрос
            }

            if (postEpic.getSubtasks() == null) {
                postEpic.setSubtasks(new ArrayList<>());
            }
            taskManager.addNewEpic(postEpic);
            return 201;
    }

    protected String handleGetById(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        String[] splitStrings = path.split("/");
        String id = splitStrings[2];
        String json = gson.toJson(taskManager.getEpicByID(Integer.parseInt(id)));
        if (taskManager.getEpicByID(Integer.parseInt(id)) == null) {
            return "Такого эпика нет";
        } else {
            return json;
        }
    }

    protected String handleGetByIdSubtasks(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        String[] splitStrings = path.split("/");
        String id = splitStrings[2];
        if (taskManager.getEpicByID(Integer.parseInt(id)) == null) {
            return "Такого эпика нет";
        } else {
            return gson.toJson(taskManager.getEpicSubtasks(taskManager.getEpicByID(Integer.parseInt(id))));
        }
    }

    protected int handleDelete(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        String[] splitStrings = path.split("/");
        String id = splitStrings[2];
        if (taskManager.getEpicByID(Integer.parseInt(id)) == null) {
            return 500;
        } else {
            taskManager.deleteEpicById(Integer.parseInt(id));
            return 204;
        }
    }

}
