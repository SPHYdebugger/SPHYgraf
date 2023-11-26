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
    private boolean aumentarBrillo;
    private boolean aplicarDifuminado;
    private Image imagenEditada;
    private String pathOriginal;

    int radioDifuminado = 5;



    public EditImageTask(Image originalImage, boolean applyBlackAndWhite, boolean invertColors, boolean aumentarBrillo, boolean aplicarDifuminado, boolean invertHorizontal, boolean invertVertical, String pathOriginal) {
        this.originalImage = originalImage;
        this.applyBlackAndWhite = applyBlackAndWhite;
        this.invertColors = invertColors;
        this.aumentarBrillo = aumentarBrillo;
        this.aplicarDifuminado = aplicarDifuminado;
        this.invertHorizontal = invertHorizontal;
        this.invertVertical = invertVertical;
        this.pathOriginal = pathOriginal;
    }

    @Override
    protected Image call() throws Exception {
        // Lógica para editar la imagen según los parámetros proporcionados
        Image editedImage = applyFilters(originalImage, applyBlackAndWhite, invertColors, aumentarBrillo, aplicarDifuminado, invertHorizontal, invertVertical);


        // Simular una tarea lenta de 1 minuto
        for (int i = 0; i < 60; i++) {
            Thread.sleep(1000);  // Esperar 1 segundo
            double progress = (i + 1) * 100.0 / 60.0;
            updateProgress(i + 1, 60);
            updateMessage(String.format("%.2f%%", progress));
        }

        // Actualizar el progreso y mensaje
        updateProgress(100, 100);
        updateMessage("100%");

        return editedImage;
    }

    private Image applyFilters(Image originalImage, boolean applyBlackAndWhite,boolean invertColors, boolean aumentarBrillo, boolean aplicarDifuminado, boolean invertHorizontal, boolean invertVertical) {



        imagenEditada = originalImage;


        // Aplicar los filtros seleccionados
        if (applyBlackAndWhite) {
            imagenEditada = applyBlancoYNegro(imagenEditada);
        }
        if (invertHorizontal) {
            imagenEditada = invertirHorizontalmente(imagenEditada);
        }
        if (invertVertical) {
            imagenEditada = invertirVerticalmente(imagenEditada);
        }
        if (invertColors) {
            imagenEditada = invertirColores(imagenEditada);
        }
        if (aumentarBrillo) {
            double factorBrillo = 1.3;
            imagenEditada = aumentarBrillo(imagenEditada, factorBrillo);
        }
        if (aplicarDifuminado) {
            // Puedes ajustar el radioDifuminado según tus necesidades
            imagenEditada = aplicarDifuminado(imagenEditada, radioDifuminado);
        }

        return imagenEditada;
    }

    private Image applyBlancoYNegro(Image originalImage) {


        // Crear una nueva imagen en blanco y negro
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

    private WritableImage invertirColores(Image originalImage) {
        // Crear una nueva imagen con las mismas dimensiones
        WritableImage imagenInvertidaColores = new WritableImage((int) originalImage.getWidth(), (int) originalImage.getHeight());
        PixelWriter pixelWriter = imagenInvertidaColores.getPixelWriter();

        // Recorrer cada píxel y invertir sus componentes de color
        for (int y = 0; y < originalImage.getHeight(); y++) {
            for (int x = 0; x < originalImage.getWidth(); x++) {
                Color colorOriginal = originalImage.getPixelReader().getColor(x, y);

                // Invertir los componentes de color
                double nuevoRojo = 1.0 - colorOriginal.getRed();
                double nuevoVerde = 1.0 - colorOriginal.getGreen();
                double nuevoAzul = 1.0 - colorOriginal.getBlue();

                // Crear el nuevo color invertido
                Color nuevoColor = new Color(nuevoRojo, nuevoVerde, nuevoAzul, colorOriginal.getOpacity());

                // Escribir el nuevo color en la imagen invertida
                pixelWriter.setColor(x, y, nuevoColor);
            }
        }

        return imagenInvertidaColores;
    }


    private WritableImage aumentarBrillo(Image originalImage, double factorBrillo) {
        // Crear una nueva imagen con las mismas dimensiones
        WritableImage imagenBrillante = new WritableImage((int) originalImage.getWidth(), (int) originalImage.getHeight());
        PixelWriter pixelWriter = imagenBrillante.getPixelWriter();

        // Recorrer cada píxel y aumentar su brillo
        for (int y = 0; y < originalImage.getHeight(); y++) {
            for (int x = 0; x < originalImage.getWidth(); x++) {
                Color colorOriginal = originalImage.getPixelReader().getColor(x, y);

                // Aumentar el brillo multiplicando cada componente de color por el factor
                double nuevoRojo = clamp(colorOriginal.getRed() * factorBrillo);
                double nuevoVerde = clamp(colorOriginal.getGreen() * factorBrillo);
                double nuevoAzul = clamp(colorOriginal.getBlue() * factorBrillo);

                // Crear el nuevo color con brillo aumentado
                Color nuevoColor = new Color(nuevoRojo, nuevoVerde, nuevoAzul, colorOriginal.getOpacity());

                // Escribir el nuevo color en la imagen con brillo aumentado
                pixelWriter.setColor(x, y, nuevoColor);
            }
        }

        return imagenBrillante;
    }

    private double clamp(double valor) {
        return Math.min(1.0, Math.max(0.0, valor));
    }

    private WritableImage aplicarDifuminado(Image originalImage, int radioDifuminado) {
        int width = (int) originalImage.getWidth();
        int height = (int) originalImage.getHeight();

        // Crear una nueva imagen con las mismas dimensiones
        WritableImage imagenDifuminada = new WritableImage(width, height);
        PixelReader pixelReader = originalImage.getPixelReader();
        PixelWriter pixelWriter = imagenDifuminada.getPixelWriter();

        // Recorrer cada píxel y aplicar el difuminado
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Obtener el color promedio de los píxeles circundantes
                Color nuevoColor = obtenerColorPromedio(pixelReader, x, y, width, height, radioDifuminado);

                // Escribir el nuevo color en la imagen difuminada
                pixelWriter.setColor(x, y, nuevoColor);
            }
        }

        return imagenDifuminada;
    }

    // Método auxiliar para obtener el color promedio de los píxeles circundantes
    private Color obtenerColorPromedio(PixelReader pixelReader, int x, int y, int width, int height, int radio) {
        double totalRojo = 0.0;
        double totalVerde = 0.0;
        double totalAzul = 0.0;

        int cantidadPixeles = 0;

        // Recorrer los píxeles dentro del radio especificado
        for (int i = -radio; i <= radio; i++) {
            for (int j = -radio; j <= radio; j++) {
                int newX = x + i;
                int newY = y + j;

                // Verificar que el píxel esté dentro de los límites de la imagen
                if (newX >= 0 && newX < width && newY >= 0 && newY < height) {
                    Color colorVecino = pixelReader.getColor(newX, newY);
                    totalRojo += colorVecino.getRed();
                    totalVerde += colorVecino.getGreen();
                    totalAzul += colorVecino.getBlue();
                    cantidadPixeles++;
                }
            }
        }

        // Calcular el color promedio
        double promedioRojo = totalRojo / cantidadPixeles;
        double promedioVerde = totalVerde / cantidadPixeles;
        double promedioAzul = totalAzul / cantidadPixeles;

        return new Color(promedioRojo, promedioVerde, promedioAzul, 1.0);
    }

    private WritableImage invertirHorizontalmente(Image originalImage) {
        // Crear una nueva imagen invertida horizontalmente
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

    private WritableImage invertirVerticalmente(Image originalImage) {
        // Crear una nueva imagen invertida verticalmente
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
