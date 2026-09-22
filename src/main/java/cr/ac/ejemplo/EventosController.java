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

public class EventosController {

    @FXML
    private TextField txtFiltro;

    @FXML
    private Label lblEstado;

    @FXML
    private TableView<Evento> tablaEventos;

    @FXML
    private TableColumn<Evento, String> colCodigo;

    @FXML
    private TableColumn<Evento, String> colNombre;

    @FXML
    private TableColumn<Evento, String> colFechaInicio;

    @FXML
    private TableColumn<Evento, String> colFechaFinal;

    @FXML
    private TableColumn<Evento, String> colLugar;

    @FXML
    private TableColumn<Evento, String> colEstado;

    @FXML
    private void initialize() {

        colCodigo.setCellValueFactory(
                celda -> new javafx.beans.property.SimpleStringProperty(
                        String.valueOf(
                                celda.getValue().getIdEvento()
                        )
                )
        );

        colNombre.setCellValueFactory(
                celda -> new javafx.beans.property.SimpleStringProperty(
                        celda.getValue().getNombre()
                )
        );

        colFechaInicio.setCellValueFactory(
                celda -> new javafx.beans.property.SimpleStringProperty(
                        celda.getValue().getFechaInicio()
                )
        );

        colFechaFinal.setCellValueFactory(
                celda -> new javafx.beans.property.SimpleStringProperty(
                        celda.getValue().getFechaFin()
                )
        );

        colLugar.setCellValueFactory(
                celda -> new javafx.beans.property.SimpleStringProperty(
                        celda.getValue().getLugar()
                )
        );

        colEstado.setCellValueFactory(
                celda -> new javafx.beans.property.SimpleStringProperty(
                        celda.getValue().getEstado()
                )
        );

        tablaEventos.setItems(
                DatosSistema.getEventos()
        );
    }

    public void agregarEvento(Evento evento) {

        if (evento != null) {

            DatosSistema.getEventos().add(
                    evento
            );

            tablaEventos.refresh();
        }
    }

    @FXML
    private void agregar() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    EventosController.class.getResource(
                            "/cr/ac/eventoFormulario.fxml"
                    )
            );

            Scene scene = new Scene(
                    loader.load()
            );

            EventoFormularioController controller =
                    loader.getController();

            controller.establecerEventosController(
                    this
            );

            Stage stage = new Stage();

            stage.setTitle(
                    "Nuevo evento"
            );

            stage.setScene(
                    scene
            );

            stage.show();

        } catch (IOException ex) {

            ex.printStackTrace();
        }
    }

    @FXML
    private void editar() {

        if (tablaEventos
                .getSelectionModel()
                .getSelectedItem() == null) {

            lblEstado.setText(
                    "Seleccione un evento de la tabla para editar."
            );

            return;
        }

        lblEstado.setText(
                "Evento seleccionado para editar."
        );
    }

    @FXML
    private void abrirEvento() {

        Evento evento =
                tablaEventos
                        .getSelectionModel()
                        .getSelectedItem();

        if (evento == null) {

            lblEstado.setText(
                    "Seleccione un evento de la tabla para abrir."
            );

            return;
        }

        try {

            FXMLLoader loader = new FXMLLoader(
                    EventosController.class.getResource(
                            "/cr/ac/eventoDetalle.fxml"
                    )
            );

            Scene scene = new Scene(
                    loader.load()
            );

            EventoDetalleController controller =
                    loader.getController();

            controller.establecerEvento(
                    evento
            );

            Stage stage = (Stage) tablaEventos
                    .getScene()
                    .getWindow();

            stage.setTitle(
                    "Detalle del evento"
            );

            stage.setScene(
                    scene
            );

            stage.show();

        } catch (IOException ex) {

            ex.printStackTrace();
        }
    }

    @FXML
    private void regresar() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    EventosController.class.getResource(
                            "/cr/ac/menu.fxml"
                    )
            );

            Scene scene = new Scene(
                    loader.load()
            );

            Stage stage = (Stage) txtFiltro
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
