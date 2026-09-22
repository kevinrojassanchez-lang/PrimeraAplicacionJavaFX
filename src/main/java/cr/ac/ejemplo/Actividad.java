package cr.ac.ejemplo;

public class Actividad {

    private int idActividad;
    private String nombre;
    private String descripcion;
    private String fecha;
    private String horaInicio;
    private String horaFin;
    private String lugar;
    private int capacidadMaxima;
    private String tipo;
    private String estado;
    private Evento evento;

    public Actividad(int idActividad, String nombre, String descripcion,
            String fecha, String horaInicio, String horaFin,String lugar,
            int capacidadMaxima, String tipo, String estado, Evento evento) {

        this.idActividad = idActividad;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.lugar = lugar;
        this.capacidadMaxima = capacidadMaxima;
        this.tipo = tipo;
        this.estado = estado;
        this.evento = evento;
    }

    public int getIdActividad() {
        return idActividad;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public String getLugar() {
        return lugar;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public String getTipo() {
        return tipo;
    }

    public String getEstado() {
        return estado;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setIdActividad(int idActividad) {
        this.idActividad = idActividad;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public void setCapacidadMaxima(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    @Override
    public String toString() {
        return idActividad + " - " + nombre;
    }
}
