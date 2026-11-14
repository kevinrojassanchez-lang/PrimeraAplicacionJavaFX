
package cr.ac.ejemplo.service;

import cr.ac.ejemplo.dao.IExpositorDAO;
import cr.ac.ejemplo.model.Expositor;

import java.sql.SQLException;
import java.util.ArrayList;

public class ExpositorService {

    private final IExpositorDAO expositorDAO;

    public ExpositorService(IExpositorDAO expositorDAO) {
        this.expositorDAO = expositorDAO;
    }

    public ArrayList<Expositor> listarPorActividad(
            int idActividad) throws SQLException {

        if (idActividad <= 0) {
            throw new IllegalArgumentException(
                    "El ID de la actividad no es válido."
            );
        }

        return expositorDAO.listarPorActividad(idActividad);
    }

    public void actualizarExpositor(
            Expositor expositor) throws SQLException {

        if (expositor == null) {
            throw new IllegalArgumentException(
                    "El expositor no puede ser nulo."
            );
        }

        expositorDAO.actualizar(expositor);
    }
}
