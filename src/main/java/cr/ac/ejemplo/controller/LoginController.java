
package cr.ac.ejemplo.controller;

import cr.ac.ejemplo.DAOFactory;
import cr.ac.ejemplo.SesionUsuario;
import cr.ac.ejemplo.VentanaUtil;
import cr.ac.ejemplo.model.Usuario;
import cr.ac.ejemplo.service.UsuarioService;

import java.io.IOException;
import java.sql.SQLException;

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

    private final UsuarioService usuarioService =
            DAOFactory.crearUsuarioService();

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

            Usuario usuarioAutenticado =
                    usuarioService.iniciarSesion(usuario, contrasena);

            if (usuarioAutenticado == null) {

                lblEstado.setText(
                        "Usuario o contraseña incorrectos, o cuenta inactiva."
                );

                return;
            }

            SesionUsuario.iniciarSesion(usuarioAutenticado);

            FXMLLoader loader = new FXMLLoader(
                    LoginController.class.getResource("/cr/ac/menu.fxml")
            );

            Scene sceneMenu = new Scene(loader.load());

            Stage stage = (Stage) txtUsuario.getScene().getWindow();

            stage.setTitle("Menú principal");
            stage.setScene(sceneMenu);
            VentanaUtil.configurar(stage);
            stage.show();

        } catch (SQLException ex) {

            lblEstado.setText(
                    "No se pudo validar el usuario en la base de datos."
            );

            ex.printStackTrace();

        } catch (IOException ex) {

            SesionUsuario.cerrarSesion();
            ex.printStackTrace();
        }
    }
}
