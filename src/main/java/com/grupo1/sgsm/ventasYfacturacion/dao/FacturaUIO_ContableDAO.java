package com.grupo1.sgsm.ventasYfacturacion.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.grupo1.sgsm.core.database.DatabaseConnection;
import com.grupo1.sgsm.core.session.SesionActual;
import com.grupo1.sgsm.core.util.ConfigSucursal;
import com.grupo1.sgsm.administracion.gestionUsuarios.dto.UsuarioSesionDTO;
import com.grupo1.sgsm.ventasYfacturacion.dto.DetalleFacturaDTO;
import com.grupo1.sgsm.ventasYfacturacion.dto.FacturaContableDTO;

public class FacturaUIO_ContableDAO {

    private void validarSesion() {
        UsuarioSesionDTO usuario = SesionActual.getUsuario();
        if (usuario == null) {
            throw new RuntimeException("No hay sesión activa");
        }
    }

    private String mapearNombreSucursal(String codigoSucursal) {
        if (codigoSucursal == null) return "";
        return codigoSucursal.trim().toUpperCase();
    }

    private FacturaContableDTO mapearFacturaContable(ResultSet rs) throws SQLException {
        return new FacturaContableDTO(
                rs.getString("numero_factura").trim(),
                mapearNombreSucursal(rs.getString("codigo_sucursal")),
                rs.getDouble("subtotal"),
                rs.getDouble("IVA"),
                rs.getString("metodo_pago") != null ? rs.getString("metodo_pago").trim() : ""
        );
    }

    public List<FacturaContableDTO> consultarTodasFacturasContables() {
        validarSesion();
        String nodoLocal = ConfigSucursal.getSucursalActual();
        String prefijo = obtenerPrefijoUIO(nodoLocal);

        String sql = String.format("""
            SELECT c.numero_factura, c.codigo_sucursal, c.subtotal, c.IVA, c.total, c.metodo_pago
            FROM %sfacturaUIO_contable c
            ORDER BY c.numero_factura DESC
            """, prefijo);

        List<FacturaContableDTO> facturas = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection(nodoLocal.toLowerCase());
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                facturas.add(mapearFacturaContable(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar todas las facturas contables UIO", e);
        }

        return facturas;
    }

    public List<FacturaContableDTO> consultarFacturasContables(LocalDate inicio, LocalDate fin) {
        validarSesion();
        if (inicio == null || fin == null) {
            return consultarTodasFacturasContables();
        }

        String nodoLocal = ConfigSucursal.getSucursalActual();
        String prefijo = obtenerPrefijoUIO(nodoLocal);

        String sql = String.format("""
            SELECT c.numero_factura, c.codigo_sucursal, c.subtotal, c.IVA, c.total, c.metodo_pago
            FROM %sfacturaUIO_contable c
            JOIN %sfacturaUIO_operativo o ON c.numero_factura = o.numero_factura
            WHERE CAST(o.fecha_emision AS DATE) BETWEEN ? AND ?
            ORDER BY o.fecha_emision DESC
            """, prefijo, prefijo);

        List<FacturaContableDTO> facturas = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection(nodoLocal.toLowerCase());
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(inicio));
            ps.setDate(2, Date.valueOf(fin));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    facturas.add(mapearFacturaContable(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al filtrar facturas contables UIO por fecha", e);
        }

        return facturas;
    }

    public List<DetalleFacturaDTO> consultarDetallesFactura(String numeroFactura) {
        validarSesion();
        String nodoLocal = ConfigSucursal.getSucursalActual();
        String prefijo = obtenerPrefijoUIO(nodoLocal);

        String sql = String.format("""
            SELECT d.codigo_producto, p.nombre AS descripcion, d.cantidad, d.precio_unitario
            FROM %sdetalle_facturaUIO d
            LEFT JOIN %sproducto_info p ON d.codigo_producto = p.codigo_producto
            WHERE d.numero_factura = ?
            """, prefijo, prefijo);

        List<DetalleFacturaDTO> detalles = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection(nodoLocal.toLowerCase());
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, numeroFactura);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    detalles.add(new DetalleFacturaDTO(
                            rs.getString("codigo_producto").trim(),
                            rs.getString("descripcion") != null ? rs.getString("descripcion").trim() : "Producto Desconocido",
                            rs.getInt("cantidad"),
                            rs.getDouble("precio_unitario")
                    ));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar detalles de factura contable UIO", e);
        }

        return detalles;
    }

    // ===============================
    // MÉTODOS AUXILIARES DE ENRUTAMIENTO
    // ===============================
    private String obtenerPrefijoUIO(String nodoLocal) {
        if ("UIO".equalsIgnoreCase(nodoLocal)) {
            return "UIO.dbo.";
        } else {
            return "[26.194.51.93].UIO.dbo.";
        }
    }
}
