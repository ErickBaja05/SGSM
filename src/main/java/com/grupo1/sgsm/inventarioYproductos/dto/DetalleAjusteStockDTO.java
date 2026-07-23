package com.grupo1.sgsm.inventarioYproductos.dto;

public class DetalleAjusteStockDTO {
    private String codigo_producto;
    private String nombre;
    private String categoria;
    private String codigo_sucursal;
    private int stock;

    public DetalleAjusteStockDTO() {
    }

    public DetalleAjusteStockDTO(String codigo_producto, String nombre, String categoria, String codigo_sucursal, int stock) {
        this.codigo_producto = codigo_producto;
        this.nombre = nombre;
        this.categoria = categoria;
        this.codigo_sucursal = codigo_sucursal;
        this.stock = stock;
    }

    public String getCodigo_producto() {
        return codigo_producto;
    }

    public void setCodigo_producto(String codigo_producto) {
        this.codigo_producto = codigo_producto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCodigo_sucursal() {
        return codigo_sucursal;
    }

    public void setCodigo_sucursal(String codigo_sucursal) {
        this.codigo_sucursal = codigo_sucursal;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
