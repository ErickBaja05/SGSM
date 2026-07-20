package com.grupo1.sgsm.ventasYfacturacion.model;
import com.grupo1.sgsm.inventarioYproductos.model.Producto;
// Clase auxiliar DetalleVenta
public class DetalleVenta {
    private Producto producto; private int cantidad;
    public DetalleVenta(Producto p, int c) { this.producto = p; this.cantidad = c; }
    public Producto getProducto() { return producto; } public int getCantidad() { return cantidad; }
    public void setCantidad(int c) { this.cantidad = c; }
}
