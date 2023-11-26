package com.home.sphygraf;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class IndexController implements Initializable {

    @FXML
    private MenuItem seleccionarImagen;

    private Image imagenOriginal;
    @FXML
    private CheckBox blancoNegro;
    @FXML
    private CheckBox invertirColores;
    @FXML
    private CheckBox aumentarBrillo;
    @FXML
    private CheckBox aplicarDifuminado;
    @FXML
    private CheckBox InvertirH;
    @FXML
    private CheckBox InvertirV;
    @FXML
    private Button seleccionarMulti;
    @FXML
    private TabPane tpEditImage;

    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    protected void seleccionarArchivo() {
        // Abrir la ventana para seleccionar imágenes
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de Imagen", "*.png", "*.jpg", "*.gif"));

        File selectedFile = fileChooser.showOpenDialog(null);

        // Verificar si se seleccionó un archivo
        if (selectedFile != null) {
            // Crear una nueva pestaña
            Tab newTab = new Tab(selectedFile.getName());

            // Crear una nueva imagen para esta pestaña
            Image imageTab = new Image(selectedFile.toURI().toString());

            // Cargar el controlador de la pestaña
            FXMLLoader loader = new FXMLLoader(getClass().getResource("editImage.fxml"));
            loader.setController(new EditController(imageTab, selectedFile.getPath()));



            try {
                // Establecer el contenido de la pestaña
                newTab.setContent(loader.load());

                // Agregar la nueva pestaña al TabPane
                tpEditImage.getTabs().add(newTab);

                // Seleccionar la nueva pestaña
                tpEditImage.getSelectionModel().select(newTab);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
