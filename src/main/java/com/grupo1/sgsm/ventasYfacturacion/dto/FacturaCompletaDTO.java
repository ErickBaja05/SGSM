package com.grupo1.sgsm.ventasYfacturacion.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.grupo1.sgsm.ventasYfacturacion.model.DetalleFactura;

public class FacturaCompletaDTO {
    // Datos de la Factura
    private String numero_factura;
    private LocalDate fecha_emision;
    private String codigo_sucursal_factura;

    // Datos del Cliente
    private String cedula_cliente;
    private String primer_nombre;
    private String segundo_nombre;
    private String primer_apellido;
    private String segundo_apellido;

    // Lista de Detalles asociados
    private List<DetalleFactura> detalles = new ArrayList<>();

    public FacturaCompletaDTO() {}

    // Getters y Setters
    public String getNumero_factura() { return numero_factura; }
    public void setNumero_factura(String numero_factura) { this.numero_factura = numero_factura; }

    public LocalDate getFecha_emision() { return fecha_emision; }
    public void setFecha_emision(LocalDate fecha_emision) { this.fecha_emision = fecha_emision; }

    public String getCodigo_sucursal_factura() { return codigo_sucursal_factura; }
    public void setCodigo_sucursal_factura(String codigo_sucursal_factura) { this.codigo_sucursal_factura = codigo_sucursal_factura; }

    public String getCedula_cliente() { return cedula_cliente; }
    public void setCedula_cliente(String cedula_cliente) { this.cedula_cliente = cedula_cliente; }

    public String getPrimer_nombre() { return primer_nombre; }
    public void setPrimer_nombre(String primer_nombre) { this.primer_nombre = primer_nombre; }

    public String getSegundo_nombre() { return segundo_nombre; }
    public void setSegundo_nombre(String segundo_nombre) { this.segundo_nombre = segundo_nombre; }

    public String getPrimer_apellido() { return primer_apellido; }
    public void setPrimer_apellido(String primer_apellido) { this.primer_apellido = primer_apellido; }

    public String getSegundo_apellido() { return segundo_apellido; }
    public void setSegundo_apellido(String segundo_apellido) { this.segundo_apellido = segundo_apellido; }

    public List<DetalleFactura> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleFactura> detalles) { this.detalles = detalles; }
    public void addDetalle(DetalleFactura detalle) { this.detalles.add(detalle); }
}