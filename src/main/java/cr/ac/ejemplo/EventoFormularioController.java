package cr.ac.ejemplo;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EventoFormularioController {

    @FXML
    private TextField txtCodigo;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextArea txtDescripcion;

    @FXML
    private DatePicker dpFechaInicio;

    @FXML
    private DatePicker dpFechaFinal;

    @FXML
    private TextField txtLugar;

    @FXML
    private ComboBox<String> cmbEstado;

    @FXML
    private Label lblTitulo;

    @FXML
    private Label lblEstado;

    private EventosController eventosController;

    @FXML
    private void initialize() {

        cmbEstado.getItems().addAll(
                "Planificación",
                "Abierto",
                "En ejecución",
                "Finalizado",
                "Cancelado"
        );

        cmbEstado.setValue("Planificación");
    }

    public void establecerEventosController(
            EventosController eventosController) {

        this.eventosController = eventosController;
    }

    @FXML
    private void guardar() {

        if (txtCodigo.getText().isBlank()) {

            lblEstado.setText(
                    "Debe ingresar el identificador."
            );

            return;
        }

        int idEvento;

        try {

            idEvento = Integer.parseInt(
                    txtCodigo.getText()
            );

        } catch (NumberFormatException ex) {

            lblEstado.setText(
                    "El identificador debe ser un número entero."
            );

            return;
        }

        if (idEvento <= 0) {

            lblEstado.setText(
                    "El identificador debe ser mayor que cero."
            );

            return;
        }

        if (txtNombre.getText().isBlank()) {

            lblEstado.setText(
                    "Debe ingresar el nombre."
            );

            return;
        }

        if (dpFechaInicio.getValue() == null) {

            lblEstado.setText(
                    "Debe seleccionar la fecha de inicio."
            );

            return;
        }

        if (dpFechaFinal.getValue() == null) {

            lblEstado.setText(
                    "Debe seleccionar la fecha final."
            );

            return;
        }

        if (dpFechaFinal.getValue()
                .isBefore(dpFechaInicio.getValue())) {

            lblEstado.setText(
                    "La fecha final no puede ser anterior a la fecha de inicio."
            );

            return;
        }

        if (txtLugar.getText().isBlank()) {

            lblEstado.setText(
                    "Debe ingresar el lugar."
            );

            return;
        }

        if (cmbEstado.getValue() == null) {

            lblEstado.setText(
                    "Debe seleccionar el estado."
            );

            return;
        }

        Evento evento = new Evento(
                idEvento,
                txtNombre.getText(),
                txtDescripcion.getText(),
                dpFechaInicio.getValue().toString(),
                dpFechaFinal.getValue().toString(),
                txtLugar.getText(),
                cmbEstado.getValue()
        );

        eventosController.agregarEvento(
                evento
        );

        lblEstado.setText(
                "Evento registrado correctamente."
        );

        Stage stage = (Stage) txtCodigo
                .getScene()
                .getWindow();

        stage.close();
    }

    @FXML
    private void cancelar() {

        Stage stage = (Stage) txtCodigo
                .getScene()
                .getWindow();

        stage.close();
    }
}
