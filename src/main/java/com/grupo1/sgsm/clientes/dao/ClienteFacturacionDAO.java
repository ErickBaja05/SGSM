package com.grupo1.sgsm.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.grupo1.sgsm.clientes.model.ClienteFacturacion;
import com.grupo1.sgsm.core.database.DatabaseConnection;
import com.grupo1.sgsm.core.session.SesionActual;
import com.grupo1.sgsm.administracion.gestionUsuarios.dto.UsuarioSesionDTO;
import com.grupo1.sgsm.core.util.ConfigSucursal;

public class ClienteFacturacionDAO {

    // ===============================
    // MÉTODO PRIVADO DE MAPEO
    // ===============================
    private ClienteFacturacion mapearClienteFacturacion(ResultSet rs) throws SQLException {
        ClienteFacturacion c = new ClienteFacturacion();
        c.setCedula_ciudadania(rs.getString("cedula_ciudadania"));
        c.setPrimer_nombre(rs.getString("primer_nombre"));
        c.setSegundo_nombre(rs.getString("segundo_nombre"));
        c.setPrimer_apellido(rs.getString("primer_apellido"));
        c.setSegundo_apellido(rs.getString("segundo_apellido"));
        return c;
    }

    // ===============================
    // CONSULTAR TODOS (Lectura local)
    // ===============================
    public List<ClienteFacturacion> consultarTodos() {
        validarSesion();
        String nodoLocal = ConfigSucursal.getSucursalActual();
        String sql = "SELECT * FROM " + obtenerTablaLectura(nodoLocal);

        List<ClienteFacturacion> clientes = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection(nodoLocal);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                clientes.add(mapearClienteFacturacion(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar clientes de facturación", e);
        }

        return clientes;
    }

    // ===============================
    // CONSULTAR POR CÉDULA (Lectura local)
    // ===============================
    public ClienteFacturacion consultarPorCedula(String cedula) {
        validarSesion();
        String nodoLocal = ConfigSucursal.getSucursalActual();
        String sql = "SELECT * FROM " + obtenerTablaLectura(nodoLocal) + " WHERE cedula_ciudadania = ?";

        try (Connection conn = DatabaseConnection.getConnection(nodoLocal);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cedula);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapearClienteFacturacion(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar cliente de facturación por cédula", e);
        }

        return null;
    }

    // ===============================
    // INSERTAR (Escritura hacia UIO)
    // ===============================
    public void insertar(ClienteFacturacion cliente) {
        validarSesion();
        String nodoLocal = ConfigSucursal.getSucursalActual();

        String sql = String.format("""
            INSERT INTO %s (cedula_ciudadania, primer_nombre, segundo_nombre, primer_apellido, segundo_apellido)
            VALUES (?, ?, ?, ?, ?)
            """, obtenerTablaEscritura(nodoLocal));

        try (Connection conn = DatabaseConnection.getConnection(nodoLocal);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cliente.getCedula_ciudadania());
            ps.setString(2, cliente.getPrimer_nombre());
            ps.setString(3, cliente.getSegundo_nombre());
            ps.setString(4, cliente.getPrimer_apellido());
            ps.setString(5, cliente.getSegundo_apellido());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar cliente de facturación", e);
        }
    }

    // ===============================
    // ACTUALIZAR NOMBRES POR CÉDULA (Escritura hacia UIO)
    // ===============================
    public void actualizarNombresPorCedula(String cedula, String primerNombre, String segundoNombre) {
        validarSesion();
        String nodoLocal = ConfigSucursal.getSucursalActual();

        String sql = String.format("UPDATE %s SET primer_nombre = ?, segundo_nombre = ? WHERE cedula_ciudadania = ?",
                obtenerTablaEscritura(nodoLocal));

        try (Connection conn = DatabaseConnection.getConnection(nodoLocal);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, primerNombre);
            ps.setString(2, segundoNombre);
            ps.setString(3, cedula);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar nombres del cliente de facturación", e);
        }
    }

    // ===============================
    // ELIMINAR POR CÉDULA (Escritura hacia UIO)
    // ===============================
    public void eliminarPorCedula(String cedula) {
        validarSesion();
        String nodoLocal = ConfigSucursal.getSucursalActual();

        String sql = String.format("DELETE FROM %s WHERE cedula_ciudadania = ?", obtenerTablaEscritura(nodoLocal));

        try (Connection conn = DatabaseConnection.getConnection(nodoLocal);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cedula);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar cliente de facturación", e);
        }
    }

    // ===============================
    // MÉTODOS AUXILIARES DE ENRUTAMIENTO
    // ===============================
    private void validarSesion() {
        UsuarioSesionDTO usuario = SesionActual.getUsuario();
        if (usuario == null) {
            throw new RuntimeException("No hay sesión activa");
        }
    }

    private String obtenerTablaLectura(String nodoLocal) {
        // Lectura siempre a la réplica local
        return nodoLocal.toUpperCase() + ".dbo.cliente_facturacion";
    }

    private String obtenerTablaEscritura(String nodoLocal) {
        // Escritura forzada a la base de datos principal (UIO) para garantizar consistencia
        if ("UIO".equalsIgnoreCase(nodoLocal)) {
            return "UIO.dbo.cliente_facturacion";
        } else {
            return "[26.194.51.93].UIO.dbo.cliente_facturacion";
        }
    }
}
