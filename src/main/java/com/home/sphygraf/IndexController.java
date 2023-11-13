package com.home.sphygraf;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class IndexController implements Initializable {


    @FXML
    private MenuItem SelectImage;
    @FXML
    private ImageView contenedorImagen;
    private Stage primaryStage;

    // Método para establecer el Stage
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    protected void seleccionarArchivo() {
        // Configurar el cuadro de diálogo para seleccionar imágenes
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de Imagen", "*.png", "*.jpg", "*.gif"));

        File selectedFile = fileChooser.showOpenDialog(null);

        // Verificar si se seleccionó un archivo
        if (selectedFile != null) {
            // Cargar la imagen seleccionada en el ImageView
            Image imagen = new Image(selectedFile.toURI().toString());
            contenedorImagen.setImage(imagen);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
