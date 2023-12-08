package com.home.sphygraf;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class MultiController implements Initializable {

    @FXML
    private MenuItem selectImage;
    @FXML
    private MenuItem selectImages;
    @FXML
    private Button selectOne;
    @FXML
    private Button selectMulti;
    @FXML
    private CheckBox bAndW;
    @FXML
    private CheckBox invertColors;
    @FXML
    private CheckBox shineUp;
    @FXML
    private CheckBox applyBlurred;
    @FXML
    private CheckBox InvertH;
    @FXML
    private CheckBox InvertV;
    @FXML
    private TabPane tpEditImage;
    @FXML
    private Button applyMulti;

    private Stage primaryStage;
    private Image imagenOriginal;
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    protected void selectOneFile() {
        // Cuadro de diálogo para seleccionar imágenes
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

    @FXML
    public void showOptions(){
        bAndW.setVisible(true);
        invertColors.setVisible(true);
        shineUp.setVisible(true);
        applyBlurred.setVisible(true);
        InvertH.setVisible(true);
        InvertV.setVisible(true);
        applyMulti.setVisible(true);

    }

    @FXML
    public void launchBatchEdit(ActionEvent event) throws Exception {

        Stage stage = (Stage) this.applyMulti.getScene().getWindow();
        FileChooser fc = new FileChooser();
        File file = fc.showOpenDialog(stage);
        Scanner sc;
        try {
            sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String pathText = sc.nextLine();
                System.out.println(pathText);

                // Cargar la imagen desde el archivo
                File imageFile = new File(pathText);
                Image image = new Image(imageFile.toURI().toString());

                FXMLLoader loader = new FXMLLoader(getClass().getResource("editImage.fxml"));
                EditController editController = new EditController(image, pathText);
                loader.setController(editController);
                String tabName = pathText.substring(pathText.lastIndexOf("/") + 1);

                try {
                    tpEditImage.getTabs().add(new Tab(tabName, loader.load()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (this.bAndW.isSelected()) {
                    editController.setbAndW(this.bAndW);
                }
                if (this.invertColors.isSelected()) {
                    editController.setInvertColors(this.invertColors);
                }
                if (this.shineUp.isSelected()) {
                    editController.setShineUp(this.shineUp);
                }
                if (this.applyBlurred.isSelected()) {
                    editController.setApplyBlurred(this.applyBlurred);
                }
                if (this.InvertH.isSelected()) {
                    editController.setInvertH(this.InvertH);
                }
                if (this.InvertV.isSelected()) {
                    editController.setInvertV(this.InvertV);
                }


                editController.applyFilters();

            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.tpEditImage.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
    }
}
