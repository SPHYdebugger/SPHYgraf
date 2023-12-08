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
    private CheckBox blancoNegro;
    @FXML
    private CheckBox InvertirH;
    @FXML
    private TabPane tpEditImage;
    @FXML
    private ProgressBar progreso;

    public void setBlancoNegro(CheckBox blancoNegro) {
        this.blancoNegro = blancoNegro;
    }

    public void setInvertirH(CheckBox invertirH) {
        InvertirH = invertirH;
    }

    public void setInvertirV(CheckBox invertirV) {
        InvertirV = invertirV;
    }

    public void setInvertirColores(CheckBox invertirColores) {
        this.invertirColores = invertirColores;
    }

    public void setAumentarBrillo(CheckBox aumentarBrillo) {
        this.aumentarBrillo = aumentarBrillo;
    }

    public void setAplicarDifuminado(CheckBox aplicarDifuminado) {
        this.aplicarDifuminado = aplicarDifuminado;
    }

    @FXML
    private CheckBox InvertirV;
    @FXML
    private CheckBox invertirColores;
    @FXML
    private CheckBox aumentarBrillo;
    @FXML
    private CheckBox aplicarDifuminado;
    @FXML
    private Button Aplicar;
    @FXML
    private Label status;
    @FXML
    private ImageView imagenOriginal;
    @FXML
    private ImageView imagenFinal;

    private Image imagenOriginalSeleccionada;
    private String pathOriginal;
    private Image imagenEditada;

    public EditController(Image imagenOriginalSeleccionada, String pathOriginal) {
        this.imagenOriginalSeleccionada = imagenOriginalSeleccionada;
        this.pathOriginal = pathOriginal;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        imagenOriginal.setImage(imagenOriginalSeleccionada);
    }

    @FXML
    protected void aplicarFiltros() {

        Image imagenOrigen = imagenOriginalSeleccionada;

        // Configurar la tarea EditImageTask
        EditImageTask editImageTask = new EditImageTask(imagenOrigen, blancoNegro.isSelected(), invertirColores.isSelected(), aumentarBrillo.isSelected(), aplicarDifuminado.isSelected(), InvertirH.isSelected(), InvertirV.isSelected(), pathOriginal);

        // Configurar la ProgressBar
        progreso.progressProperty().bind(editImageTask.progressProperty());

        // Configurar la acción al finalizar la tarea
        editImageTask.setOnSucceeded(event -> {
            imagenEditada = editImageTask.getValue();
            imagenFinal.setImage(imagenEditada);

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
