package cr.ac.ejemplo;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ActividadFormularioController {

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextArea txtDescripcion;

    @FXML
    private DatePicker dpFecha;

    @FXML
    private TextField txtHoraInicio;

    @FXML
    private TextField txtHoraFinal;

    @FXML
    private TextField txtLugar;

    @FXML
    private TextField txtCapacidad;

    @FXML
    private ComboBox<String> cmbTipo;

    @FXML
    private ComboBox<String> cmbEstado;

    @FXML
    private Label lblTitulo;

    @FXML
    private Label lblEstado;

    private ActividadesController actividadesController;

    private Evento eventoSeleccionado;

    @FXML
    private void initialize() {

        cmbTipo.getItems().addAll(
                "Conferencia",
                "Taller",
                "Charla",
                "Mesa redonda",
                "Capacitación"
        );

        cmbEstado.getItems().addAll(
                "Planificación",
                "Abierta",
                "En ejecución",
                "Finalizada",
                "Cancelada"
        );

        cmbTipo.setValue("Conferencia");
        cmbEstado.setValue("Planificación");
    }

    public void establecerActividadesController(
            ActividadesController actividadesController) {

        this.actividadesController = actividadesController;
    }

    public void establecerEvento(Evento evento) {

        this.eventoSeleccionado = evento;
    }

    @FXML
    private void guardar() {

        if (txtId.getText().isBlank()) {

            lblEstado.setText(
                    "Debe ingresar el identificador."
            );

            return;
        }

        int idActividad;

        try {

            idActividad = Integer.parseInt(
                    txtId.getText()
            );

        } catch (NumberFormatException ex) {

            lblEstado.setText(
                    "El identificador debe ser un número entero."
            );

            return;
        }

        if (idActividad <= 0) {

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

        if (dpFecha.getValue() == null) {

            lblEstado.setText(
                    "Debe seleccionar la fecha."
            );

            return;
        }

        if (txtHoraInicio.getText().isBlank()) {

            lblEstado.setText(
                    "Debe ingresar la hora de inicio."
            );

            return;
        }

        if (txtHoraFinal.getText().isBlank()) {

            lblEstado.setText(
                    "Debe ingresar la hora final."
            );

            return;
        }

        if (txtLugar.getText().isBlank()) {

            lblEstado.setText(
                    "Debe ingresar el lugar o salón."
            );

            return;
        }

        if (txtCapacidad.getText().isBlank()) {

            lblEstado.setText(
                    "Debe ingresar la capacidad máxima."
            );

            return;
        }

        int capacidad;

        try {

            capacidad = Integer.parseInt(
                    txtCapacidad.getText()
            );

        } catch (NumberFormatException ex) {

            lblEstado.setText(
                    "La capacidad debe ser un número."
            );

            return;
        }

        if (capacidad <= 0) {

            lblEstado.setText(
                    "La capacidad debe ser mayor que cero."
            );

            return;
        }

        if (cmbTipo.getValue() == null) {

            lblEstado.setText(
                    "Debe seleccionar el tipo."
            );

            return;
        }

        if (cmbEstado.getValue() == null) {

            lblEstado.setText(
                    "Debe seleccionar el estado."
            );

            return;
        }

        if (eventoSeleccionado == null) {

            lblEstado.setText(
                    "No hay un evento seleccionado."
            );

            return;
        }

        Actividad actividad = new Actividad(
                idActividad,
                txtNombre.getText(),
                txtDescripcion.getText(),
                dpFecha.getValue().toString(),
                txtHoraInicio.getText(),
                txtHoraFinal.getText(),
                txtLugar.getText(),
                capacidad,
                cmbTipo.getValue(),
                cmbEstado.getValue(),
                eventoSeleccionado
        );

        actividadesController.agregarActividad(
                actividad
        );

        Stage stage = (Stage) txtId
                .getScene()
                .getWindow();

        stage.close();
    }

    @FXML
    private void cancelar() {

        Stage stage = (Stage) txtId
                .getScene()
                .getWindow();

        stage.close();
    }
}
