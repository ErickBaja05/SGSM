package com.grupo1.sgsm.ventasYfacturacion.dto;

public class FacturaOperativaDTO {
    private String numeroFactura;
    private String fechaEmision;
    private String cedulaCliente;
    private String nombreCliente;

    public FacturaOperativaDTO() {}

    public FacturaOperativaDTO(String numeroFactura, String fechaEmision, String cedulaCliente, String nombreCliente) {
        this.numeroFactura = numeroFactura;
        this.fechaEmision = fechaEmision;
        this.cedulaCliente = cedulaCliente;
        this.nombreCliente = nombreCliente;
    }

    public String getNumeroFactura() { return numeroFactura; }
    public void setNumeroFactura(String numeroFactura) { this.numeroFactura = numeroFactura; }

    public String getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(String fechaEmision) { this.fechaEmision = fechaEmision; }

    public String getCedulaCliente() { return cedulaCliente; }
    public void setCedulaCliente(String cedulaCliente) { this.cedulaCliente = cedulaCliente; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
}
