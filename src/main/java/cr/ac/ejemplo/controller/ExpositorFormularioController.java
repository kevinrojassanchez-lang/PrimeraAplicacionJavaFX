
package cr.ac.ejemplo.controller;

import cr.ac.ejemplo.DAOFactory;

import cr.ac.ejemplo.exception.ActividadNoDisponibleException;
import cr.ac.ejemplo.exception.CupoAgotadoException;
import cr.ac.ejemplo.exception.DatosInvalidosException;
import cr.ac.ejemplo.exception.InscripcionDuplicadaException;
import cr.ac.ejemplo.exception.RegistroNoEncontradoException;

import cr.ac.ejemplo.model.Expositor;

import cr.ac.ejemplo.model.enums.EstadoInscripcion;
import cr.ac.ejemplo.model.enums.EstadoUsuario;

import cr.ac.ejemplo.service.ActividadService;
import cr.ac.ejemplo.service.ExpositorService;
import cr.ac.ejemplo.service.InscripcionService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ExpositorFormularioController {

    @FXML
    private TextField txtIdentificacion;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtApellidos;

    @FXML
    private TextField txtCorreo;

    @FXML
    private TextField txtTelefono;

    @FXML
    private TextField txtEspecialidad;

    @FXML
    private TextArea txtDescripcion;

    @FXML
    private ComboBox<String> cmbEstadoPersona;

    @FXML
    private VBox paneInscripcion;

    @FXML
    private ComboBox<cr.ac.ejemplo.model.Actividad> cmbActividad;

    @FXML
    private DatePicker dpFecha;

    @FXML
    private TextField txtHora;

    @FXML
    private ComboBox<String> cmbEstadoInscripcion;

    @FXML
    private Label lblTitulo;

    @FXML
    private Label lblEstado;

    private final ExpositorService expositorService =
            DAOFactory.crearExpositorService();

    private final ActividadService actividadService =
            DAOFactory.crearActividadService();

    private final InscripcionService inscripcionService =
            DAOFactory.crearInscripcionService();

    private InscripcionesController inscripcionesController;

    private Expositor expositorEditar;

    @FXML
    private void initialize() {

        cmbEstadoPersona.getItems().setAll(
                "Activo",
                "Inactivo"
        );

        cmbEstadoPersona.setValue("Activo");

        cmbEstadoInscripcion.getItems().setAll("Activa");
        cmbEstadoInscripcion.setValue("Activa");
        cmbEstadoInscripcion.setDisable(true);

        dpFecha.setValue(LocalDate.now());

        try {

            cmbActividad.setItems(
                    FXCollections.observableArrayList(
                            actividadService.listarActividades()
                    )
            );

        } catch (SQLException e) {

            lblEstado.setText(
                    "Error al cargar las actividades: "
                    + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    public void establecerInscripcionesController(
            InscripcionesController inscripcionesController) {

        this.inscripcionesController = inscripcionesController;
    }

    public void establecerExpositor(Expositor expositor) {

        this.expositorEditar = expositor;

        lblTitulo.setText("EDITAR EXPOSITOR");

        paneInscripcion.setVisible(false);
        paneInscripcion.setManaged(false);

        txtIdentificacion.setText(expositor.getIdentificacion());
        txtNombre.setText(expositor.getNombre());
        txtApellidos.setText(expositor.getApellidos());
        txtCorreo.setText(expositor.getCorreo());
        txtTelefono.setText(expositor.getTelefono());
        txtEspecialidad.setText(expositor.getEspecialidad());

        txtDescripcion.setText(
                expositor.getDescripcionProfesional()
        );

        cmbEstadoPersona.setValue(
                expositor.getEstado() == EstadoUsuario.ACTIVO
                ? "Activo"
                : "Inactivo"
        );
    }

    @FXML
    private void guardar() {

        if (!validarDatosPersonales()) {
            return;
        }

        if (expositorEditar != null) {
            actualizarExpositor();
        } else {
            registrarExpositor();
        }
    }

    private boolean validarDatosPersonales() {

        if (txtIdentificacion.getText().isBlank()) {
            lblEstado.setText("Debe ingresar la identificación.");
            return false;
        }

        if (txtNombre.getText().isBlank()) {
            lblEstado.setText("Debe ingresar el nombre.");
            return false;
        }

        if (txtApellidos.getText().isBlank()) {
            lblEstado.setText("Debe ingresar los apellidos.");
            return false;
        }

        if (txtCorreo.getText().isBlank()) {
            lblEstado.setText("Debe ingresar el correo.");
            return false;
        }

        if (!correoValido(txtCorreo.getText().trim())) {
            lblEstado.setText("El correo ingresado no es válido.");
            return false;
        }

        if (txtTelefono.getText().isBlank()) {
            lblEstado.setText("Debe ingresar el teléfono.");
            return false;
        }

        if (txtEspecialidad.getText().isBlank()) {
            lblEstado.setText(
                    "Debe ingresar la profesión o especialidad."
            );
            return false;
        }

        if (cmbEstadoPersona.getValue() == null) {
            lblEstado.setText(
                    "Debe seleccionar el estado de la persona."
            );
            return false;
        }

        return true;
    }

    private void registrarExpositor() {

        if (cmbActividad.getValue() == null) {
            lblEstado.setText("Debe seleccionar la actividad.");
            return;
        }

        if (dpFecha.getValue() == null) {
            lblEstado.setText("Debe seleccionar la fecha.");
            return;
        }

        if (txtHora.getText().isBlank()) {
            lblEstado.setText("Debe ingresar la hora.");
            return;
        }

        LocalTime hora;

        try {

            hora = LocalTime.parse(
                    txtHora.getText().trim()
            );

        } catch (DateTimeParseException e) {

            lblEstado.setText(
                    "La hora debe tener el formato HH:mm."
            );

            return;
        }

        Expositor expositor = new Expositor(
                0,
                txtIdentificacion.getText().trim(),
                txtNombre.getText().trim(),
                txtApellidos.getText().trim(),
                txtCorreo.getText().trim(),
                txtTelefono.getText().trim(),
                convertirEstadoPersona(cmbEstadoPersona.getValue()),
                txtEspecialidad.getText().trim(),
                txtDescripcion.getText().trim()
        );

        try {

            inscripcionService.registrarExpositorConInscripcion(
                    expositor,
                    cmbActividad.getValue().getIdActividad(),
                    dpFecha.getValue().toString(),
                    hora.toString(),
                    EstadoInscripcion.ACTIVA
            );

            if (inscripcionesController != null) {
                inscripcionesController.actualizarTabla();
            }

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);

            alerta.setTitle("Registro exitoso");
            alerta.setHeaderText(null);
            alerta.setContentText(
                    "Expositor inscrito correctamente."
            );

            alerta.showAndWait();

            cerrarFormulario();

        } catch (SQLException
                | DatosInvalidosException
                | RegistroNoEncontradoException
                | ActividadNoDisponibleException
                | CupoAgotadoException
                | InscripcionDuplicadaException e) {

            lblEstado.setText(
                    "Error al registrar el expositor: "
                    + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    private void actualizarExpositor() {

        expositorEditar.setIdentificacion(
                txtIdentificacion.getText().trim()
        );

        expositorEditar.setNombre(
                txtNombre.getText().trim()
        );

        expositorEditar.setApellidos(
                txtApellidos.getText().trim()
        );

        expositorEditar.setCorreo(
                txtCorreo.getText().trim()
        );

        expositorEditar.setTelefono(
                txtTelefono.getText().trim()
        );

        expositorEditar.setEspecialidad(
                txtEspecialidad.getText().trim()
        );

        expositorEditar.setDescripcionProfesional(
                txtDescripcion.getText().trim()
        );

        expositorEditar.setEstado(
                convertirEstadoPersona(
                        cmbEstadoPersona.getValue()
                )
        );

        try {

            expositorService.actualizarExpositor(expositorEditar);

            if (inscripcionesController != null) {
                inscripcionesController.actualizarTabla();
            }

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);

            alerta.setTitle("Actualización exitosa");
            alerta.setHeaderText(null);
            alerta.setContentText(
                    "Expositor actualizado correctamente."
            );

            alerta.showAndWait();

            cerrarFormulario();

        } catch (SQLException e) {

            lblEstado.setText(
                    "Error al actualizar el expositor: "
                    + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    private EstadoUsuario convertirEstadoPersona(String estado) {

        if ("Inactivo".equals(estado)) {
            return EstadoUsuario.INACTIVO;
        }

        return EstadoUsuario.ACTIVO;
    }

    private boolean correoValido(String correo) {

        String expresion =
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        return Pattern.matches(expresion, correo);
    }

    private void cerrarFormulario() {

        Stage stage = (Stage) txtIdentificacion
                .getScene()
                .getWindow();

        stage.close();
    }

    @FXML
    private void cancelar() {
        cerrarFormulario();
    }
}
