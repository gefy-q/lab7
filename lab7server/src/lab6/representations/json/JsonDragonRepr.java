package lab6.representations.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lab6.model.Coordinates;
import lab6.model.Dragon;
import lab6.model.DragonCave;
import lab6.model.DragonCharacter;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.time.LocalDateTime;

public class JsonDragonRepr {
    private final Integer id;
    private final String name;
    private final JsonCoordinatesRepr coordinates;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime creationDate;
    private final Integer age;
    private final String description;
    private final Double wingspan;
    private final DragonCharacter character;
    private final JsonDragonCaveRepr cave;

    @JsonCreator
    public JsonDragonRepr(
            @JsonProperty("id") Integer id,
            @JsonProperty("name") String name,
            @JsonProperty("coordinates") JsonCoordinatesRepr coordinates,
            @JsonProperty("creationDate") LocalDateTime creationDate,
            @JsonProperty("age") Integer age,
            @JsonProperty("description") String description,
            @JsonProperty("wingspan") Double wingspan,
            @JsonProperty("character") DragonCharacter character,
            @JsonProperty("cave") JsonDragonCaveRepr cave
    ) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.age = age;
        this.description = description;
        this.wingspan = wingspan;
        this.character = character;
        this.cave = cave;
    }

    public JsonDragonRepr(Dragon dragon) {
        this(dragon.getId(), dragon.getName(), new JsonCoordinatesRepr(dragon.getCoordinates()), dragon.getCreationDate(), dragon.getAge(), dragon.getDescription(), dragon.getWingspan(), dragon.getCharacter(), new JsonDragonCaveRepr(dragon.getCave()));
    }

    public Dragon toDragon() {
        return new Dragon(id, name, coordinates.toCoordinates(), creationDate, age, description, wingspan, character, cave.toDragonCave());
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public JsonCoordinatesRepr getCoordinates() {
        return coordinates;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public Integer getAge() {
        return age;
    }

    public String getDescription() {
        return description;
    }

    public Double getWingspan() {
        return wingspan;
    }

    public DragonCharacter getCharacter() {
        return character;
    }

    public JsonDragonCaveRepr getCave() {
        return cave;
    }

    public static Dragon read(Reader reader) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(reader, JsonDragonRepr.class).toDragon();
    }

    public static void write(Writer writer, Dragon dragon) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.writeValue(writer, new JsonDragonRepr(dragon));
    }
}
