package com.grupo1.sgsm.inventarioYproductos.dto;

public class ProductoMarketingDTO {
    private String codigo;
    private String nombre;
    private String descripcion;

    public ProductoMarketingDTO() {}

    public ProductoMarketingDTO(String codigo, String nombre, String descripcion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
