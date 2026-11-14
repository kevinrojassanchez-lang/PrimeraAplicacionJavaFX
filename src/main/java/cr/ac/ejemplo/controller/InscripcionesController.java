
package cr.ac.ejemplo.controller;

import cr.ac.ejemplo.DAOFactory;
import cr.ac.ejemplo.SesionUsuario;
import cr.ac.ejemplo.VentanaUtil;
import cr.ac.ejemplo.model.Inscripcion;
import cr.ac.ejemplo.model.Participante;
import cr.ac.ejemplo.model.Expositor;
import cr.ac.ejemplo.exception.DatosInvalidosException;
import cr.ac.ejemplo.exception.RegistroNoEncontradoException;
import cr.ac.ejemplo.service.InscripcionService;
import cr.ac.ejemplo.model.enums.EstadoInscripcion;

import java.io.IOException;
import java.sql.SQLException;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class InscripcionesController {

    @FXML
    private TextField txtFiltro;

    @FXML
    private Label lblEstado;

    @FXML
    private TableView<Inscripcion> tablaInscripciones;

    @FXML
    private TableColumn<Inscripcion, String> colId;

    @FXML
    private TableColumn<Inscripcion, String> colPersona;

    @FXML
    private TableColumn<Inscripcion, String> colTipo;

    @FXML
    private TableColumn<Inscripcion, String> colActividad;

    @FXML
    private TableColumn<Inscripcion, String> colFecha;

    @FXML
    private TableColumn<Inscripcion, String> colHora;

    @FXML
    private TableColumn<Inscripcion, String> colEstado;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnCancelar;

    private final InscripcionService inscripcionService =
            DAOFactory.crearInscripcionService();

    @FXML
    private void initialize() {

        boolean administrador = SesionUsuario.esAdministrador();

        btnEditar.setDisable(!administrador);
        btnCancelar.setDisable(!administrador);

        colId.setCellValueFactory(celda ->
                new SimpleStringProperty(
                        String.valueOf(
                                celda.getValue().getIdInscripcion()
                        )
                )
        );

        colPersona.setCellValueFactory(celda ->
                new SimpleStringProperty(
                        celda.getValue().getPersona().getNombre()
                        + " "
                        + celda.getValue().getPersona().getApellidos()
                )
        );

        colTipo.setCellValueFactory(celda ->
                new SimpleStringProperty(
                        celda.getValue().getTipo()
                )
        );

        colActividad.setCellValueFactory(celda ->
                new SimpleStringProperty(
                        celda.getValue().getActividad().getNombre()
                )
        );

        colFecha.setCellValueFactory(celda ->
                new SimpleStringProperty(
                        celda.getValue().getFecha()
                )
        );

        colHora.setCellValueFactory(celda ->
                new SimpleStringProperty(
                        celda.getValue().getHora()
                )
        );

        colEstado.setCellValueFactory(celda ->
                new SimpleStringProperty(
                        celda.getValue().getEstado().toString()
                )
        );

        cargarInscripciones();
    }

    private void cargarInscripciones() {

        try {

            tablaInscripciones.setItems(
                    FXCollections.observableArrayList(
                            inscripcionService.listarInscripciones()
                    )
            );

            lblEstado.setText(
                    "Inscripciones cargadas: "
                    + tablaInscripciones.getItems().size()
            );

        } catch (SQLException e) {

            lblEstado.setText(
                    "Error al cargar las inscripciones: "
                    + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    public void actualizarTabla() {
        cargarInscripciones();
    }

    @FXML
    private void inscribirParticipante() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/cr/ac/inscripcionFormulario.fxml"
                    )
            );

            Scene scene = new Scene(loader.load());

            InscripcionFormularioController controller =
                    loader.getController();

            controller.establecerInscripcionesController(this);

            Stage stage = new Stage();
            stage.setTitle("Inscribir participante");
            stage.setScene(scene);
            VentanaUtil.configurar(stage);
            stage.show();

        } catch (IOException e) {

            lblEstado.setText(
                    "Error al abrir el formulario: " + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    @FXML
    private void inscribirExpositor() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/cr/ac/expositorFormulario.fxml"
                    )
            );

            Scene scene = new Scene(loader.load());

            ExpositorFormularioController controller =
                    loader.getController();

            controller.establecerInscripcionesController(this);

            Stage stage = new Stage();
            stage.setTitle("Inscribir expositor");
            stage.setScene(scene);
            VentanaUtil.configurar(stage);
            stage.show();

        } catch (IOException e) {

            lblEstado.setText(
                    "Error al abrir el formulario: " + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    @FXML
    private void editar() {

        if (!SesionUsuario.esAdministrador()) {
            lblEstado.setText(
                    "No tiene permisos para realizar esta acción."
            );
            return;
        }

        Inscripcion inscripcion =
                tablaInscripciones.getSelectionModel()
                        .getSelectedItem();

        if (inscripcion == null) {

            lblEstado.setText(
                    "Seleccione una inscripción para editar."
            );

            return;
        }

        try {

            int idPersona =
                    inscripcion.getPersona().getIdPersona();

            if ("Participante".equalsIgnoreCase(
                    inscripcion.getTipo())) {

                Participante participante =
                        inscripcionService.buscarParticipante(idPersona);

                if (participante == null) {
                    lblEstado.setText(
                            "No se encontró el participante."
                    );
                    return;
                }

                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource(
                                "/cr/ac/participanteFormulario.fxml"
                        )
                );

                Scene scene = new Scene(loader.load());

                ParticipanteFormularioController controller =
                        loader.getController();

                controller.establecerInscripcionesController(this);
                controller.establecerParticipante(participante);

                Stage stage = new Stage();
                stage.setTitle("Editar participante");
                stage.setScene(scene);
                VentanaUtil.configurar(stage);
                stage.show();

                return;
            }

            if ("Expositor".equalsIgnoreCase(
                    inscripcion.getTipo())) {

                Expositor expositor =
                        inscripcionService.buscarExpositor(idPersona);

                if (expositor == null) {
                    lblEstado.setText(
                            "No se encontró el expositor."
                    );
                    return;
                }

                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource(
                                "/cr/ac/expositorFormulario.fxml"
                        )
                );

                Scene scene = new Scene(loader.load());

                ExpositorFormularioController controller =
                        loader.getController();

                controller.establecerInscripcionesController(this);
                controller.establecerExpositor(expositor);

                Stage stage = new Stage();
                stage.setTitle("Editar expositor");
                stage.setScene(scene);
                VentanaUtil.configurar(stage);
                stage.show();

                return;
            }

            lblEstado.setText(
                    "El tipo de inscripción no es reconocido."
            );

        } catch (SQLException | IOException e) {

            lblEstado.setText(
                    "Error al abrir la edición: " + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    @FXML
    private void cambiarEstadoInscripcion() {

        if (!SesionUsuario.esAdministrador()) {
            lblEstado.setText(
                    "No tiene permisos para realizar esta acción."
            );
            return;
        }

        Inscripcion inscripcion =
                tablaInscripciones.getSelectionModel()
                        .getSelectedItem();

        if (inscripcion == null) {
            lblEstado.setText(
                    "Seleccione una inscripción para cancelar."
            );
            return;
        }

        if (inscripcion.getEstado() == EstadoInscripcion.CANCELADA) {
            lblEstado.setText(
                    "Esta inscripción ya se encuentra cancelada."
            );
            return;
        }

        try {

            inscripcionService.cancelarInscripcion(
                    inscripcion.getIdInscripcion()
            );

            cargarInscripciones();

            lblEstado.setText(
                    "Inscripción cancelada correctamente."
            );

        } catch (
                SQLException
                | RegistroNoEncontradoException
                | DatosInvalidosException e) {

            lblEstado.setText(
                    "Error al cancelar la inscripción: " + e.getMessage()
            );
        }
    }

    @FXML
    private void regresar() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/cr/ac/menu.fxml")
            );

            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) txtFiltro
                    .getScene()
                    .getWindow();

            stage.setTitle("Menú principal");
            stage.setScene(scene);
            VentanaUtil.configurar(stage);
            stage.show();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
