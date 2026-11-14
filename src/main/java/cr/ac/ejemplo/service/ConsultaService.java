
package cr.ac.ejemplo.service;

import cr.ac.ejemplo.dao.IEventoDAO;
import cr.ac.ejemplo.dao.IActividadDAO;
import cr.ac.ejemplo.dao.IInscripcionDAO;
import cr.ac.ejemplo.model.Evento;
import cr.ac.ejemplo.model.Actividad;
import cr.ac.ejemplo.model.Inscripcion;
import cr.ac.ejemplo.model.enums.EstadoInscripcion;

import java.sql.SQLException;
import java.util.ArrayList;

public class ConsultaService {

    private final IEventoDAO eventoDAO;
    private final IActividadDAO actividadDAO;
    private final IInscripcionDAO inscripcionDAO;

    public ConsultaService(
            IEventoDAO eventoDAO,
            IActividadDAO actividadDAO,
            IInscripcionDAO inscripcionDAO) {

        this.eventoDAO = eventoDAO;
        this.actividadDAO = actividadDAO;
        this.inscripcionDAO = inscripcionDAO;
    }

    public ArrayList<Evento> listarEventos() {

        return eventoDAO.listar();
    }

    public ArrayList<Actividad> listarActividades()
            throws SQLException {

        return actividadDAO.listar();
    }

    public ArrayList<Inscripcion> listarInscripciones()
            throws SQLException {

        return inscripcionDAO.listar();
    }

    public int contarInscripcionesActivas(
            int idActividad,
            ArrayList<Inscripcion> inscripciones) {

        int cantidad = 0;

        for (Inscripcion inscripcion : inscripciones) {

            if (inscripcion.getActividad().getIdActividad()
                    == idActividad
                    && inscripcion.getEstado()
                    == EstadoInscripcion.ACTIVA) {

                cantidad++;
            }
        }

        return cantidad;
    }
}
