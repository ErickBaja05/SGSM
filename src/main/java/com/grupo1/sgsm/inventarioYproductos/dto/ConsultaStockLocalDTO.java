package com.grupo1.sgsm.inventarioYproductos.dto;

public class ConsultaStockLocalDTO {
    
    private String codigo_producto;
    private String nombre_producto;
    private int stock;
    private String codigo_sucursal;

    public ConsultaStockLocalDTO() {
    }

    public ConsultaStockLocalDTO(String codigo_producto, String nombre_producto, int stock, String codigo_sucursal) {
        this.codigo_producto = codigo_producto;
        this.nombre_producto = nombre_producto;
        this.stock = stock;
        this.codigo_sucursal = codigo_sucursal;
    }

    public String getCodigo_producto() {
        return codigo_producto;
    }

    public void setCodigo_producto(String codigo_producto) {
        this.codigo_producto = codigo_producto;
    }

    public String getNombre_producto() {
        return nombre_producto;
    }

    public void setNombre_producto(String nombre_producto) {
        this.nombre_producto = nombre_producto;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getCodigo_sucursal() {
        return codigo_sucursal;
    }

    public void setCodigo_sucursal(String codigo_sucursal) {
        this.codigo_sucursal = codigo_sucursal;
    }
}
