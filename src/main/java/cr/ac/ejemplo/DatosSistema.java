package cr.ac.ejemplo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DatosSistema {

    private static final ObservableList<Evento> eventos =
            FXCollections.observableArrayList();

    private static final ObservableList<Actividad> actividades =
            FXCollections.observableArrayList();

    private static final ObservableList<Participante> participantes =
            FXCollections.observableArrayList();

    private static final ObservableList<Expositor> expositores =
            FXCollections.observableArrayList();

    private static final ObservableList<Inscripcion> inscripciones =
            FXCollections.observableArrayList();

    private static final ObservableList<Asistencia> asistencias =
            FXCollections.observableArrayList();

    public static ObservableList<Evento> getEventos() {
        return eventos;
    }

    public static ObservableList<Actividad> getActividades() {
        return actividades;
    }

    public static ObservableList<Participante> getParticipantes() {
        return participantes;
    }

    public static ObservableList<Expositor> getExpositores() {
        return expositores;
    }

    public static ObservableList<Inscripcion> getInscripciones() {
        return inscripciones;
    }

    public static ObservableList<Asistencia> getAsistencias() {
        return asistencias;
    }
}
