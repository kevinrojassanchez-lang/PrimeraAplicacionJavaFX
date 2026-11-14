
package cr.ac.ejemplo;

import cr.ac.ejemplo.dao.*;
import cr.ac.ejemplo.database.ConexionBD;
import cr.ac.ejemplo.service.ActividadService;
import cr.ac.ejemplo.service.ConexionProvider;
import cr.ac.ejemplo.service.ConsultaService;
import cr.ac.ejemplo.service.EventoService;
import cr.ac.ejemplo.service.InscripcionService;
import cr.ac.ejemplo.service.ReporteService;
import cr.ac.ejemplo.service.AsistenciaService;
import cr.ac.ejemplo.service.ParticipanteService;
import cr.ac.ejemplo.service.ExpositorService;
import cr.ac.ejemplo.service.UsuarioService;

public class DAOFactory {

    public static IEventoDAO crearEventoDAO() {
        return new EventoDAO();
    }

    public static EventoService crearEventoService() {
        return new EventoService(crearEventoDAO());
    }

    public static IActividadDAO crearActividadDAO() {
        return new ActividadDAO();
    }

    public static ActividadService crearActividadService() {
        return new ActividadService(crearActividadDAO());
    }

    public static IPersonaDAO crearPersonaDAO() {
        return new PersonaDAO();
    }

    public static IParticipanteDAO crearParticipanteDAO() {
        return new ParticipanteDAO();
    }

    public static IExpositorDAO crearExpositorDAO() {
        return new ExpositorDAO();
    }

    public static IInscripcionDAO crearInscripcionDAO() {
        return new InscripcionDAO();
    }

    public static IAsistenciaDAO crearAsistenciaDAO() {
        return new AsistenciaDAO();
    }
    
    public static ParticipanteService crearParticipanteService() {
        return new ParticipanteService(crearParticipanteDAO());
    }
    
    public static ExpositorService crearExpositorService() {
        return new ExpositorService(crearExpositorDAO());
    }

    public static ConsultaService crearConsultaService() {
        return new ConsultaService(
                crearEventoDAO(),
                crearActividadDAO(),
                crearInscripcionDAO()
        );
    }

    public static ReporteService crearReporteService() {
        return new ReporteService(
                crearActividadDAO(),
                crearPersonaDAO(),
                crearInscripcionDAO(),
                crearAsistenciaDAO()
        );
    }
    
    public static AsistenciaService crearAsistenciaService() {
        return new AsistenciaService(
                crearAsistenciaDAO(),
                crearInscripcionDAO()
        );
    }

    public static InscripcionService crearInscripcionService() {

        ConexionProvider conexionProvider = ConexionBD::conectar;

        return new InscripcionService(
                crearInscripcionDAO(),
                crearParticipanteDAO(),
                crearExpositorDAO(),
                crearActividadDAO(),
                crearPersonaDAO(),
                conexionProvider
        );
    }
    
    public static IUsuarioDAO crearUsuarioDAO() {
        return new UsuarioDAO();
    }

    public static UsuarioService crearUsuarioService() {
        return new UsuarioService(crearUsuarioDAO());
    }
}
