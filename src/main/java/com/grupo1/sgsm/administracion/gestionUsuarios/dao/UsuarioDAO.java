package com.grupo1.sgsm.administracion.gestionUsuarios.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.grupo1.sgsm.administracion.gestionUsuarios.model.Usuario;
import com.grupo1.sgsm.core.database.DatabaseConnection;

public class UsuarioDAO {

    private static final String TABLA_UIO = "[26.194.51.93].UIO.dbo.usuarios";
    private static final String TABLA_GYE = "[26.34.243.93].GYE.dbo.usuarios";

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
    // CONSULTAR TODOS
    // ===============================
    public List<Usuario> consultarTodos(boolean redDisponible, String sucursalActual) {
        String tabla = redDisponible ? TABLA_UIO : (sucursalActual.equalsIgnoreCase("GYE") ? TABLA_GYE : TABLA_UIO);
        String sql = String.format("SELECT * FROM %s", tabla);

        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection(sucursalActual);
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
    // CONSULTAR POR CORREO
    // ===============================
    public Usuario consultarPorNombre(String nombre, boolean redDisponible, String sucursalActual) {
        String tabla = redDisponible ? TABLA_UIO : (sucursalActual.equalsIgnoreCase("GYE") ? TABLA_GYE : TABLA_UIO);
        String sql = String.format("SELECT * FROM %s WHERE nombre = ?", tabla);

        try (Connection conn = DatabaseConnection.getConnection(sucursalActual);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapearUsuario(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar usuario por correo", e);
        }

        return null;
    }

    // ===============================
    // INSERTAR (siempre UIO)
    // ===============================
    public void insertar(Usuario usuario) {
        String sql = String.format("""
            INSERT INTO %s (nombre, correo, password, rol, codigo_sucursal)
            VALUES (?, ?, ?, ?, ?)
            """, TABLA_UIO);

        try (Connection conn = DatabaseConnection.getConnection("UIO");
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
    // ELIMINAR (siempre UIO)
    // ===============================
    public void eliminarPorId(int idUsuario) {
        String sql = String.format("DELETE FROM %s WHERE idUsuario = ?", TABLA_UIO);

        try (Connection conn = DatabaseConnection.getConnection("UIO");
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar usuario", e);
        }
    }
}

