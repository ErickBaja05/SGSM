package com.grupo1.sgsm.administracion.gestionUsuarios.dto;

public class NuevoUsuarioDTO {
    String nombreUsuario;
    String rolUsuario;
    String password;
    String codigo_sucursal;
    String correo;

    public NuevoUsuarioDTO(String nombreUsuario, String rolUsuario, String password, String codigo_sucursal, String correo) {
        this.nombreUsuario = nombreUsuario;
        this.rolUsuario = rolUsuario;
        this.password = password;
        this.codigo_sucursal = codigo_sucursal;
        this.correo = correo;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getRolUsuario() {
        return rolUsuario;
    }

    public void setRolUsuario(String rolUsuario) {
        this.rolUsuario = rolUsuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCodigo_sucursal() {
        return codigo_sucursal;
    }

    public void setCodigo_sucursal(String codigo_sucursal) {
        this.codigo_sucursal = codigo_sucursal;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
