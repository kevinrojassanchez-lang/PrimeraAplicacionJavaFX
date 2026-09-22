package cr.ac.ejemplo;

public class EventoEjemplo {

    private String codigo;
    private String nombre;
    private String fechaInicio;
    private String fechaFinal;
    private String lugar;
    private String estado;

    public EventoEjemplo(
            String codigo,
            String nombre,
            String fechaInicio,
            String fechaFinal,
            String lugar,
            String estado) {

        this.codigo = codigo;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFinal = fechaFinal;
        this.lugar = lugar;
        this.estado = estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public String getFechaFinal() {
        return fechaFinal;
    }

    public String getLugar() {
        return lugar;
    }

    public String getEstado() {
        return estado;
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre;
    }
}
