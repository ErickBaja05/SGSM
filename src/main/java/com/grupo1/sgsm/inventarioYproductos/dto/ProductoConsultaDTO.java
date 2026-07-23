package com.grupo1.sgsm.inventarioYproductos.dto;

public class ProductoConsultaDTO {
    private String codigo;
    private String nombre;
    private String marca;
    private String categoria;
    private double precio;

    public ProductoConsultaDTO() {}

    public ProductoConsultaDTO(String codigo, String nombre, String marca, String categoria, double precio) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.marca = marca;
        this.categoria = categoria;
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

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
}
