module com.home.sphygraf {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.desktop;

    opens com.home.sphygraf to javafx.fxml;
    exports com.home.sphygraf;
}