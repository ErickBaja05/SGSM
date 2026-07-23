package com.grupo1.sgsm.ventasYfacturacion.dto;

public class InfoProductoDTO {
    String stock;
    String categoria;

    public InfoProductoDTO(String stock, String categoria) {
        this.stock = stock;
        this.categoria = categoria;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
