
package cr.ac.ejemplo.controller;

import cr.ac.ejemplo.DAOFactory;
import cr.ac.ejemplo.VentanaUtil;
import cr.ac.ejemplo.model.Asistencia;
import cr.ac.ejemplo.model.Inscripcion;
import cr.ac.ejemplo.model.Actividad;
import cr.ac.ejemplo.model.Evento;
import cr.ac.ejemplo.model.enums.EstadoAsistencia;
import cr.ac.ejemplo.service.AsistenciaService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class AsistenciaController {

    @FXML
    private TableView<Inscripcion> tablaAsistencia;

    @FXML
    private TableColumn<Inscripcion, String> colIdentificacion;

    @FXML
    private TableColumn<Inscripcion, String> colPersona;

    @FXML
    private TableColumn<Inscripcion, String> colTipo;

    @FXML
    private TableColumn<Inscripcion, String> colAsistencia;

    @FXML
    private Label lblActividad;

    @FXML
    private Label lblTotal;

    @FXML
    private Label lblAsistieron;

    @FXML
    private Label lblNoAsistieron;

    @FXML
    private Label lblPorcentaje;

    @FXML
    private Label lblEstado;

    private final AsistenciaService asistenciaService =
        DAOFactory.crearAsistenciaService();

    private final Map<Integer, Asistencia> asistenciasPorInscripcion =
            new HashMap<>();

    private Evento eventoSeleccionado;

    private Actividad actividadSeleccionada;

    @FXML
    private void initialize() {

        colIdentificacion.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        celda.getValue().getPersona()
                                .getIdentificacion()
                )
        );

        colPersona.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        celda.getValue().getPersona().getNombre()
                        + " "
                        + celda.getValue().getPersona().getApellidos()
                )
        );

        colTipo.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        celda.getValue().getTipo()
                )
        );

        colAsistencia.setCellFactory(
                columna -> new TableCell<Inscripcion, String>() {

                    private final ComboBox<String> combo =
                            new ComboBox<>();

                    private boolean actualizandoCombo = false;

                    {
                        combo.getItems().addAll(
                                "Pendiente",
                                "Asistió",
                                "No asistió"
                        );

                        combo.setMaxWidth(Double.MAX_VALUE);

                        combo.valueProperty().addListener(
                                (observable, anterior, nuevoValor) -> {

                                    if (actualizandoCombo
                                            || nuevoValor == null
                                            || nuevoValor.equals(anterior)) {
                                        return;
                                    }

                                    Inscripcion inscripcion =
                                            getTableRow() == null
                                            ? null
                                            : getTableRow().getItem();

                                    if (inscripcion != null) {
                                        actualizarAsistencia(
                                                inscripcion,
                                                nuevoValor
                                        );
                                    }
                                }
                        );
                    }

                    @Override
                    protected void updateItem(
                            String estado,
                            boolean vacio) {

                        super.updateItem(estado, vacio);

                        if (vacio || getTableRow() == null
                                || getTableRow().getItem() == null) {

                            setGraphic(null);

                        } else {

                            Inscripcion inscripcion =
                                    getTableRow().getItem();

                            Asistencia asistencia =
                                    asistenciasPorInscripcion.get(
                                            inscripcion.getIdInscripcion()
                                    );

                            actualizandoCombo = true;

                            if (asistencia != null) {
                                combo.setValue(
                                        convertirEstadoTexto(
                                                asistencia.getEstado()
                                        )
                                );
                            } else {
                                combo.setValue("Pendiente");
                            }

                            actualizandoCombo = false;

                            setGraphic(combo);
                        }
                    }
                }
        );

        tablaAsistencia.setItems(
                FXCollections.observableArrayList()
        );

        actualizarResumen();
    }

    private String convertirEstadoTexto(
            EstadoAsistencia estado) {

        if (estado == null) {
            return "Pendiente";
        }

        switch (estado) {
            case PENDIENTE:
                return "Pendiente";

            case ASISTIO:
                return "Asistió";

            case NO_ASISTIO:
                return "No asistió";

            default:
                return estado.name();
        }
    }

    private EstadoAsistencia convertirTextoEstado(
            String texto) {

        switch (texto) {
            case "Pendiente":
                return EstadoAsistencia.PENDIENTE;

            case "Asistió":
                return EstadoAsistencia.ASISTIO;

            case "No asistió":
                return EstadoAsistencia.NO_ASISTIO;

            default:
                return EstadoAsistencia.PENDIENTE;
        }
    }

    public void establecerActividad(
            Evento evento,
            Actividad actividad) {

        this.eventoSeleccionado = evento;
        this.actividadSeleccionada = actividad;

        if (actividad == null) {

            lblActividad.setText("Actividad seleccionada");

            tablaAsistencia.getItems().clear();

            asistenciasPorInscripcion.clear();

            actualizarResumen();

            return;
        }

        lblActividad.setText(
                "Actividad: " + actividad.getNombre()
        );

        cargarDatos();
    }

    private void cargarDatos() {

        if (actividadSeleccionada == null) {
            return;
        }

        try {

            asistenciasPorInscripcion.clear();

            int idActividad =
                    actividadSeleccionada.getIdActividad();

            ArrayList<Inscripcion> inscripciones =
                    asistenciaService.listarInscripciones(idActividad);

            ArrayList<Asistencia> asistencias =
                    asistenciaService.listarAsistencias(idActividad);

            for (Asistencia asistencia : asistencias) {

                if (asistencia.getInscripcion() != null) {

                    asistenciasPorInscripcion.put(
                            asistencia.getInscripcion().getIdInscripcion(),
                            asistencia
                    );
                }
            }

            ObservableList<Inscripcion> lista =
                    FXCollections.observableArrayList(inscripciones);

            tablaAsistencia.setItems(lista);

            actualizarResumen();

            lblEstado.setText(
                    "Personas inscritas en la actividad: "
                    + lista.size()
            );

        } catch (SQLException e) {

            lblEstado.setText(
                    "Error al consultar la asistencia: "
                    + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    private void actualizarAsistencia(
            Inscripcion inscripcion,
            String estado) {

        try {

            EstadoAsistencia estadoEnum =
                    convertirTextoEstado(estado);

            int idInscripcion =
                    inscripcion.getIdInscripcion();

            Asistencia asistencia =
                    asistenciasPorInscripcion.get(idInscripcion);

            if (asistencia == null) {

                asistencia = asistenciaService.buscarPorInscripcion(
                        idInscripcion
                );

                if (asistencia == null) {

                    asistencia = new Asistencia(
                            0,
                            inscripcion,
                            estadoEnum
                    );

                    asistenciaService.registrarAsistencia(asistencia);

                } else {

                    asistencia.setEstado(estadoEnum);

                    asistenciaService.actualizarAsistencia(asistencia);
                }

                asistenciasPorInscripcion.put(
                        idInscripcion,
                        asistencia
                );

            } else {

                asistencia.setEstado(estadoEnum);

                asistenciaService.actualizarAsistencia(asistencia);
            }

            actualizarResumen();

            lblEstado.setText(
                    "Asistencia guardada correctamente en MySQL."
            );

        } catch (SQLException e) {

            lblEstado.setText(
                    "Error al guardar la asistencia: "
                    + e.getMessage()
            );

            e.printStackTrace();

            cargarDatos();
        }
    }

    private void actualizarResumen() {

        if (tablaAsistencia == null) {
            return;
        }

        int total = tablaAsistencia.getItems().size();

        int asistieron = 0;

        int noAsistieron = 0;

        for (Inscripcion inscripcion :
                tablaAsistencia.getItems()) {

            Asistencia asistencia =
                    asistenciasPorInscripcion.get(
                            inscripcion.getIdInscripcion()
                    );

            if (asistencia != null) {

                if (asistencia.getEstado()
                        == EstadoAsistencia.ASISTIO) {

                    asistieron++;

                } else if (asistencia.getEstado()
                        == EstadoAsistencia.NO_ASISTIO) {

                    noAsistieron++;
                }
            }
        }

        double porcentaje = 0;

        if (total > 0) {
            porcentaje = (asistieron * 100.0) / total;
        }

        lblTotal.setText(String.valueOf(total));

        lblAsistieron.setText(String.valueOf(asistieron));

        lblNoAsistieron.setText(String.valueOf(noAsistieron));

        lblPorcentaje.setText(
                String.format("%.1f%%", porcentaje)
        );
    }

    @FXML
    private void guardarAsistencia() {

        cargarDatos();

        lblEstado.setText(
                "Los registros de asistencia están guardados en MySQL."
        );
    }

    @FXML
    private void consultar() {

        cargarDatos();

        if (actividadSeleccionada != null) {
            lblEstado.setText(
                    "Consulta de asistencia realizada correctamente."
            );
        }
    }

    @FXML
    private void regresar() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    AsistenciaController.class.getResource(
                            "/cr/ac/actividadDetalle.fxml"
                    )
            );

            Scene scene = new Scene(loader.load());

            ActividadDetalleController controller =
                    loader.getController();

            controller.establecerDatos(
                    eventoSeleccionado,
                    actividadSeleccionada
            );

            Stage stage = (Stage) tablaAsistencia
                    .getScene()
                    .getWindow();

            stage.setTitle("Detalle de la actividad");

            stage.setScene(scene);

            VentanaUtil.configurar(stage);

            stage.show();

        } catch (IOException ex) {

            ex.printStackTrace();
        }
    }
}
