package com.grupo1.sgsm.inventarioYproductos.dto;

public class NuevoProductoDTO {
    private String codigo;
    private String nombre;
    private String marca;
    private String categoria;
    private String descripcion;
    private double precio;

    public NuevoProductoDTO() {}

    public NuevoProductoDTO(String codigo, String nombre, String marca, String categoria, String descripcion, double precio) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.marca = marca;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
}
