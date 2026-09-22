package cr.ac.ejemplo;

public class Inscripcion {

    private int idInscripcion;
    private Persona persona;
    private Actividad actividad;
    private String fecha;
    private String hora;
    private String tipo;
    private String estado;

    public Inscripcion(int idInscripcion, Persona persona, Actividad actividad,
            String fecha, String hora, String tipo, String estado) {

        this.idInscripcion = idInscripcion;
        this.persona = persona;
        this.actividad = actividad;
        this.fecha = fecha;
        this.hora = hora;
        this.tipo = tipo;
        this.estado = estado;
    }

    public int getIdInscripcion() {
        return idInscripcion;
    }

    public Persona getPersona() {
        return persona;
    }

    public Actividad getActividad() {
        return actividad;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public String getTipo() {
        return tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setIdInscripcion(int idInscripcion) {
        this.idInscripcion = idInscripcion;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return idInscripcion + " - " + persona + " - " + actividad;
    }
}
