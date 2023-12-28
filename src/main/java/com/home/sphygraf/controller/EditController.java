package com.home.sphygraf.controller;

import com.home.sphygraf.db.Memo;
import com.home.sphygraf.task.EditImageTask;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
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
    private Button saveImage;
    @FXML
    private Label status;
    @FXML
    private ImageView originalImage;
    @FXML
    private ImageView finalImage;

    private Image originalImageSelected;
    private String pathOriginal;
    private Image editedImage;
    EditImageTask editImageTask;

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

    public EditController(Image originalImageSelected, String pathOriginal) {
        this.originalImageSelected = originalImageSelected;
        this.pathOriginal = pathOriginal;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Carga la imagen en el imageviewer
        originalImage.setImage(originalImageSelected);
    }

    @FXML
    public void applyFilters() {

        Image imagenOrigen = originalImageSelected;

        //Crea una nuevo hilo
        editImageTask = new EditImageTask(imagenOrigen, bAndW.isSelected(), invertColors.isSelected(), shineUp.isSelected(), applyBlurred.isSelected(), InvertH.isSelected(), InvertV.isSelected(), pathOriginal);


        status.setVisible(true);
        progress.setVisible(true);
        progress.progressProperty().bind(editImageTask.progressProperty());


        editImageTask.setOnSucceeded(event -> {
            editedImage = editImageTask.getValue();
            finalImage.setImage(editedImage);
            saveImage.setVisible(true);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("La edición ha finalizado");
            alert.show();
            //TODO mostrar el nombre del archivo en la alerta

        });

        editImageTask.messageProperty().addListener((observable, oldMessage, newMessage) -> {
            status.setText(newMessage);
        });

        //inicia el hilo
        new Thread(editImageTask).start();
    }

    //TODO insertar boton de cancelar

    @FXML
    public void saveImage() throws IOException {
        String inputPath = pathOriginal;
        BufferedImage imagenEditadaBuffered = toBufferedImage(editedImage);
        String outputName = inputPath.substring(0, inputPath.length() - 4) + "_1.png";

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Imagen");
        fileChooser.setInitialFileName(outputName);

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Archivos PNG (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);

        //abre el mismo directorio del archivo original
        File directorioOriginal = new File(inputPath).getParentFile();
        if (directorioOriginal != null) {
            fileChooser.setInitialDirectory(directorioOriginal);
        }


        File archivoDestino = fileChooser.showSaveDialog(finalImage.getScene().getWindow());

        String fileNameWithExtension = archivoDestino.getName();
        ImageIO.write(imagenEditadaBuffered, "png", archivoDestino);
        //Guarda la edición en el historial
        Memo memo = new Memo(fileNameWithExtension,bAndW.isSelected(),invertColors.isSelected(),shineUp.isSelected(),InvertH.isSelected(),InvertV.isSelected(),applyBlurred.isSelected(),LocalDate.now());
        memo.toJson();


    }

    public static BufferedImage toBufferedImage(Image fxImage) {
        return SwingFXUtils.fromFXImage(fxImage, null);
    }

    @FXML
    protected void cancelFilters() {

        bAndW.setSelected(false);
        invertColors.setSelected(false);
        shineUp.setSelected(false);
        applyBlurred.setSelected(false);
        InvertH.setSelected(false);
        InvertV.setSelected(false);

        finalImage.setImage(null);
        saveImage.setVisible(false);
        editImageTask.cancel();
        progress.setVisible(false);
        status.setVisible(false);

    }

}
