package com.grupo1.sgsm.inventarioYproductos.dto;

public class NuevoStockLocalDTO {
    private String codigo_sucursal;
    private String codigo_producto;
    private int stock;   

    public NuevoStockLocalDTO() {
    }

    public NuevoStockLocalDTO(String codigo_sucursal, String codigo_producto, int stock) {
        this.codigo_sucursal = codigo_sucursal;
        this.codigo_producto = codigo_producto;
        this.stock = stock;
    }

    public String getCodigo_sucursal() {
        return codigo_sucursal;
    }

    public void setCodigo_sucursal(String codigo_sucursal) {
        this.codigo_sucursal = codigo_sucursal;
    }

    public String getCodigo_producto() {
        return codigo_producto;
    }

    public void setCodigo_producto(String codigo_producto) {
        this.codigo_producto = codigo_producto;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
