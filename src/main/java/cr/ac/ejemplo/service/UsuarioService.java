
package cr.ac.ejemplo.service;

import cr.ac.ejemplo.dao.IUsuarioDAO;
import cr.ac.ejemplo.model.Usuario;

import java.sql.SQLException;

public class UsuarioService {

    private final IUsuarioDAO usuarioDAO;

    public UsuarioService(IUsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public Usuario iniciarSesion(String nombreUsuario, String contrasena)
            throws SQLException {

        if (nombreUsuario == null || nombreUsuario.isBlank()
                || contrasena == null || contrasena.isBlank()) {
            return null;
        }

        Usuario usuario = usuarioDAO.buscarPorNombreUsuario(nombreUsuario);

        if (usuario == null) {
            return null;
        }

        if (!usuario.getContrasena().equals(contrasena)) {
            return null;
        }

        if (usuario.getEstado() == null
                || !usuario.getEstado().name().equalsIgnoreCase("ACTIVO")) {
            return null;
        }

        return usuario;
    }
}

