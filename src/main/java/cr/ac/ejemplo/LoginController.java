package cr.ac.ejemplo;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtContrasena;

    @FXML
    private Label lblEstado;

    @FXML
    private void iniciarSesion() {

        String usuario = txtUsuario.getText();
        String contrasena = txtContrasena.getText();

        if (usuario == null || usuario.isBlank()) {
            lblEstado.setText("Debe ingresar el usuario");
            return;
        }

        if (contrasena == null || contrasena.isBlank()) {
            lblEstado.setText("Debe ingresar la contraseña");
            return;
        }

        try {

            FXMLLoader loader = new FXMLLoader(
                    LoginController.class.getResource("/cr/ac/menu.fxml")
            );

            Scene sceneMenu = new Scene(loader.load());

            Stage stage = (Stage) txtUsuario.getScene().getWindow();

            stage.setTitle("Menú principal");
            stage.setScene(sceneMenu);
            stage.show();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
