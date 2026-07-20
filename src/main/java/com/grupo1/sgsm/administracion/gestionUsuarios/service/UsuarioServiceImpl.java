package com.grupo1.sgsm.administracion.gestionUsuarios.service;

import com.grupo1.sgsm.administracion.gestionUsuarios.dao.UsuarioDAO;
import com.grupo1.sgsm.administracion.gestionUsuarios.dto.UsuarioSesionDTO;
import com.grupo1.sgsm.administracion.gestionUsuarios.model.Usuario;
import com.grupo1.sgsm.core.database.NetworkChecker;
import com.grupo1.sgsm.core.util.ConfigSucursal;

public class UsuarioServiceImpl implements IUsuarioService{

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    public UsuarioSesionDTO login(String username, String password) {

        String sucursalActual = ConfigSucursal.getSucursalActual();
        boolean redDisponible = NetworkChecker.hayConexionUIO();

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("El usuario no puede estar vacío.");
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía.");
        }

        Usuario usuario = usuarioDAO.consultarPorNombre(username,redDisponible,sucursalActual);

        if(usuario == null || !usuario.getPassword().equals(password)) {
            throw new IllegalArgumentException("Credenciales incorrectas");
        }

        return new UsuarioSesionDTO(
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getRol(),
                usuario.getCodigo_sucursal()
        );

    }

}
