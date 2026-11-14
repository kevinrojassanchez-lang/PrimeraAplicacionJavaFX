
package cr.ac.ejemplo.controller;

import cr.ac.ejemplo.SesionUsuario;
import cr.ac.ejemplo.VentanaUtil;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MenuController {

    @FXML
    private Button btnEventos;

    @FXML
    private Button btnInscripciones;

    @FXML
    private Button btnConsultas;

    @FXML
    private Button btnReportes;

    @FXML
    private void initialize() {

        boolean administrador = SesionUsuario.esAdministrador();

        btnEventos.setDisable(false);
        btnInscripciones.setDisable(false);
        btnConsultas.setDisable(false);
        btnReportes.setDisable(!administrador);
    }

    @FXML
    private void abrirEventos(ActionEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    MenuController.class.getResource(
                            "/cr/ac/eventos.fxml"
                    )
            );

            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.setTitle("Gestión de Eventos");
            stage.setScene(scene);
            VentanaUtil.configurar(stage);
            stage.show();

        } catch (IOException ex) {

            ex.printStackTrace();
        }
    }

    @FXML
    private void abrirInscripciones(ActionEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    MenuController.class.getResource(
                            "/cr/ac/inscripciones.fxml"
                    )
            );

            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.setTitle("Gestión de Inscripciones");
            stage.setScene(scene);
            VentanaUtil.configurar(stage);
            stage.show();

        } catch (IOException ex) {

            ex.printStackTrace();
        }
    }

    @FXML
    private void abrirConsultas(ActionEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    MenuController.class.getResource(
                            "/cr/ac/consultas.fxml"
                    )
            );

            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.setTitle("Consultas");
            stage.setScene(scene);
            VentanaUtil.configurar(stage);
            stage.show();

        } catch (IOException ex) {

            ex.printStackTrace();
        }
    }

    @FXML
    private void abrirReportes(ActionEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    MenuController.class.getResource(
                            "/cr/ac/reportes.fxml"
                    )
            );

            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.setTitle("Reportes");
            stage.setScene(scene);
            VentanaUtil.configurar(stage);
            stage.show();

        } catch (IOException ex) {

            ex.printStackTrace();
        }
    }

    @FXML
    private void cerrarSesion() {

        SesionUsuario.cerrarSesion();

        try {

            FXMLLoader loader = new FXMLLoader(
                    MenuController.class.getResource(
                            "/cr/ac/login.fxml"
                    )
            );

            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) btnEventos.getScene()
                    .getWindow();

            stage.setTitle("Inicio de sesión");
            stage.setScene(scene);
            VentanaUtil.configurar(stage);
            stage.show();

        } catch (IOException ex) {

            ex.printStackTrace();
        }
    }
}
