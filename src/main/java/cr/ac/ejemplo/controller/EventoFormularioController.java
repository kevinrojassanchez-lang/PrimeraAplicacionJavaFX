
package cr.ac.ejemplo.controller;

import cr.ac.ejemplo.model.Evento;
import cr.ac.ejemplo.model.enums.EstadoEvento;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class EventoFormularioController {

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
    private ComboBox<EstadoEvento> cmbEstado;

    @FXML
    private Label lblTitulo;

    @FXML
    private Label lblEstado;

    private EventosController eventosController;

    private Evento eventoEditar;

    @FXML
    private void initialize() {

        cmbEstado.getItems().addAll(EstadoEvento.values());

        cmbEstado.setConverter(new StringConverter<EstadoEvento>() {

            @Override
            public String toString(EstadoEvento estado) {

                if (estado == null) {
                    return "";
                }

                switch (estado) {
                    case ABIERTO:
                        return "Abierto";
                    case EN_EJECUCION:
                        return "En ejecución";
                    case FINALIZADO:
                        return "Finalizado";
                    case CANCELADO:
                        return "Cancelado";
                    default:
                        return estado.name();
                }
            }

            @Override
            public EstadoEvento fromString(String texto) {
                return null;
            }
        });

        cmbEstado.setValue(EstadoEvento.ABIERTO);
    }

    public void establecerEventosController(
            EventosController eventosController) {

        this.eventosController = eventosController;
    }

    // Preparar el formulario para editar
    public void establecerEvento(Evento evento) {

        this.eventoEditar = evento;

        lblTitulo.setText("EDITAR EVENTO");

        txtNombre.setText(evento.getNombre());
        txtDescripcion.setText(evento.getDescripcion());
        dpFechaInicio.setValue(
                java.time.LocalDate.parse(evento.getFechaInicio())
        );
        dpFechaFinal.setValue(
                java.time.LocalDate.parse(evento.getFechaFin())
        );
        txtLugar.setText(evento.getLugar());
        cmbEstado.setValue(evento.getEstado());
    }

    @FXML
    private void guardar() {

        if (txtNombre.getText().isBlank()) {
            lblEstado.setText("Debe ingresar el nombre.");
            return;
        }

        if (dpFechaInicio.getValue() == null) {
            lblEstado.setText("Debe seleccionar la fecha de inicio.");
            return;
        }

        if (dpFechaFinal.getValue() == null) {
            lblEstado.setText("Debe seleccionar la fecha final.");
            return;
        }

        if (dpFechaFinal.getValue().isBefore(dpFechaInicio.getValue())) {
            lblEstado.setText(
                    "La fecha final no puede ser anterior a la fecha de inicio."
            );
            return;
        }

        if (txtLugar.getText().isBlank()) {
            lblEstado.setText("Debe ingresar el lugar.");
            return;
        }

        if (cmbEstado.getValue() == null) {
            lblEstado.setText("Debe seleccionar el estado.");
            return;
        }

        int idEvento = (eventoEditar == null) ? 0 : eventoEditar.getIdEvento();

        Evento evento = new Evento(
                idEvento,
                txtNombre.getText().trim(),
                txtDescripcion.getText().trim(),
                dpFechaInicio.getValue().toString(),
                dpFechaFinal.getValue().toString(),
                txtLugar.getText().trim(),
                cmbEstado.getValue()
        );

        boolean guardado;

        if (eventoEditar == null) {
            guardado = eventosController.agregarEvento(evento);
        } else {
            guardado = eventosController.actualizarEvento(evento);
        }

        if (guardado) {

            lblEstado.setText("Evento guardado correctamente.");

            Stage stage = (Stage) txtNombre.getScene().getWindow();
            stage.close();

        } else {

            lblEstado.setText(
                    "No se pudo guardar el evento."
            );
        }
    }

    @FXML
    private void cancelar() {

        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }
}
