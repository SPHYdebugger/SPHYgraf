package com.home.sphygraf;

import com.home.sphygraf.task.EditImageTask;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class EditController implements Initializable {


    @FXML
    private CheckBox bAndW;
    @FXML
    private CheckBox InvertH;
    @FXML
    private TabPane tpEditImage;
    @FXML
    private ProgressBar progress;

    public void setbAndW(CheckBox bAndW) {
        this.bAndW = bAndW;
    }

    public void setInvertH(CheckBox invertH) {
        this.InvertH = invertH;
    }

    public void setInvertV(CheckBox invertV) {
        this.InvertV = invertV;
    }

    public void setInvertColors(CheckBox invertColors) {
        this.invertColors = invertColors;
    }

    public void setShineUp(CheckBox shineUp) {
        this.shineUp = shineUp;
    }

    public void setApplyBlurred(CheckBox applyBlurred) {
        this.applyBlurred = applyBlurred;
    }

    @FXML
    private CheckBox InvertV;
    @FXML
    private CheckBox invertColors;
    @FXML
    private CheckBox shineUp;
    @FXML
    private CheckBox applyBlurred;
    @FXML
    private Button Apply;
    @FXML
    private Label status;
    @FXML
    private ImageView originalImage;
    @FXML
    private ImageView finalImage;

    private Image originalImageSelected;
    private String pathOriginal;
    private Image editedImage;

    public EditController(Image originalImageSelected, String pathOriginal) {
        this.originalImageSelected = originalImageSelected;
        this.pathOriginal = pathOriginal;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        originalImage.setImage(originalImageSelected);
    }

    @FXML
    protected void applyFilters() {

        Image imagenOrigen = originalImageSelected;

        // Configurar la tarea EditImageTask
        EditImageTask editImageTask = new EditImageTask(imagenOrigen, bAndW.isSelected(), invertColors.isSelected(), shineUp.isSelected(), applyBlurred.isSelected(), InvertH.isSelected(), InvertV.isSelected(), pathOriginal);

        // Configurar la ProgressBar
        progress.progressProperty().bind(editImageTask.progressProperty());

        // Configurar la acción al finalizar la tarea
        editImageTask.setOnSucceeded(event -> {
            editedImage = editImageTask.getValue();
            finalImage.setImage(editedImage);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("La edición ha finalizado");
            alert.show();
            //TODO mostrar el nombre del archivo

        });

        // Configurar la acción al cambiar el mensaje de la tarea
        editImageTask.messageProperty().addListener((observable, oldMessage, newMessage) -> {

            // Actualizar el mensaje
            status.setText(newMessage);
        });

        // Iniciar la tarea en un nuevo hilo
        new Thread(editImageTask).start();
    }

    //TODO insertar boton de cancelar
    //TODO poder cerrar las pestañas
    //TODO guardar imagen modificada



}
