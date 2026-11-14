
package cr.ac.ejemplo.service;

import cr.ac.ejemplo.dao.IParticipanteDAO;
import cr.ac.ejemplo.model.Participante;

import java.sql.SQLException;
import java.util.ArrayList;

public class ParticipanteService {

    private final IParticipanteDAO participanteDAO;

    public ParticipanteService(IParticipanteDAO participanteDAO) {
        this.participanteDAO = participanteDAO;
    }

    public ArrayList<Participante> listarPorActividad(
            int idActividad) throws SQLException {

        if (idActividad <= 0) {
            throw new IllegalArgumentException(
                    "El ID de la actividad no es válido."
            );
        }

        return participanteDAO.listarPorActividad(idActividad);
    }

    public void actualizarParticipante(
            Participante participante) throws SQLException {

        if (participante == null) {
            throw new IllegalArgumentException(
                    "El participante no puede ser nulo."
            );
        }

        participanteDAO.actualizar(participante);
    }
}
