
package cr.ac.ejemplo.dao;

import cr.ac.ejemplo.database.ConexionBD;
import cr.ac.ejemplo.model.Persona;
import cr.ac.ejemplo.model.enums.EstadoUsuario;

import java.sql.*;
import java.util.ArrayList;

public class PersonaDAO implements IPersonaDAO {

    @Override
    public void insertar(Persona persona) throws SQLException {

        try (Connection conexion = ConexionBD.conectar()) {

            conexion.setAutoCommit(false);

            try {
                insertar(persona, conexion);
                conexion.commit();

            } catch (SQLException | RuntimeException ex) {
                conexion.rollback();
                throw ex;
            }
        }
    }

    @Override
    public void insertar(
            Persona persona,
            Connection conexion
    ) throws SQLException {

        String sql = "INSERT INTO persona "
                + "(identificacion, nombre, apellidos, correo, telefono, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conexion.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, persona.getIdentificacion());
            ps.setString(2, persona.getNombre());
            ps.setString(3, persona.getApellidos());
            ps.setString(4, persona.getCorreo());
            ps.setString(5, persona.getTelefono());
            ps.setString(6, persona.getEstado().name());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {

                if (!rs.next()) {
                    throw new SQLException(
                            "No se pudo obtener el ID de la persona."
                    );
                }

                persona.setIdPersona(rs.getInt(1));
            }
        }
    }

    @Override
    public int buscarIdPorIdentificacion(
            String identificacion,
            Connection conexion
    ) throws SQLException {

        String sql = "SELECT id_persona FROM persona "
                + "WHERE identificacion = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, identificacion.trim());

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt("id_persona");
                }
            }
        }

        return 0;
    }

    @Override
    public ArrayList<Persona> listar() throws SQLException {

        ArrayList<Persona> personas = new ArrayList<>();

        String sql = "SELECT * FROM persona ORDER BY id_persona";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Persona persona = new Persona(
                        rs.getInt("id_persona"),
                        rs.getString("identificacion"),
                        rs.getString("nombre"),
                        rs.getString("apellidos"),
                        rs.getString("correo"),
                        rs.getString("telefono"),
                        EstadoUsuario.valueOf(rs.getString("estado"))
                );

                personas.add(persona);
            }
        }

        return personas;
    }

    @Override
    public Persona buscarPorId(int idPersona) throws SQLException {

        String sql = "SELECT * FROM persona WHERE id_persona = ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idPersona);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return new Persona(
                            rs.getInt("id_persona"),
                            rs.getString("identificacion"),
                            rs.getString("nombre"),
                            rs.getString("apellidos"),
                            rs.getString("correo"),
                            rs.getString("telefono"),
                            EstadoUsuario.valueOf(rs.getString("estado"))
                    );
                }
            }
        }

        return null;
    }

    @Override
    public void actualizar(Persona persona) throws SQLException {

        String sql = "UPDATE persona SET "
                + "identificacion = ?, nombre = ?, apellidos = ?, "
                + "correo = ?, telefono = ?, estado = ? "
                + "WHERE id_persona = ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, persona.getIdentificacion());
            ps.setString(2, persona.getNombre());
            ps.setString(3, persona.getApellidos());
            ps.setString(4, persona.getCorreo());
            ps.setString(5, persona.getTelefono());
            ps.setString(6, persona.getEstado().name());
            ps.setInt(7, persona.getIdPersona());

            ps.executeUpdate();
        }
    }
}
