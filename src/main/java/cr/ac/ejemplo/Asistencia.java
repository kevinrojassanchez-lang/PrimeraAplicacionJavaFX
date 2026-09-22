package cr.ac.ejemplo;

public class Asistencia {

    private int idAsistencia;
    private Inscripcion inscripcion;
    private String estado;

    public Asistencia(int idAsistencia, Inscripcion inscripcion, String estado){

        this.idAsistencia = idAsistencia;
        this.inscripcion = inscripcion;
        this.estado = estado;
    }

    public int getIdAsistencia() {
        return idAsistencia;
    }

    public Inscripcion getInscripcion() {
        return inscripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setIdAsistencia(int idAsistencia) {
        this.idAsistencia = idAsistencia;
    }

    public void setInscripcion(Inscripcion inscripcion) {
        this.inscripcion = inscripcion;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return idAsistencia + " - " + estado;
    }
}
