package com.grupo1.sgsm.core.session;


import com.grupo1.sgsm.administracion.gestionUsuarios.dto.UsuarioSesionDTO;

public class SesionActual {

    private static UsuarioSesionDTO usuario;
    private static Double valorIva = 0.15;

    private SesionActual() {
        // Evita instanciación
    }

    public static void iniciarSesion(UsuarioSesionDTO usuarioSesion) {
        usuario = usuarioSesion;
    }

    public static UsuarioSesionDTO getUsuario() {
        return usuario;
    }

    public static boolean haySesion() {
        return usuario != null;
    }

    public static void cerrarSesion() {
        usuario = null;
    }

    public static Double getValorIva() {
        return valorIva;
    }

    public static void setValorIva(Double valorIva) {
        SesionActual.valorIva = valorIva;
    }
}
