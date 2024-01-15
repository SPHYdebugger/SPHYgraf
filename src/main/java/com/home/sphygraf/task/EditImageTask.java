package com.home.sphygraf.task;

import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class EditImageTask extends Task<Image> {

    private Image originalImage;
    private boolean applyBlackAndWhite;
    private boolean invertHorizontal;
    private boolean invertVertical;
    private boolean invertColors;
    private boolean shineUp;
    private boolean applyBlurred;
    private Image editedImage;
    private String pathOriginal;

    int radioDifuminado = 5;



    public EditImageTask(Image originalImage, boolean applyBlackAndWhite, boolean invertColors, boolean shineUp, boolean applyBlurred, boolean invertHorizontal, boolean invertVertical, String pathOriginal) {
        this.originalImage = originalImage;
        this.applyBlackAndWhite = applyBlackAndWhite;
        this.invertColors = invertColors;
        this.shineUp = shineUp;
        this.applyBlurred = applyBlurred;
        this.invertHorizontal = invertHorizontal;
        this.invertVertical = invertVertical;
        this.pathOriginal = pathOriginal;
    }

    @Override
    protected Image call() throws Exception {
        // Editar la imagen
        Image editedImage = applyFilters(originalImage, applyBlackAndWhite, invertColors, shineUp, applyBlurred, invertHorizontal, invertVertical);

        // Simular una tarea lenta de 1 minuto
        for (int i = 0; i < 60; i++) {
            Thread.sleep(1000);  // Esperar 1 segundo
            double progress = (i + 1) * 100.0 / 60.0;
            updateProgress(i + 1, 60);
            updateMessage(String.format("%.1f%%", progress));
        }

        // Actualizar el progreso y mensaje
        updateProgress(100, 100);
        updateMessage("100%");

        return editedImage;
    }

    private Image applyFilters(Image originalImage, boolean applyBlackAndWhite,boolean invertColors, boolean shineUp, boolean applyBlurred, boolean invertHorizontal, boolean invertVertical) {

        editedImage = originalImage;

        if (applyBlackAndWhite) {
            editedImage = applyBlackAndWhite(editedImage);
        }
        if (invertHorizontal) {
            editedImage = invertHorizontal(editedImage);
        }
        if (invertVertical) {
            editedImage = invertVertical(editedImage);
        }
        if (invertColors) {
            editedImage = invertColors(editedImage);
        }
        if (shineUp) {
            double factorBrillo = 1.3;
            editedImage = shineUp(editedImage, factorBrillo);
        }
        if (applyBlurred) {
            editedImage = applyBlurred(editedImage, radioDifuminado);
        }

        return editedImage;
    }



    private Image applyBlackAndWhite(Image originalImage) {

        // Crear una nueva imagen en blanco y negro con el mismo tamaño que la original
        WritableImage imagenBlancoNegro = new WritableImage((int) originalImage.getWidth(), (int) originalImage.getHeight());
        PixelWriter pixelWriter = imagenBlancoNegro.getPixelWriter();

        // Convertir a blanco y negro
        for (int y = 0; y < originalImage.getHeight(); y++) {
            for (int x = 0; x < originalImage.getWidth(); x++) {
                Color colorOriginal = originalImage.getPixelReader().getColor(x, y);
                double promedio = (colorOriginal.getRed() + colorOriginal.getGreen() + colorOriginal.getBlue()) / 3.0;
                Color nuevoColor = Color.color(promedio, promedio, promedio);
                pixelWriter.setColor(x, y, nuevoColor);
            }
        }

        return imagenBlancoNegro;
    }

    private WritableImage invertColors(Image originalImage) {
        WritableImage imagenInvertidaColores = new WritableImage((int) originalImage.getWidth(), (int) originalImage.getHeight());
        PixelWriter pixelWriter = imagenInvertidaColores.getPixelWriter();

        // invertir sus colores
        for (int y = 0; y < originalImage.getHeight(); y++) {
            for (int x = 0; x < originalImage.getWidth(); x++) {
                Color colorOriginal = originalImage.getPixelReader().getColor(x, y);

                double nuevoRojo = 1.0 - colorOriginal.getRed();
                double nuevoVerde = 1.0 - colorOriginal.getGreen();
                double nuevoAzul = 1.0 - colorOriginal.getBlue();

                Color nuevoColor = new Color(nuevoRojo, nuevoVerde, nuevoAzul, colorOriginal.getOpacity());

                pixelWriter.setColor(x, y, nuevoColor);
            }
        }

        return imagenInvertidaColores;
    }


    private WritableImage shineUp(Image originalImage, double factorBrillo) {
        WritableImage imagenBrillante = new WritableImage((int) originalImage.getWidth(), (int) originalImage.getHeight());
        PixelWriter pixelWriter = imagenBrillante.getPixelWriter();

        for (int y = 0; y < originalImage.getHeight(); y++) {
            for (int x = 0; x < originalImage.getWidth(); x++) {
                Color colorOriginal = originalImage.getPixelReader().getColor(x, y);

                double nuevoRojo = shine(colorOriginal.getRed() * factorBrillo);
                double nuevoVerde = shine(colorOriginal.getGreen() * factorBrillo);
                double nuevoAzul = shine(colorOriginal.getBlue() * factorBrillo);

                Color nuevoColor = new Color(nuevoRojo, nuevoVerde, nuevoAzul, colorOriginal.getOpacity());

                pixelWriter.setColor(x, y, nuevoColor);
            }
        }

        return imagenBrillante;
    }


    private double shine(double valor) {
        return Math.min(1.0, Math.max(0.0, valor));
    }

    private WritableImage applyBlurred(Image originalImage, int radioDifuminado) {

        WritableImage imagenDifuminada = new WritableImage((int) originalImage.getWidth(), (int) originalImage.getHeight());
        PixelReader pixelReader = originalImage.getPixelReader();
        PixelWriter pixelWriter = imagenDifuminada.getPixelWriter();

        // Aplicar el difuminado
        for (int y = 0; y < (int) originalImage.getHeight(); y++) {
            for (int x = 0; x < (int) originalImage.getWidth(); x++) {

                Color nuevoColor = medioColor(pixelReader, x, y, (int) originalImage.getWidth(), (int) originalImage.getHeight(), radioDifuminado);

                pixelWriter.setColor(x, y, nuevoColor);
            }
        }

        return imagenDifuminada;
    }

    //obtener el color de los píxeles circundantes
    private Color medioColor(PixelReader pixelReader, int x, int y, int width, int height, int radio) {
        double totalRed = 0.0;
        double totalGreen = 0.0;
        double totalBlue = 0.0;

        int Pixels = 0;

        for (int i = -radio; i <= radio; i++) {
            for (int j = -radio; j <= radio; j++) {
                int newX = x + i;
                int newY = y + j;

                if (newX >= 0 && newX < width && newY >= 0 && newY < height) {
                    Color colorVecino = pixelReader.getColor(newX, newY);
                    totalRed += colorVecino.getRed();
                    totalGreen += colorVecino.getGreen();
                    totalBlue += colorVecino.getBlue();
                    Pixels++;
                }
            }
        }

        double medRed = totalRed / Pixels;
        double medGreen = totalGreen / Pixels;
        double medBlue = totalBlue / Pixels;

        return new Color(medRed, medGreen, medBlue, 1.0);
    }

    private WritableImage invertHorizontal(Image originalImage) {

        WritableImage imagenInvertidaH = new WritableImage((int) originalImage.getWidth(), (int) originalImage.getHeight());
        PixelWriter pixelWriter = imagenInvertidaH.getPixelWriter();

        // Invertir horizontalmente
        for (int y = 0; y < originalImage.getHeight(); y++) {
            for (int x = 0; x < originalImage.getWidth(); x++) {
                Color colorOriginal = originalImage.getPixelReader().getColor(x, y);
                int xInvertido = (int) originalImage.getWidth() - x - 1;
                pixelWriter.setColor(xInvertido, y, colorOriginal);
            }
        }

        return imagenInvertidaH;
    }

    private WritableImage invertVertical(Image originalImage) {

        WritableImage imagenInvertidaV = new WritableImage((int) originalImage.getWidth(), (int) originalImage.getHeight());
        PixelWriter pixelWriter = imagenInvertidaV.getPixelWriter();

        // Invertir verticalmente
        for (int y = 0; y < originalImage.getHeight(); y++) {
            int yInvertido = (int) originalImage.getHeight() - y - 1;
            for (int x = 0; x < originalImage.getWidth(); x++) {
                Color colorOriginal = originalImage.getPixelReader().getColor(x, y);
                pixelWriter.setColor(x, yInvertido, colorOriginal);
            }
        }

        return imagenInvertidaV;
    }

}
