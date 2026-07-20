package com.grupo1.sgsm.inventarioYproductos.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.grupo1.sgsm.inventarioYproductos.model.Inventario;
import com.grupo1.sgsm.core.database.DatabaseConnection;
import com.grupo1.sgsm.core.session.SesionActual;
import com.grupo1.sgsm.administracion.gestionUsuarios.dto.UsuarioSesionDTO;

public class InventarioDAO {

    // ===============================
    // MÉTODO PRIVADO DE MAPEO
    // ===============================
    private Inventario mapearInventario(ResultSet rs) throws SQLException {
        Inventario inv = new Inventario();
        inv.setCodigo_sucursal(rs.getString("codigo_sucursal"));
        inv.setCodigo_producto(rs.getString("codigo_producto"));
        inv.setStock(rs.getInt("stock"));
        return inv;
    }

    // ===============================
    // CONSULTAR TODOS
    // ===============================
    public List<Inventario> consultarTodos() {
        UsuarioSesionDTO usuario = SesionActual.getUsuario();
        if (usuario == null) {
            throw new RuntimeException("No hay sesión activa");
        }

        String vista = obtenerVistaPorSucursal(usuario.getCodigo_sucursal());
        String sql = String.format("SELECT * FROM %s", vista);

        List<Inventario> inventarios = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection(usuario.getCodigo_sucursal());
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                inventarios.add(mapearInventario(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar inventario", e);
        }

        return inventarios;
    }

    // ===============================
    // CONSULTAR POR PRODUCTO
    // ===============================
    public Inventario consultarPorProducto(String codigoProducto) {
        UsuarioSesionDTO usuario = SesionActual.getUsuario();
        if (usuario == null) {
            throw new RuntimeException("No hay sesión activa");
        }

        String vista = obtenerVistaPorSucursal(usuario.getCodigo_sucursal());
        String sql = String.format("SELECT * FROM %s WHERE codigo_producto = ?", vista);

        try (Connection conn = DatabaseConnection.getConnection(usuario.getCodigo_sucursal());
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, codigoProducto);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapearInventario(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar inventario por producto", e);
        }

        return null;
    }

    // ===============================
    // INSERTAR
    // ===============================
    public void insertar(Inventario inv) {
        UsuarioSesionDTO usuario = SesionActual.getUsuario();
        if (usuario == null) {
            throw new RuntimeException("No hay sesión activa");
        }

        String vista = obtenerVistaPorSucursal(usuario.getCodigo_sucursal());
        String sql = String.format("""
            INSERT INTO %s (codigo_sucursal, codigo_producto, stock)
            VALUES (?, ?, ?)
            """, vista);

        try (Connection conn = DatabaseConnection.getConnection(usuario.getCodigo_sucursal());
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, inv.getCodigo_sucursal());
            ps.setString(2, inv.getCodigo_producto());
            ps.setInt(3, inv.getStock());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar inventario", e);
        }
    }

    // ===============================
    // ACTUALIZAR STOCK POR PRODUCTO
    // ===============================
    public void actualizarStock(String codigoProducto, int nuevoStock) {
        UsuarioSesionDTO usuario = SesionActual.getUsuario();
        if (usuario == null) {
            throw new RuntimeException("No hay sesión activa");
        }

        String vista = obtenerVistaPorSucursal(usuario.getCodigo_sucursal());
        String sql = String.format("UPDATE %s SET stock = ? WHERE codigo_producto = ?", vista);

        try (Connection conn = DatabaseConnection.getConnection(usuario.getCodigo_sucursal());
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, nuevoStock);
            ps.setString(2, codigoProducto);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar stock en inventario", e);
        }
    }

    // ===============================
    // ELIMINAR POR PRODUCTO
    // ===============================
    public void eliminarPorProducto(String codigoProducto) {
        UsuarioSesionDTO usuario = SesionActual.getUsuario();
        if (usuario == null) {
            throw new RuntimeException("No hay sesión activa");
        }

        String vista = obtenerVistaPorSucursal(usuario.getCodigo_sucursal());
        String sql = String.format("DELETE FROM %s WHERE codigo_producto = ?", vista);

        try (Connection conn = DatabaseConnection.getConnection(usuario.getCodigo_sucursal());
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, codigoProducto);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar inventario por producto", e);
        }
    }

    // ===============================
    // AUXILIAR PARA VISTA
    // ===============================
    private String obtenerVistaPorSucursal(String codigoSucursal) {
        switch (codigoSucursal.toUpperCase()) {
            case "UIO":
                return "[26.194.51.93].UIO.dbo.V_inventario";
            case "GYE":
                return "[26.34.243.93].GYE.dbo.V_inventario";
            default:
                throw new IllegalArgumentException("Sucursal desconocida: " + codigoSucursal);
        }
    }
}

