
package cr.ac.ejemplo.controller;

import cr.ac.ejemplo.DAOFactory;
import cr.ac.ejemplo.model.Actividad;
import cr.ac.ejemplo.model.Evento;
import cr.ac.ejemplo.model.enums.EstadoActividad;
import cr.ac.ejemplo.service.ActividadService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class ActividadFormularioController {

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
    private ComboBox<EstadoActividad> cmbEstado;

    @FXML
    private Label lblTitulo;

    @FXML
    private Label lblEstado;

    private final ActividadService actividadService =
            DAOFactory.crearActividadService();

    private ActividadesController actividadesController;

    private Evento eventoSeleccionado;

    private Actividad actividadEditar;

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
                EstadoActividad.values()
        );

        cmbEstado.setConverter(
                new StringConverter<EstadoActividad>() {

            @Override
            public String toString(EstadoActividad estado) {

                if (estado == null) {
                    return "";
                }

                switch (estado) {
                    case ABIERTA:
                        return "Abierta";

                    case EN_EJECUCION:
                        return "En ejecución";

                    case FINALIZADA:
                        return "Finalizada";

                    case CANCELADA:
                        return "Cancelada";

                    default:
                        return estado.name();
                }
            }

            @Override
            public EstadoActividad fromString(String texto) {
                return null;
            }
        });

        cmbTipo.setValue("Conferencia");
        cmbEstado.setValue(EstadoActividad.ABIERTA);
    }

    public void establecerActividadesController(
            ActividadesController actividadesController) {

        this.actividadesController = actividadesController;
    }

    public void establecerEvento(Evento evento) {

        this.eventoSeleccionado = evento;
    }

    public void establecerActividad(Actividad actividad) {

        this.actividadEditar = actividad;

        if (actividad != null) {

            lblTitulo.setText("EDITAR ACTIVIDAD");

            txtNombre.setText(actividad.getNombre());
            txtDescripcion.setText(actividad.getDescripcion());

            dpFecha.setValue(
                    LocalDate.parse(actividad.getFecha())
            );

            txtHoraInicio.setText(actividad.getHoraInicio());
            txtHoraFinal.setText(actividad.getHoraFin());
            txtLugar.setText(actividad.getLugar());

            txtCapacidad.setText(
                    String.valueOf(
                            actividad.getCapacidadMaxima()
                    )
            );

            cmbTipo.setValue(actividad.getTipo());
            cmbEstado.setValue(actividad.getEstado());

            eventoSeleccionado = actividad.getEvento();
        }
    }

    @FXML
    private void guardar() {

        String nombre = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        String horaInicioTexto = txtHoraInicio.getText().trim();
        String horaFinalTexto = txtHoraFinal.getText().trim();
        String lugar = txtLugar.getText().trim();
        String capacidadTexto = txtCapacidad.getText().trim();

        if (nombre.isEmpty()) {
            lblEstado.setText("Debe ingresar el nombre.");
            return;
        }

        if (dpFecha.getValue() == null) {
            lblEstado.setText("Debe seleccionar la fecha.");
            return;
        }

        LocalDate fecha = dpFecha.getValue();

        if (actividadEditar == null
                && fecha.isBefore(LocalDate.now())) {

            lblEstado.setText(
                    "La fecha de la actividad no puede ser pasada."
            );

            return;
        }

        if (horaInicioTexto.isEmpty()
                || horaFinalTexto.isEmpty()) {

            lblEstado.setText(
                    "Debe ingresar ambas horas."
            );

            return;
        }

        LocalTime horaInicio;
        LocalTime horaFinal;

        try {

            horaInicio = LocalTime.parse(horaInicioTexto);
            horaFinal = LocalTime.parse(horaFinalTexto);

        } catch (DateTimeParseException ex) {

            lblEstado.setText(
                    "Las horas deben tener el formato HH:mm."
            );

            return;
        }

        if (!horaFinal.isAfter(horaInicio)) {

            lblEstado.setText(
                    "La hora final debe ser posterior a la inicial."
            );

            return;
        }

        if (lugar.isEmpty()) {
            lblEstado.setText("Debe ingresar el lugar o salón.");
            return;
        }

        if (capacidadTexto.isEmpty()) {
            lblEstado.setText("Debe ingresar la capacidad máxima.");
            return;
        }

        int capacidad;

        try {

            capacidad = Integer.parseInt(capacidadTexto);

        } catch (NumberFormatException ex) {

            lblEstado.setText(
                    "La capacidad debe ser un número entero."
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
                    "Debe seleccionar el tipo de actividad."
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

        if (actividadesController == null) {

            lblEstado.setText(
                    "No se pudo establecer la pantalla de actividades."
            );

            return;
        }

        int idActividad = actividadEditar == null
                ? 0
                : actividadEditar.getIdActividad();

        Actividad actividad = new Actividad(
                idActividad,
                nombre,
                descripcion,
                fecha.toString(),
                horaInicioTexto,
                horaFinalTexto,
                lugar,
                capacidad,
                cmbTipo.getValue(),
                cmbEstado.getValue(),
                eventoSeleccionado
        );

        try {

            if (actividadEditar == null) {

                actividadService.registrarActividad(actividad);

                lblEstado.setText(
                        "Actividad registrada correctamente."
                );

            } else {

                actividadService.actualizarActividad(actividad);

                lblEstado.setText(
                        "Actividad actualizada correctamente."
                );
            }

            actividadesController.cargarActividades();

            Stage stage = (Stage) lblEstado
                    .getScene()
                    .getWindow();

            stage.close();

        } catch (SQLException | IllegalArgumentException ex) {

            lblEstado.setText(ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void cancelar() {

        Stage stage = (Stage) lblEstado
                .getScene()
                .getWindow();

        stage.close();
    }
}
