package api.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.time.Duration;

import java.io.IOException;

public class DurationAdapter extends TypeAdapter<Duration> {

    @Override
    public void write(JsonWriter jsonWriter, Duration duration) {
        try {
            if (duration == null) {
                jsonWriter.value(0);
            } else {
                jsonWriter.value(duration.toMinutes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Duration read(JsonReader jsonReader) {
        try {
            return Duration.ofMinutes(jsonReader.nextInt());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}