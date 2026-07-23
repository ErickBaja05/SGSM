package com.grupo1.sgsm.inventarioYproductos.dto;

public class InfoProductoDTO {

    private String stock;
    private String categoria;

    public InfoProductoDTO() {
    }

    public InfoProductoDTO(String stock, String categoria) {
        this.stock = stock;
        this.categoria = categoria;
    }

    public InfoProductoDTO(int stock, String categoria) {
        this.stock = String.valueOf(stock);
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
