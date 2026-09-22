package cr.ac.ejemplo;

public class Participante extends Persona {

    private String fechaRegistro;

    public Participante(int idPersona, String identificacion, String nombre,
            String apellidos, String correo, String telefono, String estado,
            String fechaRegistro) {

        super(idPersona, identificacion, nombre, apellidos, correo, telefono,
                estado);

        this.fechaRegistro = fechaRegistro;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    @Override
    public String toString() {
        return getIdPersona() + " - "
                + getIdentificacion() + " - "
                + getNombre() + " "
                + getApellidos();
    }
}
