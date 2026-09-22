package cr.ac.ejemplo;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ActividadesController {

    @FXML
    private TextField txtFiltro;

    @FXML
    private Label lblEstado;

    @FXML
    private TableView<Actividad> tablaActividades;

    @FXML
    private TableColumn<Actividad, String> colId;

    @FXML
    private TableColumn<Actividad, String> colNombre;

    @FXML
    private TableColumn<Actividad, String> colTipo;

    @FXML
    private TableColumn<Actividad, String> colFecha;

    @FXML
    private TableColumn<Actividad, String> colHorario;

    @FXML
    private TableColumn<Actividad, String> colCapacidad;

    @FXML
    private TableColumn<Actividad, String> colEstado;

    private Evento eventoSeleccionado;

    @FXML
    private void initialize() {

        colId.setCellValueFactory(
                celda -> new javafx.beans.property.SimpleStringProperty(
                        String.valueOf(
                                celda.getValue().getIdActividad()
                        )
                )
        );

        colNombre.setCellValueFactory(
                celda -> new javafx.beans.property.SimpleStringProperty(
                        celda.getValue().getNombre()
                )
        );

        colTipo.setCellValueFactory(
                celda -> new javafx.beans.property.SimpleStringProperty(
                        celda.getValue().getTipo()
                )
        );

        colFecha.setCellValueFactory(
                celda -> new javafx.beans.property.SimpleStringProperty(
                        celda.getValue().getFecha()
                )
        );

        colHorario.setCellValueFactory(
                celda -> new javafx.beans.property.SimpleStringProperty(
                        celda.getValue().getHoraInicio()
                        + " - "
                        + celda.getValue().getHoraFin()
                )
        );

        colCapacidad.setCellValueFactory(
                celda -> new javafx.beans.property.SimpleStringProperty(
                        String.valueOf(
                                celda.getValue().getCapacidadMaxima()
                        )
                )
        );

        colEstado.setCellValueFactory(
                celda -> new javafx.beans.property.SimpleStringProperty(
                        celda.getValue().getEstado()
                )
        );

        tablaActividades.setItems(
                DatosSistema.getActividades()
        );
    }

    public void establecerEvento(Evento evento) {

        this.eventoSeleccionado = evento;

        if (evento == null) {

            lblEstado.setText(
                    "No hay un evento seleccionado."
            );

            tablaActividades.getItems().clear();

            return;
        }

        tablaActividades.setItems(
                DatosSistema.getActividades()
                        .filtered(
                                actividad ->
                                        actividad.getEvento() == evento
                        )
        );

        lblEstado.setText(
                "Actividades del evento: "
                + evento.getNombre()
        );
    }

    public void agregarActividad(Actividad actividad) {

        if (actividad != null) {

            DatosSistema.getActividades().add(
                    actividad
            );

            tablaActividades.refresh();
        }
    }

    @FXML
    private void agregar() {

        if (eventoSeleccionado == null) {

            lblEstado.setText(
                    "No hay un evento seleccionado."
            );

            return;
        }

        try {

            FXMLLoader loader = new FXMLLoader(
                    ActividadesController.class.getResource(
                            "/cr/ac/actividadFormulario.fxml"
                    )
            );

            Scene scene = new Scene(loader.load());

            ActividadFormularioController controller =
                    loader.getController();

            controller.establecerActividadesController(
                    this
            );

            controller.establecerEvento(
                    eventoSeleccionado
            );

            Stage stage = new Stage();

            stage.setTitle(
                    "Nueva actividad"
            );

            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {

            ex.printStackTrace();
        }
    }

    @FXML
    private void editar() {

        Actividad actividad =
                tablaActividades
                        .getSelectionModel()
                        .getSelectedItem();

        if (actividad == null) {

            lblEstado.setText(
                    "Seleccione una actividad de la tabla para editar."
            );

            return;
        }

        lblEstado.setText(
                "Actividad seleccionada para editar."
        );
    }

    @FXML
    private void abrirActividad() {

        Actividad actividad =
                tablaActividades
                        .getSelectionModel()
                        .getSelectedItem();

        if (actividad == null) {

            lblEstado.setText(
                    "Seleccione una actividad de la tabla para abrir."
            );

            return;
        }

        if (eventoSeleccionado == null) {

            lblEstado.setText(
                    "No hay un evento seleccionado."
            );

            return;
        }

        try {

            FXMLLoader loader = new FXMLLoader(
                    ActividadesController.class.getResource(
                            "/cr/ac/actividadDetalle.fxml"
                    )
            );

            Scene scene = new Scene(loader.load());

            ActividadDetalleController controller =
                    loader.getController();

            controller.establecerDatos(
                    eventoSeleccionado,
                    actividad
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

    @FXML
    private void regresar() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    ActividadesController.class.getResource(
                            "/cr/ac/eventoDetalle.fxml"
                    )
            );

            Scene scene = new Scene(loader.load());

            EventoDetalleController controller =
                    loader.getController();

            controller.establecerEvento(
                    eventoSeleccionado
            );

            Stage stage = (Stage) txtFiltro
                    .getScene()
                    .getWindow();

            stage.setTitle(
                    "Detalle del evento"
            );

            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {

            ex.printStackTrace();
        }
    }
}
