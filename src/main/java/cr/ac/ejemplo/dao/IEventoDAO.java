package cr.ac.ejemplo.dao;

import cr.ac.ejemplo.model.Evento;
import java.util.ArrayList;

public interface IEventoDAO {

    boolean insertar(Evento evento);

    ArrayList<Evento> listar();

    Evento buscarPorId(int idEvento);

    boolean actualizar(Evento evento);

    boolean eliminar(int idEvento);
}

