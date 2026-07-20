package com.grupo1.sgsm.clientes.model;

public class ClienteContacto {
    private String cedula_ciudadania;
    private String correo_electronico;
    private String direccion;
    private String cod_sucursal_registro;

    public String getCedula_ciudadania() {
        return cedula_ciudadania;
    }

    public void setCedula_ciudadania(String cedula_ciudadania) {
        this.cedula_ciudadania = cedula_ciudadania;
    }

    public String getCorreo_electronico() {
        return correo_electronico;
    }

    public void setCorreo_electronico(String correo_electronico) {
        this.correo_electronico = correo_electronico;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCod_sucursal_registro() {
        return cod_sucursal_registro;
    }

    public void setCod_sucursal_registro(String cod_sucursal_registro) {
        this.cod_sucursal_registro = cod_sucursal_registro;
    }
}
