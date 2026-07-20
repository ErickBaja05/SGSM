package com.grupo1.sgsm.ventasYfacturacion.model;

public class DetalleFactura {
    private String numero_factura;
    private String codigo_producto;
    private String codigo_sucursal;
    private Integer cantidad;
    private Double precio_unitario;
    private Double subtotal_producto;

    public String getNumero_factura() {
        return numero_factura;
    }

    public void setNumero_factura(String numero_factura) {
        this.numero_factura = numero_factura;
    }

    public String getCodigo_producto() {
        return codigo_producto;
    }

    public void setCodigo_producto(String codigo_producto) {
        this.codigo_producto = codigo_producto;
    }

    public String getCodigo_sucursal() {
        return codigo_sucursal;
    }

    public void setCodigo_sucursal(String codigo_sucursal) {
        this.codigo_sucursal = codigo_sucursal;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecio_unitario() {
        return precio_unitario;
    }

    public void setPrecio_unitario(Double precio_unitario) {
        this.precio_unitario = precio_unitario;
    }

    public Double getSubtotal_producto() {
        return subtotal_producto;
    }

    public void setSubtotal_producto(Double subtotal_producto) {
        this.subtotal_producto = subtotal_producto;
    }
}
