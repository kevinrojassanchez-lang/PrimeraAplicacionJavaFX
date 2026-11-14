
package cr.ac.ejemplo.dao;

import cr.ac.ejemplo.database.ConexionBD;
import cr.ac.ejemplo.model.Inscripcion;
import cr.ac.ejemplo.model.Persona;
import cr.ac.ejemplo.model.Actividad;
import cr.ac.ejemplo.model.Evento;
import cr.ac.ejemplo.model.enums.EstadoInscripcion;
import cr.ac.ejemplo.model.enums.EstadoUsuario;
import cr.ac.ejemplo.model.enums.EstadoActividad;
import cr.ac.ejemplo.model.enums.EstadoEvento;

import java.sql.*;
import java.util.ArrayList;

public class InscripcionDAO implements IInscripcionDAO {

    private final String consultaBase =
            "SELECT "
            + "i.id_inscripcion, i.fecha AS fecha_inscripcion, "
            + "i.hora AS hora_inscripcion, i.tipo, "
            + "i.estado AS estado_inscripcion, "
            + "p.id_persona, p.identificacion, p.nombre, "
            + "p.apellidos, p.correo, p.telefono, "
            + "p.estado AS estado_persona, "
            + "a.id_actividad, a.nombre AS nombre_actividad, "
            + "a.descripcion AS descripcion_actividad, "
            + "a.fecha AS fecha_actividad, "
            + "a.hora_inicio, a.hora_fin, a.lugar, "
            + "a.capacidad_maxima, a.tipo AS tipo_actividad, "
            + "a.estado AS estado_actividad, "
            + "e.id_evento, e.nombre AS nombre_evento, "
            + "e.descripcion AS descripcion_evento, "
            + "e.fecha_inicio, e.fecha_fin, "
            + "e.lugar AS lugar_evento, "
            + "e.estado AS estado_evento "
            + "FROM inscripcion i "
            + "INNER JOIN persona p ON i.id_persona = p.id_persona "
            + "INNER JOIN actividad a ON i.id_actividad = a.id_actividad "
            + "INNER JOIN evento e ON a.id_evento = e.id_evento ";

    @Override
    public void insertar(Inscripcion inscripcion) throws SQLException {

        try (Connection conexion = ConexionBD.conectar()) {

            conexion.setAutoCommit(false);

            try {
                insertar(inscripcion, conexion);
                conexion.commit();

            } catch (SQLException | RuntimeException e) {
                conexion.rollback();
                throw e;
            }
        }
    }
    
    @Override
    public void insertar(
            Inscripcion inscripcion,
            Connection conexion
    ) throws SQLException {

        String sql = "INSERT INTO inscripcion "
                + "(id_persona, id_actividad, fecha, hora, tipo, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conexion.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, inscripcion.getPersona().getIdPersona());
            ps.setInt(2, inscripcion.getActividad().getIdActividad());
            ps.setDate(3, Date.valueOf(inscripcion.getFecha()));
            ps.setTime(4, Time.valueOf(convertirHora(inscripcion.getHora())));
            ps.setString(5, inscripcion.getTipo());
            ps.setString(6, inscripcion.getEstado().name());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {

                if (rs.next()) {
                    inscripcion.setIdInscripcion(rs.getInt(1));
                } else {
                    throw new SQLException(
                            "No se pudo obtener el ID de la inscripción."
                    );
                }
            }
        }
    }

    @Override
    public ArrayList<Inscripcion> listar() throws SQLException {

        ArrayList<Inscripcion> inscripciones = new ArrayList<>();

        String sql = consultaBase + "ORDER BY i.id_inscripcion";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                inscripciones.add(mapearInscripcion(rs));
            }
        }

        return inscripciones;
    }

    @Override
