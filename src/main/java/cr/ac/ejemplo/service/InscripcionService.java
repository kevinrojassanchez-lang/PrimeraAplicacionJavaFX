
package cr.ac.ejemplo.service;

import cr.ac.ejemplo.dao.IActividadDAO;
import cr.ac.ejemplo.dao.IExpositorDAO;
import cr.ac.ejemplo.dao.IInscripcionDAO;
import cr.ac.ejemplo.dao.IParticipanteDAO;
import cr.ac.ejemplo.dao.IPersonaDAO;

import cr.ac.ejemplo.exception.ActividadNoDisponibleException;
import cr.ac.ejemplo.exception.CupoAgotadoException;
import cr.ac.ejemplo.exception.DatosInvalidosException;
import cr.ac.ejemplo.exception.InscripcionDuplicadaException;
import cr.ac.ejemplo.exception.RegistroNoEncontradoException;

import cr.ac.ejemplo.model.Actividad;
import cr.ac.ejemplo.model.Expositor;
import cr.ac.ejemplo.model.Inscripcion;
import cr.ac.ejemplo.model.Participante;
import cr.ac.ejemplo.model.Persona;

import cr.ac.ejemplo.model.enums.EstadoActividad;
import cr.ac.ejemplo.model.enums.EstadoEvento;
import cr.ac.ejemplo.model.enums.EstadoInscripcion;

import java.sql.Connection;
import java.sql.SQLException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.util.ArrayList;

public class InscripcionService {

    private final IInscripcionDAO inscripcionDAO;
    private final IParticipanteDAO participanteDAO;
    private final IExpositorDAO expositorDAO;
    private final IActividadDAO actividadDAO;
    private final IPersonaDAO personaDAO;
    private final ConexionProvider conexionProvider;

    public InscripcionService(
            IInscripcionDAO inscripcionDAO,
            IParticipanteDAO participanteDAO,
            IExpositorDAO expositorDAO,
            IActividadDAO actividadDAO,
            IPersonaDAO personaDAO,
            ConexionProvider conexionProvider) {

        this.inscripcionDAO = inscripcionDAO;
        this.participanteDAO = participanteDAO;
        this.expositorDAO = expositorDAO;
        this.actividadDAO = actividadDAO;
        this.personaDAO = personaDAO;
        this.conexionProvider = conexionProvider;
    }

    // Consultar todas las inscripciones
    public ArrayList<Inscripcion> listarInscripciones()
            throws SQLException {

        return inscripcionDAO.listar();
    }

    // Buscar un participante por su identificador
    public Participante buscarParticipante(int idPersona)
            throws SQLException {

        if (idPersona <= 0) {
            throw new IllegalArgumentException(
                    "El identificador del participante no es válido."
            );
        }

        return participanteDAO.buscarPorId(idPersona);
    }

    // Buscar un expositor por su identificador
    public Expositor buscarExpositor(int idPersona)
            throws SQLException {

        if (idPersona <= 0) {
            throw new IllegalArgumentException(
                    "El identificador del expositor no es válido."
            );
        }

        return expositorDAO.buscarPorId(idPersona);
    }

    // Inscribir un participante existente
    public void registrarInscripcion(
            int idParticipante,
            int idActividad,
            String tipo)
            throws SQLException,
            DatosInvalidosException,
            RegistroNoEncontradoException,
            ActividadNoDisponibleException,
            InscripcionDuplicadaException,
            CupoAgotadoException {

        if (idParticipante <= 0 || idActividad <= 0) {
            throw new DatosInvalidosException(
                    "Los identificadores deben ser válidos."
            );
        }

        if (tipo == null
                || !"Participante".equalsIgnoreCase(tipo.trim())) {

            throw new DatosInvalidosException(
                    "El tipo de inscripción debe ser Participante."
            );
        }

        try (Connection conexion = conexionProvider.conectar()) {

            conexion.setAutoCommit(false);

            try {

                Participante participante =
                        participanteDAO.buscarPorId(
                                idParticipante,
                                conexion
                        );

                if (participante == null) {
                    throw new RegistroNoEncontradoException(
                            "No se encontró el participante."
                    );
                }

                Actividad actividad =
                        actividadDAO.buscarPorId(
                                idActividad,
                                conexion
                        );

                validarActividadDisponible(actividad);

                if (inscripcionDAO.existeInscripcionActiva(
                        idParticipante,
                        idActividad,
                        conexion)) {

                    throw new InscripcionDuplicadaException(
                            "El participante ya tiene una inscripción activa."
                    );
                }

                ArrayList<Inscripcion> inscripciones =
                        inscripcionDAO.listarPorActividad(
                                idActividad,
                                conexion
                        );

                validarCupo(actividad, inscripciones.size());

                String fecha = LocalDate.now().toString();

                String hora = LocalTime.now().format(
                        DateTimeFormatter.ofPattern("HH:mm")
                );

                Inscripcion inscripcion = new Inscripcion(
                        0,
                        participante,
                        actividad,
                        fecha,
                        hora,
                        "Participante",
                        EstadoInscripcion.ACTIVA
                );

                inscripcionDAO.insertar(inscripcion, conexion);

                conexion.commit();

            } catch (
                    SQLException
                    | RegistroNoEncontradoException
                    | ActividadNoDisponibleException
                    | InscripcionDuplicadaException
                    | CupoAgotadoException
                    | RuntimeException ex) {

                conexion.rollback();
                throw ex;
            }
        }
    }

