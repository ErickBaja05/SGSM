package com.grupo1.sgsm.ventasYfacturacion.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.grupo1.sgsm.ventasYfacturacion.model.DetalleFactura;
import com.grupo1.sgsm.ventasYfacturacion.dto.FacturaCompletaDTO;
import com.grupo1.sgsm.ventasYfacturacion.dto.DetalleFacturaDTO;
import com.grupo1.sgsm.core.database.DatabaseConnection;
import com.grupo1.sgsm.core.database.NetworkChecker;
import com.grupo1.sgsm.core.util.ConfigSucursal;

public class DetalleFacturaDAO {

    // ===============================
    // MÉTODO PRIVADO DE MAPEO SIMPLE
    // ===============================
    private DetalleFactura mapearDetalleFactura(ResultSet rs) throws SQLException {
        DetalleFactura df = new DetalleFactura();
        df.setNumero_factura(rs.getString("numero_factura"));
        df.setCodigo_producto(rs.getString("codigo_producto"));
        df.setCodigo_sucursal(rs.getString("codigo_sucursal"));
        df.setCantidad(rs.getInt("cantidad"));
        df.setPrecio_unitario(rs.getDouble("precio_unitario"));
        df.setSubtotal_producto(rs.getDouble("subtotal_producto"));
        return df;
    }

