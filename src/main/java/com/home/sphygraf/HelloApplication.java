package com.home.sphygraf;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class HelloApplication extends Application {



    @Override
    public void init() throws Exception {
        System.out.println("Initializing Application");
        super.init();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Cargar el fxml
        FXMLLoader uiLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        //definir el controlador de la escena
        uiLoader.setController(new HelloControler());
        //Mostrar la escena
        Scene helloScene = new Scene(uiLoader.load());
        primaryStage.getIcons().add(new Image("C:\\Users\\sanph\\IdeaProjects\\SphyGraf\\src\\main\\resources\\com\\home\\sphygraf\\Diseno-sin-texto.jpg"));
        primaryStage.setTitle("SPHYgraf V1.0");
        primaryStage.setScene(helloScene);
        primaryStage.show();

        // Establecer el Stage en el controlador (primaryDtage)
        HelloControler helloController = uiLoader.getController();
        helloController.setPrimaryStage(primaryStage);
    }



    @Override
    public void stop() throws Exception {
        System.out.println("Application finished");
        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }
}