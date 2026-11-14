
package cr.ac.ejemplo.dao;

import cr.ac.ejemplo.model.Persona;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public interface IPersonaDAO {

    void insertar(Persona persona) throws SQLException;

    void insertar(Persona persona, Connection conexion)
            throws SQLException;

    int buscarIdPorIdentificacion(
            String identificacion,
            Connection conexion
    ) throws SQLException;

    ArrayList<Persona> listar() throws SQLException;

    Persona buscarPorId(int idPersona) throws SQLException;

    void actualizar(Persona persona) throws SQLException;
}
