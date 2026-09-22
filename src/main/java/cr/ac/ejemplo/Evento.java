package cr.ac.ejemplo;

public class Evento {

    private int idEvento;
    private String nombre;
    private String descripcion;
    private String fechaInicio;
    private String fechaFin;
    private String lugar;
    private String estado;

    public Evento(int idEvento, String nombre, String descripcion,
            String fechaInicio, String fechaFin, String lugar, String estado) {

        this.idEvento = idEvento;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.lugar = lugar;
        this.estado = estado;
    }

    public int getIdEvento() {
        return idEvento;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public String getLugar() {
        return lugar;
    }

    public String getEstado() {
        return estado;
    }

    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return idEvento + " - " + nombre;
    }
}
