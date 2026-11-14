
package cr.ac.ejemplo.controller;

import cr.ac.ejemplo.DAOFactory;
import cr.ac.ejemplo.SesionUsuario;
import cr.ac.ejemplo.VentanaUtil;
import cr.ac.ejemplo.model.Actividad;
import cr.ac.ejemplo.model.Evento;
import cr.ac.ejemplo.service.ActividadService;

import java.io.IOException;
import java.sql.SQLException;
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

    @FXML
    private Button btnAgregar;

    @FXML
    private Button btnEditar;

    private Evento eventoSeleccionado;

    private final ActividadService actividadService =
            DAOFactory.crearActividadService();

    private ArrayList<Actividad> listaActividades =
            new ArrayList<>();

    @FXML
    private void initialize() {

        boolean administrador = SesionUsuario.esAdministrador();

        btnAgregar.setDisable(!administrador);
        btnEditar.setDisable(!administrador);

        colId.setCellValueFactory(celda ->
                new javafx.beans.property.SimpleStringProperty(
                        String.valueOf(
                                celda.getValue().getIdActividad()
                        )
                )
        );

        colNombre.setCellValueFactory(celda ->
                new javafx.beans.property.SimpleStringProperty(
                        celda.getValue().getNombre()
                )
        );

        colTipo.setCellValueFactory(celda ->
                new javafx.beans.property.SimpleStringProperty(
                        celda.getValue().getTipo()
                )
        );

        colFecha.setCellValueFactory(celda ->
                new javafx.beans.property.SimpleStringProperty(
                        celda.getValue().getFecha()
                )
        );

        colHorario.setCellValueFactory(celda ->
                new javafx.beans.property.SimpleStringProperty(
                        celda.getValue().getHoraInicio()
                        + " - "
                        + celda.getValue().getHoraFin()
                )
        );

        colCapacidad.setCellValueFactory(celda ->
                new javafx.beans.property.SimpleStringProperty(
                        String.valueOf(
                                celda.getValue().getCapacidadMaxima()
                        )
                )
        );

        colEstado.setCellValueFactory(celda ->
                new javafx.beans.property.SimpleStringProperty(
                        celda.getValue().getEstado() != null
                        ? celda.getValue().getEstado().toString()
                        : ""
                )
        );

        txtFiltro.textProperty().addListener(
                (observable, valorAnterior, valorNuevo) -> {
                    filtrarActividades();
                }
        );

        cargarActividades();
    }

    public void cargarActividades() {

        try {

            listaActividades =
                    actividadService.listarActividades();

            filtrarActividades();

            lblEstado.setText(
                    "Actividades cargadas correctamente."
            );

        } catch (SQLException ex) {

            lblEstado.setText(
                    "Error al cargar las actividades desde MySQL."
            );

            ex.printStackTrace();
        }
    }

    private void filtrarActividades() {

        ObservableList<Actividad> actividadesMostrar =
                FXCollections.observableArrayList();

        String filtro = txtFiltro.getText();

        if (filtro == null) {
            filtro = "";
        }

        filtro = filtro.trim().toLowerCase();

        for (Actividad actividad : listaActividades) {

            if (actividad == null) {
                continue;
            }

            if (eventoSeleccionado != null) {

                if (actividad.getEvento() == null
                        || actividad.getEvento().getIdEvento()
                        != eventoSeleccionado.getIdEvento()) {

                    continue;
                }
            }

            String nombre = actividad.getNombre() != null
                    ? actividad.getNombre().toLowerCase()
                    : "";

            String tipo = actividad.getTipo() != null
                    ? actividad.getTipo().toLowerCase()
                    : "";

            String fecha = actividad.getFecha() != null
                    ? actividad.getFecha().toLowerCase()
                    : "";

            String estado = actividad.getEstado() != null
                    ? actividad.getEstado().toString().toLowerCase()
                    : "";

            String id = String.valueOf(
                    actividad.getIdActividad()
            );

            if (nombre.contains(filtro)
                    || tipo.contains(filtro)
                    || fecha.contains(filtro)
                    || estado.contains(filtro)
                    || id.contains(filtro)) {

                actividadesMostrar.add(actividad);
            }
        }

        tablaActividades.setItems(actividadesMostrar);
        tablaActividades.refresh();
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

        lblEstado.setText(
                "Actividades del evento: " + evento.getNombre()
        );

        cargarActividades();
    }

    @FXML
    private void agregar() {

        if (!SesionUsuario.esAdministrador()) {
            lblEstado.setText(
                    "No tiene permisos para agregar actividades."
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
                            "/cr/ac/actividadFormulario.fxml"
                    )
            );

            Scene scene = new Scene(loader.load());

            ActividadFormularioController controller =
                    loader.getController();

            controller.establecerActividadesController(this);
            controller.establecerEvento(eventoSeleccionado);

            Stage stage = new Stage();

            stage.setTitle("Nueva actividad");
            stage.setScene(scene);

            VentanaUtil.configurar(stage);

            stage.show();

        } catch (IOException ex) {

            lblEstado.setText(
                    "Error al abrir el formulario de actividades."
            );

            ex.printStackTrace();
        }
    }

    @FXML
    private void editar() {

        if (!SesionUsuario.esAdministrador()) {
            lblEstado.setText(
                    "No tiene permisos para editar actividades."
            );
            return;
        }

        Actividad actividad =
                tablaActividades.getSelectionModel()
                        .getSelectedItem();

        if (actividad == null) {

            lblEstado.setText(
                    "Seleccione una actividad de la tabla para editar."
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

            controller.establecerActividadesController(this);
            controller.establecerEvento(actividad.getEvento());
            controller.establecerActividad(actividad);

            Stage stage = new Stage();

            stage.setTitle("Editar actividad");
            stage.setScene(scene);

            VentanaUtil.configurar(stage);

            stage.show();

        } catch (IOException ex) {

            lblEstado.setText(
                    "Error al abrir el formulario de edición."
            );

            ex.printStackTrace();
        }
    }

    @FXML
    private void abrirActividad() {

        Actividad actividad =
                tablaActividades.getSelectionModel()
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

            stage.setTitle("Detalle de la actividad");
            stage.setScene(scene);

            VentanaUtil.configurar(stage);

            stage.show();

        } catch (IOException ex) {

            lblEstado.setText(
                    "Error al abrir el detalle de la actividad."
            );

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

            controller.establecerEvento(eventoSeleccionado);

            Stage stage = (Stage) txtFiltro
                    .getScene()
                    .getWindow();

            stage.setTitle("Detalle del evento");
            stage.setScene(scene);

            VentanaUtil.configurar(stage);

            stage.show();

        } catch (IOException ex) {

            lblEstado.setText(
                    "Error al regresar al detalle del evento."
            );

            ex.printStackTrace();
        }
    }
}
