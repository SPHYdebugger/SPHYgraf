package com.home.sphygraf;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuItem;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class IndexController implements Initializable {


    @FXML
    private MenuItem SelectImage;
    @FXML
    private ImageView imagenOriginal;
    @FXML
    private CheckBox BNcheckBox;
    @FXML
    private CheckBox HcheckBox;
    @FXML
    private CheckBox VcheckBox;
    @FXML
    private ImageView imagenModificada;
    @FXML
    private Button guardarBoton;
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
            imagenOriginal.setImage(imagen);

        }


    }

    private void aplicarBlancoYNegro() {
        try {
            // Obtener la imagen actual del ImageView
            Image imagenOrigen = imagenOriginal.getImage();

            // Verificar si hay una imagen cargada
            if (imagenOrigen != null) {
                // Crear un nuevo PixelReader
                PixelReader pixelReader = imagenOrigen.getPixelReader();

                // Obtener las dimensiones de la imagen
                int width = (int) imagenOrigen.getWidth();
                int height = (int) imagenOrigen.getHeight();

                // Crear una nueva imagen en blanco y negro
                WritableImage imagenBlancoNegro = new WritableImage(width, height);
                PixelWriter pixelWriter = imagenBlancoNegro.getPixelWriter();

                // Convertir a blanco y negro
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        Color colorOriginal = pixelReader.getColor(x, y);
                        double promedio = (colorOriginal.getRed() + colorOriginal.getGreen() + colorOriginal.getBlue()) / 3.0;
                        Color nuevoColor = Color.color(promedio, promedio, promedio);
                        pixelWriter.setColor(x, y, nuevoColor);
                    }
                }

                // Mostrar la imagen en blanco y negro en el ImageView
                imagenModificada.setImage(imagenBlancoNegro);
                actualizarVisibilidadBoton();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void invertirHorizontalmente() {
        try {
            // Obtener la imagen actual del ImageView
            Image imagenOrigen = imagenOriginal.getImage();

            // Verificar si hay una imagen cargada
            if (imagenOrigen != null) {
                // Crear un nuevo PixelReader
                PixelReader pixelReader = imagenOrigen.getPixelReader();

                // Obtener las dimensiones de la imagen
                int width = (int) imagenOrigen.getWidth();
                int height = (int) imagenOrigen.getHeight();

                WritableImage imagenInvertidaH = new WritableImage(width, height);
                PixelWriter pixelWriter = imagenInvertidaH.getPixelWriter();

                // Invertir horizontalmente
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        Color colorOriginal = pixelReader.getColor(x, y);
                        // Invertir la posición horizontal
                        int xInvertido = width - x - 1;
                        pixelWriter.setColor(xInvertido, y, colorOriginal);
                    }
                }

                // Mostrar la imagen invertida horizontalmente en el ImageView
                imagenModificada.setImage(imagenInvertidaH);
                actualizarVisibilidadBoton();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void invertirVerticalmente() {

        try {
            // Obtener la imagen actual del ImageView
            Image imagenOrigen = imagenOriginal.getImage();

            // Verificar si hay una imagen cargada
            if (imagenOrigen != null) {
                // Crear un nuevo PixelReader
                PixelReader pixelReader = imagenOrigen.getPixelReader();

                // Obtener las dimensiones de la imagen
                int width = (int) imagenOrigen.getWidth();
                int height = (int) imagenOrigen.getHeight();

                WritableImage imagenInvertidaV = new WritableImage(width, height);
                PixelWriter pixelWriter = imagenInvertidaV.getPixelWriter();

                // Invertir verticalmente
                for (int y = 0; y < height; y++) {
                    int yInvertido = height - y - 1;
                    for (int x = 0; x < width; x++) {
                        Color colorOriginal = pixelReader.getColor(x, y);
                        pixelWriter.setColor(x, yInvertido, colorOriginal);
                    }
                }

                // Mostrar la imagen invertida horizontalmente en el ImageView
                imagenModificada.setImage(imagenInvertidaV);
                actualizarVisibilidadBoton();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void guardarImagen() {

        try {
            // Obtener la imagen actual del ImageView
            Image imagenActual = imagenModificada.getImage();

            // Mostrar un diálogo para que el usuario seleccione la ubicación y el nombre del archivo
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Guardar Imagen");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de Imagen", "*.png", "*.jpg", "*.jpeg"));

            File archivoDestino = fileChooser.showSaveDialog(null);

            // Verificar si el usuario ha seleccionado un archivo
            if (archivoDestino != null) {
                // Obtener la URL de la imagen actual
                String imageUrl = imagenActual.getUrl();

                // Crear un objeto File desde la URL de la imagen
                File inputFile = new File(imageUrl.substring(5)); // Eliminar "file:" del inicio

                // Copiar la imagen original al archivo destino
                ImageIO.write(ImageIO.read(inputFile), "png", archivoDestino);

                System.out.println("Imagen guardada exitosamente en: " + archivoDestino.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Agregar un listener al CheckBox
        BNcheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && imagenOriginal!=null) {
                // Si el CheckBox está marcado, aplicar blanco y negro
                aplicarBlancoYNegro();
            }else {
                // Si el CheckBox está desmarcado, restaurar la imagen original
                imagenModificada.setImage(null);
            }
            actualizarVisibilidadBoton();
        });

        // Agregar un listener al CheckBox
        HcheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && imagenOriginal!=null) {
                // Si el CheckBox está marcado, aplicar blanco y negro
                invertirHorizontalmente();
            }else {
                // Si el CheckBox está desmarcado, restaurar la imagen original
                imagenModificada.setImage(null);
            }
            actualizarVisibilidadBoton();
        });

        // Agregar un listener al CheckBox
        VcheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && imagenOriginal!=null) {
                // Si el CheckBox está marcado, aplicar blanco y negro
                invertirVerticalmente();
            }else {
                // Si el CheckBox está desmarcado, restaurar la imagen original
                imagenModificada.setImage(null);
            }
            actualizarVisibilidadBoton();
        });

        // Configurar el evento del botón para guardar la imagen
        guardarBoton.setOnAction(event -> guardarImagen());
        // Actualizar la visibilidad del botón al inicio
        actualizarVisibilidadBoton();
    }

    private void actualizarVisibilidadBoton() {
        // Configurar la visibilidad del botón en función de si hay una imagen en el ImageView
        guardarBoton.setVisible(imagenModificada.getImage() != null);

    }
}