    // ===============================
    // INSERTAR
    // ===============================
    public void insertar(DetalleFactura detalle) {
        String nodoLocal = ConfigSucursal.getSucursalActual();
        String sql = String.format("""
            INSERT INTO %s (numero_factura, codigo_producto, codigo_sucursal, cantidad, precio_unitario, subtotal_producto)
            VALUES (?, ?, ?, ?, ?, ?)
            """, obtenerTablaOVistaLocal(nodoLocal));

        try (Connection conn = DatabaseConnection.getConnection(nodoLocal);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, detalle.getNumero_factura());
            ps.setString(2, detalle.getCodigo_producto());
            ps.setString(3, detalle.getCodigo_sucursal());
            ps.setInt(4, detalle.getCantidad());
            ps.setDouble(5, detalle.getPrecio_unitario());
            ps.setDouble(6, detalle.getSubtotal_producto());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar detalle de factura", e);
        }
    }

    // ===============================
    // CONSULTA SIMPLE: SOLO DETALLES
    // ===============================
    public List<DetalleFactura> consultarPorNumero(String numeroFactura) {
        String nodoLocal = ConfigSucursal.getSucursalActual();
        String sql = String.format("SELECT * FROM %s WHERE numero_factura = ?", obtenerTablaOVistaLocal(nodoLocal));

        List<DetalleFactura> detalles = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection(nodoLocal);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, numeroFactura);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    detalles.add(mapearDetalleFactura(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar detalles de la factura", e);
        }

        return detalles;
    }

    // ===============================
    // CONSULTA COMPLEJA: JOIN (DTO)
    // ===============================
    public FacturaCompletaDTO consultarPorNumeroConJoin(String numeroFactura) {
        String nodoLocal = ConfigSucursal.getSucursalActual();

        // Tablas dinámicas según la arquitectura requerida
        String tablaDetalle = obtenerTablaOVistaLocal(nodoLocal);
        String tablaFactura = obtenerTablaFactura(nodoLocal);
        String tablaCliente = nodoLocal.toUpperCase() + ".dbo.cliente_facturacion"; // Siempre local

        String sql = String.format("""
            SELECT 
                f.numero_factura, f.fecha_emision, f.codigo_sucursal AS sucursal_factura,
                c.cedula_ciudadania, c.primer_nombre, c.segundo_nombre, c.primer_apellido, c.segundo_apellido,
                df.codigo_producto, df.codigo_sucursal AS sucursal_detalle, df.cantidad, df.precio_unitario, df.subtotal_producto
            FROM %s f
            INNER JOIN %s c ON f.cedula_ciudadania = c.cedula_ciudadania
            INNER JOIN %s df ON f.numero_factura = df.numero_factura
            WHERE f.numero_factura = ?
            """, tablaFactura, tablaCliente, tablaDetalle);

        FacturaCompletaDTO facturaDTO = null;

        try (Connection conn = DatabaseConnection.getConnection(nodoLocal);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, numeroFactura);
            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    // Si es el primer registro, inicializamos el objeto cabecera (Factura + Cliente)
                    if (facturaDTO == null) {
                        facturaDTO = new FacturaCompletaDTO();
                        facturaDTO.setNumero_factura(rs.getString("numero_factura"));
                        facturaDTO.setCodigo_sucursal_factura(rs.getString("sucursal_factura"));
                        if (rs.getDate("fecha_emision") != null) {
                            facturaDTO.setFecha_emision(rs.getDate("fecha_emision").toLocalDate());
                        }

                        facturaDTO.setCedula_cliente(rs.getString("cedula_ciudadania"));
                        facturaDTO.setPrimer_nombre(rs.getString("primer_nombre"));
                        facturaDTO.setSegundo_nombre(rs.getString("segundo_nombre"));
                        facturaDTO.setPrimer_apellido(rs.getString("primer_apellido"));
                        facturaDTO.setSegundo_apellido(rs.getString("segundo_apellido"));
                    }

                    // Por cada fila, añadimos un detalle a la lista
                    DetalleFactura df = new DetalleFactura();
                    df.setNumero_factura(rs.getString("numero_factura"));
                    df.setCodigo_producto(rs.getString("codigo_producto"));
                    df.setCodigo_sucursal(rs.getString("sucursal_detalle"));
                    df.setCantidad(rs.getInt("cantidad"));
                    df.setPrecio_unitario(rs.getDouble("precio_unitario"));
                    df.setSubtotal_producto(rs.getDouble("subtotal_producto"));

                    facturaDTO.addDetalle(df);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar la factura completa con JOINs", e);
        }

        return facturaDTO;
    }

    // ===============================
    // CONSULTA DE DETALLES CON PRODUCTO (DTO)
    // ===============================
    public List<DetalleFacturaDTO> consultarDetallesPorNumeroFactura(String numeroFactura) {
        String nodoLocal = ConfigSucursal.getSucursalActual();
        String tablaDetalle = obtenerTablaOVistaLocal(nodoLocal);
        String tablaProducto = obtenerTablaProductoInfo(nodoLocal);

        String sql = String.format("""
            SELECT d.codigo_producto, p.nombre AS descripcion, d.cantidad, d.precio_unitario
            FROM %s d
            LEFT JOIN %s p ON d.codigo_producto = p.codigo_producto
            WHERE d.numero_factura = ?
            """, tablaDetalle, tablaProducto);

        List<DetalleFacturaDTO> detalles = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection(nodoLocal);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, numeroFactura);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    detalles.add(new DetalleFacturaDTO(
                            rs.getString("codigo_producto") != null ? rs.getString("codigo_producto").trim() : "",
                            rs.getString("descripcion") != null ? rs.getString("descripcion").trim() : "Producto Desconocido",
                            rs.getInt("cantidad"),
                            rs.getDouble("precio_unitario")
                    ));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar detalles de la factura GYE", e);
        }

        return detalles;
    }

    // ===============================
    // MÉTODOS AUXILIARES DE ENRUTAMIENTO
    // ===============================
    private String obtenerTablaProductoInfo(String nodoLocal) {
        if ("UIO".equalsIgnoreCase(nodoLocal)) {
            return "UIO.dbo.producto_info";
        } else if ("GYE".equalsIgnoreCase(nodoLocal)) {
            if (verificarConectividad()) {
                return "[26.194.51.93].UIO.dbo.producto_info";
            }
            return "GYE.dbo.producto_info";
        }
        return nodoLocal.toUpperCase() + ".dbo.producto_info";
    }

    private String obtenerTablaOVistaLocal(String nodoLocal) {
        if (verificarConectividad()) {
            return nodoLocal.toUpperCase() + ".dbo.V_detalleFactura";
        }
        return nodoLocal.toUpperCase() + ".dbo.detalle_factura" + nodoLocal.toUpperCase();
    }

    private String obtenerTablaFactura(String nodoLocal) {
        // Enrutamiento forzado hacia el nodo de GYE
        if ("GYE".equalsIgnoreCase(nodoLocal)) {
            return "GYE.dbo.facturaGYE";
        } else {
            return "[26.34.243.93].GYE.dbo.facturaGYE";
        }
    }

    private boolean verificarConectividad() {
        if (ConfigSucursal.getSucursalActual().equalsIgnoreCase("UIO")) {
            return NetworkChecker.hayConexionGYE();
        }
        return NetworkChecker.hayConexionUIO();
    }
}