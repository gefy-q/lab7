package lab6.representations.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lab6.model.Coordinates;
import lab6.model.DragonCave;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class JsonDragonCaveRepr {
    private final long numberOfTreasures;

    @JsonCreator
    public JsonDragonCaveRepr(@JsonProperty("numberOfTreasures") long numberOfTreasures) {
        this.numberOfTreasures = numberOfTreasures;
    }

    public JsonDragonCaveRepr(DragonCave dragonCave) {
        this(dragonCave.getNumberOfTreasures());
    }

    public DragonCave toDragonCave() {
        return new DragonCave(numberOfTreasures);
    }

    public long getNumberOfTreasures() {
        return this.numberOfTreasures;
    }

    public static DragonCave read(Reader reader) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(reader, JsonDragonCaveRepr.class).toDragonCave();
    }

    public static void write(Writer writer, DragonCave cave) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(writer, new JsonDragonCaveRepr(cave));
    }
}