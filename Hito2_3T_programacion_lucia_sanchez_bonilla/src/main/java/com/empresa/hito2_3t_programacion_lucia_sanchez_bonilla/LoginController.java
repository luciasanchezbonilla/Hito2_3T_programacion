package com.empresa.hito2_3t_programacion_lucia_sanchez_bonilla;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorMessage;

    @FXML
    protected void login() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.equals("lucia") && password.equals("1234")) {
            // Iniciar sesión exitosa, cargar y mostrar la ventana principal
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setTitle("Hello!");
                stage.setScene(scene);
                stage.show();

                // Cerrar la ventana de inicio de sesión
                usernameField.getScene().getWindow().hide();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Mostrar mensaje de error
            errorMessage.setText("Usuario o contraseña incorrectos");
        }
    }
}
