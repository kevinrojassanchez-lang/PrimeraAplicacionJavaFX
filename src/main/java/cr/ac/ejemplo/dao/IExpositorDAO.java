
package cr.ac.ejemplo.dao;

import cr.ac.ejemplo.model.Expositor;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public interface IExpositorDAO {

    void insertar(Expositor expositor) throws SQLException;

    void insertar(Expositor expositor, Connection conexion)
            throws SQLException;

    void insertarRol(Expositor expositor, Connection conexion)
            throws SQLException;

    boolean existeRol(int idPersona, Connection conexion)
            throws SQLException;

    ArrayList<Expositor> listar() throws SQLException;

    ArrayList<Expositor> listarPorActividad(int idActividad)
            throws SQLException;

    Expositor buscarPorId(int idPersona) throws SQLException;

    void actualizar(Expositor expositor) throws SQLException;
}
