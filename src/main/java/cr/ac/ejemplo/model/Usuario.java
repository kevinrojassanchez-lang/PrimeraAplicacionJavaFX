
package cr.ac.ejemplo.model;

import cr.ac.ejemplo.model.enums.EstadoUsuario;

public class Usuario {

    private int id;
    private String nombreUsuario;
    private String contrasena;
    private String rol;
    private EstadoUsuario estado;

    public Usuario(int id, String nombreUsuario, String contrasena,
            String rol, EstadoUsuario estado) {

        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.rol = rol;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getRol() {
        return rol;
    }

    public EstadoUsuario getEstado() {
        return estado;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public void setEstado(EstadoUsuario estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return nombreUsuario + " - " + rol;
    }
}
