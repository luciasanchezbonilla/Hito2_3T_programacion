module com.empresa.hito2_3t_programacion_lucia_sanchez_bonilla {
    requires javafx.controls;
    requires javafx.fxml;
    requires mongo.java.driver;


    opens com.empresa.hito2_3t_programacion_lucia_sanchez_bonilla to javafx.fxml;
    exports com.empresa.hito2_3t_programacion_lucia_sanchez_bonilla;
}