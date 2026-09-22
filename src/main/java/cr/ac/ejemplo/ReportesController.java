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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class ReportesController {

    // REPORTE MAYOR PARTICIPACIÓN

    @FXML
    private TableView<Actividad> tablaMayorParticipacion;

    @FXML
    private TableColumn<Actividad, String> colMayorActividad;

    @FXML
    private TableColumn<Actividad, String> colMayorEvento;

    @FXML
    private TableColumn<Actividad, String> colMayorInscritos;


    // REPORTE ASISTENCIA

    @FXML
    private ComboBox<String> cmbActividadAsistencia;

    @FXML
    private TableView<Actividad> tablaAsistenciaReporte;

    @FXML
    private TableColumn<Actividad, String> colAsistenciaActividad;

    @FXML
    private TableColumn<Actividad, String> colTotalInscritos;

    @FXML
    private TableColumn<Actividad, String> colAsistieron;

    @FXML
    private TableColumn<Actividad, String> colNoAsistieron;

    @FXML
    private TableColumn<Actividad, String> colPorcentaje;


    // REPORTE HISTORIAL

    @FXML
    private ComboBox<String> cmbParticipanteHistorial;

    @FXML
    private TableView<Inscripcion> tablaHistorial;

    @FXML
    private TableColumn<Inscripcion, String> colHistorialEvento;

    @FXML
    private TableColumn<Inscripcion, String> colHistorialActividad;

    @FXML
    private TableColumn<Inscripcion, String> colHistorialFecha;

    @FXML
    private TableColumn<Inscripcion, String> colHistorialEstado;

    @FXML
    private TableColumn<Inscripcion, String> colHistorialAsistencia;


    // GENERAL

    @FXML
    private Label lblEstado;

    // INICIALIZAR

    @FXML
    private void initialize() {

        configurarTablaMayorParticipacion();

        configurarTablaAsistencia();

        configurarTablaHistorial();

        cargarActividades();

        cargarParticipantes();

        lblEstado.setText("");
    }

    // CONFIGURAR TABLA MAYOR PARTICIPACIÓN

    private void configurarTablaMayorParticipacion() {

        colMayorActividad.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        celda.getValue().getNombre()
                )
        );

        colMayorEvento.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        celda.getValue()
                                .getEvento()
                                .getNombre()
                )
        );

        colMayorInscritos.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        String.valueOf(
                                contarInscripcionesActivas(
                                        celda.getValue()
                                )
                        )
                )
        );

        tablaMayorParticipacion.setItems(
                FXCollections.observableArrayList()
        );
    }

    // CONFIGURAR TABLA ASISTENCIA

    private void configurarTablaAsistencia() {

        colAsistenciaActividad.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        celda.getValue().getNombre()
                )
        );

        colTotalInscritos.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        String.valueOf(
                                contarInscripcionesActivas(
                                        celda.getValue()
                                )
                        )
                )
        );

        colAsistieron.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        String.valueOf(
                                contarAsistieron(
                                        celda.getValue()
                                )
                        )
                )
        );

        colNoAsistieron.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        String.valueOf(
                                contarNoAsistieron(
                                        celda.getValue()
                                )
                        )
                )
        );

        colPorcentaje.setCellValueFactory(
                celda -> {

                    Actividad actividad =
                            celda.getValue();

                    int total =
                            contarInscripcionesActivas(
                                    actividad
                            );

                    int asistieron =
                            contarAsistieron(
                                    actividad
                            );

                    double porcentaje = 0;

                    if (total > 0) {

                        porcentaje =
                                (asistieron * 100.0)
                                / total;
                    }

                    return new SimpleStringProperty(
                            String.format(
                                    "%.1f%%",
                                    porcentaje
                            )
                    );
                }
        );

        tablaAsistenciaReporte.setItems(
                FXCollections.observableArrayList()
        );
    }

    // CONFIGURAR TABLA HISTORIAL

    private void configurarTablaHistorial() {

        colHistorialEvento.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        celda.getValue()
                                .getActividad()
                                .getEvento()
                                .getNombre()
                )
        );

        colHistorialActividad.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        celda.getValue()
                                .getActividad()
                                .getNombre()
                )
        );

        colHistorialFecha.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        celda.getValue().getFecha()
                )
        );

        colHistorialEstado.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        celda.getValue().getEstado()
                )
        );

        colHistorialAsistencia.setCellValueFactory(
                celda -> {

                    Asistencia asistencia =
                            buscarAsistencia(
                                    celda.getValue()
                            );

                    if (asistencia == null) {

                        return new SimpleStringProperty(
                                "Pendiente"
                        );
                    }

                    return new SimpleStringProperty(
                            asistencia.getEstado()
                    );
                }
        );

        tablaHistorial.setItems(
                FXCollections.observableArrayList()
        );
    }

    // CARGAR ACTIVIDADES

    private void cargarActividades() {

        cmbActividadAsistencia
                .getItems()
                .clear();

        for (Actividad actividad :
                DatosSistema.getActividades()) {

            cmbActividadAsistencia
                    .getItems()
                    .add(
                            actividad.getIdActividad()
                            + " - "
                            + actividad.getNombre()
                    );
        }
    }

    // CARGAR PARTICIPANTES

    private void cargarParticipantes() {

        cmbParticipanteHistorial
                .getItems()
                .clear();

        for (Participante participante :
                DatosSistema.getParticipantes()) {

            cmbParticipanteHistorial
                    .getItems()
                    .add(
                            participante.getIdentificacion()
                            + " - "
                            + participante.getNombre()
                            + " "
                            + participante.getApellidos()
                    );
        }
    }

    // REPORTE MAYOR PARTICIPACIÓN

    @FXML
    private void reporteMayorParticipacion() {

        ObservableList<Actividad> resultados =
                FXCollections.observableArrayList();

        resultados.addAll(
                DatosSistema.getActividades()
        );

        tablaMayorParticipacion.setItems(
                resultados
        );

        if (resultados.isEmpty()) {

            lblEstado.setText(
                    "No hay actividades registradas."
            );

            return;
        }

        lblEstado.setText(
                "Reporte generado. "
                + resultados.size()
                + " actividades encontradas."
        );
    }

    // REPORTE ASISTENCIA

    @FXML
    private void reporteAsistencia() {

        if (cmbActividadAsistencia.getValue()
                == null) {

            lblEstado.setText(
                    "Debe seleccionar una actividad."
            );

            tablaAsistenciaReporte
                    .getItems()
                    .clear();

            return;
        }


        Actividad actividadSeleccionada =
                buscarActividad(
                        cmbActividadAsistencia
                                .getValue()
                );


        if (actividadSeleccionada == null) {

            lblEstado.setText(
                    "No se encontró la actividad seleccionada."
            );

            return;
        }


        ObservableList<Actividad> resultado =
                FXCollections.observableArrayList();

        resultado.add(
                actividadSeleccionada
        );


        tablaAsistenciaReporte.setItems(
                resultado
        );


        int total =
                contarInscripcionesActivas(
                        actividadSeleccionada
                );

        int asistieron =
                contarAsistieron(
                        actividadSeleccionada
                );

        int noAsistieron =
                contarNoAsistieron(
                        actividadSeleccionada
                );


        lblEstado.setText(
                "Reporte generado: "
                + actividadSeleccionada.getNombre()
                + " | Total: "
                + total
                + " | Asistieron: "
                + asistieron
                + " | No asistieron: "
                + noAsistieron
        );
    }

    // REPORTE HISTORIAL

    @FXML
    private void reporteHistorial() {

        if (cmbParticipanteHistorial.getValue()
                == null) {

            lblEstado.setText(
                    "Debe seleccionar un participante."
            );

            tablaHistorial
                    .getItems()
                    .clear();

            return;
        }


        String seleccionado =
                cmbParticipanteHistorial
                        .getValue();


        String identificacion =
                seleccionado.substring(
                        0,
                        seleccionado.indexOf(" - ")
                );


        ObservableList<Inscripcion> resultados =
                FXCollections.observableArrayList();


        for (Inscripcion inscripcion :
                DatosSistema.getInscripciones()) {

            if (inscripcion
                    .getPersona()
                    .getIdentificacion()
                    .equals(identificacion)) {

                resultados.add(
                        inscripcion
                );
            }
        }


        tablaHistorial.setItems(
                resultados
        );


        if (resultados.isEmpty()) {

            lblEstado.setText(
                    "El participante no tiene inscripciones registradas."
            );

            return;
        }


        lblEstado.setText(
                "Historial generado. "
                + resultados.size()
                + " inscripción(es) encontradas."
        );
    }

    // BUSCAR ACTIVIDAD

    private Actividad buscarActividad(
            String texto) {

        for (Actividad actividad :
                DatosSistema.getActividades()) {

            String actual =
                    actividad.getIdActividad()
                    + " - "
                    + actividad.getNombre();

            if (actual.equals(texto)) {

                return actividad;
            }
        }

        return null;
    }

    // CONTAR INSCRIPCIONES ACTIVAS

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

    // CONTAR ASISTIERON

    private int contarAsistieron(
            Actividad actividad) {

        int cantidad = 0;


        for (Inscripcion inscripcion :
                DatosSistema.getInscripciones()) {

            if (inscripcion.getActividad()
                    == actividad
                    && inscripcion.getEstado()
                            .equals("Activa")) {

                Asistencia asistencia =
                        buscarAsistencia(
                                inscripcion
                        );

                if (asistencia != null
                        && asistencia
                                .getEstado()
                                .equals("Asistió")) {

                    cantidad++;
                }
            }
        }


        return cantidad;
    }

    // CONTAR NO ASISTIERON

    private int contarNoAsistieron(
            Actividad actividad) {

        int cantidad = 0;


        for (Inscripcion inscripcion :
                DatosSistema.getInscripciones()) {

            if (inscripcion.getActividad()
                    == actividad
                    && inscripcion.getEstado()
                            .equals("Activa")) {

                Asistencia asistencia =
                        buscarAsistencia(
                                inscripcion
                        );

                if (asistencia != null
                        && asistencia
                                .getEstado()
                                .equals("No asistió")) {

                    cantidad++;
                }
            }
        }


        return cantidad;
    }


    // BUSCAR ASISTENCIA

    private Asistencia buscarAsistencia(
            Inscripcion inscripcion) {

        for (Asistencia asistencia :
                DatosSistema.getAsistencias()) {

            if (asistencia.getInscripcion()
                    == inscripcion) {

                return asistencia;
            }
        }

        return null;
    }


    @FXML
    private void regresar(
            ActionEvent event) {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            ReportesController.class
                                    .getResource(
                                            "/cr/ac/menu.fxml"
                                    )
                    );


            Scene scene =
                    new Scene(
                            loader.load()
                    );


            Stage stage =
                    (Stage) ((Node) event
                            .getSource())
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
