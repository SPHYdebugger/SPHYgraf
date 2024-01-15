package com.home.sphygraf.controller;

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


    private void loadHistoryToListView() {
        List<String> historyList = loadJsonFromFile();
        historyView.getItems().addAll(historyList);
    }

    private List<String> loadJsonFromFile() {
        try {
            File file = new File("history.json");
            if (!file.exists()) {
                return new ArrayList<>();
            }

            // convertir los registros para visualizarlos
            return objectMapper.readValue(file, new TypeReference<List<String>>() {});

        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo JSON: " + e.getMessage(), e);
        }
    }
}
