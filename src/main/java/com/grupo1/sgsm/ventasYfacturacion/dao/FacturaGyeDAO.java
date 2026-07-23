package com.grupo1.sgsm.ventasYfacturacion.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.grupo1.sgsm.ventasYfacturacion.model.FacturaGYE;
import com.grupo1.sgsm.core.database.DatabaseConnection;
import com.grupo1.sgsm.core.session.SesionActual;
import com.grupo1.sgsm.administracion.gestionUsuarios.dto.UsuarioSesionDTO;
import com.grupo1.sgsm.core.util.ConfigSucursal;

public class FacturaGyeDAO {

    // ===============================
    // MÉTODO PRIVADO DE MAPEO
    // ===============================
    private FacturaGYE mapearFactura(ResultSet rs) throws SQLException {
        FacturaGYE f = new FacturaGYE();
        f.setNumero_factura(rs.getString("numero_factura"));
        f.setCedula_ciudadania(rs.getString("cedula_ciudadania"));
        f.setCodigo_sucursal(rs.getString("codigo_sucursal"));

        // Transformación de java.sql.Date a java.time.LocalDate
        if (rs.getDate("fecha_emision") != null) {
            f.setFecha_emision(rs.getDate("fecha_emision").toLocalDate());
        }

        // Auto-boxing de double primitivo a Double
        f.setTotal(rs.getDouble("total"));
        f.setMetodo_pago(rs.getString("metodo_pago"));
        f.setSubtotal(rs.getDouble("subtotal"));
        f.setIVA(rs.getDouble("IVA"));
        return f;
    }

    // ===============================
    // INSERTAR FACTURA
    // ===============================
    public void insertar(FacturaGYE factura) {
        validarSesion();
        String nodoLocal = ConfigSucursal.getSucursalActual();

        String sql = String.format("""
            INSERT INTO %s (numero_factura, cedula_ciudadania, codigo_sucursal, fecha_emision, total, metodo_pago, subtotal, IVA)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """, obtenerTablaGye(nodoLocal));

        try (Connection conn = DatabaseConnection.getConnection(nodoLocal);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, factura.getNumero_factura());
            ps.setString(2, factura.getCedula_ciudadania());
            ps.setString(3, factura.getCodigo_sucursal());

            // Transformación de LocalDate a java.sql.Date para el PreparedStatement
            ps.setDate(4, java.sql.Date.valueOf(factura.getFecha_emision()));

            ps.setDouble(5, factura.getTotal());
            ps.setString(6, factura.getMetodo_pago());
            ps.setDouble(7, factura.getSubtotal());
            ps.setDouble(8, factura.getIVA());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar la factura en GYE", e);
        }
    }

    // ===============================
    // CONSULTAR POR NÚMERO DE FACTURA
    // ===============================
    public FacturaGYE consultarPorNumero(String numeroFactura) {
        validarSesion();
        String nodoLocal = ConfigSucursal.getSucursalActual();
        String sql = "SELECT * FROM " + obtenerTablaGye(nodoLocal) + " WHERE numero_factura = ?";

        try (Connection conn = DatabaseConnection.getConnection(nodoLocal);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, numeroFactura);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapearFactura(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar la factura por número", e);
        }

        return null;
    }

    // ===============================
    // CONSULTAR POR FECHA DE EMISIÓN
    // ===============================
    public List<FacturaGYE> consultarPorFecha(LocalDate fechaEmision) {
        validarSesion();
        String nodoLocal = ConfigSucursal.getSucursalActual();
        String sql = "SELECT * FROM " + obtenerTablaGye(nodoLocal) + " WHERE fecha_emision = ?";

        List<FacturaGYE> facturas = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection(nodoLocal);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(fechaEmision));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                facturas.add(mapearFactura(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar facturas por fecha", e);
        }

        return facturas;
    }

    // ===============================
    // CONSULTAR POR RANGO DE FECHAS
    // ===============================
    public List<FacturaGYE> consultarPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        validarSesion();
        String nodoLocal = ConfigSucursal.getSucursalActual();
        String sql = "SELECT * FROM " + obtenerTablaGye(nodoLocal) + " WHERE fecha_emision BETWEEN ? AND ?";

        List<FacturaGYE> facturas = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection(nodoLocal);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(fechaInicio));
            ps.setDate(2, java.sql.Date.valueOf(fechaFin));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                facturas.add(mapearFactura(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar facturas por rango de fechas", e);
        }

        return facturas;
    }

    // ===============================
    // MÉTODOS AUXILIARES
    // ===============================
    private void validarSesion() {
        UsuarioSesionDTO usuario = SesionActual.getUsuario();
        if (usuario == null) {
            throw new RuntimeException("No hay sesión activa");
        }
    }

    private String obtenerTablaGye(String nodoLocal) {
        // Enrutamiento forzado hacia el nodo de Guayaquil
        if ("GYE".equalsIgnoreCase(nodoLocal)) {
            return "GYE.dbo.facturaGYE";
        } else {
            return "[26.34.243.93].GYE.dbo.facturaGYE";
        }
    }
}