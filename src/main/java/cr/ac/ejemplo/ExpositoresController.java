package cr.ac.ejemplo;

import java.io.IOException;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ExpositoresController {

    @FXML
    private TextField txtFiltro;

    @FXML
    private Label lblEstado;

    @FXML
    private TableView<Expositor> tablaExpositores;

    @FXML
    private TableColumn<Expositor, String> colIdentificacion;

    @FXML
    private TableColumn<Expositor, String> colNombre;

    @FXML
    private TableColumn<Expositor, String> colApellidos;

    @FXML
    private TableColumn<Expositor, String> colCorreo;

    @FXML
    private TableColumn<Expositor, String> colTelefono;

    @FXML
    private TableColumn<Expositor, String> colEspecialidad;

    @FXML
    private TableColumn<Expositor, String> colEstado;

    private Evento eventoSeleccionado;

    private Actividad actividadSeleccionada;

    @FXML
    private void initialize() {

        colIdentificacion.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        celda.getValue().getIdentificacion()
                )
        );

        colNombre.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        celda.getValue().getNombre()
                )
        );

        colApellidos.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        celda.getValue().getApellidos()
                )
        );

        colCorreo.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        celda.getValue().getCorreo()
                )
        );

        colTelefono.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        celda.getValue().getTelefono()
                )
        );

        colEspecialidad.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        celda.getValue().getEspecialidad()
                )
        );

        colEstado.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        celda.getValue().getEstado()
                )
        );

        tablaExpositores.setItems(
                FXCollections.observableArrayList()
        );
    }

    public void establecerActividad(
            Evento evento,
            Actividad actividad) {

        this.eventoSeleccionado = evento;
        this.actividadSeleccionada = actividad;

        if (actividad == null) {

            lblEstado.setText(
                    "No hay una actividad seleccionada."
            );

            tablaExpositores.getItems().clear();

            return;
        }

        ObservableList<Expositor> expositores =
                FXCollections.observableArrayList();

        for (Inscripcion inscripcion :
                DatosSistema.getInscripciones()) {

            if (inscripcion.getActividad() == actividad
                    && inscripcion.getTipo().equals("Expositor")
                    && inscripcion.getEstado().equals("Activa")) {

                if (inscripcion.getPersona()
                        instanceof Expositor) {

                    Expositor expositor =
                            (Expositor) inscripcion.getPersona();

                    expositores.add(expositor);
                }
            }
        }

        tablaExpositores.setItems(expositores);

        lblEstado.setText(
                "Expositores inscritos en la actividad: "
                + actividad.getNombre()
                + " | Total: "
                + expositores.size()
        );
    }

    @FXML
    private void editar() {

        Expositor expositor =
                tablaExpositores
                        .getSelectionModel()
                        .getSelectedItem();

        if (expositor == null) {

            lblEstado.setText(
                    "Seleccione un expositor de la tabla para editar."
            );

            return;
        }

        lblEstado.setText(
                "Expositor seleccionado para editar: "
                + expositor.getNombre()
                + " "
                + expositor.getApellidos()
        );
    }

    @FXML
    private void regresar() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    ExpositoresController.class.getResource(
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

            Stage stage = (Stage) txtFiltro
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
