package com.grupo1.sgsm.inventarioYproductos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.grupo1.sgsm.inventarioYproductos.model.Inventario;
import com.grupo1.sgsm.core.database.DatabaseConnection;
import com.grupo1.sgsm.core.database.NetworkChecker;
import com.grupo1.sgsm.core.util.ConfigSucursal;

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
    // CONSULTAR TODOS CON DOBLE VALIDACIÓN DE RED Y NODO FÍSICO
    // ===============================
    public List<Inventario> consultarTodos() {
        String nodoFisico = ConfigSucursal.getSucursalActual().toUpperCase();
        boolean redDisponible = verificarConectividad();
        List<Inventario> inventarios = new ArrayList<>();

        // 1. Si la red está activa, consultamos el stock perteneciente al nodo local físico
        if (redDisponible) {
            String vista = nodoFisico + ".dbo.V_inventario";
            String sql = String.format("""
                SELECT v.codigo_producto, v.stock, COALESCE(s.codigo_sucursal, v.codigo_sucursal) AS codigo_sucursal
                FROM %s v
                LEFT JOIN %s.dbo.sucursal s ON v.codigo_sucursal = s.codigo_sucursal
                WHERE UPPER(v.codigo_sucursal) = ?
                """, vista, nodoFisico);

            try (Connection conn = DatabaseConnection.getConnection(nodoFisico);
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, nodoFisico);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        inventarios.add(mapearInventario(rs));
                    }
                }
                return inventarios;
            } catch (SQLException e) {
                // Fallback si la vista falla
            }
        }

        // 2. Si la red está CAÍDA (o falla la vista), se redirige al fragmento físico local disponible
        String tablaFragmentoLocal = nodoFisico + ".dbo.inventario" + nodoFisico;
        String sqlFallback = String.format("SELECT * FROM %s", tablaFragmentoLocal);

        try (Connection conn = DatabaseConnection.getConnection(nodoFisico);
             PreparedStatement ps = conn.prepareStatement(sqlFallback);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                inventarios.add(mapearInventario(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar inventario en nodo local físico: " + e.getMessage());
        }

        return inventarios;
    }

    // ===============================
    // CONSULTAR REMOTO (NODOS DIFERENTES AL NODO LOCAL FÍSICO)
    // ===============================
    public List<Inventario> consultarRemoto() {
        String nodoFisico = ConfigSucursal.getSucursalActual().toUpperCase();
        List<Inventario> inventarios = new ArrayList<>();

        if (!verificarConectividad()) {
            // Sin red no se puede consultar inventario remoto
            return inventarios;
        }

        String vista = nodoFisico + ".dbo.V_inventario";
        String sql = String.format("""
            SELECT v.codigo_producto, v.stock, COALESCE(s.codigo_sucursal, v.codigo_sucursal) AS codigo_sucursal
            FROM %s v
            LEFT JOIN %s.dbo.sucursal s ON v.codigo_sucursal = s.codigo_sucursal
            WHERE UPPER(v.codigo_sucursal) <> ?
            """, vista, nodoFisico);

        try (Connection conn = DatabaseConnection.getConnection(nodoFisico);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nodoFisico);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    inventarios.add(mapearInventario(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar inventario remoto: " + e.getMessage());
        }

        return inventarios;
    }

    // ===============================
    // CONSULTAR POR PRODUCTO
    // ===============================
    public Inventario consultarPorProducto(String codigoProducto) {
        String nodoFisico = ConfigSucursal.getSucursalActual();
        String tablaOVista = obtenerTablaOVistaInventario();
        String sql = String.format("SELECT * FROM %s WHERE codigo_producto = ?", tablaOVista);

        try (Connection conn = DatabaseConnection.getConnection(nodoFisico);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, codigoProducto);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearInventario(rs);
                }
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
        String nodoFisico = ConfigSucursal.getSucursalActual();
        String tablaOVista = obtenerTablaOVistaInventario();
        String sql = String.format("""
            INSERT INTO %s (codigo_sucursal, codigo_producto, stock)
            VALUES (?, ?, ?)
            """, tablaOVista);

        try (Connection conn = DatabaseConnection.getConnection(nodoFisico);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, inv.getCodigo_sucursal());
            ps.setString(2, inv.getCodigo_producto());
            ps.setInt(3, inv.getStock() != null ? inv.getStock() : 0);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar en inventario", e);
        }
    }

    // ===============================
    // ACTUALIZAR STOCK
    // ===============================
    public void actualizarStock(String codigoProducto, int nuevoStock) {
        String nodoFisico = ConfigSucursal.getSucursalActual();
        String tablaOVista = obtenerTablaOVistaInventario();
        String sql = String.format("UPDATE %s SET stock = ? WHERE codigo_producto = ?", tablaOVista);

        try (Connection conn = DatabaseConnection.getConnection(nodoFisico);
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
        String nodoFisico = ConfigSucursal.getSucursalActual();
        String tablaOVista = obtenerTablaOVistaInventario();
        String sql = String.format("DELETE FROM %s WHERE codigo_producto = ?", tablaOVista);

        try (Connection conn = DatabaseConnection.getConnection(nodoFisico);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, codigoProducto);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar inventario por producto", e);
        }
    }

    // ===============================
    // MÉTODOS AUXILIARES Y VERIFICACIÓN DE RED
    // ===============================
    private String obtenerTablaOVistaInventario() {
        String nodoFisico = ConfigSucursal.getSucursalActual().toUpperCase();
        if (verificarConectividad()) {
            return nodoFisico + ".dbo.V_inventario";
        }
        return nodoFisico + ".dbo.inventario" + nodoFisico;
    }

    private boolean verificarConectividad() {
        if (ConfigSucursal.getSucursalActual().equalsIgnoreCase("UIO")) {
            return NetworkChecker.hayConexionGYE();
        }
        return NetworkChecker.hayConexionUIO();
    }
}
