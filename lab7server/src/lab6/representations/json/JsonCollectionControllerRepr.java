package lab6.representations.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lab6.controllers.CollectionController;
import lab6.model.Dragon;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;

public class JsonCollectionControllerRepr {
    public static void read(Reader reader, CollectionController controller) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        ArrayList<JsonDragonRepr> dragonReprs = objectMapper.readValue(reader, new TypeReference<>() {});
        for (JsonDragonRepr dragon : dragonReprs) {
            controller.add(dragon.toDragon());
        }
    }

    public static void write(Writer writer, CollectionController controller) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        ArrayList<JsonDragonRepr> dragonReprs = new ArrayList<>();
        for (Dragon dragon : controller) {
            dragonReprs.add(new JsonDragonRepr(dragon));
        }
        objectMapper.writeValue(writer, dragonReprs);
    }
}
