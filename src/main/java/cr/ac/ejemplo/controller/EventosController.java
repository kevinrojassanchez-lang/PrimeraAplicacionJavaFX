
package cr.ac.ejemplo.controller;

import cr.ac.ejemplo.DAOFactory;
import cr.ac.ejemplo.SesionUsuario;
import cr.ac.ejemplo.VentanaUtil;
import cr.ac.ejemplo.model.Evento;
import cr.ac.ejemplo.service.EventoService;

import java.io.IOException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    private Button btnAgregar;

    @FXML
    private Button btnEditar;

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

    private final EventoService eventoService =
            DAOFactory.crearEventoService();

    private final ObservableList<Evento> listaEventos =
            FXCollections.observableArrayList();

    @FXML
    private void initialize() {

        boolean administrador = SesionUsuario.esAdministrador();

        if (btnAgregar != null) {
            btnAgregar.setDisable(!administrador);
        }

        if (btnEditar != null) {
            btnEditar.setDisable(!administrador);
        }

        colCodigo.setCellValueFactory(
                celda -> new javafx.beans.property.SimpleStringProperty(
                        String.valueOf(celda.getValue().getIdEvento())
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
                        celda.getValue().getEstado().toString()
                )
        );

        tablaEventos.setItems(listaEventos);

        cargarEventos();
    }

    private void cargarEventos() {

        listaEventos.clear();

        ArrayList<Evento> eventos = eventoService.listarEventos();

        listaEventos.addAll(eventos);
    }

    public boolean agregarEvento(Evento evento) {

        if (!SesionUsuario.esAdministrador()) {
            lblEstado.setText(
                    "No tiene permisos para registrar eventos."
            );
            return false;
        }

        if (evento == null) {
            return false;
        }

        try {

            boolean guardado = eventoService.registrarEvento(evento);

            if (guardado) {
                cargarEventos();
                lblEstado.setText(
                        "Evento registrado correctamente."
                );
                return true;
            }

            lblEstado.setText(
                    "No se pudo registrar el evento."
            );

            return false;

        } catch (IllegalArgumentException ex) {

            lblEstado.setText(ex.getMessage());
            return false;
        }
    }

    @FXML
    private void agregar() {

        if (!SesionUsuario.esAdministrador()) {
            lblEstado.setText(
                    "No tiene permisos para registrar eventos."
            );
            return;
        }

        try {

            FXMLLoader loader = new FXMLLoader(
                    EventosController.class.getResource(
                            "/cr/ac/eventoFormulario.fxml"
                    )
            );

            Scene scene = new Scene(loader.load());

            EventoFormularioController controller =
                    loader.getController();

            controller.establecerEventosController(this);

            Stage stage = new Stage();

            stage.setTitle("Nuevo evento");
            stage.setScene(scene);

            VentanaUtil.configurar(stage);

            stage.show();

        } catch (IOException ex) {

            ex.printStackTrace();
        }
    }

    public boolean actualizarEvento(Evento evento) {

        if (!SesionUsuario.esAdministrador()) {
            lblEstado.setText(
                    "No tiene permisos para actualizar eventos."
            );
            return false;
        }

        if (evento == null) {
            return false;
        }

        try {

            boolean actualizado =
                    eventoService.actualizarEvento(evento);

            if (actualizado) {
                cargarEventos();
                lblEstado.setText(
                        "Evento actualizado correctamente."
                );
                return true;
            }

            lblEstado.setText(
                    "No se pudo actualizar el evento."
            );

            return false;

        } catch (IllegalArgumentException ex) {

            lblEstado.setText(ex.getMessage());
            return false;
        }
    }

    @FXML
    private void editar() {

        if (!SesionUsuario.esAdministrador()) {
            lblEstado.setText(
                    "No tiene permisos para editar eventos."
            );
            return;
        }

        Evento evento = tablaEventos
                .getSelectionModel()
                .getSelectedItem();

        if (evento == null) {

            lblEstado.setText(
                    "Seleccione un evento de la tabla para editar."
            );

            return;
        }

        try {

            FXMLLoader loader = new FXMLLoader(
                    EventosController.class.getResource(
                            "/cr/ac/eventoFormulario.fxml"
                    )
            );

            Scene scene = new Scene(loader.load());

            EventoFormularioController controller =
                    loader.getController();

            controller.establecerEventosController(this);
            controller.establecerEvento(evento);

            Stage stage = new Stage();

            stage.setTitle("Editar evento");
            stage.setScene(scene);

            VentanaUtil.configurar(stage);

            stage.show();

        } catch (IOException ex) {

            ex.printStackTrace();

            lblEstado.setText(
                    "No se pudo abrir el formulario de edición."
            );
        }
    }

    @FXML
    private void abrirEvento() {

        Evento evento = tablaEventos
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

            Scene scene = new Scene(loader.load());

            EventoDetalleController controller =
                    loader.getController();

            controller.establecerEvento(evento);

            Stage stage = (Stage) tablaEventos
                    .getScene()
                    .getWindow();

            stage.setTitle("Detalle del evento");
            stage.setScene(scene);

            VentanaUtil.configurar(stage);

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

            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) txtFiltro
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
