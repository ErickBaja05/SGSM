package com.grupo1.sgsm.administracion.gestionUsuarios.dto;

public class UsuarioSesionDTO {
    private int id;
    private String nombre_us;
    private String perfil_us;

    public UsuarioSesionDTO(int id, String nombre_us, String perfil_us) {
        this.id = id;
        this.nombre_us = nombre_us;
        this.perfil_us = perfil_us;
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

    public String getPerfil_us() {
        return perfil_us;
    }

    public void setPerfil_us(String perfil_us) {
        this.perfil_us = perfil_us;
    }
}
