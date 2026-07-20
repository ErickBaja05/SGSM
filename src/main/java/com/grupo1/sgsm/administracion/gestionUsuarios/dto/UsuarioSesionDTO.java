package com.grupo1.sgsm.administracion.gestionUsuarios.dto;

public class UsuarioSesionDTO {
    private int id;
    private String nombre_us;
    private String rol;
    private String codigo_sucursal;

    public UsuarioSesionDTO(int id, String nombre_us, String rol, String codigo_sucursal) {
        this.id = id;
        this.nombre_us = nombre_us;
        this.rol = rol;
        this.codigo_sucursal = codigo_sucursal;
    }

    public String getCodigo_sucursal() {
        return codigo_sucursal;
    }

    public void setCodigo_sucursal(String codigo_sucursal) {
        this.codigo_sucursal = codigo_sucursal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre_us() {
        return nombre_us;
    }

    public void setNombre_us(String nombre_us) {
        this.nombre_us = nombre_us;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
