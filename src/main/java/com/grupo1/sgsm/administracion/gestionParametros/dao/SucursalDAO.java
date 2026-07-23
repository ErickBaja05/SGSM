package com.grupo1.sgsm.administracion.gestionParametros.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.grupo1.sgsm.administracion.gestionParametros.model.Sucursal;
import com.grupo1.sgsm.core.database.DatabaseConnection;
import com.grupo1.sgsm.core.util.ConfigSucursal;

public class SucursalDAO {

    // ===============================
    // MÉTODO PRIVADO DE MAPEO
    // ===============================
    private Sucursal mapearSucursal(ResultSet rs) throws SQLException {
        Sucursal s = new Sucursal();
        s.setCodigo_sucursal(rs.getString("codigo_sucursal"));
        s.setNombre(rs.getString("nombre"));
        s.setCiudad(rs.getString("ciudad"));
        s.setDireccion(rs.getString("direccion"));
        s.setTelefono(rs.getString("telefono"));
        return s;
    }

    // ===============================
    // CONSULTAR TODAS (Siempre a la tabla local)
    // ===============================
    public List<Sucursal> consultarTodasLasSucursales() {
        String nodoLocal = ConfigSucursal.getSucursalActual();
        String sql = "SELECT * FROM " + obtenerTablaLectura(nodoLocal);

        List<Sucursal> sucursales = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection(nodoLocal);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                sucursales.add(mapearSucursal(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar sucursales", e);
        }

        return sucursales;
    }

    // ===============================
    // CONSULTAR POR CÓDIGO (Siempre a la tabla local)
    // ===============================
    public Sucursal consultarPorCodigoSucursal(String codigoSucursal) {
        String nodoLocal = ConfigSucursal.getSucursalActual();
        String sql = "SELECT * FROM " + obtenerTablaLectura(nodoLocal) + " WHERE codigo_sucursal = ?";

        try (Connection conn = DatabaseConnection.getConnection(nodoLocal);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, codigoSucursal);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapearSucursal(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar sucursal por código", e);
        }

        return null;
    }

    // ===============================
    // REGISTRAR SUCURSAL (Siempre hacia UIO)
    // ===============================
    public void registrarSucursal(Sucursal sucursal) {
        String nodoLocal = ConfigSucursal.getSucursalActual();
        String sql = String.format("""
            INSERT INTO %s (codigo_sucursal, nombre, ciudad, direccion, telefono)
            VALUES (?, ?, ?, ?, ?)
            """, obtenerTablaEscritura(nodoLocal));

        try (Connection conn = DatabaseConnection.getConnection(nodoLocal);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sucursal.getCodigo_sucursal());
            ps.setString(2, sucursal.getNombre());
            ps.setString(3, sucursal.getCiudad());
            ps.setString(4, sucursal.getDireccion());
            ps.setString(5, sucursal.getTelefono());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar sucursal", e);
        }
    }

    // ===============================
    // ELIMINAR POR CÓDIGO (Siempre hacia UIO)
    // ===============================
    public void eliminarPorCodigoSucursal(String codigoSucursal) {
        String nodoLocal = ConfigSucursal.getSucursalActual();
        String sql = String.format("DELETE FROM %s WHERE codigo_sucursal = ?", obtenerTablaEscritura(nodoLocal));

        try (Connection conn = DatabaseConnection.getConnection(nodoLocal);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, codigoSucursal);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar sucursal", e);
        }
    }

    // ===============================
    // ACTUALIZAR NOMBRE POR CÓDIGO (Siempre hacia UIO)
    // ===============================
    public void actualizarNombrePorCodigoSucursal(String codigoSucursal, String nuevoNombre) {
        String nodoLocal = ConfigSucursal.getSucursalActual();
        String sql = String.format("UPDATE %s SET nombre = ? WHERE codigo_sucursal = ?", obtenerTablaEscritura(nodoLocal));

        try (Connection conn = DatabaseConnection.getConnection(nodoLocal);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nuevoNombre);
            ps.setString(2, codigoSucursal);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar nombre de sucursal", e);
        }
    }

    // ===============================
    // ACTUALIZAR TELÉFONO POR CÓDIGO (Siempre hacia UIO)
    // ===============================
    public void actualizarTelefonoPorCodigoSucursal(String codigoSucursal, String nuevoTelefono) {
        String nodoLocal = ConfigSucursal.getSucursalActual();
        String sql = String.format("UPDATE %s SET telefono = ? WHERE codigo_sucursal = ?", obtenerTablaEscritura(nodoLocal));

        try (Connection conn = DatabaseConnection.getConnection(nodoLocal);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nuevoTelefono);
            ps.setString(2, codigoSucursal);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar teléfono de sucursal", e);
        }
    }

    // ===============================
    // ACTUALIZAR DIRECCIÓN POR CÓDIGO (Siempre hacia UIO)
    // ===============================
    public void actualizarDireccionPorCodigoSucursal(String codigoSucursal, String nuevaDireccion) {
        String nodoLocal = ConfigSucursal.getSucursalActual();
        String sql = String.format("UPDATE %s SET direccion = ? WHERE codigo_sucursal = ?", obtenerTablaEscritura(nodoLocal));

        try (Connection conn = DatabaseConnection.getConnection(nodoLocal);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nuevaDireccion);
            ps.setString(2, codigoSucursal);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar dirección de sucursal", e);
        }
    }

    // ===============================
    // MÉTODOS AUXILIARES DE ENRUTAMIENTO
    // ===============================
    private String obtenerTablaLectura(String nodoLocal) {
        return nodoLocal.toUpperCase() + ".dbo.sucursal";
    }

    private String obtenerTablaEscritura(String nodoLocal) {
        if ("UIO".equalsIgnoreCase(nodoLocal)) {
            return "UIO.dbo.sucursal";
        } else {
            return "[26.194.51.93].UIO.dbo.sucursal"; // Linked Server de GYE hacia UIO
        }
    }
}

