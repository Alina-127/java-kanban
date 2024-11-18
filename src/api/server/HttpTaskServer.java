package api.server;

import api.handlers.*;
import com.sun.net.httpserver.HttpServer;
import manager.InMemoryTaskManager;


import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
        public static final int PORT = 8080;
        private HttpServer httpServer;

        public HttpTaskServer(InMemoryTaskManager taskManager) throws IOException {
            httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
            httpServer.createContext("/tasks", new TasksHttpHandler(taskManager));
            httpServer.createContext("/subtasks", new SubtasksHttpHandler(taskManager));
            httpServer.createContext("/epics", new EpicsHttpHandler(taskManager));
            httpServer.createContext("/history", new HistoryHttpHandler(taskManager));
            httpServer.createContext("/prioritized", new PrioritizedHttpHandler(taskManager));

        }

        public void start() {
            httpServer.start();
            System.out.println("HTTP-сервер запущен на " + PORT + " порту");
        }

        public void stop() {
        httpServer.stop(0);
        System.out.println("HTTP-сервер остановлен на " + PORT + " порту");
        }

    }

