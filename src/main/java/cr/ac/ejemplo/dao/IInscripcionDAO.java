package cr.ac.ejemplo.dao;

import cr.ac.ejemplo.model.Inscripcion;
import cr.ac.ejemplo.model.enums.EstadoInscripcion;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public interface IInscripcionDAO {

    ArrayList<Inscripcion> listar() throws SQLException;

    ArrayList<Inscripcion> listarPorActividad(int idActividad)
            throws SQLException;
    
    ArrayList<Inscripcion> listarPorActividad(
        int idActividad, Connection conexion) throws SQLException;

    Inscripcion buscarPorId(int idInscripcion) throws SQLException;

    void insertar(Inscripcion inscripcion) throws SQLException;

    void insertar(Inscripcion inscripcion, Connection conexion)
            throws SQLException;

    void actualizar(Inscripcion inscripcion) throws SQLException;

    void cambiarEstado(int idInscripcion, EstadoInscripcion nuevoEstado)
            throws SQLException;
    
    boolean existeInscripcionActiva(int idPersona, int idActividad, 
            Connection conexion) 
            throws SQLException;
}
