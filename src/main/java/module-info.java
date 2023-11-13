module com.home.sphygraf {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.home.sphygraf to javafx.fxml;
    exports com.home.sphygraf;
}