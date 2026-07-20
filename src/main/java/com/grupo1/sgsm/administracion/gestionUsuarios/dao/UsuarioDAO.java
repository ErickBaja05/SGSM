package com.grupo1.sgsm.administracion.gestionUsuarios.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.grupo1.sgsm.administracion.gestionUsuarios.model.Usuario;
import com.grupo1.sgsm.core.database.DatabaseConnection;
import com.grupo1.sgsm.core.util.ConfigSucursal;

public class UsuarioDAO {

    // ===============================
    // MÉTODO PRIVADO DE MAPEO
    // ===============================
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setIdUsuario(rs.getInt("idUsuario"));
        u.setNombre(rs.getString("nombre"));
        u.setCorreo(rs.getString("correo"));
        u.setPassword(rs.getString("password"));
        u.setRol(rs.getString("rol"));
        u.setCodigo_sucursal(rs.getString("codigo_sucursal"));
        return u;
    }

    // ===============================
    // CONSULTAR TODOS (Siempre a la tabla local)
    // ===============================
    public List<Usuario> consultarTodos() {
        String nodoLocal = ConfigSucursal.getSucursalActual();
        String sql = "SELECT * FROM " + obtenerTablaLectura(nodoLocal);

        List<Usuario> usuarios = new ArrayList<>();

        // Java siempre abre la conexión al nodo local
        try (Connection conn = DatabaseConnection.getConnection(nodoLocal);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar usuarios", e);
        }

        return usuarios;
    }

    // ===============================
    // CONSULTAR POR NOMBRE (Siempre a la tabla local)
    // ===============================
    public Usuario consultarPorNombre(String nombre) {
        String nodoLocal = ConfigSucursal.getSucursalActual();
        String sql = "SELECT * FROM " + obtenerTablaLectura(nodoLocal) + " WHERE nombre = ?";

        try (Connection conn = DatabaseConnection.getConnection(nodoLocal);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapearUsuario(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar usuario por nombre", e);
        }

        return null;
    }

    // ===============================
    // INSERTAR (Siempre hacia UIO)
    // ===============================
    public void insertar(Usuario usuario) {
        String nodoLocal = ConfigSucursal.getSucursalActual();
        String sql = String.format("""
            INSERT INTO %s (nombre, correo, password, rol, codigo_sucursal)
            VALUES (?, ?, ?, ?, ?)
            """, obtenerTablaEscritura(nodoLocal));

        // Conexión local, pero el target SQL forzará el envío a UIO si estamos en GYE
        try (Connection conn = DatabaseConnection.getConnection(nodoLocal);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getCorreo());
            ps.setString(3, usuario.getPassword());
            ps.setString(4, usuario.getRol());
            ps.setString(5, usuario.getCodigo_sucursal());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar usuario", e);
        }
    }

    // ===============================
    // ELIMINAR (Siempre hacia UIO)
    // ===============================
    public void eliminarPorId(int idUsuario) {
        String nodoLocal = ConfigSucursal.getSucursalActual();
        String sql = String.format("DELETE FROM %s WHERE idUsuario = ?", obtenerTablaEscritura(nodoLocal));

        try (Connection conn = DatabaseConnection.getConnection(nodoLocal);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar usuario", e);
        }
    }

    // ===============================
    // MÉTODOS AUXILIARES DE ENRUTAMIENTO
    // ===============================
    private String obtenerTablaLectura(String nodoLocal) {
        // Retorna "UIO.dbo.usuarios" o "GYE.dbo.usuarios" para lectura ultrarrápida local
        return nodoLocal.toUpperCase() + ".dbo.usuarios";
    }

    private String obtenerTablaEscritura(String nodoLocal) {
        // Enrutamiento de escritura: La sede principal (UIO) recibe todos los cambios
        if ("UIO".equalsIgnoreCase(nodoLocal)) {
            return "UIO.dbo.usuarios";
        } else {
            return "[26.194.51.93].UIO.dbo.usuarios"; // Linked Server de GYE hacia UIO
        }
    }
}