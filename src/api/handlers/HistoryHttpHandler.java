package api.handlers;

import api.adapters.DurationAdapter;
import api.adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.InMemoryTaskManager;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class HistoryHttpHandler implements HttpHandler {
    protected InMemoryTaskManager taskManager;
    protected Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    private BaseHttpHandler baseSend = new BaseHttpHandler();

    public HistoryHttpHandler(InMemoryTaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Началась обработка /epics запроса от клиента.");
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET: {
                String json = gson.toJson(taskManager.getHistory());
                baseSend.sendText(exchange, json, 200);
                break;
            }
            default:
                baseSend.sendText(exchange, "Такого метода не существует", 505);
        }
    }


    protected Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");

        if (pathParts.length == 2 && pathParts[1].equals("history")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET;
            }
        }
        return Endpoint.UNKNOWN;
    }
}
