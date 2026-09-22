package cr.ac.ejemplo;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ActividadDetalleController {

    @FXML
    private Label lblEvento;

    @FXML
    private Label lblActividad;

    @FXML
    private Label lblEstado;

    private Evento eventoSeleccionado;

    private Actividad actividadSeleccionada;

    public void establecerDatos(
            Evento evento,
            Actividad actividad) {

        this.eventoSeleccionado = evento;
        this.actividadSeleccionada = actividad;

        if (evento != null) {

            lblEvento.setText(
                    "Evento: " + evento.getNombre()
            );

        } else {

            lblEvento.setText(
                    "Evento seleccionado"
            );
        }

        if (actividad != null) {

            lblActividad.setText(
                    "Actividad: " + actividad.getNombre()
            );

        } else {

            lblActividad.setText(
                    "Actividad seleccionada"
            );
        }

        lblEstado.setText("");
    }

    @FXML
    private void abrirParticipantes() {

        if (actividadSeleccionada == null) {

            lblEstado.setText(
                    "No hay una actividad seleccionada."
            );

            return;
        }

        try {

            FXMLLoader loader = new FXMLLoader(
                    ActividadDetalleController.class.getResource(
                            "/cr/ac/participantes.fxml"
                    )
            );

            Scene scene = new Scene(loader.load());

            ParticipantesController controller =
                    loader.getController();

            controller.establecerActividad(
                    eventoSeleccionado,
                    actividadSeleccionada
            );

            Stage stage = (Stage) lblActividad
                    .getScene()
                    .getWindow();

            stage.setTitle(
                    "Participantes de la actividad"
            );

            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {

            ex.printStackTrace();
        }
    }

    @FXML
    private void abrirExpositores() {

        if (actividadSeleccionada == null) {

            lblEstado.setText(
                    "No hay una actividad seleccionada."
            );

            return;
        }

        try {

            FXMLLoader loader = new FXMLLoader(
                    ActividadDetalleController.class.getResource(
                            "/cr/ac/expositores.fxml"
                    )
            );

            Scene scene = new Scene(loader.load());

            ExpositoresController controller =
                    loader.getController();

            controller.establecerActividad(
                    eventoSeleccionado,
                    actividadSeleccionada
            );

            Stage stage = (Stage) lblActividad
                    .getScene()
                    .getWindow();

            stage.setTitle(
                    "Expositores de la actividad"
            );

            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {

            ex.printStackTrace();
        }
    }

    @FXML
    private void abrirAsistencia() {

        if (actividadSeleccionada == null) {

            lblEstado.setText(
                    "No hay una actividad seleccionada."
            );

            return;
        }

        try {

            FXMLLoader loader = new FXMLLoader(
                    ActividadDetalleController.class.getResource(
                            "/cr/ac/asistencia.fxml"
                    )
            );

            Scene scene = new Scene(loader.load());

            AsistenciaController controller =
                    loader.getController();

            controller.establecerActividad(
                    eventoSeleccionado,
                    actividadSeleccionada
            );

            Stage stage = (Stage) lblActividad
                    .getScene()
                    .getWindow();

            stage.setTitle(
                    "Asistencia de la actividad"
            );

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
                    ActividadDetalleController.class.getResource(
                            "/cr/ac/actividades.fxml"
                    )
            );

            Scene scene = new Scene(loader.load());

            ActividadesController controller =
                    loader.getController();

            controller.establecerEvento(
                    eventoSeleccionado
            );

            Stage stage = (Stage) lblActividad
                    .getScene()
                    .getWindow();

            stage.setTitle(
                    "Actividades del evento"
            );

            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {

            ex.printStackTrace();
        }
    }
}
