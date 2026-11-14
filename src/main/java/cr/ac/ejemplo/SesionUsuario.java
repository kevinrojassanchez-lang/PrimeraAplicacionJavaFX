
package cr.ac.ejemplo;

import cr.ac.ejemplo.model.Usuario;

public class SesionUsuario {

    private static Usuario usuarioActual;

    public static void iniciarSesion(Usuario usuario) {
        usuarioActual = usuario;
    }

    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public static boolean esAdministrador() {

        return usuarioActual != null
                && "ADMINISTRADOR".equalsIgnoreCase(
                        usuarioActual.getRol()
                );
    }

    public static boolean esOperador() {

        return usuarioActual != null
                && "OPERADOR".equalsIgnoreCase(
                        usuarioActual.getRol()
                );
    }

    public static void cerrarSesion() {
        usuarioActual = null;
    }
}

