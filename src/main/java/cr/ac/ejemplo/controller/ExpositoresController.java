
package cr.ac.ejemplo.controller;

import cr.ac.ejemplo.DAOFactory;
import cr.ac.ejemplo.VentanaUtil;
import cr.ac.ejemplo.service.ExpositorService;
import cr.ac.ejemplo.model.Expositor;
import cr.ac.ejemplo.model.Actividad;
import cr.ac.ejemplo.model.Evento;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

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

    private final ExpositorService expositorService =
            DAOFactory.crearExpositorService();

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
                        celda.getValue().getEstado().toString()
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

        cargarExpositores();
    }

    private void cargarExpositores() {

        try {

            ArrayList<Expositor> expositores =
                    expositorService.listarPorActividad(
                            actividadSeleccionada.getIdActividad()
                    );

            tablaExpositores.setItems(
                    FXCollections.observableArrayList(expositores)
            );

            lblEstado.setText(
                    "Expositores inscritos en la actividad: "
                    + actividadSeleccionada.getNombre()
                    + " | Total: "
                    + expositores.size()
            );

        } catch (SQLException e) {

            lblEstado.setText(
                    "Error al cargar los expositores: "
                    + e.getMessage()
            );

            e.printStackTrace();
        }
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

            VentanaUtil.configurar(stage);

            stage.show();

        } catch (IOException ex) {

            ex.printStackTrace();
        }
    }
}
