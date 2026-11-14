
package cr.ac.ejemplo.controller;

import cr.ac.ejemplo.DAOFactory;
import cr.ac.ejemplo.VentanaUtil;
import cr.ac.ejemplo.service.ParticipanteService;
import cr.ac.ejemplo.model.Participante;
import cr.ac.ejemplo.model.Actividad;
import cr.ac.ejemplo.model.Evento;

import java.io.IOException;
import java.sql.SQLException;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
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

    private final ParticipanteService participanteService =
            DAOFactory.crearParticipanteService();

    @FXML
    private void initialize() {

        colIdentificacion.setCellValueFactory(celda ->
                new SimpleStringProperty(
                        celda.getValue().getIdentificacion()
                )
        );

        colNombre.setCellValueFactory(celda ->
                new SimpleStringProperty(
                        celda.getValue().getNombre()
                )
        );

        colApellidos.setCellValueFactory(celda ->
                new SimpleStringProperty(
                        celda.getValue().getApellidos()
                )
        );

        colCorreo.setCellValueFactory(celda ->
                new SimpleStringProperty(
                        celda.getValue().getCorreo()
                )
        );

        colTelefono.setCellValueFactory(celda ->
                new SimpleStringProperty(
                        celda.getValue().getTelefono()
                )
        );

        colFecha.setCellValueFactory(celda ->
                new SimpleStringProperty(
                        celda.getValue().getFechaRegistro()
                )
        );

        colEstado.setCellValueFactory(celda ->
                new SimpleStringProperty(
                        celda.getValue().getEstado().toString()
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

        cargarParticipantes();
    }

    private void cargarParticipantes() {

        if (actividadSeleccionada == null) {

            lblEstado.setText(
                    "No hay una actividad seleccionada."
            );

            tablaParticipantes.getItems().clear();
            return;
        }

        try {

            tablaParticipantes.setItems(
                    FXCollections.observableArrayList(
                            participanteService.listarPorActividad(
                                    actividadSeleccionada.getIdActividad()
                            )
                    )
            );

            lblEstado.setText(
                    "Participantes inscritos en la actividad: "
                    + actividadSeleccionada.getNombre()
                    + " | Total: "
                    + tablaParticipantes.getItems().size()
            );

        } catch (SQLException e) {

            lblEstado.setText(
                    "Error al cargar participantes: " + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    @FXML
    private void editar() {

        Participante participante =
                tablaParticipantes.getSelectionModel()
                        .getSelectedItem();

        if (participante == null) {

            lblEstado.setText(
                    "Seleccione un participante de la tabla para editar."
            );

            return;
        }

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/cr/ac/participanteFormulario.fxml"
                    )
            );

            Scene scene = new Scene(loader.load());

            ParticipanteFormularioController controller =
                    loader.getController();

            controller.establecerParticipante(participante);

            Stage stage = new Stage();

            stage.setTitle("Editar participante");
            stage.setScene(scene);

            VentanaUtil.configurar(stage);

            stage.showAndWait();

            cargarParticipantes();

        } catch (IOException e) {

            lblEstado.setText(
                    "Error al abrir el formulario: " + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    @FXML
    private void regresar() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
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

            Stage stage = (Stage) txtFiltro
                    .getScene()
                    .getWindow();

            stage.setTitle("Detalle de la actividad");
            stage.setScene(scene);

            VentanaUtil.configurar(stage);

            stage.show();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
