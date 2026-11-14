package cr.ac.ejemplo.controller;

import cr.ac.ejemplo.model.Evento;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class EventoDetalleController {

    @FXML
    private Label lblEvento;

    @FXML
    private Label lblEstado;

    private Evento eventoSeleccionado;

    public void establecerEvento(Evento evento) {

        this.eventoSeleccionado = evento;

        if (evento == null) {

            lblEvento.setText(
                    "Evento seleccionado"
            );

        } else {

            lblEvento.setText(
                    evento.getNombre()
            );
        }
    }

    @FXML
    private void abrirActividades() {

        if (eventoSeleccionado == null) {

            lblEstado.setText(
                    "No hay un evento seleccionado."
            );

            return;
        }

        try {

            FXMLLoader loader = new FXMLLoader(
                    EventoDetalleController.class.getResource(
                            "/cr/ac/actividades.fxml"
                    )
            );

            Scene scene = new Scene(loader.load());

            ActividadesController controller =
                    loader.getController();

            controller.establecerEvento(
                    eventoSeleccionado
            );

            Stage stage = (Stage) lblEvento
                    .getScene()
                    .getWindow();

            stage.setTitle("Actividades del evento");
            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {

            ex.printStackTrace();
        }
    }

    @FXML
    private void regresar() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    EventoDetalleController.class.getResource(
                            "/cr/ac/eventos.fxml"
                    )
            );

            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) lblEvento
                    .getScene()
                    .getWindow();

            stage.setTitle("Gestión de Eventos");
            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {

            ex.printStackTrace();
        }
    }
}
