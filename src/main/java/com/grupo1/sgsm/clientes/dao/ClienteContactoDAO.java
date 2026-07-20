package com.grupo1.sgsm.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.grupo1.sgsm.clientes.model.ClienteContacto;
import com.grupo1.sgsm.core.database.DatabaseConnection;
import com.grupo1.sgsm.core.session.SesionActual;
import com.grupo1.sgsm.administracion.gestionUsuarios.dto.UsuarioSesionDTO;
import com.grupo1.sgsm.core.util.ConfigSucursal;

public class ClienteContactoDAO {

    // ===============================
    // MÉTODO PRIVADO DE MAPEO
    // ===============================
    private ClienteContacto mapearClienteContacto(ResultSet rs) throws SQLException {
        ClienteContacto c = new ClienteContacto();
        c.setCedula_ciudadania(rs.getString("cedula_ciudadania"));
        c.setCorreo_electronico(rs.getString("correo_electronico"));
        c.setDireccion(rs.getString("direccion"));
        c.setCod_sucursal_registro(rs.getString("cod_sucursal_registro"));
        return c;
    }

    // ===============================
    // CONSULTAR TODOS
    // ===============================
    public List<ClienteContacto> consultarTodos() {
        validarSesion();
        String nodoLocal = ConfigSucursal.getSucursalActual();
        String sql = "SELECT * FROM " + obtenerVistaLocal(nodoLocal);

        List<ClienteContacto> clientes = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection(nodoLocal);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                clientes.add(mapearClienteContacto(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar clientes de contacto", e);
        }

        return clientes;
    }

    // ===============================
    // CONSULTAR POR CÉDULA
    // ===============================
    public ClienteContacto consultarPorCedula(String cedula) {
        validarSesion();
        String nodoLocal = ConfigSucursal.getSucursalActual();
        String sql = "SELECT * FROM " + obtenerVistaLocal(nodoLocal) + " WHERE cedula_ciudadania = ?";

        try (Connection conn = DatabaseConnection.getConnection(nodoLocal);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cedula);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapearClienteContacto(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar cliente por cédula", e);
        }

        return null;
    }

    // ===============================
    // INSERTAR
    // ===============================
    public void insertar(ClienteContacto cliente) {
        validarSesion();
        String nodoLocal = ConfigSucursal.getSucursalActual();

        String sql = String.format("""
        INSERT INTO %s (cedula_ciudadania, correo_electronico, direccion, cod_sucursal_registro)
        VALUES (?, ?, ?, ?)
        """, obtenerVistaLocal(nodoLocal));

        try (Connection conn = DatabaseConnection.getConnection(nodoLocal);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cliente.getCedula_ciudadania());
            ps.setString(2, cliente.getCorreo_electronico());
            ps.setString(3, cliente.getDireccion());
            ps.setString(4, cliente.getCod_sucursal_registro());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar cliente de contacto", e);
        }
    }

    // ===============================
    // ACTUALIZAR CORREO POR CÉDULA
    // ===============================
    public void actualizarCorreoPorCedula(String cedula, String nuevoCorreo) {
        validarSesion();
        String nodoLocal = ConfigSucursal.getSucursalActual();

        String sql = String.format("""
        UPDATE %s
        SET correo_electronico = ?
        WHERE cedula_ciudadania = ?
        """, obtenerVistaLocal(nodoLocal));

        try (Connection conn = DatabaseConnection.getConnection(nodoLocal);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nuevoCorreo);
            ps.setString(2, cedula);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar correo", e);
        }
    }

    // ===============================
    // ACTUALIZAR DIRECCIÓN POR CÉDULA
    // ===============================
    public void actualizarDireccionPorCedula(String cedula, String nuevaDireccion) {
        validarSesion();
        String nodoLocal = ConfigSucursal.getSucursalActual();

        String sql = String.format("""
        UPDATE %s
        SET direccion = ?
        WHERE cedula_ciudadania = ?
        """, obtenerVistaLocal(nodoLocal));

        try (Connection conn = DatabaseConnection.getConnection(nodoLocal);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nuevaDireccion);
            ps.setString(2, cedula);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar dirección", e);
        }
    }

    // ===============================
    // ELIMINAR POR CÉDULA
    // ===============================
    public void eliminarPorCedula(String cedula) {
        validarSesion();
        String nodoLocal = ConfigSucursal.getSucursalActual();
        String sql = "DELETE FROM " + obtenerVistaLocal(nodoLocal) + " WHERE cedula_ciudadania = ?";

        try (Connection conn = DatabaseConnection.getConnection(nodoLocal);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cedula);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar cliente de contacto", e);
        }
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

    private String obtenerVistaLocal(String nodoLocal) {
        // Retorna directamente la vista de la base de datos donde se ejecuta la app
        // Ejemplo: "UIO.dbo.V_clientesContacto" o "GYE.dbo.V_clientesContacto"
        return nodoLocal.toUpperCase() + ".dbo.V_clientesContacto";
    }
}