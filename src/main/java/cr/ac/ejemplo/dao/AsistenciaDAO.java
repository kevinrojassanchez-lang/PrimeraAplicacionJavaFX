
package cr.ac.ejemplo.dao;

import cr.ac.ejemplo.database.ConexionBD;
import cr.ac.ejemplo.model.Asistencia;
import cr.ac.ejemplo.model.Inscripcion;
import cr.ac.ejemplo.model.Persona;
import cr.ac.ejemplo.model.Actividad;
import cr.ac.ejemplo.model.Evento;
import cr.ac.ejemplo.model.enums.EstadoAsistencia;
import cr.ac.ejemplo.model.enums.EstadoInscripcion;
import cr.ac.ejemplo.model.enums.EstadoUsuario;
import cr.ac.ejemplo.model.enums.EstadoActividad;
import cr.ac.ejemplo.model.enums.EstadoEvento;

import java.sql.*;
import java.util.ArrayList;

public class AsistenciaDAO implements IAsistenciaDAO {

    @Override
    public ArrayList<Asistencia> listarPorActividad(
            int idActividad) throws SQLException {

        ArrayList<Asistencia> asistencias = new ArrayList<>();

        String sql = "SELECT "
                + "a.id_asistencia, a.estado AS estado_asistencia, "
                + "i.id_inscripcion, i.fecha AS fecha_inscripcion, "
                + "i.hora AS hora_inscripcion, i.tipo, "
                + "i.estado AS estado_inscripcion, "
                + "p.id_persona, p.identificacion, p.nombre, "
                + "p.apellidos, p.correo, p.telefono, "
                + "p.estado AS estado_persona, "
                + "ac.id_actividad, ac.nombre AS nombre_actividad, "
                + "ac.descripcion AS descripcion_actividad, "
                + "ac.fecha AS fecha_actividad, "
                + "ac.hora_inicio, ac.hora_fin, ac.lugar, "
                + "ac.capacidad_maxima, ac.tipo AS tipo_actividad, "
                + "ac.estado AS estado_actividad, "
                + "e.id_evento, e.nombre AS nombre_evento, "
                + "e.descripcion AS descripcion_evento, "
                + "e.fecha_inicio, e.fecha_fin, "
                + "e.lugar AS lugar_evento, "
                + "e.estado AS estado_evento "
                + "FROM asistencia a "
                + "INNER JOIN inscripcion i "
                + "ON a.id_inscripcion = i.id_inscripcion "
                + "INNER JOIN persona p "
                + "ON i.id_persona = p.id_persona "
                + "INNER JOIN actividad ac "
                + "ON i.id_actividad = ac.id_actividad "
                + "INNER JOIN evento e "
                + "ON ac.id_evento = e.id_evento "
                + "WHERE i.id_actividad = ? "
                + "AND i.estado = 'ACTIVA' "
                + "ORDER BY a.id_asistencia";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idActividad);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

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

                    Inscripcion inscripcion = new Inscripcion(
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

                    Asistencia asistencia = new Asistencia(
                            rs.getInt("id_asistencia"),
                            inscripcion,
                            EstadoAsistencia.valueOf(
                                    rs.getString("estado_asistencia")
                            )
                    );

                    asistencias.add(asistencia);
                }
            }
        }

        return asistencias;
    }

    
    @Override
    public Asistencia buscarPorInscripcion(
            int idInscripcion) throws SQLException {

        String sql = "SELECT "
                + "a.id_asistencia, a.estado AS estado_asistencia, "
                + "i.id_inscripcion, i.fecha AS fecha_inscripcion, "
                + "i.hora AS hora_inscripcion, i.tipo, "
                + "i.estado AS estado_inscripcion, "
                + "p.id_persona, p.identificacion, p.nombre, "
                + "p.apellidos, p.correo, p.telefono, "
                + "p.estado AS estado_persona, "
                + "ac.id_actividad, ac.nombre AS nombre_actividad, "
                + "ac.descripcion AS descripcion_actividad, "
                + "ac.fecha AS fecha_actividad, "
                + "ac.hora_inicio, ac.hora_fin, ac.lugar, "
                + "ac.capacidad_maxima, ac.tipo AS tipo_actividad, "
                + "ac.estado AS estado_actividad, "
                + "e.id_evento, e.nombre AS nombre_evento, "
                + "e.descripcion AS descripcion_evento, "
                + "e.fecha_inicio, e.fecha_fin, "
                + "e.lugar AS lugar_evento, "
                + "e.estado AS estado_evento "
                + "FROM asistencia a "
                + "INNER JOIN inscripcion i "
                + "ON a.id_inscripcion = i.id_inscripcion "
                + "INNER JOIN persona p "
                + "ON i.id_persona = p.id_persona "
                + "INNER JOIN actividad ac "
                + "ON i.id_actividad = ac.id_actividad "
                + "INNER JOIN evento e "
                + "ON ac.id_evento = e.id_evento "
                + "WHERE i.id_inscripcion = ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idInscripcion);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

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

                    Inscripcion inscripcion = new Inscripcion(
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

                    return new Asistencia(
                            rs.getInt("id_asistencia"),
                            inscripcion,
                            EstadoAsistencia.valueOf(
                                    rs.getString("estado_asistencia")
                            )
                    );
                }
            }
        }

        return null;
    }


    @Override
    public void insertar(Asistencia asistencia)
            throws SQLException {

        String sql = "INSERT INTO asistencia "
                + "(id_inscripcion, estado) VALUES (?, ?)";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(
                    1,
                    asistencia.getInscripcion().getIdInscripcion()
            );

            ps.setString(2, asistencia.getEstado().name());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {

                if (rs.next()) {
                    asistencia.setIdAsistencia(rs.getInt(1));
                } else {
                    throw new SQLException(
                            "No se pudo obtener el ID de la asistencia."
                    );
                }
            }
        }
    }

    @Override
    public void actualizar(Asistencia asistencia)
            throws SQLException {

        String sql = "UPDATE asistencia "
                + "SET estado = ? "
                + "WHERE id_asistencia = ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, asistencia.getEstado().name());

            ps.setInt(2, asistencia.getIdAsistencia());

            int filas = ps.executeUpdate();

            if (filas == 0) {
                throw new SQLException(
                        "No se encontró la asistencia para actualizar."
                );
            }
        }
    }
}
