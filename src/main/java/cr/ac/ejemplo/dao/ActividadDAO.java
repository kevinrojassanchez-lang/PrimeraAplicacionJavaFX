package cr.ac.ejemplo.dao;

import cr.ac.ejemplo.database.ConexionBD;
import cr.ac.ejemplo.model.Actividad;
import cr.ac.ejemplo.model.Evento;
import cr.ac.ejemplo.model.enums.EstadoActividad;
import cr.ac.ejemplo.model.enums.EstadoEvento;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;

public class ActividadDAO implements IActividadDAO {

    @Override
    public void insertar(Actividad actividad) throws SQLException {

        String sql = "INSERT INTO actividad "
                + "(id_evento, nombre, descripcion, fecha, hora_inicio, "
                + "hora_fin, lugar, capacidad_maxima, tipo, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, actividad.getEvento().getIdEvento());
            ps.setString(2, actividad.getNombre());
            ps.setString(3, actividad.getDescripcion());
            ps.setDate(4, Date.valueOf(actividad.getFecha()));
            ps.setTime(5, convertirHora(actividad.getHoraInicio()));
            ps.setTime(6, convertirHora(actividad.getHoraFin()));
            ps.setString(7, actividad.getLugar());
            ps.setInt(8, actividad.getCapacidadMaxima());
            ps.setString(9, actividad.getTipo());
            ps.setString(10, actividad.getEstado().name());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    actividad.setIdActividad(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public ArrayList<Actividad> listar() throws SQLException {

        ArrayList<Actividad> actividades = new ArrayList<>();

        String sql = "SELECT a.*, "
                + "e.nombre AS nombre_evento, "
                + "e.descripcion AS descripcion_evento, "
                + "e.fecha_inicio, e.fecha_fin, "
                + "e.lugar AS lugar_evento, "
                + "e.estado AS estado_evento "
                + "FROM actividad a "
                + "INNER JOIN evento e ON a.id_evento = e.id_evento "
                + "ORDER BY a.id_actividad";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Evento evento = new Evento(
                        rs.getInt("id_evento"),
                        rs.getString("nombre_evento"),
                        rs.getString("descripcion_evento"),
                        rs.getDate("fecha_inicio").toString(),
                        rs.getDate("fecha_fin").toString(),
                        rs.getString("lugar_evento"),
                        EstadoEvento.valueOf(rs.getString("estado_evento"))
                );

                Actividad actividad = new Actividad(
                        rs.getInt("id_actividad"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getDate("fecha").toString(),
                        rs.getTime("hora_inicio").toString().substring(0, 5),
                        rs.getTime("hora_fin").toString().substring(0, 5),
                        rs.getString("lugar"),
                        rs.getInt("capacidad_maxima"),
                        rs.getString("tipo"),
                        EstadoActividad.valueOf(rs.getString("estado")),
                        evento
                );

                actividades.add(actividad);
            }
        }

        return actividades;
    }

    @Override
    public Actividad buscarPorId(int idActividad) throws SQLException {

        String sql = "SELECT a.*, "
                + "e.nombre AS nombre_evento, "
                + "e.descripcion AS descripcion_evento, "
                + "e.fecha_inicio, e.fecha_fin, "
                + "e.lugar AS lugar_evento, "
                + "e.estado AS estado_evento "
                + "FROM actividad a "
                + "INNER JOIN evento e ON a.id_evento = e.id_evento "
                + "WHERE a.id_actividad = ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idActividad);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Evento evento = new Evento(
                            rs.getInt("id_evento"),
                            rs.getString("nombre_evento"),
                            rs.getString("descripcion_evento"),
                            rs.getDate("fecha_inicio").toString(),
                            rs.getDate("fecha_fin").toString(),
                            rs.getString("lugar_evento"),
                            EstadoEvento.valueOf(rs.getString("estado_evento"))
                    );

                    return new Actividad(
                            rs.getInt("id_actividad"),
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getDate("fecha").toString(),
                            rs.getTime("hora_inicio").toString().substring(0, 5),
                            rs.getTime("hora_fin").toString().substring(0, 5),
                            rs.getString("lugar"),
                            rs.getInt("capacidad_maxima"),
                            rs.getString("tipo"),
                            EstadoActividad.valueOf(rs.getString("estado")),
                            evento
                    );
                }
            }
        }

        return null;
    }

    @Override
    public void actualizar(Actividad actividad) throws SQLException {

        String sql = "UPDATE actividad SET "
                + "id_evento = ?, nombre = ?, descripcion = ?, "
                + "fecha = ?, hora_inicio = ?, hora_fin = ?, "
                + "lugar = ?, capacidad_maxima = ?, tipo = ?, estado = ? "
                + "WHERE id_actividad = ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, actividad.getEvento().getIdEvento());
            ps.setString(2, actividad.getNombre());
            ps.setString(3, actividad.getDescripcion());
            ps.setDate(4, Date.valueOf(actividad.getFecha()));
            ps.setTime(5, convertirHora(actividad.getHoraInicio()));
            ps.setTime(6, convertirHora(actividad.getHoraFin()));
            ps.setString(7, actividad.getLugar());
            ps.setInt(8, actividad.getCapacidadMaxima());
            ps.setString(9, actividad.getTipo());
            ps.setString(10, actividad.getEstado().name());
            ps.setInt(11, actividad.getIdActividad());

            ps.executeUpdate();
        }
    }

    private Time convertirHora(String hora) {

        if (hora.length() == 5) {
            hora = hora + ":00";
        }

        return Time.valueOf(hora);
    }
    
    @Override
    public Actividad buscarPorId(
            int idActividad,
            Connection conexion) throws SQLException {

        String sql = "SELECT a.*, "
                + "e.nombre AS nombre_evento, "
                + "e.descripcion AS descripcion_evento, "
                + "e.fecha_inicio, e.fecha_fin, "
                + "e.lugar AS lugar_evento, "
                + "e.estado AS estado_evento "
                + "FROM actividad a "
                + "INNER JOIN evento e ON a.id_evento = e.id_evento "
                + "WHERE a.id_actividad = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idActividad);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Evento evento = new Evento(
                            rs.getInt("id_evento"),
                            rs.getString("nombre_evento"),
                            rs.getString("descripcion_evento"),
                            rs.getDate("fecha_inicio").toString(),
                            rs.getDate("fecha_fin").toString(),
                            rs.getString("lugar_evento"),
                            EstadoEvento.valueOf(rs.getString("estado_evento"))
                    );

                    return new Actividad(
                            rs.getInt("id_actividad"),
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getDate("fecha").toString(),
                            rs.getTime("hora_inicio").toString().substring(0, 5),
                            rs.getTime("hora_fin").toString().substring(0, 5),
                            rs.getString("lugar"),
                            rs.getInt("capacidad_maxima"),
                            rs.getString("tipo"),
                            EstadoActividad.valueOf(rs.getString("estado")),
                            evento
                    );
                }
            }
        }

        return null;
    }
}
