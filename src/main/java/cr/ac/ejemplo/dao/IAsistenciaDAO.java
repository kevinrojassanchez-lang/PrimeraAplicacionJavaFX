package cr.ac.ejemplo.dao;

import cr.ac.ejemplo.model.Asistencia;
import java.sql.SQLException;
import java.util.ArrayList;

public interface IAsistenciaDAO {

    ArrayList<Asistencia> listarPorActividad(int idActividad)
            throws SQLException;

    Asistencia buscarPorInscripcion(int idInscripcion)
            throws SQLException;

    void insertar(Asistencia asistencia)
            throws SQLException;

    void actualizar(Asistencia asistencia)
            throws SQLException;
}
