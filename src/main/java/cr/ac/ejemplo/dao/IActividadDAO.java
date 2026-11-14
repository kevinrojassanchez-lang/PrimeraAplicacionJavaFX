package cr.ac.ejemplo.dao;

import cr.ac.ejemplo.model.Actividad;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Connection;

public interface IActividadDAO {

    void insertar(Actividad actividad) throws SQLException;

    ArrayList<Actividad> listar() throws SQLException;

    Actividad buscarPorId(int idActividad) throws SQLException;

    void actualizar(Actividad actividad) throws SQLException;
    
    Actividad buscarPorId(int idActividad, Connection conexion)
        throws SQLException;
}

