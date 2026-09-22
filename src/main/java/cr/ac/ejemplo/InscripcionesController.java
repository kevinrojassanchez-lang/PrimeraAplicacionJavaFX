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
    private void initialize() {

        colId.setCellValueFactory(
                celda -> new javafx.beans.property.SimpleStringProperty(
                        String.valueOf(
                                celda.getValue().getIdInscripcion()
                        )
                )
        );

        colPersona.setCellValueFactory(
                celda -> new javafx.beans.property.SimpleStringProperty(
                        celda.getValue().getPersona().getNombre()
                        + " "
                        + celda.getValue().getPersona().getApellidos()
                )
        );

        colTipo.setCellValueFactory(
                celda -> new javafx.beans.property.SimpleStringProperty(
                        celda.getValue().getTipo()
                )
        );

        colActividad.setCellValueFactory(
                celda -> new javafx.beans.property.SimpleStringProperty(
                        celda.getValue().getActividad().getNombre()
                )
        );

        colFecha.setCellValueFactory(
                celda -> new javafx.beans.property.SimpleStringProperty(
                        celda.getValue().getFecha()
                )
        );

        colHora.setCellValueFactory(
                celda -> new javafx.beans.property.SimpleStringProperty(
                        celda.getValue().getHora()
                )
        );

        colEstado.setCellValueFactory(
                celda -> new javafx.beans.property.SimpleStringProperty(
                        celda.getValue().getEstado()
                )
        );

        tablaInscripciones.setItems(
                DatosSistema.getInscripciones()
        );

        lblEstado.setText("");
    }

    @FXML
    private void inscribirParticipante() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    InscripcionesController.class.getResource(
                            "/cr/ac/inscripcionFormulario.fxml"
                    )
            );

            Scene scene = new Scene(loader.load());

            InscripcionFormularioController controller =
                    loader.getController();

            controller.establecerInscripcionesController(
                    this
            );

            Stage stage = new Stage();

            stage.setTitle(
                    "Inscribir participante"
            );

            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {

            ex.printStackTrace();
        }
    }

    @FXML
    private void inscribirExpositor() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    InscripcionesController.class.getResource(
                            "/cr/ac/expositorFormulario.fxml"
                    )
            );

            Scene scene = new Scene(loader.load());

            ExpositorFormularioController controller =
                    loader.getController();

            controller.establecerInscripcionesController(
                    this
            );

            Stage stage = new Stage();

            stage.setTitle(
                    "Inscribir expositor"
            );

            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {

            ex.printStackTrace();
        }
    }

    public void actualizarTabla() {

        tablaInscripciones.refresh();
    }

    @FXML
    private void editar() {

        Inscripcion inscripcion =
                tablaInscripciones
                        .getSelectionModel()
                        .getSelectedItem();

        if (inscripcion == null) {

            lblEstado.setText(
                    "Seleccione una inscripción de la tabla para editar."
            );

            return;
        }

        lblEstado.setText(
                "Inscripción seleccionada para editar."
        );
    }

    @FXML
    private void cancelarInscripcion() {

        Inscripcion inscripcion =
                tablaInscripciones
                        .getSelectionModel()
                        .getSelectedItem();

        if (inscripcion == null) {

            lblEstado.setText(
                    "Seleccione una inscripción de la tabla para cancelar."
            );

            return;
        }

        lblEstado.setText(
                "Inscripción seleccionada para cancelar."
        );
    }

    @FXML
    private void regresar() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    InscripcionesController.class.getResource(
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

            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {

            ex.printStackTrace();
        }
    }
}
