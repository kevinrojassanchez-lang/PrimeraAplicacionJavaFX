package cr.ac.ejemplo.dao;

import cr.ac.ejemplo.model.Usuario;
import java.sql.SQLException;

public interface IUsuarioDAO {

    Usuario buscarPorNombreUsuario(String nombreUsuario)
            throws SQLException;

}
