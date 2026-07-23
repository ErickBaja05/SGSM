package com.grupo1.sgsm.inventarioYproductos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.grupo1.sgsm.core.database.DatabaseConnection;
import com.grupo1.sgsm.core.database.NetworkChecker;
import com.grupo1.sgsm.core.session.SesionActual;
import com.grupo1.sgsm.core.util.ConfigSucursal;
import com.grupo1.sgsm.administracion.gestionUsuarios.dto.UsuarioSesionDTO;
import com.grupo1.sgsm.inventarioYproductos.model.Producto;

public class ProductoDAO {

    private void validarSesion() {
        UsuarioSesionDTO usuario = SesionActual.getUsuario();
        if (usuario == null) {
            throw new RuntimeException("No hay sesión activa");
        }
    }

    private String obtenerNodoConexion() {
        String sucursalActual = ConfigSucursal.getSucursalActual().toLowerCase();
        if ("uio".equals(sucursalActual)) {
            return "uio";
        } else {
            // Si estamos en GYE, comprobamos si podemos llegar al nodo principal UIO
            if (NetworkChecker.hayConexionUIO()) {
                return "uio";
            } else {
                return "gye";
            }
        }
    }

    private String obtenerNombreTabla(String nodoConexion) {
        return nodoConexion.toUpperCase() + ".dbo.producto_info";
    }

    private Producto mapearProducto(ResultSet rs) throws SQLException {
        return new Producto(
                rs.getString("codigo_producto").trim(),
                rs.getString("nombre"),
                rs.getString("marca"),
                rs.getString("categoria"),
                0.0, // Stock no es manejado en esta fase
                rs.getDouble("precio_venta")
        );
    }

    // ===============================
    // CONSULTAR TODOS
    // ===============================
    public List<Producto> consultarTodos() {
        validarSesion();
        String nodo = obtenerNodoConexion();
        String tabla = obtenerNombreTabla(nodo);
        String sql = "SELECT codigo_producto, nombre, categoria, marca, precio_venta FROM " + tabla;

        List<Producto> productos = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection(nodo);
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
    // CONSULTAR POR CÓDIGO
    // ===============================
    public Producto consultarPorCodigo(String codigo) {
        validarSesion();
        String nodo = obtenerNodoConexion();
        String tabla = obtenerNombreTabla(nodo);
        String sql = "SELECT codigo_producto, nombre, categoria, marca, precio_venta FROM " + tabla + " WHERE codigo_producto = ?";

        try (Connection conn = DatabaseConnection.getConnection(nodo);
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
    // INSERTAR
    // ===============================
    public void insertar(Producto producto) {
        validarSesion();
        String nodo = obtenerNodoConexion();
        String tabla = obtenerNombreTabla(nodo);

        String sql = String.format("""
            INSERT INTO %s (codigo_producto, nombre, categoria, marca, precio_venta)
            VALUES (?, ?, ?, ?, ?)
            """, tabla);

        try (Connection conn = DatabaseConnection.getConnection(nodo);
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
    // ACTUALIZAR
    // ===============================
    public void actualizar(Producto producto) {
        validarSesion();
        String nodo = obtenerNodoConexion();
        String tabla = obtenerNombreTabla(nodo);

        String sql = String.format("""
            UPDATE %s
            SET nombre = ?, categoria = ?, marca = ?, precio_venta = ?
            WHERE codigo_producto = ?
            """, tabla);

        try (Connection conn = DatabaseConnection.getConnection(nodo);
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
    // ELIMINAR
    // ===============================
    public void eliminar(String codigo) {
        validarSesion();
        String nodo = obtenerNodoConexion();
        String tabla = obtenerNombreTabla(nodo);

        String sql = "DELETE FROM " + tabla + " WHERE codigo_producto = ?";

        try (Connection conn = DatabaseConnection.getConnection(nodo);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, codigo);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar producto", e);
        }
    }
}
