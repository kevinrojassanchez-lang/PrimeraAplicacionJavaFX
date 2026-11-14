package cr.ac.ejemplo.service;

import cr.ac.ejemplo.dao.IAsistenciaDAO;
import cr.ac.ejemplo.dao.IInscripcionDAO;
import cr.ac.ejemplo.model.Asistencia;
import cr.ac.ejemplo.model.Inscripcion;
import java.sql.SQLException;
import java.util.ArrayList;

public class AsistenciaService {

    private final IAsistenciaDAO asistenciaDAO;
    private final IInscripcionDAO inscripcionDAO;

    public AsistenciaService(IAsistenciaDAO asistenciaDAO,
            IInscripcionDAO inscripcionDAO) {

        this.asistenciaDAO = asistenciaDAO;
        this.inscripcionDAO = inscripcionDAO;
    }

    public ArrayList<Inscripcion> listarInscripciones(int idActividad)
            throws SQLException {

        return inscripcionDAO.listarPorActividad(idActividad);
    }

    public ArrayList<Asistencia> listarAsistencias(int idActividad)
            throws SQLException {

        return asistenciaDAO.listarPorActividad(idActividad);
    }

    public Asistencia buscarPorInscripcion(int idInscripcion)
            throws SQLException {

        return asistenciaDAO.buscarPorInscripcion(idInscripcion);
    }

    public void registrarAsistencia(Asistencia asistencia)
            throws SQLException {

        asistenciaDAO.insertar(asistencia);
    }

    public void actualizarAsistencia(Asistencia asistencia)
            throws SQLException {

        asistenciaDAO.actualizar(asistencia);
    }
}
