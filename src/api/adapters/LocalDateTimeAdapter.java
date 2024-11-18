package api.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import manager.FileBackedTaskManager;

import java.io.IOException;
import java.time.LocalDateTime;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {

    @Override
    public void write(JsonWriter out, LocalDateTime value) throws IOException {
        out.value(value != null ? value.toString() : null);
    }

    @Override
    public LocalDateTime read(JsonReader in) throws IOException {
        String date = in.nextString();
        if ("0".equals(date)) {
            return null; // Или возвращаем текущее время
        }
        return LocalDateTime.parse(date);
    }
}
