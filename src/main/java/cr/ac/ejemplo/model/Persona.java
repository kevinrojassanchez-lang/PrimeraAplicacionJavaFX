
package cr.ac.ejemplo.model;

import cr.ac.ejemplo.model.enums.EstadoUsuario;

public class Persona {

    private int idPersona;
    private String identificacion;
    private String nombre;
    private String apellidos;
    private String correo;
    private String telefono;
    private EstadoUsuario estado;

    public Persona(int idPersona, String identificacion, String nombre,
            String apellidos, String correo, String telefono,
            EstadoUsuario estado) {

        this.idPersona = idPersona;
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
        this.telefono = telefono;
        this.estado = estado;
    }

    public int getIdPersona() {
        return idPersona;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public EstadoUsuario getEstado() {
        return estado;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setEstado(EstadoUsuario estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return idPersona + " - "
                + identificacion + " - "
                + nombre + " "
                + apellidos;
    }
}
