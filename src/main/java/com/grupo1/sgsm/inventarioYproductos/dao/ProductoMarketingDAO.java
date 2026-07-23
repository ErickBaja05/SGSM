package com.grupo1.sgsm.inventarioYproductos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.grupo1.sgsm.inventarioYproductos.model.ProductoMarketing;
import com.grupo1.sgsm.core.database.DatabaseConnection;
import com.grupo1.sgsm.core.session.SesionActual;
import com.grupo1.sgsm.core.util.ConfigSucursal;
import com.grupo1.sgsm.administracion.gestionUsuarios.dto.UsuarioSesionDTO;

public class ProductoMarketingDAO {

    private void validarSesion() {
        UsuarioSesionDTO usuario = SesionActual.getUsuario();
        if (usuario == null) {
            throw new RuntimeException("No hay sesión activa");
        }
    }

    private ProductoMarketing mapearProductoMarketing(ResultSet rs) throws SQLException {
        ProductoMarketing p = new ProductoMarketing();
        p.setCodigo_producto(rs.getString("codigo_producto"));
        p.setDescripcion(rs.getString("descripcion"));
        return p;
    }

    // ===============================
    // INSERTAR
    // ===============================
    public void insertar(ProductoMarketing producto) {
        validarSesion();
        String nodoLocal = ConfigSucursal.getSucursalActual();
        String sql = String.format("""
            INSERT INTO %s (codigo_producto, descripcion)
            VALUES (?, ?)
            """, obtenerTablaMarketing(nodoLocal));

        try (Connection conn = DatabaseConnection.getConnection(nodoLocal);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, producto.getCodigo_producto());
            ps.setString(2, producto.getDescripcion());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar producto marketing", e);
        }
    }

    // ===============================
    // CONSULTAR TODOS
    // ===============================
    public List<ProductoMarketing> consultarTodos() {
        validarSesion();
        String nodoLocal = ConfigSucursal.getSucursalActual();
        String sql = String.format("SELECT * FROM %s", obtenerTablaMarketing(nodoLocal));
        List<ProductoMarketing> productos = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection(nodoLocal);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                productos.add(mapearProductoMarketing(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar productos marketing", e);
        }

        return productos;
    }

    // ===============================
    // CONSULTAR POR CÓDIGO
    // ===============================
    public ProductoMarketing consultarPorCodigo(String codigoProducto) {
        validarSesion();
        String nodoLocal = ConfigSucursal.getSucursalActual();
        String sql = String.format("SELECT * FROM %s WHERE codigo_producto = ?", obtenerTablaMarketing(nodoLocal));

        try (Connection conn = DatabaseConnection.getConnection(nodoLocal);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, codigoProducto);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapearProductoMarketing(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar producto marketing por código", e);
        }

        return null;
    }

    // ===============================
    // ACTUALIZAR DESCRIPCIÓN POR CÓDIGO
    // ===============================
    public void actualizarDescripcionPorCodigo(String codigoProducto, String nuevaDescripcion) {
        validarSesion();
        String nodoLocal = ConfigSucursal.getSucursalActual();
        String sql = String.format("UPDATE %s SET descripcion = ? WHERE codigo_producto = ?", obtenerTablaMarketing(nodoLocal));

        try (Connection conn = DatabaseConnection.getConnection(nodoLocal);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nuevaDescripcion);
            ps.setString(2, codigoProducto);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar descripción del producto marketing", e);
        }
    }

    // ===============================
    // ELIMINAR POR CÓDIGO
    // ===============================
    public void eliminarPorCodigo(String codigoProducto) {
        validarSesion();
        String nodoLocal = ConfigSucursal.getSucursalActual();
        String sql = String.format("DELETE FROM %s WHERE codigo_producto = ?", obtenerTablaMarketing(nodoLocal));

        try (Connection conn = DatabaseConnection.getConnection(nodoLocal);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, codigoProducto);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar producto marketing", e);
        }
    }

    // ===============================
    // MÉTODOS AUXILIARES DE ENRUTAMIENTO
    // ===============================
    private String obtenerTablaMarketing(String nodoLocal) {
        if ("UIO".equalsIgnoreCase(nodoLocal)) {
            return "UIO.dbo.producto_marketing";
        } else {
            return "[26.194.51.93].UIO.dbo.producto_marketing"; // Linked Server de GYE a UIO
        }
    }
}
