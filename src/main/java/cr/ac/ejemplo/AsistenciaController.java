package cr.ac.ejemplo;

import java.io.IOException;

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

    private Evento eventoSeleccionado;

    private Actividad actividadSeleccionada;

    @FXML
    private void initialize() {

        colIdentificacion.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        celda.getValue()
                                .getPersona()
                                .getIdentificacion()
                )
        );

        colPersona.setCellValueFactory(
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

        colTipo.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        celda.getValue().getTipo()
                )
        );

        colAsistencia.setCellFactory(
                columna -> new TableCell<Inscripcion, String>() {

                    private final ComboBox<String> combo =
                            new ComboBox<>();

                    {
                        combo.getItems().addAll(
                                "Pendiente",
                                "Asistió",
                                "No asistió"
                        );

                        combo.setMaxWidth(
                                Double.MAX_VALUE
                        );

                        combo.valueProperty()
                                .addListener(
                                        (observable,
                                         valorAnterior,
                                         nuevoValor) -> {

                                            if (nuevoValor != null
                                                    && getTableRow()
                                                            .getItem() != null) {

                                                Inscripcion inscripcion =
                                                        getTableRow()
                                                                .getItem();

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

                        super.updateItem(
                                estado,
                                vacio
                        );

                        if (vacio
                                || getTableRow()
                                        .getItem() == null) {

                            setGraphic(null);

                        } else {

                            Inscripcion inscripcion =
                                    getTableRow()
                                            .getItem();

                            Asistencia asistencia =
                                    buscarAsistencia(
                                            inscripcion
                                    );

                            if (asistencia != null) {

                                combo.setValue(
                                        asistencia.getEstado()
                                );

                            } else {

                                combo.setValue(
                                        "Pendiente"
                                );
                            }

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

    public void establecerActividad(
            Evento evento,
            Actividad actividad) {

        this.eventoSeleccionado = evento;
        this.actividadSeleccionada = actividad;

        if (actividad == null) {

            lblActividad.setText(
                    "Actividad seleccionada"
            );

            tablaAsistencia.getItems().clear();

            actualizarResumen();

            return;
        }

        lblActividad.setText(
                "Actividad: "
                + actividad.getNombre()
        );

        ObservableList<Inscripcion> inscripciones =
                FXCollections.observableArrayList();

        for (Inscripcion inscripcion :
                DatosSistema.getInscripciones()) {

            if (inscripcion.getActividad() == actividad
                    && inscripcion.getEstado()
                            .equals("Activa")) {

                inscripciones.add(
                        inscripcion
                );
            }
        }

        tablaAsistencia.setItems(
                inscripciones
        );

        actualizarResumen();

        lblEstado.setText(
                "Personas inscritas en la actividad: "
                + inscripciones.size()
        );
    }

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

    private void actualizarAsistencia(
            Inscripcion inscripcion,
            String estado) {

        Asistencia asistencia =
                buscarAsistencia(inscripcion);

        if (asistencia == null) {

            int idAsistencia =
                    DatosSistema
                            .getAsistencias()
                            .size() + 1;

            asistencia =
                    new Asistencia(
                            idAsistencia,
                            inscripcion,
                            estado
                    );

            DatosSistema
                    .getAsistencias()
                    .add(asistencia);

        } else {

            asistencia.setEstado(
                    estado
            );
        }

        actualizarResumen();

        lblEstado.setText(
                "Asistencia actualizada."
        );
    }

    private void actualizarResumen() {

        if (tablaAsistencia == null) {
            return;
        }

        int total =
                tablaAsistencia
                        .getItems()
                        .size();

        int asistieron = 0;

        int noAsistieron = 0;

        for (Inscripcion inscripcion :
                tablaAsistencia.getItems()) {

            Asistencia asistencia =
                    buscarAsistencia(
                            inscripcion
                    );

            if (asistencia != null) {

                if (asistencia.getEstado()
                        .equals("Asistió")) {

                    asistieron++;

                } else if (
                        asistencia.getEstado()
                                .equals("No asistió")) {

                    noAsistieron++;
                }
            }
        }

        double porcentaje = 0;

        if (total > 0) {

            porcentaje =
                    (asistieron * 100.0)
                    / total;
        }

        lblTotal.setText(
                String.valueOf(total)
        );

        lblAsistieron.setText(
                String.valueOf(asistieron)
        );

        lblNoAsistieron.setText(
                String.valueOf(noAsistieron)
        );

        lblPorcentaje.setText(
                String.format(
                        "%.1f%%",
                        porcentaje
                )
        );
    }

    @FXML
    private void guardarAsistencia() {

        actualizarResumen();

        lblEstado.setText(
                "Asistencia guardada correctamente."
        );
    }

    @FXML
    private void consultar() {

        actualizarResumen();

        lblEstado.setText(
                "Consulta de asistencia realizada correctamente."
        );
    }

    @FXML
    private void regresar() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    AsistenciaController.class.getResource(
                            "/cr/ac/actividadDetalle.fxml"
                    )
            );

            Scene scene = new Scene(
                    loader.load()
            );

            ActividadDetalleController controller =
                    loader.getController();

            controller.establecerDatos(
                    eventoSeleccionado,
                    actividadSeleccionada
            );

            Stage stage =
                    (Stage) tablaAsistencia
                            .getScene()
                            .getWindow();

            stage.setTitle(
                    "Detalle de la actividad"
            );

            stage.setScene(scene);

            stage.show();

        } catch (IOException ex) {

            ex.printStackTrace();
        }
    }
}
