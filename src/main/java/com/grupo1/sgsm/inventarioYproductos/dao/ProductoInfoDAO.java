package com.grupo1.sgsm.inventarioYproductos.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.grupo1.sgsm.inventarioYproductos.model.ProductoInfo;
import com.grupo1.sgsm.core.database.DatabaseConnection;

public class ProductoInfoDAO {

    private static final String TABLA = "[26.194.51.93].UIO.dbo.producto_info";

    // ===============================
    // MÉTODO PRIVADO DE MAPEO
    // ===============================
    private ProductoInfo mapearProductoInfo(ResultSet rs) throws SQLException {
        ProductoInfo p = new ProductoInfo();
        p.setCodigo_producto(rs.getString("codigo_producto"));
        p.setNombre(rs.getString("nombre"));
        p.setCategoria(rs.getString("categoria"));
        p.setMarca(rs.getString("marca"));
        p.setPrecio_venta(rs.getString("precio_venta"));
        return p;
    }

    // ===============================
    // INSERTAR
    // ===============================
    public void insertar(ProductoInfo producto) {
        String sql = String.format("""
            INSERT INTO %s (codigo_producto, nombre, categoria, marca, precio_venta)
            VALUES (?, ?, ?, ?, ?)
            """, TABLA);

        try (Connection conn = DatabaseConnection.getConnection("UIO");
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, producto.getCodigo_producto());
            ps.setString(2, producto.getNombre());
            ps.setString(3, producto.getCategoria());
            ps.setString(4, producto.getMarca());
            ps.setString(5, producto.getPrecio_venta());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar producto info", e);
        }
    }

    // ===============================
    // CONSULTAR TODOS
    // ===============================
    public List<ProductoInfo> consultarTodos() {
        String sql = String.format("SELECT * FROM %s", TABLA);
        List<ProductoInfo> productos = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection("UIO");
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                productos.add(mapearProductoInfo(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar productos info", e);
        }

        return productos;
    }

    // ===============================
    // CONSULTAR POR CÓDIGO
    // ===============================
    public ProductoInfo consultarPorCodigo(String codigoProducto) {
        String sql = String.format("SELECT * FROM %s WHERE codigo_producto = ?", TABLA);

        try (Connection conn = DatabaseConnection.getConnection("UIO");
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, codigoProducto);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapearProductoInfo(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar producto info por código", e);
        }

        return null;
    }

    // ===============================
    // ACTUALIZAR PRECIO POR CÓDIGO
    // ===============================
    public void actualizarPrecioPorCodigo(String codigoProducto, String nuevoPrecio) {
        String sql = String.format("UPDATE %s SET precio_venta = ? WHERE codigo_producto = ?", TABLA);

        try (Connection conn = DatabaseConnection.getConnection("UIO");
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nuevoPrecio);
            ps.setString(2, codigoProducto);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar precio del producto info", e);
        }
    }

    // ===============================
    // ELIMINAR POR CÓDIGO
    // ===============================
    public void eliminarPorCodigo(String codigoProducto) {
        String sql = String.format("DELETE FROM %s WHERE codigo_producto = ?", TABLA);

        try (Connection conn = DatabaseConnection.getConnection("UIO");
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, codigoProducto);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar producto info", e);
        }
    }
}

