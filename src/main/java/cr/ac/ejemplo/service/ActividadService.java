
package cr.ac.ejemplo.service;

import cr.ac.ejemplo.dao.IActividadDAO;
import cr.ac.ejemplo.model.Actividad;
import java.sql.SQLException;
import java.util.ArrayList;

public class ActividadService {

    private final IActividadDAO actividadDAO;

    public ActividadService(IActividadDAO actividadDAO) {
        this.actividadDAO = actividadDAO;
    }

    public ArrayList<Actividad> listarActividades()
            throws SQLException {

        return actividadDAO.listar();
    }

    public Actividad buscarActividadPorId(int idActividad)
            throws SQLException {

        if (idActividad <= 0) {
            throw new IllegalArgumentException(
                    "El identificador de la actividad no es válido."
            );
        }

        Actividad actividad = actividadDAO.buscarPorId(idActividad);

        if (actividad == null) {
            throw new IllegalArgumentException(
                    "No se encontró la actividad."
            );
        }

        return actividad;
    }

    public void registrarActividad(Actividad actividad)
            throws SQLException {

        validarActividad(actividad);
        actividadDAO.insertar(actividad);
    }

    public void actualizarActividad(Actividad actividad)
            throws SQLException {

        validarActividad(actividad);

        if (actividad.getIdActividad() <= 0) {
            throw new IllegalArgumentException(
                    "El identificador de la actividad no es válido."
            );
        }

        actividadDAO.actualizar(actividad);
    }

    private void validarActividad(Actividad actividad) {

        if (actividad == null) {
            throw new IllegalArgumentException(
                    "La actividad no puede ser nula."
            );
        }

        if (actividad.getEvento() == null
                || actividad.getEvento().getIdEvento() <= 0) {
            throw new IllegalArgumentException(
                    "Debe seleccionar un evento válido."
            );
        }

        if (actividad.getNombre() == null
                || actividad.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre de la actividad es obligatorio."
            );
        }

        if (actividad.getFecha() == null
                || actividad.getFecha().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "La fecha de la actividad es obligatoria."
            );
        }

        if (actividad.getHoraInicio() == null
                || actividad.getHoraFin() == null) {
            throw new IllegalArgumentException(
                    "Debe indicar las horas de inicio y finalización."
            );
        }

        if (actividad.getLugar() == null
                || actividad.getLugar().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El lugar de la actividad es obligatorio."
            );
        }

        if (actividad.getCapacidadMaxima() <= 0) {
            throw new IllegalArgumentException(
                    "La capacidad debe ser mayor que cero."
            );
        }

        if (actividad.getTipo() == null
                || actividad.getTipo().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Debe seleccionar el tipo de actividad."
            );
        }

        if (actividad.getEstado() == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar el estado de la actividad."
            );
        }
    }
}

