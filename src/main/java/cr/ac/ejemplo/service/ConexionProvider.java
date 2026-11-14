package cr.ac.ejemplo.service;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface ConexionProvider {

    Connection conectar() throws SQLException;
}
