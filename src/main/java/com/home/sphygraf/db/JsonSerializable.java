package com.home.sphygraf.db;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface JsonSerializable {
    ObjectMapper objectMapper = new ObjectMapper();

    default String toJson() {
        try {
            String json = objectMapper.writeValueAsString(this);
            saveJsonToFile(json);
            return json;
        } catch (IOException e) {
            throw new RuntimeException("Error al serializar a JSON: " + e.getMessage(), e);
        }
    }

    default void saveJsonToFile(String json) {
        try {

            List<String> existingHistory = loadJsonFromFile();
            existingHistory.add(json);

            //Guardar historial actualizado
            objectMapper.writeValue(new File("history.json"), existingHistory);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar en el archivo JSON: " + e.getMessage(), e);
        }
    }

    default List<String> loadJsonFromFile() {
        try {
            File file = new File("history.json");


            if (!file.exists()) {
                return new ArrayList<>();
            }

            // Leer el historial desde el archivo
            return objectMapper.readValue(file, new TypeReference<List<String>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo JSON: " + e.getMessage(), e);
        }
    }
}
