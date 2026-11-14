
package cr.ac.ejemplo.dao;

import cr.ac.ejemplo.database.ConexionBD;
import cr.ac.ejemplo.model.Usuario;
import cr.ac.ejemplo.model.enums.EstadoUsuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO implements IUsuarioDAO {

    @Override
    public Usuario buscarPorNombreUsuario(String nombreUsuario)
            throws SQLException {

        String sql = "SELECT id_usuario, nombre_usuario, contrasena, rol, estado "
                + "FROM usuario WHERE nombre_usuario = ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setString(1, nombreUsuario);

            try (ResultSet resultado = consulta.executeQuery()) {

                if (resultado.next()) {

                    int id = resultado.getInt("id_usuario");
                    String nombre = resultado.getString("nombre_usuario");
                    String contrasena = resultado.getString("contrasena");
                    String rol = resultado.getString("rol");

                    EstadoUsuario estado = EstadoUsuario.valueOf(
                            resultado.getString("estado").toUpperCase()
                    );

                    return new Usuario(
                            id,
                            nombre,
                            contrasena,
                            rol,
                            estado
                    );
                }
            }
        }

        return null;
    }
}

