
package cr.ac.ejemplo.dao;

import cr.ac.ejemplo.database.ConexionBD;
import cr.ac.ejemplo.model.Evento;
import cr.ac.ejemplo.model.enums.EstadoEvento;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EventoDAO implements IEventoDAO {

    // Registrar un evento
    @Override
    public boolean insertar(Evento evento) {

        String sql = "INSERT INTO evento "
                + "(nombre, descripcion, fecha_inicio, fecha_fin, lugar, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, evento.getNombre());
            ps.setString(2, evento.getDescripcion());
            ps.setDate(3, Date.valueOf(evento.getFechaInicio()));
            ps.setDate(4, Date.valueOf(evento.getFechaFin()));
            ps.setString(5, evento.getLugar());
            ps.setString(6, evento.getEstado().name());

            return ps.executeUpdate() > 0;

        } catch (SQLException | IllegalArgumentException e) {
            System.out.println("Error al insertar evento: " + e.getMessage());
            return false;
        }
    }

    // Consultar todos los eventos
    @Override
    public ArrayList<Evento> listar() {

        ArrayList<Evento> eventos = new ArrayList<>();

        String sql = "SELECT * FROM evento ORDER BY id_evento";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Evento evento = new Evento(
                        rs.getInt("id_evento"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getDate("fecha_inicio").toString(),
                        rs.getDate("fecha_fin").toString(),
                        rs.getString("lugar"),
                        EstadoEvento.valueOf(rs.getString("estado"))
                );

                eventos.add(evento);
            }

        } catch (SQLException | IllegalArgumentException e) {
            System.out.println("Error al listar eventos: " + e.getMessage());
        }

        return eventos;
    }

    // Buscar evento por ID
    @Override
    public Evento buscarPorId(int idEvento) {

        String sql = "SELECT * FROM evento WHERE id_evento = ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idEvento);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return new Evento(
                            rs.getInt("id_evento"),
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getDate("fecha_inicio").toString(),
                            rs.getDate("fecha_fin").toString(),
                            rs.getString("lugar"),
                            EstadoEvento.valueOf(rs.getString("estado"))
                    );
                }
            }

        } catch (SQLException | IllegalArgumentException e) {
            System.out.println("Error al buscar evento: " + e.getMessage());
        }

        return null;
    }
    
    @Override
    // Actualizar evento
    public boolean actualizar(Evento evento) {

        String sql = "UPDATE evento SET nombre = ?, descripcion = ?, "
                + "fecha_inicio = ?, fecha_fin = ?, lugar = ?, estado = ? "
                + "WHERE id_evento = ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, evento.getNombre());
            ps.setString(2, evento.getDescripcion());
            ps.setDate(3, Date.valueOf(evento.getFechaInicio()));
            ps.setDate(4, Date.valueOf(evento.getFechaFin()));
            ps.setString(5, evento.getLugar());
            ps.setString(6, evento.getEstado().name());
            ps.setInt(7, evento.getIdEvento());

            return ps.executeUpdate() > 0;

        } catch (SQLException | IllegalArgumentException e) {
            System.out.println("Error al actualizar evento: " + e.getMessage());
            return false;
        }
    }

    // Eliminar evento
    @Override
    public boolean eliminar(int idEvento) {

        String sql = "DELETE FROM evento WHERE id_evento = ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idEvento);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar evento: " + e.getMessage());
            return false;
        }
    }
}

