package cr.ac.ejemplo;

import javafx.fxml.FXML;
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

    @FXML
    private void initialize() {

        cmbEstado.getItems().addAll(
                "Activo",
                "Inactivo"
        );

        cmbEstado.setValue("Activo");
    }

    @FXML
    private void guardar() {

        if (txtIdentificacion.getText().isBlank()) {
            lblEstado.setText(
                    "Debe ingresar la identificación."
            );
            return;
        }

        if (txtNombre.getText().isBlank()) {
            lblEstado.setText(
                    "Debe ingresar el nombre."
            );
            return;
        }

        if (txtApellidos.getText().isBlank()) {
            lblEstado.setText(
                    "Debe ingresar los apellidos."
            );
            return;
        }

        if (txtCorreo.getText().isBlank()) {
            lblEstado.setText(
                    "Debe ingresar el correo."
            );
            return;
        }

        if (txtTelefono.getText().isBlank()) {
            lblEstado.setText(
                    "Debe ingresar el teléfono."
            );
            return;
        }

        if (dpFechaInscripcion.getValue() == null) {
            lblEstado.setText(
                    "Debe seleccionar la fecha de inscripción."
            );
            return;
        }

        if (cmbEstado.getValue() == null) {
            lblEstado.setText(
                    "Debe seleccionar el estado."
            );
            return;
        }

        lblEstado.setText(
                "Participante guardado correctamente."
        );
    }

    @FXML
    private void cancelar() {

        Stage stage = (Stage) txtIdentificacion
                .getScene()
                .getWindow();

        stage.close();
    }
}
