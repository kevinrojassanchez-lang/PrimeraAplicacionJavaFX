package cr.ac.ejemplo;

import java.io.IOException;

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
import javafx.stage.Stage;

public class ConsultasController {

    // ================= ACTIVIDADES =================

    @FXML
    private ComboBox<String> cmbEventoActividad;

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


    // ================= INSCRIPCIONES =================

    @FXML
    private ComboBox<String> cmbPersonaInscripcion;

    @FXML
    private ComboBox<String> cmbTipoInscripcion;

    @FXML
    private ComboBox<String> cmbActividadInscripcion;

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


    // ================= GENERALES =================

    @FXML
    private Label lblEstado;


    // ================= INICIALIZAR =================

    @FXML
    private void initialize() {

        configurarTablaActividades();

        configurarTablaInscripciones();

        cargarCombos();

        lblEstado.setText("");
    }


    // =========================================================
    // TABLA DE ACTIVIDADES
    // =========================================================

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
                        String.valueOf(
                                celda.getValue()
                                        .getEvento()
                                        .getIdEvento()
                        )
                        + " - "
                        + celda.getValue()
                                .getEvento()
                                .getNombre()
                )
        );

        colActividadDisponibles.setCellValueFactory(
                celda -> {

                    Actividad actividad =
                            celda.getValue();

                    int inscritos =
                            contarInscripcionesActivas(
                                    actividad
                            );

                    int disponibles =
                            actividad.getCapacidadMaxima()
                            - inscritos;

                    return new SimpleStringProperty(
                            String.valueOf(
                                    disponibles
                            )
                    );
                }
        );

        tablaActividades.setItems(
                FXCollections.observableArrayList()
        );
    }


    // =========================================================
    // TABLA DE INSCRIPCIONES
    // =========================================================

    private void configurarTablaInscripciones() {

        colInscripcionId.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        String.valueOf(
                                celda.getValue()
                                        .getIdInscripcion()
                        )
                )
        );

        colInscripcionPersona.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        celda.getValue()
                                .getPersona()
                                .getNombre()
                        + " "
                        + celda.getValue()
                                .getPersona()
                                .getApellidos()
                )
        );

        colInscripcionTipo.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        celda.getValue().getTipo()
                )
        );

        colInscripcionActividad.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        celda.getValue()
                                .getActividad()
                                .getNombre()
                )
        );

        colInscripcionFecha.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        celda.getValue().getFecha()
                )
        );

        colInscripcionEstado.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        celda.getValue().getEstado()
                )
        );

        tablaInscripciones.setItems(
                FXCollections.observableArrayList()
        );
    }


    // =========================================================
    // CARGAR COMBOBOX
    // =========================================================

    private void cargarCombos() {

        // ================= EVENTOS =================

        for (Evento evento :
                DatosSistema.getEventos()) {

            cmbEventoActividad.getItems().add(
                    String.valueOf(
                            evento.getIdEvento()
                    )
                    + " - "
                    + evento.getNombre()
            );
        }


        // ================= TIPOS DE ACTIVIDAD =================

        cmbTipoActividad.getItems().addAll(
                "Conferencia",
                "Taller",
                "Charla",
                "Mesa redonda",
                "Capacitación"
        );


        // ================= DISPONIBILIDAD =================

        cmbDisponibilidad.getItems().addAll(
                "Todas",
                "Con espacios",
                "Sin espacios"
        );


        // ================= PERSONAS =================

        for (Inscripcion inscripcion :
                DatosSistema.getInscripciones()) {

            String persona =
                    inscripcion
                            .getPersona()
                            .getIdentificacion()
                    + " - "
                    + inscripcion
                            .getPersona()
                            .getNombre()
                    + " "
                    + inscripcion
                            .getPersona()
                            .getApellidos();

            if (!cmbPersonaInscripcion
                    .getItems()
                    .contains(persona)) {

                cmbPersonaInscripcion
                        .getItems()
                        .add(persona);
            }
        }


        // ================= TIPOS DE INSCRIPCIÓN =================

        cmbTipoInscripcion.getItems().addAll(
                "Participante",
                "Expositor"
        );


        // ================= ACTIVIDADES =================

        for (Actividad actividad :
                DatosSistema.getActividades()) {

            cmbActividadInscripcion
                    .getItems()
                    .add(
                            String.valueOf(
                                    actividad.getIdActividad()
                            )
                            + " - "
                            + actividad.getNombre()
                    );
        }


        // ================= ESTADOS =================

        cmbEstadoInscripcion.getItems().addAll(
                "Activa",
                "Cancelada",
                "Completada"
        );
    }


    // =========================================================
    // BUSCAR ACTIVIDADES
    // =========================================================

    @FXML
    private void buscarActividades() {

        boolean hayCriterio =
                cmbEventoActividad.getValue() != null
                || dpActividad.getValue() != null
                || cmbTipoActividad.getValue() != null
                || cmbDisponibilidad.getValue() != null;


        if (!hayCriterio) {

            lblEstado.setText(
                    "Seleccione al menos un criterio para buscar actividades."
            );

            tablaActividades.getItems().clear();

            return;
        }


        ObservableList<Actividad> resultados =
                FXCollections.observableArrayList();


        for (Actividad actividad :
                DatosSistema.getActividades()) {

            boolean cumple = true;


            // ================= EVENTO =================

            if (cmbEventoActividad.getValue() != null) {

                String eventoActual =
                        String.valueOf(
                                actividad
                                        .getEvento()
                                        .getIdEvento()
                        )
                        + " - "
                        + actividad
                                .getEvento()
                                .getNombre();

                if (!eventoActual.equals(
                        cmbEventoActividad.getValue()
                )) {

                    cumple = false;
                }
            }


            // ================= FECHA =================

            if (dpActividad.getValue() != null) {

                if (!actividad.getFecha()
                        .equals(
                                dpActividad
                                        .getValue()
                                        .toString()
                        )) {

                    cumple = false;
                }
            }


            // ================= TIPO =================

            if (cmbTipoActividad.getValue() != null) {

                if (!actividad.getTipo()
                        .equals(
                                cmbTipoActividad.getValue()
                        )) {

                    cumple = false;
                }
            }


            // ================= DISPONIBILIDAD =================

            if (cmbDisponibilidad.getValue() != null) {

                int inscritos =
                        contarInscripcionesActivas(
                                actividad
                        );

                int disponibles =
                        actividad.getCapacidadMaxima()
                        - inscritos;


                if (cmbDisponibilidad
                        .getValue()
                        .equals("Con espacios")) {

                    if (disponibles <= 0) {

                        cumple = false;
                    }
                }


                if (cmbDisponibilidad
                        .getValue()
                        .equals("Sin espacios")) {

                    if (disponibles > 0) {

                        cumple = false;
                    }
                }
            }


            if (cumple) {

                resultados.add(
                        actividad
                );
            }
        }


        tablaActividades.setItems(
                resultados
        );


        lblEstado.setText(
                "Actividades encontradas: "
                + resultados.size()
        );
    }


    // =========================================================
    // BUSCAR INSCRIPCIONES
    // =========================================================

    @FXML
    private void buscarInscripciones() {

        boolean hayCriterio =
                cmbPersonaInscripcion.getValue() != null
                || cmbTipoInscripcion.getValue() != null
                || cmbActividadInscripcion.getValue() != null
                || cmbEstadoInscripcion.getValue() != null;


        if (!hayCriterio) {

            lblEstado.setText(
                    "Seleccione al menos un criterio para buscar inscripciones."
            );

            tablaInscripciones.getItems().clear();

            return;
        }


        ObservableList<Inscripcion> resultados =
                FXCollections.observableArrayList();


        for (Inscripcion inscripcion :
                DatosSistema.getInscripciones()) {

            boolean cumple = true;


            // ================= PERSONA =================

            if (cmbPersonaInscripcion.getValue()
                    != null) {

                String personaActual =
                        inscripcion
                                .getPersona()
                                .getIdentificacion()
                        + " - "
                        + inscripcion
                                .getPersona()
                                .getNombre()
                        + " "
                        + inscripcion
                                .getPersona()
                                .getApellidos();

                if (!personaActual.equals(
                        cmbPersonaInscripcion.getValue()
                )) {

                    cumple = false;
                }
            }


            // ================= TIPO =================

            if (cmbTipoInscripcion.getValue()
                    != null) {

                if (!inscripcion
                        .getTipo()
                        .equals(
                                cmbTipoInscripcion
                                        .getValue()
                        )) {

                    cumple = false;
                }
            }


            // ================= ACTIVIDAD =================

            if (cmbActividadInscripcion.getValue()
                    != null) {

                String actividadActual =
                        String.valueOf(
                                inscripcion
                                        .getActividad()
                                        .getIdActividad()
                        )
                        + " - "
                        + inscripcion
                                .getActividad()
                                .getNombre();

                if (!actividadActual.equals(
                        cmbActividadInscripcion
                                .getValue()
                )) {

                    cumple = false;
                }
            }


            // ================= ESTADO =================

            if (cmbEstadoInscripcion.getValue()
                    != null) {

                if (!inscripcion
                        .getEstado()
                        .equals(
                                cmbEstadoInscripcion
                                        .getValue()
                        )) {

                    cumple = false;
                }
            }


            if (cumple) {

                resultados.add(
                        inscripcion
                );
            }
        }


        tablaInscripciones.setItems(
                resultados
        );


        lblEstado.setText(
                "Inscripciones encontradas: "
                + resultados.size()
        );
    }


    // =========================================================
    // CONTAR INSCRIPCIONES ACTIVAS
    // =========================================================

    private int contarInscripcionesActivas(
            Actividad actividad) {

        int cantidad = 0;


        for (Inscripcion inscripcion :
                DatosSistema.getInscripciones()) {

            if (inscripcion.getActividad()
                    == actividad
                    && inscripcion.getEstado()
                            .equals("Activa")) {

                cantidad++;
            }
        }


        return cantidad;
    }


    // =========================================================
    // REGRESAR
    // =========================================================

    @FXML
    private void regresar(ActionEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    ConsultasController.class.getResource(
                            "/cr/ac/menu.fxml"
                    )
            );

            Scene scene = new Scene(
                    loader.load()
            );

            Stage stage =
                    (Stage) ((Node) event.getSource())
                            .getScene()
                            .getWindow();

            stage.setTitle(
                    "Menú principal"
            );

            stage.setScene(
                    scene
            );

            stage.show();

        } catch (IOException ex) {

            ex.printStackTrace();
        }
    }
}
