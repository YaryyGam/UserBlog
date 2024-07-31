module com.jmc.medicalapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.sql;
    requires org.xerial.sqlitejdbc;


    opens com.jmc.medicalapplication to javafx.fxml;
    exports com.jmc.medicalapplication;
    exports com.jmc.medicalapplication.Controller;
    exports com.jmc.medicalapplication.Controller.Client;
    exports com.jmc.medicalapplication.Model;
    exports com.jmc.medicalapplication.Util;

}