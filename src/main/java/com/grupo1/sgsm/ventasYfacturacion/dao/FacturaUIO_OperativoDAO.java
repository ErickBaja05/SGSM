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
import com.grupo1.sgsm.ventasYfacturacion.dto.FacturaOperativaDTO;
import com.grupo1.sgsm.ventasYfacturacion.model.FacturaUIOOperativo;

public class FacturaUIO_OperativoDAO {

    private void validarSesion() {
        UsuarioSesionDTO usuario = SesionActual.getUsuario();
        if (usuario == null) {
            throw new RuntimeException("No hay sesión activa");
        }
    }

    private FacturaOperativaDTO mapearFacturaOperativa(ResultSet rs) throws SQLException {
        Date sqlDate = rs.getDate("fecha_emision");
        String fechaStr = "";
        if (sqlDate != null) {
            LocalDate ld = sqlDate.toLocalDate();
            fechaStr = String.format("%02d/%02d/%d", ld.getDayOfMonth(), ld.getMonthValue(), ld.getYear());
        }

        String primerNombre = rs.getString("primer_nombre");
        String primerApellido = rs.getString("primer_apellido");
        String clienteNombre = "Consumidor Final";
        if (primerNombre != null && primerApellido != null) {
            clienteNombre = (primerNombre.trim() + " " + primerApellido.trim()).trim();
        }

        return new FacturaOperativaDTO(
                rs.getString("numero_factura").trim(),
                fechaStr,
                rs.getString("cedula_ciudadania") != null ? rs.getString("cedula_ciudadania").trim() : "",
                clienteNombre
        );
    }

    // ===============================
    // INSERTAR
    // ===============================
    public void insertar(FacturaUIOOperativo factura) {
        validarSesion();
        String nodoLocal = ConfigSucursal.getSucursalActual();
        String prefijo = obtenerPrefijoUIO(nodoLocal);

        String sql = String.format("""
            INSERT INTO %sfacturaUIO_operativo (numero_factura, codigo_sucursal, cedula_ciudadania, fecha_emision)
            VALUES (?, ?, ?, ?)
            """, prefijo);

        try (Connection conn = DatabaseConnection.getConnection(nodoLocal.toLowerCase());
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, factura.getNumero_factura());
            ps.setString(2, factura.getCodigo_sucursal());
            ps.setString(3, factura.getCedula_ciudadania());
            ps.setDate(4, Date.valueOf(factura.getFecha_emision()));

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar factura operativa en UIO", e);
        }
    }

    public List<FacturaOperativaDTO> consultarTodasFacturasOperativas() {
        validarSesion();
        String nodoLocal = ConfigSucursal.getSucursalActual();
        String prefijo = obtenerPrefijoUIO(nodoLocal);

        String sql = String.format("""
            SELECT f.numero_factura, f.fecha_emision, f.cedula_ciudadania,
                   c.primer_nombre, c.primer_apellido
            FROM %sfacturaUIO_operativo f
            LEFT JOIN %scliente_facturacion c ON f.cedula_ciudadania = c.cedula_ciudadania
            ORDER BY f.fecha_emision DESC
            """, prefijo, prefijo);

        List<FacturaOperativaDTO> facturas = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection(nodoLocal.toLowerCase());
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                facturas.add(mapearFacturaOperativa(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar todas las facturas operativas UIO", e);
        }

        return facturas;
    }

    public List<FacturaOperativaDTO> consultarFacturasOperativas(LocalDate inicio, LocalDate fin) {
        validarSesion();
        if (inicio == null || fin == null) {
            return consultarTodasFacturasOperativas();
        }

        String nodoLocal = ConfigSucursal.getSucursalActual();
        String prefijo = obtenerPrefijoUIO(nodoLocal);

        String sql = String.format("""
            SELECT f.numero_factura, f.fecha_emision, f.cedula_ciudadania,
                   c.primer_nombre, c.primer_apellido
            FROM %sfacturaUIO_operativo f
            LEFT JOIN %scliente_facturacion c ON f.cedula_ciudadania = c.cedula_ciudadania
            WHERE CAST(f.fecha_emision AS DATE) BETWEEN ? AND ?
            ORDER BY f.fecha_emision DESC
            """, prefijo, prefijo);

        List<FacturaOperativaDTO> facturas = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection(nodoLocal.toLowerCase());
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(inicio));
            ps.setDate(2, Date.valueOf(fin));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    facturas.add(mapearFacturaOperativa(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al filtrar facturas operativas UIO por fecha", e);
        }

        return facturas;
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