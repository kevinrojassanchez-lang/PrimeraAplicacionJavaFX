
package cr.ac.ejemplo.controller;

import cr.ac.ejemplo.DAOFactory;

import cr.ac.ejemplo.exception.ActividadNoDisponibleException;
import cr.ac.ejemplo.exception.CupoAgotadoException;
import cr.ac.ejemplo.exception.DatosInvalidosException;
import cr.ac.ejemplo.exception.InscripcionDuplicadaException;
import cr.ac.ejemplo.exception.RegistroNoEncontradoException;

import cr.ac.ejemplo.model.Actividad;
import cr.ac.ejemplo.model.Participante;

import cr.ac.ejemplo.model.enums.EstadoInscripcion;
import cr.ac.ejemplo.model.enums.EstadoUsuario;

import cr.ac.ejemplo.service.ActividadService;
import cr.ac.ejemplo.service.InscripcionService;

import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class InscripcionFormularioController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtIdentificacion;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtTelefono;
    @FXML private ComboBox<Actividad> cmbActividad;
    @FXML private DatePicker dpFecha;
    @FXML private TextField txtHora;
    @FXML private ComboBox<String> cmbEstado;
    @FXML private Label lblTitulo;
    @FXML private Label lblEstado;

    private InscripcionesController inscripcionesController;

    private final ActividadService actividadService =
            DAOFactory.crearActividadService();

    private final InscripcionService inscripcionService =
            DAOFactory.crearInscripcionService();

    @FXML
    private void initialize() {

        cmbEstado.getItems().setAll("Activa");
        cmbEstado.setValue("Activa");
        cmbEstado.setDisable(true);

        try {

            cmbActividad.getItems().setAll(
                    actividadService.listarActividades()
            );

        } catch (SQLException ex) {

            lblEstado.setText(
                    "Error al cargar actividades: " + ex.getMessage()
            );

            ex.printStackTrace();
        }
    }

    public void establecerInscripcionesController(
            InscripcionesController controller) {

        this.inscripcionesController = controller;
    }

    @FXML
    private void guardar() {

        if (txtNombre.getText().isBlank()
                || txtApellidos.getText().isBlank()
                || txtIdentificacion.getText().isBlank()
                || txtCorreo.getText().isBlank()
                || txtTelefono.getText().isBlank()) {

            lblEstado.setText(
                    "Complete todos los datos personales."
            );
            return;
        }

        if (!correoValido(txtCorreo.getText().trim())) {

            lblEstado.setText(
                    "El correo ingresado no es válido."
            );
            return;
        }

        if (cmbActividad.getValue() == null) {

            lblEstado.setText(
                    "Debe seleccionar la actividad."
            );
            return;
        }

        if (dpFecha.getValue() == null) {

            lblEstado.setText(
                    "Debe seleccionar la fecha."
            );
            return;
        }

        if (txtHora.getText().isBlank()) {

            lblEstado.setText(
                    "Debe ingresar la hora."
            );
            return;
        }

        LocalTime hora;

        try {

            hora = LocalTime.parse(
                    txtHora.getText().trim()
            );

        } catch (DateTimeParseException ex) {

            lblEstado.setText(
                    "La hora debe tener el formato HH:mm."
            );
            return;
        }

        Participante participante = new Participante(
                0,
                txtIdentificacion.getText().trim(),
                txtNombre.getText().trim(),
                txtApellidos.getText().trim(),
                txtCorreo.getText().trim(),
                txtTelefono.getText().trim(),
                EstadoUsuario.ACTIVO,
                dpFecha.getValue().toString()
        );

        try {

            inscripcionService.registrarParticipanteConInscripcion(
                    participante,
                    cmbActividad.getValue().getIdActividad(),
                    dpFecha.getValue().toString(),
                    hora.toString(),
                    EstadoInscripcion.ACTIVA
            );

            if (inscripcionesController != null) {
                inscripcionesController.actualizarTabla();
            }

            Alert alerta = new Alert(
                    Alert.AlertType.INFORMATION
            );

            alerta.setTitle("Registro exitoso");
            alerta.setHeaderText(null);
            alerta.setContentText(
                    "Participante e inscripción guardados correctamente."
            );

            alerta.showAndWait();

            Stage stage = (Stage) txtNombre
                    .getScene()
                    .getWindow();

            stage.close();

        } catch (SQLException
                | DatosInvalidosException
                | RegistroNoEncontradoException
                | ActividadNoDisponibleException
                | CupoAgotadoException
                | InscripcionDuplicadaException ex) {

            lblEstado.setText(
                    "Error al guardar: " + ex.getMessage()
            );

            ex.printStackTrace();
        }
    }

    private boolean correoValido(String correo) {

        String expresion =
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        return Pattern.matches(expresion, correo);
    }

    @FXML
    private void cancelar() {

        Stage stage = (Stage) txtNombre
                .getScene()
                .getWindow();

        stage.close();
    }
}
