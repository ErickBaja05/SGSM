package com.grupo1.sgsm.administracion.gestionParametros.dto;

public class SucursalDTO {
    String codigo_sucursal;
    String direccion;
    String nombre;
    String ciudad;
    String telefono;

    public SucursalDTO(String codigo_sucursal, String direccion, String nombre, String ciudad, String telefono) {
        this.codigo_sucursal = codigo_sucursal;
        this.direccion = direccion;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.telefono = telefono;
    }

    public String getCodigo_sucursal() {
        return codigo_sucursal;
    }

    public void setCodigo_sucursal(String codigo_sucursal) {
        this.codigo_sucursal = codigo_sucursal;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
}
