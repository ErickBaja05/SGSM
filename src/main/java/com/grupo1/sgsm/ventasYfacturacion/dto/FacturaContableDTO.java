package com.grupo1.sgsm.ventasYfacturacion.dto;

public class FacturaContableDTO {
    private String numeroFactura;
    private String sucursal;
    private double subtotal;
    private double iva;
    private double total;
    private String metodoPago;

    public FacturaContableDTO() {}

    public FacturaContableDTO(String numeroFactura, String sucursal, double subtotal, double iva, String metodoPago) {
        this.numeroFactura = numeroFactura;
        this.sucursal = sucursal;
        this.subtotal = subtotal;
        this.iva = iva;
        this.total = subtotal + iva;
        this.metodoPago = metodoPago;
    }

    public String getNumeroFactura() { return numeroFactura; }
    public void setNumeroFactura(String numeroFactura) { this.numeroFactura = numeroFactura; }

    public String getSucursal() { return sucursal; }
    public void setSucursal(String sucursal) { this.sucursal = sucursal; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
        this.total = this.subtotal + this.iva;
    }

    public double getIva() { return iva; }
    public void setIva(double iva) {
        this.iva = iva;
        this.total = this.subtotal + this.iva;
    }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
}
