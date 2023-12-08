package com.home.sphygraf;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloControler implements Initializable {


    @FXML
    private Button BtStart;


    private Stage primaryStage;

    // Establecer e iniciar el Stage
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    @FXML
    protected void startApp() {
        try {
            //cargar el fxml (Escena)
            FXMLLoader secondLoader = new FXMLLoader(getClass().getResource("multi.fxml"));
            // definir el controlador
            secondLoader.setController(new MultiController());
            //cargar la escena
            Scene indexScene = new Scene(secondLoader.load());
            primaryStage.setScene(indexScene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