    // Registrar un participante nuevo o existente
    public void registrarParticipanteConInscripcion(
            Participante participante,
            int idActividad,
            String fecha,
            String hora,
            EstadoInscripcion estado)
            throws SQLException,
            DatosInvalidosException,
            RegistroNoEncontradoException,
            ActividadNoDisponibleException,
            CupoAgotadoException,
            InscripcionDuplicadaException {

        validarDatosInscripcion(
                participante,
                idActividad,
                fecha,
                hora,
                estado
        );

        try (Connection conexion = conexionProvider.conectar()) {

            conexion.setAutoCommit(false);

            try {

                Actividad actividad =
                        actividadDAO.buscarPorId(
                                idActividad,
                                conexion
                        );

                validarActividadDisponible(actividad);

                int idPersona =
                        personaDAO.buscarIdPorIdentificacion(
                                participante.getIdentificacion(),
                                conexion
                        );

                if (idPersona == 0) {
                    personaDAO.insertar(participante, conexion);
                } else {
                    participante.setIdPersona(idPersona);
                }

                if (estado == EstadoInscripcion.ACTIVA) {

                    if (inscripcionDAO.existeInscripcionActiva(
                            participante.getIdPersona(),
                            idActividad,
                            conexion)) {

                        throw new InscripcionDuplicadaException(
                                "La persona ya tiene una inscripción activa en esta actividad."
                        );
                    }

                    ArrayList<Inscripcion> inscripciones =
                            inscripcionDAO.listarPorActividad(
                                    idActividad,
                                    conexion
                            );

                    validarCupo(actividad, inscripciones.size());
                }

                if (!participanteDAO.existeRol(
                        participante.getIdPersona(),
                        conexion)) {

                    participanteDAO.insertarRol(
                            participante,
                            conexion
                    );
                }

                Inscripcion inscripcion = new Inscripcion(
                        0,
                        participante,
                        actividad,
                        fecha.trim(),
                        hora.trim(),
                        "Participante",
                        estado
                );

                inscripcionDAO.insertar(inscripcion, conexion);

                conexion.commit();

            } catch (
                    SQLException
                    | RegistroNoEncontradoException
                    | ActividadNoDisponibleException
                    | CupoAgotadoException
                    | InscripcionDuplicadaException
                    | RuntimeException ex) {

                conexion.rollback();
                throw ex;
            }
        }
    }

    // Registrar un expositor nuevo o existente
    public void registrarExpositorConInscripcion(
            Expositor expositor,
            int idActividad,
            String fecha,
            String hora,
            EstadoInscripcion estado)
            throws SQLException,
            DatosInvalidosException,
            RegistroNoEncontradoException,
            ActividadNoDisponibleException,
            CupoAgotadoException,
            InscripcionDuplicadaException {

        validarDatosInscripcion(
                expositor,
                idActividad,
                fecha,
                hora,
                estado
        );

        try (Connection conexion = conexionProvider.conectar()) {

            conexion.setAutoCommit(false);

            try {

                Actividad actividad =
                        actividadDAO.buscarPorId(
                                idActividad,
                                conexion
                        );

                validarActividadDisponible(actividad);

                int idPersona =
                        personaDAO.buscarIdPorIdentificacion(
                                expositor.getIdentificacion(),
                                conexion
                        );

                if (idPersona == 0) {
                    personaDAO.insertar(expositor, conexion);
                } else {
                    expositor.setIdPersona(idPersona);
                }

                if (estado == EstadoInscripcion.ACTIVA) {

                    if (inscripcionDAO.existeInscripcionActiva(
                            expositor.getIdPersona(),
                            idActividad,
                            conexion)) {

                        throw new InscripcionDuplicadaException(
                                "La persona ya tiene una inscripción activa en esta actividad."
                        );
                    }

                    ArrayList<Inscripcion> inscripciones =
                            inscripcionDAO.listarPorActividad(
                                    idActividad,
                                    conexion
                            );

                    validarCupo(actividad, inscripciones.size());
                }

                if (!expositorDAO.existeRol(
                        expositor.getIdPersona(),
                        conexion)) {

                    expositorDAO.insertarRol(
                            expositor,
                            conexion
                    );
                }

                Inscripcion inscripcion = new Inscripcion(
                        0,
                        expositor,
                        actividad,
                        fecha.trim(),
                        hora.trim(),
                        "Expositor",
                        estado
                );

                inscripcionDAO.insertar(inscripcion, conexion);

                conexion.commit();

            } catch (
                    SQLException
                    | RegistroNoEncontradoException
                    | ActividadNoDisponibleException
                    | CupoAgotadoException
                    | InscripcionDuplicadaException
                    | RuntimeException ex) {

                conexion.rollback();
                throw ex;
            }
        }
    }

