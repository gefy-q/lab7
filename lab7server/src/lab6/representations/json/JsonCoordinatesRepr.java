package lab6.representations.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lab6.model.Coordinates;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class JsonCoordinatesRepr {
    private final Long x;
    private final double y;

    @JsonCreator
    public JsonCoordinatesRepr(
            @JsonProperty("x") Long x,
            @JsonProperty("y") double y
    ) {
        this.x = x;
        this.y = y;
    }

    public JsonCoordinatesRepr(Coordinates coordinates) {
        this(coordinates.getX(), coordinates.getY());
    }

    public Coordinates toCoordinates() {
        return new Coordinates(x, y);
    }

    public Long getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public static Coordinates read(Reader reader) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(reader, JsonCoordinatesRepr.class).toCoordinates();
    }

    public static void write(Writer writer, Coordinates coordinates) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(writer, new JsonCoordinatesRepr(coordinates));
    }
}
