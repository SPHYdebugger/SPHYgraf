package com.home.sphygraf;

import com.home.sphygraf.task.EditImageTask;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
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
        originalImage.setImage(originalImageSelected);
    }

    @FXML
    protected void applyFilters() {

        Image imagenOrigen = originalImageSelected;

        // Configurar la tarea EditImageTask
        editImageTask = new EditImageTask(imagenOrigen, bAndW.isSelected(), invertColors.isSelected(), shineUp.isSelected(), applyBlurred.isSelected(), InvertH.isSelected(), InvertV.isSelected(), pathOriginal);

        // Configurar la ProgressBar
        status.setVisible(true);
        progress.setVisible(true);
        progress.progressProperty().bind(editImageTask.progressProperty());

        // Configurar la acción al finalizar la tarea
        editImageTask.setOnSucceeded(event -> {
            editedImage = editImageTask.getValue();
            finalImage.setImage(editedImage);
            saveImage.setVisible(true);

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

    @FXML
    public void saveImage() throws IOException {
        String inputPath = pathOriginal;
        BufferedImage imagenEditadaBuffered = toBufferedImage(editedImage);
        String outputName = inputPath.substring(0, inputPath.length() - 4) + "_1.png";
        // Configura el DirectoryChooser con la ruta de la imagen original como directorio inicial
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Imagen");
        fileChooser.setInitialFileName(outputName);
        // Configura la extensión predeterminada
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Archivos PNG (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);

        // Obtiene la ruta del directorio del archivo original
        File directorioOriginal = new File(inputPath).getParentFile();
        if (directorioOriginal != null) {
            fileChooser.setInitialDirectory(directorioOriginal);
        }

        // Muestra el diálogo y obtiene el archivo de destino
        File archivoDestino = fileChooser.showSaveDialog(finalImage.getScene().getWindow());

        // Guarda la BufferedImage en el archivo de destino
        if (archivoDestino != null) {
            try {
                ImageIO.write(imagenEditadaBuffered, "png", archivoDestino);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static BufferedImage toBufferedImage(Image fxImage) {
        int width = (int) fxImage.getWidth();
        int height = (int) fxImage.getHeight();

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        int[] buffer = new int[width * height];

        PixelReader pixelReader = fxImage.getPixelReader();
        pixelReader.getPixels(0, 0, width, height, javafx.scene.image.PixelFormat.getIntArgbInstance(), buffer, 0, width);

        DataBufferInt dataBuffer = ((DataBufferInt) bufferedImage.getRaster().getDataBuffer());
        System.arraycopy(buffer, 0, dataBuffer.getData(), 0, buffer.length);

        return bufferedImage;
    }

    @FXML
    protected void cancelFilters() {
        // Anula los filtros seleccionados
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
