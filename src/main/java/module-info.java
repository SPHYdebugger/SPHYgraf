module com.home.sphygraf {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.desktop;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;

    opens com.home.sphygraf to javafx.fxml;
    exports com.home.sphygraf;
    opens com.home.sphygraf.db to com.fasterxml.jackson.databind;

}