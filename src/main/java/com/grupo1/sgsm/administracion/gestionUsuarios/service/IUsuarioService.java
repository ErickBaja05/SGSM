package com.grupo1.sgsm.administracion.gestionUsuarios.service;

import com.grupo1.sgsm.administracion.gestionUsuarios.dto.UsuarioSesionDTO;

public interface IUsuarioService {
    UsuarioSesionDTO login(String username, String password);

}
