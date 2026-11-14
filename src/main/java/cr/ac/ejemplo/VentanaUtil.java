
package cr.ac.ejemplo;

import javafx.stage.Screen;
import javafx.stage.Stage;

public class VentanaUtil {

    private static final double ANCHO = 1000;
    private static final double ALTO = 700;

    public static void configurar(Stage stage) {

        stage.setWidth(ANCHO);
        stage.setHeight(ALTO);

        stage.setResizable(false);

        stage.centerOnScreen();
    }

}
