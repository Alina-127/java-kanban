package api.handlers;

import com.sun.net.httpserver.HttpExchange;
import manager.InMemoryTaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public class BaseHttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    protected void sendText(HttpExchange h, String text, int rCode) throws IOException {
        byte[] resp = text.getBytes(DEFAULT_CHARSET);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(rCode, resp.length);
        try (OutputStream os = h.getResponseBody()) {
            os.write(resp);
        }
        h.close();
    }
}
