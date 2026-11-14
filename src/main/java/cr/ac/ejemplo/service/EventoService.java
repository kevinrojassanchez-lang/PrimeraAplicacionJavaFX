
package cr.ac.ejemplo.service;

import cr.ac.ejemplo.dao.IEventoDAO;
import cr.ac.ejemplo.model.Evento;
import java.time.LocalDate;
import java.util.ArrayList;

public class EventoService {

    private final IEventoDAO eventoDAO;

    public EventoService(IEventoDAO eventoDAO) {
        this.eventoDAO = eventoDAO;
    }

    public ArrayList<Evento> listarEventos() {
        return eventoDAO.listar();
    }

    public Evento buscarEventoPorId(int idEvento) {

        if (idEvento <= 0) {
            throw new IllegalArgumentException(
                    "El identificador del evento no es válido."
            );
        }

        Evento evento = eventoDAO.buscarPorId(idEvento);

        if (evento == null) {
            throw new IllegalArgumentException(
                    "No se encontró el evento."
            );
        }

        return evento;
    }

    public boolean registrarEvento(Evento evento) {

        validarEvento(evento);
        return eventoDAO.insertar(evento);
    }

    public boolean actualizarEvento(Evento evento) {

        validarEvento(evento);

        if (evento.getIdEvento() <= 0) {
            throw new IllegalArgumentException(
                    "El identificador del evento no es válido."
            );
        }

        return eventoDAO.actualizar(evento);
    }

    public boolean eliminarEvento(int idEvento) {

        if (idEvento <= 0) {
            throw new IllegalArgumentException(
                    "El identificador del evento no es válido."
            );
        }

        return eventoDAO.eliminar(idEvento);
    }

    private void validarEvento(Evento evento) {

        if (evento == null) {
            throw new IllegalArgumentException(
                    "El evento no puede ser nulo."
            );
        }

        if (evento.getNombre() == null
                || evento.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre del evento es obligatorio."
            );
        }

        if (evento.getFechaInicio() == null
                || evento.getFechaFin() == null) {
            throw new IllegalArgumentException(
                    "Debe indicar las fechas del evento."
            );
        }

        LocalDate fechaInicio;
        LocalDate fechaFin;

        try {

            fechaInicio = LocalDate.parse(evento.getFechaInicio());
            fechaFin = LocalDate.parse(evento.getFechaFin());

        } catch (Exception ex) {

            throw new IllegalArgumentException(
                    "Las fechas del evento no son válidas."
            );
        }

        if (fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException(
                    "La fecha final no puede ser anterior a la inicial."
            );
        }

        if (evento.getLugar() == null
                || evento.getLugar().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El lugar del evento es obligatorio."
            );
        }

        if (evento.getEstado() == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar el estado del evento."
            );
        }
    }
}

