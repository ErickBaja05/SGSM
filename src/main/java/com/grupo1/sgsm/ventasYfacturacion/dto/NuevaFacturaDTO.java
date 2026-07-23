package com.grupo1.sgsm.ventasYfacturacion.dto;

import java.time.LocalDate;

public class NuevaFacturaDTO {
    private String numero_factura;
    private String cedula_ciudadania;
    private String codigo_sucursal;
    private LocalDate fecha_emision;
    private Double total;
    private String metodo_pago;
    private Double subtotal;
    private Double IVA;

    public NuevaFacturaDTO(String numero_factura, String cedula_ciudadania, String codigo_sucursal, LocalDate fecha_emision, Double total, String metodo_pago, Double subtotal, Double IVA) {
        this.numero_factura = numero_factura;
        this.cedula_ciudadania = cedula_ciudadania;
        this.codigo_sucursal = codigo_sucursal;
        this.fecha_emision = fecha_emision;
        this.total = total;
        this.metodo_pago = metodo_pago;
        this.subtotal = subtotal;
        this.IVA = IVA;
    }

    public String getNumero_factura() {
        return numero_factura;
    }

    public void setNumero_factura(String numero_factura) {
        this.numero_factura = numero_factura;
    }

    public String getCedula_ciudadania() {
        return cedula_ciudadania;
    }

    public void setCedula_ciudadania(String cedula_ciudadania) {
        this.cedula_ciudadania = cedula_ciudadania;
    }

    public String getCodigo_sucursal() {
        return codigo_sucursal;
    }

    public void setCodigo_sucursal(String codigo_sucursal) {
        this.codigo_sucursal = codigo_sucursal;
    }

    public LocalDate getFecha_emision() {
        return fecha_emision;
    }

    public void setFecha_emision(LocalDate fecha_emision) {
        this.fecha_emision = fecha_emision;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getMetodo_pago() {
        return metodo_pago;
    }

    public void setMetodo_pago(String metodo_pago) {
        this.metodo_pago = metodo_pago;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getIVA() {
        return IVA;
    }

    public void setIVA(Double IVA) {
        this.IVA = IVA;
    }
}
