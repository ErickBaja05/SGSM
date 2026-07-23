package com.grupo1.sgsm.ventasYfacturacion.dto;

public class DetalleFacturaDTO {

    private String codigo_producto;
    private String descripcion;
    private String codigo_sucursal;
    private Integer cantidad;
    private Double precio_unitario;
    private Double subtotal_producto;

    public DetalleFacturaDTO() {
    }

    // Constructor de 5 parámetros (Original)
    public DetalleFacturaDTO(String codigo_producto, String codigo_sucursal, Integer cantidad, Double precio_unitario, Double subtotal_producto) {
        this.codigo_producto = codigo_producto;
        this.codigo_sucursal = codigo_sucursal;
        this.cantidad = cantidad;
        this.precio_unitario = precio_unitario;
        this.subtotal_producto = subtotal_producto;
    }

    // Constructor de 4 parámetros (Usado por FacturaUIO_ContableDAO)
    public DetalleFacturaDTO(String codigo_producto, String descripcion, Integer cantidad, Double precio_unitario) {
        this.codigo_producto = codigo_producto;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.precio_unitario = precio_unitario;
        if (cantidad != null && precio_unitario != null) {
            this.subtotal_producto = cantidad * precio_unitario;
        }
    }

    // Constructor de 6 parámetros
    public DetalleFacturaDTO(String codigo_producto, String descripcion, String codigo_sucursal, Integer cantidad, Double precio_unitario, Double subtotal_producto) {
        this.codigo_producto = codigo_producto;
        this.descripcion = descripcion;
        this.codigo_sucursal = codigo_sucursal;
        this.cantidad = cantidad;
        this.precio_unitario = precio_unitario;
        this.subtotal_producto = subtotal_producto;
    }

    // --- Aliases y Getters CamelCase ---
    public String getCodigo() {
        return codigo_producto;
    }

    public void setCodigo(String codigo) {
        this.codigo_producto = codigo;
    }

    public String getDescripcion() {
        return descripcion != null ? descripcion : "Producto Desconocido";
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecioUnitario() {
        return precio_unitario != null ? precio_unitario : 0.0;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precio_unitario = precioUnitario;
    }

    public Double getSubtotal() {
        return getSubtotal_producto();
    }

    // --- Getters y Setters Estándar ---
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
        return cantidad != null ? cantidad : 0;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecio_unitario() {
        return precio_unitario != null ? precio_unitario : 0.0;
    }

    public void setPrecio_unitario(Double precio_unitario) {
        this.precio_unitario = precio_unitario;
    }

    public Double getSubtotal_producto() {
        if (subtotal_producto != null) return subtotal_producto;
        if (cantidad != null && precio_unitario != null) return cantidad * precio_unitario;
        return 0.0;
    }

    public void setSubtotal_producto(Double subtotal_producto) {
        this.subtotal_producto = subtotal_producto;
    }
}