public ArrayList<Inscripcion> listarPorActividad(
        int idActividad) throws SQLException {

    try (Connection conexion = ConexionBD.conectar()) {
        return listarPorActividad(idActividad, conexion);
    }
}

    @Override
    public ArrayList<Inscripcion> listarPorActividad(
            int idActividad, Connection conexion) throws SQLException {

        ArrayList<Inscripcion> inscripciones = new ArrayList<>();

        String sql = consultaBase
                + "WHERE i.id_actividad = ? "
                + "AND i.estado = 'ACTIVA' "
                + "ORDER BY i.id_inscripcion";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idActividad);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    inscripciones.add(mapearInscripcion(rs));
                }
            }
        }

        return inscripciones;
    }

    @Override
    public Inscripcion buscarPorId(int idInscripcion)
            throws SQLException {

        String sql = consultaBase
                + "WHERE i.id_inscripcion = ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idInscripcion);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapearInscripcion(rs);
                }
            }
        }

        return null;
    }
    
    @Override
    public boolean existeInscripcionActiva(
            int idPersona,
            int idActividad,
            Connection conexion
        ) throws SQLException {

        String sql = "SELECT COUNT(*) "
                + "FROM inscripcion "
                + "WHERE id_persona = ? "
                + "AND id_actividad = ? "
                + "AND estado = 'ACTIVA'";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idPersona);
            ps.setInt(2, idActividad);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    private Inscripcion mapearInscripcion(ResultSet rs)
            throws SQLException {

        Persona persona = new Persona(
                rs.getInt("id_persona"),
                rs.getString("identificacion"),
                rs.getString("nombre"),
                rs.getString("apellidos"),
                rs.getString("correo"),
                rs.getString("telefono"),
                EstadoUsuario.valueOf(
                        rs.getString("estado_persona")
                )
        );

        Evento evento = new Evento(
                rs.getInt("id_evento"),
                rs.getString("nombre_evento"),
                rs.getString("descripcion_evento"),
                rs.getDate("fecha_inicio").toString(),
                rs.getDate("fecha_fin").toString(),
                rs.getString("lugar_evento"),
                EstadoEvento.valueOf(
                        rs.getString("estado_evento")
                )
        );

        Actividad actividad = new Actividad(
                rs.getInt("id_actividad"),
                rs.getString("nombre_actividad"),
                rs.getString("descripcion_actividad"),
                rs.getDate("fecha_actividad").toString(),
                rs.getTime("hora_inicio").toString().substring(0, 5),
                rs.getTime("hora_fin").toString().substring(0, 5),
                rs.getString("lugar"),
                rs.getInt("capacidad_maxima"),
                rs.getString("tipo_actividad"),
                EstadoActividad.valueOf(
                        rs.getString("estado_actividad")
                ),
                evento
        );

        return new Inscripcion(
                rs.getInt("id_inscripcion"),
                persona,
                actividad,
                rs.getDate("fecha_inscripcion").toString(),
                rs.getTime("hora_inscripcion").toString().substring(0, 5),
                rs.getString("tipo"),
                EstadoInscripcion.valueOf(
                        rs.getString("estado_inscripcion")
                )
        );
    }

    @Override
    public void actualizar(Inscripcion inscripcion)
            throws SQLException {

        String sql = "UPDATE inscripcion SET "
                + "id_persona = ?, id_actividad = ?, fecha = ?, "
                + "hora = ?, tipo = ?, estado = ? "
                + "WHERE id_inscripcion = ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, inscripcion.getPersona().getIdPersona());
            ps.setInt(2, inscripcion.getActividad().getIdActividad());
            ps.setDate(3, Date.valueOf(inscripcion.getFecha()));
            ps.setTime(4, Time.valueOf(convertirHora(inscripcion.getHora())));
            ps.setString(5, inscripcion.getTipo());
            ps.setString(6, inscripcion.getEstado().name());
            ps.setInt(7, inscripcion.getIdInscripcion());

            ps.executeUpdate();
        }
    }

    @Override
    public void cambiarEstado(
            int idInscripcion,
            EstadoInscripcion nuevoEstado
    ) throws SQLException {

        String sql = "UPDATE inscripcion "
                + "SET estado = ? "
                + "WHERE id_inscripcion = ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado.name());
            ps.setInt(2, idInscripcion);

            ps.executeUpdate();
        }
    }

    private String convertirHora(String hora) {

        if (hora.length() == 5) {
            return hora + ":00";
        }

        return hora;
    }
}
