
package cr.ac.ejemplo.controller;

import cr.ac.ejemplo.DAOFactory;
import cr.ac.ejemplo.model.Participante;
import cr.ac.ejemplo.model.enums.EstadoUsuario;
import cr.ac.ejemplo.service.ParticipanteService;

import java.sql.SQLException;
import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ParticipanteFormularioController {

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
    private DatePicker dpFechaInscripcion;

    @FXML
    private ComboBox<String> cmbEstado;

    @FXML
    private Label lblTitulo;

    @FXML
    private Label lblEstado;

    private final ParticipanteService participanteService =
            DAOFactory.crearParticipanteService();

    private Participante participanteEditar;

    private InscripcionesController inscripcionesController;

    @FXML
    private void initialize() {

        cmbEstado.getItems().addAll(
                "Activo",
                "Inactivo"
        );

        cmbEstado.setValue("Activo");
        dpFechaInscripcion.setValue(LocalDate.now());
    }

    public void establecerInscripcionesController(
            InscripcionesController controller) {

        this.inscripcionesController = controller;
    }

    public void establecerParticipante(Participante participante) {

        this.participanteEditar = participante;

        lblTitulo.setText("EDITAR PARTICIPANTE");

        txtIdentificacion.setText(participante.getIdentificacion());
        txtNombre.setText(participante.getNombre());
        txtApellidos.setText(participante.getApellidos());
        txtCorreo.setText(participante.getCorreo());
        txtTelefono.setText(participante.getTelefono());

        dpFechaInscripcion.setValue(
                LocalDate.parse(participante.getFechaRegistro())
        );

        cmbEstado.setValue(
                participante.getEstado() == EstadoUsuario.ACTIVO
                        ? "Activo"
                        : "Inactivo"
        );

        dpFechaInscripcion.setDisable(true);
    }

    @FXML
    private void guardar() {

        if (txtIdentificacion.getText().isBlank()) {
            lblEstado.setText("Debe ingresar la identificación.");
            return;
        }

        if (txtNombre.getText().isBlank()) {
            lblEstado.setText("Debe ingresar el nombre.");
            return;
        }

        if (txtApellidos.getText().isBlank()) {
            lblEstado.setText("Debe ingresar los apellidos.");
            return;
        }

        if (txtCorreo.getText().isBlank()) {
            lblEstado.setText("Debe ingresar el correo.");
            return;
        }

        if (txtTelefono.getText().isBlank()) {
            lblEstado.setText("Debe ingresar el teléfono.");
            return;
        }

        if (cmbEstado.getValue() == null) {
            lblEstado.setText("Debe seleccionar el estado.");
            return;
        }

        EstadoUsuario estado = cmbEstado.getValue().equals("Activo")
                ? EstadoUsuario.ACTIVO
                : EstadoUsuario.INACTIVO;

        try {

            if (participanteEditar != null) {

                participanteEditar.setIdentificacion(
                        txtIdentificacion.getText().trim()
                );

                participanteEditar.setNombre(
                        txtNombre.getText().trim()
                );

                participanteEditar.setApellidos(
                        txtApellidos.getText().trim()
                );

                participanteEditar.setCorreo(
                        txtCorreo.getText().trim()
                );

                participanteEditar.setTelefono(
                        txtTelefono.getText().trim()
                );

                participanteEditar.setEstado(estado);

                participanteService.actualizarParticipante(
                        participanteEditar
                );

                if (inscripcionesController != null) {
                    inscripcionesController.actualizarTabla();
                }

                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("Actualización exitosa");
                alerta.setHeaderText(null);
                alerta.setContentText(
                        "Participante actualizado correctamente."
                );
                alerta.showAndWait();

            } else {

                lblEstado.setText(
                        "El registro de participantes se realiza desde Inscripciones."
                );

                return;
            }

            Stage stage = (Stage) txtIdentificacion
                    .getScene()
                    .getWindow();

            stage.close();

        } catch (SQLException e) {

            lblEstado.setText(
                    "Error al guardar: " + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    @FXML
    private void cancelar() {

        Stage stage = (Stage) txtIdentificacion
                .getScene()
                .getWindow();

        stage.close();
    }
}
