
package cr.ac.ejemplo.model;

import cr.ac.ejemplo.model.enums.EstadoAsistencia;

public class Asistencia {

    private int idAsistencia;
    private Inscripcion inscripcion;
    private EstadoAsistencia estado;

    public Asistencia(int idAsistencia, Inscripcion inscripcion,
            EstadoAsistencia estado) {

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

    public EstadoAsistencia getEstado() {
        return estado;
    }

    public void setIdAsistencia(int idAsistencia) {
        this.idAsistencia = idAsistencia;
    }

    public void setInscripcion(Inscripcion inscripcion) {
        this.inscripcion = inscripcion;
    }

    public void setEstado(EstadoAsistencia estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return idAsistencia + " - " + estado;
    }
}
