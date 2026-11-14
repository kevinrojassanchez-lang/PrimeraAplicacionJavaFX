
package cr.ac.ejemplo.controller;

import cr.ac.ejemplo.DAOFactory;
import cr.ac.ejemplo.SesionUsuario;
import cr.ac.ejemplo.VentanaUtil;
import cr.ac.ejemplo.service.ReporteService;

import cr.ac.ejemplo.model.Actividad;
import cr.ac.ejemplo.model.Asistencia;
import cr.ac.ejemplo.model.Inscripcion;

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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ReportesController {

    private final ReporteService reporteService =
            DAOFactory.crearReporteService();

    private ArrayList<Actividad> actividades = new ArrayList<>();
    private ArrayList<Inscripcion> inscripciones = new ArrayList<>();
    private ArrayList<Asistencia> asistencias = new ArrayList<>();

    @FXML
    private TableView<Actividad> tablaMayorParticipacion;

    @FXML
    private TableColumn<Actividad, String> colMayorActividad;

    @FXML
    private TableColumn<Actividad, String> colMayorEvento;

    @FXML
    private TableColumn<Actividad, String> colMayorInscritos;

    @FXML
    private TextField txtIdActividadAsistencia;

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

    @FXML
    private TextField txtCedulaHistorial;

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

    @FXML
    private Label lblEstado;

    @FXML
    private void initialize() {

        if (!SesionUsuario.esAdministrador()) {
            lblEstado.setText(
                    "No tiene permisos para acceder a los reportes."
            );
            return;
        }

        configurarTablaMayorParticipacion();
        configurarTablaAsistencia();
        configurarTablaHistorial();

        try {

            actividades = reporteService.listarActividades();
            inscripciones = reporteService.listarInscripciones();

            asistencias =
                    reporteService.listarAsistencias(actividades);

            lblEstado.setText("");

        } catch (SQLException ex) {

            lblEstado.setText(
                    "Error al cargar los datos de los reportes."
            );

            ex.printStackTrace();
        }
    }

    private void configurarTablaMayorParticipacion() {

        colMayorActividad.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        celda.getValue().getNombre()
                )
        );

        colMayorEvento.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        celda.getValue().getEvento().getNombre()
                )
        );

        colMayorInscritos.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        String.valueOf(
                                reporteService.contarInscripcionesActivas(
                                        celda.getValue(),
                                        inscripciones
                                )
                        )
                )
        );

        tablaMayorParticipacion.setItems(
                FXCollections.observableArrayList()
        );
    }

    private void configurarTablaAsistencia() {

        colAsistenciaActividad.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        celda.getValue().getNombre()
                )
        );

        colTotalInscritos.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        String.valueOf(
                                reporteService.contarInscripcionesActivas(
                                        celda.getValue(),
                                        inscripciones
                                )
                        )
                )
        );

        colAsistieron.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        String.valueOf(
                                reporteService.contarAsistieron(
                                        celda.getValue(),
                                        inscripciones,
                                        asistencias
                                )
                        )
                )
        );

        colNoAsistieron.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        String.valueOf(
                                reporteService.contarNoAsistieron(
                                        celda.getValue(),
                                        inscripciones,
                                        asistencias
                                )
                        )
                )
        );

        colPorcentaje.setCellValueFactory(
                celda -> {

                    Actividad actividad = celda.getValue();

                    int total =
                            reporteService.contarInscripcionesActivas(
                                    actividad,
                                    inscripciones
                            );

                    int asistieron =
                            reporteService.contarAsistieron(
                                    actividad,
                                    inscripciones,
                                    asistencias
                            );

                    double porcentaje = 0;

                    if (total > 0) {
                        porcentaje = (asistieron * 100.0) / total;
                    }

                    return new SimpleStringProperty(
                            String.format("%.1f%%", porcentaje)
                    );
                }
        );

        tablaAsistenciaReporte.setItems(
                FXCollections.observableArrayList()
        );
    }

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
                        celda.getValue().getEstado().toString()
                )
        );

        colHistorialAsistencia.setCellValueFactory(
                celda -> {

                    Asistencia asistencia =
                            reporteService.buscarAsistencia(
                                    celda.getValue(),
                                    asistencias
                            );

                    if (asistencia == null) {
                        return new SimpleStringProperty("Pendiente");
                    }

                    return new SimpleStringProperty(
                            asistencia.getEstado().toString()
                    );
                }
        );

        tablaHistorial.setItems(
                FXCollections.observableArrayList()
        );
    }

    @FXML
    private void reporteMayorParticipacion() {

        if (!SesionUsuario.esAdministrador()) {
            lblEstado.setText(
                    "No tiene permisos para generar reportes."
            );
            return;
        }

        ObservableList<Actividad> resultados =
                FXCollections.observableArrayList();

        resultados.addAll(
                reporteService.listarActividadesMayorParticipacion(
                        actividades,
                        inscripciones
                )
        );

        tablaMayorParticipacion.setItems(resultados);

        if (resultados.isEmpty()) {
            lblEstado.setText("No hay actividades registradas.");
            return;
        }

        lblEstado.setText(
                "Reporte generado. " + resultados.size()
                + " actividades encontradas."
        );
    }

    @FXML
    private void reporteAsistencia() {

        if (!SesionUsuario.esAdministrador()) {
            lblEstado.setText(
                    "No tiene permisos para generar reportes."
            );
            return;
        }

        String textoId =
                txtIdActividadAsistencia.getText().trim();

        if (textoId.isEmpty()) {

            lblEstado.setText(
                    "Debe ingresar el ID de la actividad."
            );

            tablaAsistenciaReporte.getItems().clear();
            return;
        }

        int idActividad;

        try {

            idActividad = Integer.parseInt(textoId);

            if (idActividad <= 0) {

                lblEstado.setText(
                        "El ID de actividad debe ser mayor que cero."
                );

                tablaAsistenciaReporte.getItems().clear();
                return;
            }

        } catch (NumberFormatException ex) {

            lblEstado.setText(
                    "El ID de actividad debe contener solamente números."
            );

            tablaAsistenciaReporte.getItems().clear();
            return;
        }

        Actividad actividadSeleccionada =
                buscarActividad(idActividad);

        if (actividadSeleccionada == null) {

            lblEstado.setText(
                    "No existe una actividad con ese ID."
            );

            tablaAsistenciaReporte.getItems().clear();
            return;
        }

        ObservableList<Actividad> resultado =
                FXCollections.observableArrayList();

        resultado.add(actividadSeleccionada);

        tablaAsistenciaReporte.setItems(resultado);

        int total =
                reporteService.contarInscripcionesActivas(
                        actividadSeleccionada,
                        inscripciones
                );

        int asistieron =
                reporteService.contarAsistieron(
                        actividadSeleccionada,
                        inscripciones,
                        asistencias
                );

        int noAsistieron =
                reporteService.contarNoAsistieron(
                        actividadSeleccionada,
                        inscripciones,
                        asistencias
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

    @FXML
    private void reporteHistorial() {

        if (!SesionUsuario.esAdministrador()) {
            lblEstado.setText(
                    "No tiene permisos para generar reportes."
            );
            return;
        }

        String identificacion =
                txtCedulaHistorial.getText().trim();

        if (identificacion.isEmpty()) {

            lblEstado.setText(
                    "Debe ingresar la cédula del participante."
            );

            tablaHistorial.getItems().clear();
            return;
        }

        ObservableList<Inscripcion> resultados =
                FXCollections.observableArrayList();

        for (Inscripcion inscripcion : inscripciones) {

            if (inscripcion.getPersona() != null
                    && inscripcion.getPersona().getIdentificacion() != null
                    && inscripcion.getPersona()
                            .getIdentificacion()
                            .equals(identificacion)) {

                resultados.add(inscripcion);
            }
        }

        tablaHistorial.setItems(resultados);

        if (resultados.isEmpty()) {

            lblEstado.setText(
                    "No se encontraron inscripciones para la cédula ingresada."
            );

            return;
        }

        lblEstado.setText(
                "Historial generado. "
                + resultados.size()
                + " inscripción(es) encontradas."
        );
    }

    private Actividad buscarActividad(int idActividad) {

        for (Actividad actividad : actividades) {

            if (actividad.getIdActividad() == idActividad) {
                return actividad;
            }
        }

        return null;
    }

    @FXML
    private void regresar(ActionEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    ReportesController.class.getResource(
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
