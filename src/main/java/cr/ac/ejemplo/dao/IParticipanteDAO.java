
package cr.ac.ejemplo.dao;

import cr.ac.ejemplo.model.Participante;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public interface IParticipanteDAO {

    void insertar(Participante participante) throws SQLException;

    void insertar(Participante participante, Connection conexion)
            throws SQLException;

    void insertarRol(Participante participante, Connection conexion)
            throws SQLException;

    boolean existeRol(int idPersona, Connection conexion)
            throws SQLException;

    ArrayList<Participante> listarPorActividad(int idActividad)
            throws SQLException;

    Participante buscarPorId(int idPersona) throws SQLException;

    Participante buscarPorId(int idPersona, Connection conexion)
            throws SQLException;

    void actualizar(Participante participante) throws SQLException;
}
