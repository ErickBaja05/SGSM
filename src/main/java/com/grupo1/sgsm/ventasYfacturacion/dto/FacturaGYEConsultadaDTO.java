package com.grupo1.sgsm.ventasYfacturacion.dto;

public class FacturaGYEConsultadaDTO {
    private String numero_factura;
    private String fecha_emision;
    private String cedula_cliente;

    public FacturaGYEConsultadaDTO(String numero_factura, String fecha_emision, String cedula_cliente) {
        this.numero_factura = numero_factura;
        this.fecha_emision = fecha_emision;
        this.cedula_cliente = cedula_cliente;
    }

    public String getNumero_factura() {
        return numero_factura;
    }

    public void setNumero_factura(String numero_factura) {
        this.numero_factura = numero_factura;
    }

    public String getFecha_emision() {
        return fecha_emision;
    }

    public void setFecha_emision(String fecha_emision) {
        this.fecha_emision = fecha_emision;
    }

    public String getCedula_cliente() {
        return cedula_cliente;
    }

    public void setCedula_cliente(String cedula_cliente) {
        this.cedula_cliente = cedula_cliente;
    }
}
