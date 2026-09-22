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

public class ParticipantesController {

    @FXML
    private TextField txtFiltro;

    @FXML
    private Label lblEstado;

    @FXML
    private TableView<Participante> tablaParticipantes;

    @FXML
    private TableColumn<Participante, String> colIdentificacion;

    @FXML
    private TableColumn<Participante, String> colNombre;

    @FXML
    private TableColumn<Participante, String> colApellidos;

    @FXML
    private TableColumn<Participante, String> colCorreo;

    @FXML
    private TableColumn<Participante, String> colTelefono;

    @FXML
    private TableColumn<Participante, String> colFecha;

    @FXML
    private TableColumn<Participante, String> colEstado;

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

        colFecha.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        celda.getValue().getFechaRegistro()
                )
        );

        colEstado.setCellValueFactory(
                celda -> new SimpleStringProperty(
                        celda.getValue().getEstado()
                )
        );

        tablaParticipantes.setItems(
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

            tablaParticipantes.getItems().clear();

            return;
        }

        ObservableList<Participante> participantes =
                FXCollections.observableArrayList();

        for (Inscripcion inscripcion :
                DatosSistema.getInscripciones()) {

            if (inscripcion.getActividad() == actividad
                    && inscripcion.getTipo().equals("Participante")
                    && inscripcion.getEstado().equals("Activa")) {

                if (inscripcion.getPersona()
                        instanceof Participante) {

                    Participante participante =
                            (Participante) inscripcion.getPersona();

                    participantes.add(participante);
                }
            }
        }

        tablaParticipantes.setItems(participantes);

        lblEstado.setText(
                "Participantes inscritos en la actividad: "
                + actividad.getNombre()
                + " | Total: "
                + participantes.size()
        );
    }

    @FXML
    private void editar() {

        Participante participante =
                tablaParticipantes
                        .getSelectionModel()
                        .getSelectedItem();

        if (participante == null) {

            lblEstado.setText(
                    "Seleccione un participante de la tabla para editar."
            );

            return;
        }

        lblEstado.setText(
                "Participante seleccionado para editar: "
                + participante.getNombre()
                + " "
                + participante.getApellidos()
        );
    }

    @FXML
    private void regresar() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    ParticipantesController.class.getResource(
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
