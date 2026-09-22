package cr.ac.ejemplo;

import java.util.regex.Pattern;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class InscripcionFormularioController {

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtApellidos;

    @FXML
    private TextField txtIdentificacion;

    @FXML
    private TextField txtCorreo;

    @FXML
    private TextField txtTelefono;

    @FXML
    private ComboBox<Actividad> cmbActividad;

    @FXML
    private DatePicker dpFecha;

    @FXML
    private TextField txtHora;

    @FXML
    private ComboBox<String> cmbEstado;

    @FXML
    private Label lblTitulo;

    @FXML
    private Label lblEstado;

    private InscripcionesController inscripcionesController;

    @FXML
    private void initialize() {

        cmbActividad.setItems(
                DatosSistema.getActividades()
        );

        cmbEstado.getItems().addAll(
                "Activa",
                "Cancelada",
                "Completada"
        );

        cmbEstado.setValue("Activa");
    }

    public void establecerInscripcionesController(
            InscripcionesController inscripcionesController) {

        this.inscripcionesController =
                inscripcionesController;
    }

    @FXML
    private void guardar() {

        if (txtId.getText().isBlank()) {

            lblEstado.setText(
                    "Debe ingresar el ID de la inscripción."
            );

            return;
        }

        int idInscripcion;

        try {

            idInscripcion = Integer.parseInt(
                    txtId.getText()
            );

        } catch (NumberFormatException ex) {

            lblEstado.setText(
                    "El ID de la inscripción debe ser un número entero."
            );

            return;
        }

        if (idInscripcion <= 0) {

            lblEstado.setText(
                    "El ID de la inscripción debe ser mayor que cero."
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

        if (txtIdentificacion.getText().isBlank()) {

            lblEstado.setText(
                    "Debe ingresar la identificación."
            );

            return;
        }

        if (txtCorreo.getText().isBlank()) {

            lblEstado.setText(
                    "Debe ingresar el correo."
            );

            return;
        }

        if (!correoValido(txtCorreo.getText())) {

            lblEstado.setText(
                    "El correo ingresado no es válido."
            );

            return;
        }

        if (txtTelefono.getText().isBlank()) {

            lblEstado.setText(
                    "Debe ingresar el teléfono."
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

        if (cmbEstado.getValue() == null) {

            lblEstado.setText(
                    "Debe seleccionar el estado."
            );

            return;
        }

        int idPersona =
                DatosSistema.getParticipantes().size()
                + DatosSistema.getExpositores().size()
                + 1;

        Participante participante =
                new Participante(
                        idPersona,
                        txtIdentificacion.getText(),
                        txtNombre.getText(),
                        txtApellidos.getText(),
                        txtCorreo.getText(),
                        txtTelefono.getText(),
                        "Activo",
                        dpFecha.getValue().toString()
                );

        DatosSistema.getParticipantes().add(
                participante
        );

        Inscripcion inscripcion =
                new Inscripcion(
                        idInscripcion,
                        participante,
                        cmbActividad.getValue(),
                        dpFecha.getValue().toString(),
                        txtHora.getText(),
                        "Participante",
                        cmbEstado.getValue()
                );

        DatosSistema.getInscripciones().add(
                inscripcion
        );

        lblEstado.setText(
                "Participante inscrito correctamente."
        );

        Stage stage = (Stage) txtId
                .getScene()
                .getWindow();

        stage.close();
    }

    private boolean correoValido(String correo) {

        String expresion =
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        return Pattern.matches(
                expresion,
                correo
        );
    }

    @FXML
    private void cancelar() {

        Stage stage = (Stage) txtId
                .getScene()
                .getWindow();

        stage.close();
    }
}
