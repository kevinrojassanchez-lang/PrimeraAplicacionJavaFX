package cr.ac.ejemplo;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MenuController {

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
            stage.show();

        } catch (IOException ex) {

            ex.printStackTrace();
        }
    }

    @FXML
    private void cerrarSesion() {

        System.out.println("Cerrar sesion");
    }
}
