package com.home.sphygraf;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.sphygraf.db.Memo;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ListController implements Initializable {

    @FXML
    private ListView<String> historyView;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        loadHistoryToListView();
    }

    // Método para cargar el historial desde el archivo y mostrarlo en el ListView
    private void loadHistoryToListView() {
        // Obtener el historial desde el método loadJsonFromFile
        List<String> historyList = loadJsonFromFile();

        // Mostrar el historial en el ListView
        historyView.getItems().addAll(historyList);

    }

    private List<String> loadJsonFromFile() {
        try {
            File file = new File("history.json");

            if (!file.exists()) {
                return new ArrayList<>();
            }

            // Convertir la lista de Memo a una lista de cadenas JSON
            return objectMapper.readValue(file, new TypeReference<List<String>>() {});

        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo JSON: " + e.getMessage(), e);
        }
    }
}
