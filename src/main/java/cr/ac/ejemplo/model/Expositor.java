
package cr.ac.ejemplo.model;

import cr.ac.ejemplo.model.enums.EstadoUsuario;

public class Expositor extends Persona {

    private String especialidad;
    private String descripcionProfesional;

    public Expositor(int idPersona, String identificacion, String nombre,
            String apellidos, String correo, String telefono,
            EstadoUsuario estado, String especialidad,
            String descripcionProfesional) {

        super(idPersona, identificacion, nombre, apellidos, correo, telefono,
                estado);

        this.especialidad = especialidad;
        this.descripcionProfesional = descripcionProfesional;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public String getDescripcionProfesional() {
        return descripcionProfesional;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public void setDescripcionProfesional(String descripcionProfesional) {
        this.descripcionProfesional = descripcionProfesional;
    }

    @Override
    public String toString() {
        return getIdPersona() + " - "
                + getIdentificacion() + " - "
                + getNombre() + " "
                + getApellidos();
    }
}
