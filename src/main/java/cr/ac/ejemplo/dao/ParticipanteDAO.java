
package cr.ac.ejemplo.dao;

import cr.ac.ejemplo.database.ConexionBD;
import cr.ac.ejemplo.model.Participante;
import cr.ac.ejemplo.model.enums.EstadoUsuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ParticipanteDAO implements IParticipanteDAO {

    @Override
    public void insertar(Participante participante)
            throws SQLException {

        try (Connection conexion = ConexionBD.conectar()) {

            conexion.setAutoCommit(false);

            try {
                insertar(participante, conexion);
                conexion.commit();

            } catch (SQLException | RuntimeException e) {
                conexion.rollback();
                throw e;
            }
        }
    }

    @Override
    public void insertar(
            Participante participante,
            Connection conexion
    ) throws SQLException {

        String sqlPersona = "INSERT INTO persona "
                + "(identificacion, nombre, apellidos, correo, telefono, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        int idPersona;

        try (PreparedStatement ps = conexion.prepareStatement(
                sqlPersona, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, participante.getIdentificacion());
            ps.setString(2, participante.getNombre());
            ps.setString(3, participante.getApellidos());
            ps.setString(4, participante.getCorreo());
            ps.setString(5, participante.getTelefono());
            ps.setString(6, participante.getEstado().name());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {

                if (rs.next()) {
                    idPersona = rs.getInt(1);
                } else {
                    throw new SQLException(
                            "No se pudo obtener el ID de la persona."
                    );
                }
            }
        }

        participante.setIdPersona(idPersona);
        insertarRol(participante, conexion);
    }

    @Override
    public void insertarRol(
            Participante participante,
            Connection conexion
    ) throws SQLException {

        String sql = "INSERT INTO participante "
                + "(id_persona, fecha_registro) VALUES (?, ?)";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, participante.getIdPersona());

            ps.setDate(2, java.sql.Date.valueOf(
                    participante.getFechaRegistro()
            ));

            ps.executeUpdate();
        }
    }

    @Override
    public boolean existeRol(
            int idPersona,
            Connection conexion
    ) throws SQLException {

        String sql = "SELECT 1 FROM participante "
                + "WHERE id_persona = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idPersona);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public ArrayList<Participante> listarPorActividad(int idActividad)
            throws SQLException {

        ArrayList<Participante> participantes = new ArrayList<>();

        String sql = "SELECT p.id_persona, p.identificacion, "
                + "p.nombre, p.apellidos, p.correo, p.telefono, "
                + "p.estado, pa.fecha_registro "
                + "FROM persona p "
                + "INNER JOIN participante pa "
                + "ON p.id_persona = pa.id_persona "
                + "INNER JOIN inscripcion i "
                + "ON pa.id_persona = i.id_persona "
                + "WHERE i.id_actividad = ? "
                + "AND i.tipo = 'Participante' "
                + "AND i.estado = 'ACTIVA' "
                + "ORDER BY p.nombre";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idActividad);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Participante participante = new Participante(
                            rs.getInt("id_persona"),
                            rs.getString("identificacion"),
                            rs.getString("nombre"),
                            rs.getString("apellidos"),
                            rs.getString("correo"),
                            rs.getString("telefono"),
                            EstadoUsuario.valueOf(rs.getString("estado")),
                            rs.getDate("fecha_registro").toString()
                    );

                    participantes.add(participante);
                }
            }
        }

        return participantes;
    }

    @Override
    public Participante buscarPorId(int idPersona)
            throws SQLException {

        try (Connection conexion = ConexionBD.conectar()) {
            return ejecutarBusquedaPorId(idPersona, conexion);
        }
    }

    @Override
    public Participante buscarPorId(
            int idPersona,
            Connection conexion
    ) throws SQLException {

        return ejecutarBusquedaPorId(idPersona, conexion);
    }

    private Participante ejecutarBusquedaPorId(
            int idPersona,
            Connection conexion
    ) throws SQLException {

        String sql = "SELECT p.id_persona, p.identificacion, "
                + "p.nombre, p.apellidos, p.correo, p.telefono, "
                + "p.estado, pa.fecha_registro "
                + "FROM persona p "
                + "INNER JOIN participante pa "
                + "ON p.id_persona = pa.id_persona "
                + "WHERE p.id_persona = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idPersona);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return new Participante(
                            rs.getInt("id_persona"),
                            rs.getString("identificacion"),
                            rs.getString("nombre"),
                            rs.getString("apellidos"),
                            rs.getString("correo"),
                            rs.getString("telefono"),
                            EstadoUsuario.valueOf(rs.getString("estado")),
                            rs.getDate("fecha_registro").toString()
                    );
                }
            }
        }

        return null;
    }

    @Override
    public void actualizar(Participante participante)
            throws SQLException {

        String sql = "UPDATE persona SET "
                + "identificacion = ?, "
                + "nombre = ?, "
                + "apellidos = ?, "
                + "correo = ?, "
                + "telefono = ?, "
                + "estado = ? "
                + "WHERE id_persona = ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, participante.getIdentificacion());
            ps.setString(2, participante.getNombre());
            ps.setString(3, participante.getApellidos());
            ps.setString(4, participante.getCorreo());
            ps.setString(5, participante.getTelefono());
            ps.setString(6, participante.getEstado().name());
            ps.setInt(7, participante.getIdPersona());

            int filasActualizadas = ps.executeUpdate();

            if (filasActualizadas == 0) {
                throw new SQLException(
                        "No se encontró el participante para actualizar."
                );
            }
        }
    }
}
