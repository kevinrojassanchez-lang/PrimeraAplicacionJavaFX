
package cr.ac.ejemplo.dao;

import cr.ac.ejemplo.database.ConexionBD;
import cr.ac.ejemplo.model.Expositor;
import cr.ac.ejemplo.model.enums.EstadoUsuario;

import java.sql.*;
import java.util.ArrayList;

public class ExpositorDAO implements IExpositorDAO {

    @Override
    public void insertar(Expositor expositor) throws SQLException {

        try (Connection conexion = ConexionBD.conectar()) {

            conexion.setAutoCommit(false);

            try {
                insertar(expositor, conexion);
                conexion.commit();

            } catch (SQLException | RuntimeException e) {
                conexion.rollback();
                throw e;
            }
        }
    }

    @Override
    public void insertar(
            Expositor expositor,
            Connection conexion
    ) throws SQLException {

        String sqlPersona = "INSERT INTO persona "
                + "(identificacion, nombre, apellidos, correo, telefono, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conexion.prepareStatement(
                sqlPersona, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, expositor.getIdentificacion());
            ps.setString(2, expositor.getNombre());
            ps.setString(3, expositor.getApellidos());
            ps.setString(4, expositor.getCorreo());
            ps.setString(5, expositor.getTelefono());
            ps.setString(6, expositor.getEstado().name());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {

                if (!rs.next()) {
                    throw new SQLException(
                            "No se pudo obtener el ID de la persona."
                    );
                }

                expositor.setIdPersona(rs.getInt(1));
            }
        }

        insertarRol(expositor, conexion);
    }

    @Override
    public void insertarRol(
            Expositor expositor,
            Connection conexion
    ) throws SQLException {

        String sql = "INSERT INTO expositor "
                + "(id_persona, especialidad, descripcion_profesional) "
                + "VALUES (?, ?, ?)";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, expositor.getIdPersona());
            ps.setString(2, expositor.getEspecialidad());
            ps.setString(3, expositor.getDescripcionProfesional());

            ps.executeUpdate();
        }
    }

    @Override
    public boolean existeRol(
            int idPersona,
            Connection conexion
    ) throws SQLException {

        String sql = "SELECT 1 FROM expositor WHERE id_persona = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idPersona);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public ArrayList<Expositor> listar() throws SQLException {

        ArrayList<Expositor> expositores = new ArrayList<>();

        String sql = "SELECT p.*, e.especialidad, "
                + "e.descripcion_profesional "
                + "FROM persona p "
                + "INNER JOIN expositor e ON p.id_persona = e.id_persona "
                + "ORDER BY p.id_persona";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Expositor expositor = new Expositor(
                        rs.getInt("id_persona"),
                        rs.getString("identificacion"),
                        rs.getString("nombre"),
                        rs.getString("apellidos"),
                        rs.getString("correo"),
                        rs.getString("telefono"),
                        EstadoUsuario.valueOf(rs.getString("estado")),
                        rs.getString("especialidad"),
                        rs.getString("descripcion_profesional")
                );

                expositores.add(expositor);
            }
        }

        return expositores;
    }

    @Override
    public ArrayList<Expositor> listarPorActividad(int idActividad)
            throws SQLException {

        ArrayList<Expositor> expositores = new ArrayList<>();

        String sql = "SELECT p.*, e.especialidad, "
                + "e.descripcion_profesional "
                + "FROM persona p "
                + "INNER JOIN expositor e ON p.id_persona = e.id_persona "
                + "INNER JOIN inscripcion i ON p.id_persona = i.id_persona "
                + "WHERE i.id_actividad = ? "
                + "AND i.tipo = 'Expositor' "
                + "AND i.estado = 'ACTIVA' "
                + "ORDER BY p.nombre, p.apellidos";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idActividad);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Expositor expositor = new Expositor(
                            rs.getInt("id_persona"),
                            rs.getString("identificacion"),
                            rs.getString("nombre"),
                            rs.getString("apellidos"),
                            rs.getString("correo"),
                            rs.getString("telefono"),
                            EstadoUsuario.valueOf(rs.getString("estado")),
                            rs.getString("especialidad"),
                            rs.getString("descripcion_profesional")
                    );

                    expositores.add(expositor);
                }
            }
        }

        return expositores;
    }

    @Override
    public Expositor buscarPorId(int idPersona) throws SQLException {

        String sql = "SELECT p.*, e.especialidad, "
                + "e.descripcion_profesional "
                + "FROM persona p "
                + "INNER JOIN expositor e ON p.id_persona = e.id_persona "
                + "WHERE p.id_persona = ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idPersona);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return new Expositor(
                            rs.getInt("id_persona"),
                            rs.getString("identificacion"),
                            rs.getString("nombre"),
                            rs.getString("apellidos"),
                            rs.getString("correo"),
                            rs.getString("telefono"),
                            EstadoUsuario.valueOf(rs.getString("estado")),
                            rs.getString("especialidad"),
                            rs.getString("descripcion_profesional")
                    );
                }
            }
        }

        return null;
    }

    @Override
    public void actualizar(Expositor expositor) throws SQLException {

        String sqlPersona = "UPDATE persona SET "
                + "identificacion = ?, nombre = ?, apellidos = ?, "
                + "correo = ?, telefono = ?, estado = ? "
                + "WHERE id_persona = ?";

        String sqlExpositor = "UPDATE expositor SET "
                + "especialidad = ?, descripcion_profesional = ? "
                + "WHERE id_persona = ?";

        try (Connection conexion = ConexionBD.conectar()) {

            conexion.setAutoCommit(false);

            try {
                try (PreparedStatement ps = conexion.prepareStatement(
                        sqlPersona)) {

                    ps.setString(1, expositor.getIdentificacion());
                    ps.setString(2, expositor.getNombre());
                    ps.setString(3, expositor.getApellidos());
                    ps.setString(4, expositor.getCorreo());
                    ps.setString(5, expositor.getTelefono());
                    ps.setString(6, expositor.getEstado().name());
                    ps.setInt(7, expositor.getIdPersona());

                    ps.executeUpdate();
                }

                try (PreparedStatement ps = conexion.prepareStatement(
                        sqlExpositor)) {

                    ps.setString(1, expositor.getEspecialidad());
                    ps.setString(2, expositor.getDescripcionProfesional());
                    ps.setInt(3, expositor.getIdPersona());

                    int filas = ps.executeUpdate();

                    if (filas == 0) {
                        throw new SQLException(
                                "No se encontró el expositor para actualizar."
                        );
                    }
                }

                conexion.commit();

            } catch (SQLException | RuntimeException e) {
                conexion.rollback();
                throw e;
            }
        }
    }
}
