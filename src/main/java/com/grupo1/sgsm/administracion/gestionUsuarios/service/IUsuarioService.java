package com.grupo1.sgsm.administracion.gestionUsuarios.service;

import com.grupo1.sgsm.administracion.gestionUsuarios.dto.NuevoUsuarioDTO;
import com.grupo1.sgsm.administracion.gestionUsuarios.dto.UsuarioConsultadoDTO;
import com.grupo1.sgsm.administracion.gestionUsuarios.dto.UsuarioSesionDTO;

import java.util.List;

public interface IUsuarioService {
    UsuarioSesionDTO login(String username, String password);
    void crearUsuario(NuevoUsuarioDTO nuevoUsuario);
    List<UsuarioConsultadoDTO> consultarUsuarios();
    void actualizarNombreUsuario(Integer idUsuario, String nombreUsuario);
    void actualizarRolUsuario(Integer idUsuario,String rolUsuario);
    void actualizarCorreoUsuario(Integer idUsuario, String correo);
    void eliminarUsuario(Integer idUsuario);



}