    // Validar los datos de la inscripción
    private void validarDatosInscripcion(
            Persona persona,
            int idActividad,
            String fecha,
            String hora,
            EstadoInscripcion estado)
            throws DatosInvalidosException {

        if (persona == null) {
            throw new DatosInvalidosException(
                    "Los datos de la persona son obligatorios."
            );
        }

        if (persona.getIdentificacion() == null
                || persona.getIdentificacion().isBlank()) {

            throw new DatosInvalidosException(
                    "La identificación es obligatoria."
            );
        }

        if (idActividad <= 0) {
            throw new DatosInvalidosException(
                    "Debe seleccionar una actividad válida."
            );
        }

        if (fecha == null || fecha.isBlank()) {
            throw new DatosInvalidosException(
                    "Debe indicar la fecha de inscripción."
            );
        }

        if (hora == null || hora.isBlank()) {
            throw new DatosInvalidosException(
                    "Debe indicar la hora de inscripción."
            );
        }

        if (estado == null) {
            throw new DatosInvalidosException(
                    "Debe indicar el estado de la inscripción."
            );
        }

        try {
            LocalDate.parse(fecha.trim());
        } catch (DateTimeParseException ex) {
            throw new DatosInvalidosException(
                    "La fecha no tiene un formato válido."
            );
        }

        try {
            LocalTime.parse(hora.trim());
        } catch (DateTimeParseException ex) {
            throw new DatosInvalidosException(
                    "La hora debe tener el formato HH:mm."
            );
        }
    }

    // Validar que la actividad y el evento estén disponibles
    private void validarActividadDisponible(
            Actividad actividad)
            throws RegistroNoEncontradoException,
            ActividadNoDisponibleException {

        if (actividad == null) {
            throw new RegistroNoEncontradoException(
                    "No se encontró la actividad."
            );
        }

        if (actividad.getEstado() != EstadoActividad.ABIERTA) {
            throw new ActividadNoDisponibleException(
                    "La actividad no está abierta para inscripciones."
            );
        }

        if (actividad.getEvento() == null
                || actividad.getEvento().getEstado()
                != EstadoEvento.ABIERTO) {

            throw new ActividadNoDisponibleException(
                    "El evento no está abierto para inscripciones."
            );
        }
    }

    // Validar la capacidad disponible
    private void validarCupo(
            Actividad actividad,
            int cantidadInscripciones)
            throws CupoAgotadoException {

        if (actividad.getCapacidadMaxima() <= 0
                || cantidadInscripciones
                >= actividad.getCapacidadMaxima()) {

            throw new CupoAgotadoException(
                    "La actividad ya no tiene espacios disponibles."
            );
        }
    }

    // Cancelar una inscripción
    public void cancelarInscripcion(int idInscripcion)
            throws SQLException,
            RegistroNoEncontradoException,
            DatosInvalidosException {

        if (idInscripcion <= 0) {
            throw new DatosInvalidosException(
                    "El identificador de inscripción no es válido."
            );
        }

        Inscripcion inscripcion =
                inscripcionDAO.buscarPorId(idInscripcion);

        if (inscripcion == null) {
            throw new RegistroNoEncontradoException(
                    "No se encontró la inscripción."
            );
        }

        if (inscripcion.getEstado()
                == EstadoInscripcion.CANCELADA) {

            throw new DatosInvalidosException(
                    "La inscripción ya se encuentra cancelada."
            );
        }

        inscripcionDAO.cambiarEstado(
                idInscripcion,
                EstadoInscripcion.CANCELADA
        );
    }
}
