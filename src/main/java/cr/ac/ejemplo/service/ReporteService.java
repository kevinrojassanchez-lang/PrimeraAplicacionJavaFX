
package cr.ac.ejemplo.service;

import cr.ac.ejemplo.dao.IActividadDAO;
import cr.ac.ejemplo.dao.IPersonaDAO;
import cr.ac.ejemplo.dao.IInscripcionDAO;
import cr.ac.ejemplo.dao.IAsistenciaDAO;

import cr.ac.ejemplo.model.Actividad;
import cr.ac.ejemplo.model.Persona;
import cr.ac.ejemplo.model.Participante;
import cr.ac.ejemplo.model.Inscripcion;
import cr.ac.ejemplo.model.Asistencia;

import cr.ac.ejemplo.model.enums.EstadoInscripcion;
import cr.ac.ejemplo.model.enums.EstadoAsistencia;

import java.sql.SQLException;
import java.util.ArrayList;

public class ReporteService {

    private final IActividadDAO actividadDAO;
    private final IPersonaDAO personaDAO;
    private final IInscripcionDAO inscripcionDAO;
    private final IAsistenciaDAO asistenciaDAO;

    public ReporteService(
            IActividadDAO actividadDAO,
            IPersonaDAO personaDAO,
            IInscripcionDAO inscripcionDAO,
            IAsistenciaDAO asistenciaDAO) {

        this.actividadDAO = actividadDAO;
        this.personaDAO = personaDAO;
        this.inscripcionDAO = inscripcionDAO;
        this.asistenciaDAO = asistenciaDAO;
    }

    public ArrayList<Actividad> listarActividades()
            throws SQLException {

        return actividadDAO.listar();
    }

    public ArrayList<Participante> listarParticipantes()
            throws SQLException {

        ArrayList<Participante> participantes =
                new ArrayList<>();

        ArrayList<Persona> personas = personaDAO.listar();

        for (Persona persona : personas) {

            if (persona instanceof Participante) {
                participantes.add((Participante) persona);
            }
        }

        return participantes;
    }

    public ArrayList<Inscripcion> listarInscripciones()
            throws SQLException {

        return inscripcionDAO.listar();
    }

    public ArrayList<Asistencia> listarAsistencias(
            ArrayList<Actividad> actividades)
            throws SQLException {

        ArrayList<Asistencia> asistencias =
                new ArrayList<>();

        for (Actividad actividad : actividades) {

            asistencias.addAll(
                    asistenciaDAO.listarPorActividad(
                            actividad.getIdActividad()
                    )
            );
        }

        return asistencias;
    }

    // Actividades ordenadas de mayor a menor participación
    public ArrayList<Actividad> listarActividadesMayorParticipacion(
            ArrayList<Actividad> actividades,
            ArrayList<Inscripcion> inscripciones) {

        ArrayList<Actividad> resultados =
                new ArrayList<>(actividades);

        for (int i = 0; i < resultados.size() - 1; i++) {

            for (int j = 0; j < resultados.size() - 1 - i; j++) {

                int cantidadActual =
                        contarInscripcionesActivas(
                                resultados.get(j),
                                inscripciones
                        );

                int cantidadSiguiente =
                        contarInscripcionesActivas(
                                resultados.get(j + 1),
                                inscripciones
                        );

                if (cantidadActual < cantidadSiguiente) {

                    Actividad temporal = resultados.get(j);

                    resultados.set(j, resultados.get(j + 1));

                    resultados.set(j + 1, temporal);
                }
            }
        }

        return resultados;
    }

    public int contarInscripcionesActivas(
            Actividad actividad,
            ArrayList<Inscripcion> inscripciones) {

        int cantidad = 0;

        for (Inscripcion inscripcion : inscripciones) {

            if (inscripcion.getActividad() != null
                    && inscripcion.getActividad().getIdActividad()
                    == actividad.getIdActividad()
                    && inscripcion.getEstado()
                    == EstadoInscripcion.ACTIVA) {

                cantidad++;
            }
        }

        return cantidad;
    }

    public int contarAsistieron(
            Actividad actividad,
            ArrayList<Inscripcion> inscripciones,
            ArrayList<Asistencia> asistencias) {

        int cantidad = 0;

        for (Inscripcion inscripcion : inscripciones) {

            if (inscripcion.getActividad() != null
                    && inscripcion.getActividad().getIdActividad()
                    == actividad.getIdActividad()
                    && inscripcion.getEstado()
                    == EstadoInscripcion.ACTIVA) {

                Asistencia asistencia =
                        buscarAsistencia(inscripcion, asistencias);

                if (asistencia != null
                        && asistencia.getEstado()
                        == EstadoAsistencia.ASISTIO) {

                    cantidad++;
                }
            }
        }

        return cantidad;
    }

    public int contarNoAsistieron(
            Actividad actividad,
            ArrayList<Inscripcion> inscripciones,
            ArrayList<Asistencia> asistencias) {

        int cantidad = 0;

        for (Inscripcion inscripcion : inscripciones) {

            if (inscripcion.getActividad() != null
                    && inscripcion.getActividad().getIdActividad()
                    == actividad.getIdActividad()
                    && inscripcion.getEstado()
                    == EstadoInscripcion.ACTIVA) {

                Asistencia asistencia =
                        buscarAsistencia(inscripcion, asistencias);

                if (asistencia != null
                        && asistencia.getEstado()
                        == EstadoAsistencia.NO_ASISTIO) {

                    cantidad++;
                }
            }
        }

        return cantidad;
    }

    public Asistencia buscarAsistencia(
            Inscripcion inscripcion,
            ArrayList<Asistencia> asistencias) {

        for (Asistencia asistencia : asistencias) {

            if (asistencia.getInscripcion() != null
                    && asistencia.getInscripcion().getIdInscripcion()
                    == inscripcion.getIdInscripcion()) {

                return asistencia;
            }
        }

        return null;
    }
}
