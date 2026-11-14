
package cr.ac.ejemplo.controller;

import cr.ac.ejemplo.VentanaUtil;
import cr.ac.ejemplo.DAOFactory;
import cr.ac.ejemplo.model.Inscripcion;
import cr.ac.ejemplo.model.Actividad;
import cr.ac.ejemplo.model.enums.EstadoInscripcion;
import cr.ac.ejemplo.service.ConsultaService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ConsultasController {

    private final ConsultaService consultaService =
            DAOFactory.crearConsultaService();

    private ArrayList<Actividad> actividades = new ArrayList<>();
    private ArrayList<Inscripcion> inscripciones = new ArrayList<>();

    @FXML
    private TextField txtIdEventoActividad;

    @FXML
    private DatePicker dpActividad;

    @FXML
    private ComboBox<String> cmbTipoActividad;

    @FXML
    private ComboBox<String> cmbDisponibilidad;

    @FXML
    private TableView<Actividad> tablaActividades;

    @FXML
    private TableColumn<Actividad, String> colActividadId;

    @FXML
    private TableColumn<Actividad, String> colActividadNombre;

    @FXML
    private TableColumn<Actividad, String> colActividadTipo;

    @FXML
    private TableColumn<Actividad, String> colActividadFecha;

    @FXML
    private TableColumn<Actividad, String> colActividadEvento;

    @FXML
    private TableColumn<Actividad, String> colActividadDisponibles;

    @FXML
    private TextField txtCedulaInscripcion;

    @FXML
    private ComboBox<String> cmbTipoInscripcion;

    @FXML
    private TextField txtIdActividadInscripcion;

    @FXML
    private ComboBox<String> cmbEstadoInscripcion;

    @FXML
    private TableView<Inscripcion> tablaInscripciones;

    @FXML
    private TableColumn<Inscripcion, String> colInscripcionId;

    @FXML
    private TableColumn<Inscripcion, String> colInscripcionPersona;

    @FXML
    private TableColumn<Inscripcion, String> colInscripcionTipo;

    @FXML
    private TableColumn<Inscripcion, String> colInscripcionActividad;

    @FXML
    private TableColumn<Inscripcion, String> colInscripcionFecha;

    @FXML
    private TableColumn<Inscripcion, String> colInscripcionEstado;

    @FXML
    private Label lblEstado;

    @FXML
    private void initialize() {

        configurarTablaActividades();
        configurarTablaInscripciones();

        cmbTipoActividad.getItems().setAll(
                "Conferencia",
                "Taller",
                "Charla",
                "Mesa redonda",
                "Capacitación"
        );

        cmbDisponibilidad.getItems().setAll(
                "Todas",
                "Con espacios",
                "Sin espacios"
        );

        cmbTipoInscripcion.getItems().setAll(
                "Participante",
                "Expositor"
        );

        cmbEstadoInscripcion.getItems().setAll(
                "Activa",
                "Cancelada"
        );

        try {

            actividades = consultaService.listarActividades();
            inscripciones = consultaService.listarInscripciones();

            lblEstado.setText("");

        } catch (SQLException ex) {

            lblEstado.setText(
                    "Error al cargar los datos de las consultas."
            );

            ex.printStackTrace();
        }
    }

    private void configurarTablaActividades() {

        colActividadId.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        String.valueOf(
                                celda.getValue().getIdActividad()
                        )
                )
        );

        colActividadNombre.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        celda.getValue().getNombre()
                )
        );

        colActividadTipo.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        celda.getValue().getTipo()
                )
        );

        colActividadFecha.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        celda.getValue().getFecha()
                )
        );

        colActividadEvento.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        celda.getValue().getEvento().getIdEvento()
                        + " - "
                        + celda.getValue().getEvento().getNombre()
                )
        );

        colActividadDisponibles.setCellValueFactory(
                celda -> {

                    Actividad actividad = celda.getValue();

                    int inscritos =
                            contarInscripcionesActivas(actividad);

                    int disponibles =
                            actividad.getCapacidadMaxima() - inscritos;

                    return new SimpleStringProperty(
                            String.valueOf(disponibles)
                    );
                }
        );

        tablaActividades.setItems(
                FXCollections.observableArrayList()
        );
    }

    private void configurarTablaInscripciones() {

        colInscripcionId.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        String.valueOf(
                                celda.getValue().getIdInscripcion()
                        )
                )
        );

        colInscripcionPersona.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        celda.getValue().getPersona().getNombre()
                        + " "
                        + celda.getValue().getPersona().getApellidos()
                )
        );

        colInscripcionTipo.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        celda.getValue().getTipo()
                )
        );

        colInscripcionActividad.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        celda.getValue().getActividad().getNombre()
                )
        );

        colInscripcionFecha.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        celda.getValue().getFecha()
                )
        );

        colInscripcionEstado.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        convertirEstadoTexto(
                                celda.getValue().getEstado()
                        )
                )
        );

        tablaInscripciones.setItems(
                FXCollections.observableArrayList()
        );
    }

    private String convertirEstadoTexto(
            EstadoInscripcion estado) {

        if (estado == null) {
            return "";
        }

        switch (estado) {

            case ACTIVA:
                return "Activa";

            case CANCELADA:
                return "Cancelada";

            default:
                return estado.name();
        }
    }

    private EstadoInscripcion convertirTextoEstado(
            String texto) {

        switch (texto) {

            case "Activa":
                return EstadoInscripcion.ACTIVA;

            case "Cancelada":
                return EstadoInscripcion.CANCELADA;

            default:
                return null;
        }
    }

    @FXML
    private void buscarActividades() {

        try {

            // Actualizar inscripciones desde MySQL.
            inscripciones = consultaService.listarInscripciones();

            String textoIdEvento =
                    txtIdEventoActividad.getText().trim();

            Integer idEvento = null;

            if (!textoIdEvento.isEmpty()) {

                idEvento = Integer.parseInt(textoIdEvento);

                if (idEvento <= 0) {

                    lblEstado.setText(
                            "El ID del evento debe ser mayor que cero."
                    );

                    tablaActividades.getItems().clear();
                    return;
                }
            }

            boolean hayCriterio =
                    idEvento != null
                    || dpActividad.getValue() != null
                    || cmbTipoActividad.getValue() != null
                    || cmbDisponibilidad.getValue() != null;

            if (!hayCriterio) {

                lblEstado.setText(
                        "Escriba un ID o seleccione al menos un criterio."
                );

                tablaActividades.getItems().clear();
                return;
            }

            ObservableList<Actividad> resultados =
                    FXCollections.observableArrayList();

            for (Actividad actividad : actividades) {

                boolean cumple = true;

                if (idEvento != null) {

                    if (actividad.getEvento().getIdEvento()
                            != idEvento) {

                        cumple = false;
                    }
                }

                if (dpActividad.getValue() != null) {

                    if (!actividad.getFecha().equals(
                            dpActividad.getValue().toString())) {

                        cumple = false;
                    }
                }

                if (cmbTipoActividad.getValue() != null) {

                    if (!actividad.getTipo().equals(
                            cmbTipoActividad.getValue())) {

                        cumple = false;
                    }
                }

                if (cmbDisponibilidad.getValue() != null) {

                    int inscritos =
                            contarInscripcionesActivas(actividad);

                    int disponibles =
                            actividad.getCapacidadMaxima() - inscritos;

                    if (cmbDisponibilidad.getValue().equals(
                            "Con espacios")) {

                        if (disponibles <= 0) {
                            cumple = false;
                        }
                    }

                    if (cmbDisponibilidad.getValue().equals(
                            "Sin espacios")) {

                        if (disponibles > 0) {
                            cumple = false;
                        }
                    }
                }

                if (cumple) {
                    resultados.add(actividad);
                }
            }

            tablaActividades.setItems(resultados);

            lblEstado.setText(
                    "Actividades encontradas: " + resultados.size()
            );

        } catch (NumberFormatException ex) {

            lblEstado.setText(
                    "El ID del evento debe contener solamente números."
            );

            tablaActividades.getItems().clear();

        } catch (SQLException ex) {

            lblEstado.setText(
                    "Error al actualizar la disponibilidad."
            );

            ex.printStackTrace();
        }
    }

    @FXML
    private void buscarInscripciones() {

        try {

            // Actualizar inscripciones desde MySQL.
            inscripciones = consultaService.listarInscripciones();

            String cedula =
                    txtCedulaInscripcion.getText().trim();

            String textoIdActividad =
                    txtIdActividadInscripcion.getText().trim();

            Integer idActividad = null;

            if (!textoIdActividad.isEmpty()) {

                idActividad = Integer.parseInt(textoIdActividad);

                if (idActividad <= 0) {

                    lblEstado.setText(
                            "El ID de actividad debe ser mayor que cero."
                    );

                    tablaInscripciones.getItems().clear();
                    return;
                }
            }

            boolean hayCriterio =
                    !cedula.isEmpty()
                    || cmbTipoInscripcion.getValue() != null
                    || idActividad != null
                    || cmbEstadoInscripcion.getValue() != null;

            if (!hayCriterio) {

                lblEstado.setText(
                        "Escriba una cédula o un ID, o seleccione un filtro."
                );

                tablaInscripciones.getItems().clear();
                return;
            }

            ObservableList<Inscripcion> resultados =
                    FXCollections.observableArrayList();

            for (Inscripcion inscripcion : inscripciones) {

                boolean cumple = true;

                if (!cedula.isEmpty()) {

                    String cedulaActual =
                            inscripcion.getPersona().getIdentificacion();

                    if (cedulaActual == null
                            || !cedulaActual.trim().equalsIgnoreCase(cedula)) {

                        cumple = false;
                    }
                }

                if (cmbTipoInscripcion.getValue() != null) {

                    if (!inscripcion.getTipo().equals(
                            cmbTipoInscripcion.getValue())) {

                        cumple = false;
                    }
                }

                if (idActividad != null) {

                    if (inscripcion.getActividad().getIdActividad()
                            != idActividad) {

                        cumple = false;
                    }
                }

                if (cmbEstadoInscripcion.getValue() != null) {

                    EstadoInscripcion estadoBuscado =
                            convertirTextoEstado(
                                    cmbEstadoInscripcion.getValue()
                            );

                    if (inscripcion.getEstado() != estadoBuscado) {
                        cumple = false;
                    }
                }

                if (cumple) {
                    resultados.add(inscripcion);
                }
            }

            tablaInscripciones.setItems(resultados);

            lblEstado.setText(
                    "Inscripciones encontradas: " + resultados.size()
            );

        } catch (NumberFormatException ex) {

            lblEstado.setText(
                    "El ID de actividad debe contener solamente números."
            );

            tablaInscripciones.getItems().clear();

        } catch (SQLException ex) {

            lblEstado.setText(
                    "Error al actualizar las inscripciones."
            );

            ex.printStackTrace();
        }
    }

    private int contarInscripcionesActivas(
            Actividad actividad) {

        return consultaService.contarInscripcionesActivas(
                actividad.getIdActividad(),
                inscripciones
        );
    }

    @FXML
    private void regresar(ActionEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    ConsultasController.class.getResource(
                            "/cr/ac/menu.fxml"
                    )
            );

            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.setTitle("Menú principal");
            stage.setScene(scene);
            VentanaUtil.configurar(stage);
            stage.show();

        } catch (IOException ex) {

            ex.printStackTrace();
        }
    }
}
