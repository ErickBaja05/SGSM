package com.grupo1.sgsm.inventarioYproductos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.grupo1.sgsm.core.database.DatabaseConnection;
import com.grupo1.sgsm.core.session.SesionActual;
import com.grupo1.sgsm.core.util.ConfigSucursal;
import com.grupo1.sgsm.administracion.gestionUsuarios.dto.UsuarioSesionDTO;
import com.grupo1.sgsm.inventarioYproductos.model.Producto;

public class ProductoInfoDAO {

    private void validarSesion() {
        UsuarioSesionDTO usuario = SesionActual.getUsuario();
        if (usuario == null) {
            throw new RuntimeException("No hay sesión activa");
        }
    }

    private Producto mapearProducto(ResultSet rs) throws SQLException {
        return new Producto(
                rs.getString("codigo_producto").trim(),
                rs.getString("nombre"),
                rs.getString("marca"),
                rs.getString("categoria"),
                0.0, // El stock no se maneja en esta fase
                rs.getDouble("precio_venta")
        );
    }

    // ===============================
    // CONSULTAR TODOS (Siempre a la tabla local)
    // ===============================
    public List<Producto> consultarTodos() {
        validarSesion();
        String nodoLocal = ConfigSucursal.getSucursalActual();
        String sql = "SELECT codigo_producto, nombre, categoria, marca, precio_venta FROM " + obtenerTablaLectura(nodoLocal);

        List<Producto> productos = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection(nodoLocal);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                productos.add(mapearProducto(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar productos", e);
        }

        return productos;
    }

    // ===============================
    // CONSULTAR POR CÓDIGO (Siempre a la tabla local)
    // ===============================
    public Producto consultarPorCodigo(String codigo) {
        validarSesion();
        String nodoLocal = ConfigSucursal.getSucursalActual();
        String sql = "SELECT codigo_producto, nombre, categoria, marca, precio_venta FROM " + obtenerTablaLectura(nodoLocal) + " WHERE codigo_producto = ?";

        try (Connection conn = DatabaseConnection.getConnection(nodoLocal);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearProducto(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar producto por código", e);
        }

        return null;
    }

    // ===============================
    // INSERTAR (Siempre hacia UIO)
    // ===============================
    public void insertar(Producto producto) {
        validarSesion();
        String nodoLocal = ConfigSucursal.getSucursalActual();
        String sql = String.format("""
            INSERT INTO %s (codigo_producto, nombre, categoria, marca, precio_venta)
            VALUES (?, ?, ?, ?, ?)
            """, obtenerTablaEscritura(nodoLocal));

        try (Connection conn = DatabaseConnection.getConnection(nodoLocal);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, producto.getCodigo());
            ps.setString(2, producto.getNombre());
            ps.setString(3, producto.getCategoria());
            ps.setString(4, producto.getMarca());
            ps.setDouble(5, producto.getPrecio());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar producto", e);
        }
    }

    // ===============================
    // ACTUALIZAR (Siempre hacia UIO)
    // ===============================
    public void actualizar(Producto producto) {
        validarSesion();
        String nodoLocal = ConfigSucursal.getSucursalActual();
        String sql = String.format("""
            UPDATE %s
            SET nombre = ?, categoria = ?, marca = ?, precio_venta = ?
            WHERE codigo_producto = ?
            """, obtenerTablaEscritura(nodoLocal));

        try (Connection conn = DatabaseConnection.getConnection(nodoLocal);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getCategoria());
            ps.setString(3, producto.getMarca());
            ps.setDouble(4, producto.getPrecio());
            ps.setString(5, producto.getCodigo());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar producto", e);
        }
    }

    // ===============================
    // ELIMINAR (Siempre hacia UIO)
    // ===============================
    public void eliminar(String codigo) {
        validarSesion();
        String nodoLocal = ConfigSucursal.getSucursalActual();
        String sql = "DELETE FROM " + obtenerTablaEscritura(nodoLocal) + " WHERE codigo_producto = ?";

        try (Connection conn = DatabaseConnection.getConnection(nodoLocal);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, codigo);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar producto", e);
        }
    }

    // ===============================
    // MÉTODOS AUXILIARES DE ENRUTAMIENTO
    // ===============================
    private String obtenerTablaLectura(String nodoLocal) {
        return nodoLocal.toUpperCase() + ".dbo.producto_info";
    }

    private String obtenerTablaEscritura(String nodoLocal) {
        if ("UIO".equalsIgnoreCase(nodoLocal)) {
            return "UIO.dbo.producto_info";
        } else {
            return "[26.194.51.93].UIO.dbo.producto_info"; // Linked Server de GYE a UIO
        }
    }
}
