package com.grupo1.sgsm.administracion.gestionUsuarios.service;

import com.grupo1.sgsm.administracion.gestionParametros.exception.CorreoNoValidoException;
import com.grupo1.sgsm.administracion.gestionUsuarios.dao.UsuarioDAO;
import com.grupo1.sgsm.administracion.gestionUsuarios.dto.NuevoUsuarioDTO;
import com.grupo1.sgsm.administracion.gestionUsuarios.dto.UsuarioConsultadoDTO;
import com.grupo1.sgsm.administracion.gestionUsuarios.dto.UsuarioSesionDTO;
import com.grupo1.sgsm.administracion.gestionUsuarios.exception.PasswordInseguroException;
import com.grupo1.sgsm.administracion.gestionUsuarios.exception.UsuarioYaRegistradoException;
import com.grupo1.sgsm.administracion.gestionUsuarios.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class UsuarioServiceImpl implements IUsuarioService{

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    private static final String PASSWORD_REGEX =
            "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$";

    private static final String EMAIL_REGEX =
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)+$";

    @Override
    public UsuarioSesionDTO login(String username, String password) {

        // Ya no necesitamos NetworkChecker ni ConfigSucursal aquí,
        // el DAO se encarga de todo el enrutamiento de forma interna.

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("El usuario no puede estar vacío.");
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía.");
        }

        // El DAO actualizado ahora solo recibe el nombre
        Usuario usuario = usuarioDAO.consultarPorNombre(username);

        if (usuario == null || !usuario.getPassword().equals(password)) {
            throw new IllegalArgumentException("Credenciales incorrectas");
        }

        return new UsuarioSesionDTO(
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getRol(),
                usuario.getCodigo_sucursal()
        );
    }

    @Override
    public void crearUsuario(NuevoUsuarioDTO nuevoUsuario) {

        if(usuarioDAO.consultarPorCorreo(nuevoUsuario.getCorreo())!=null){
            throw new UsuarioYaRegistradoException("Ya existe un Usuario con el correo electronico proporcionado");
        }

        if(!nuevoUsuario.getPassword().matches(PASSWORD_REGEX)){
            throw new PasswordInseguroException("La contraseña debe tener al menos una letra mayúscula, una minúscula, un símbolo especial y mínimo 8 caracteres");
        }

        if(!nuevoUsuario.getCorreo().matches(EMAIL_REGEX)){
            throw new CorreoNoValidoException("El correo no tiene formato válido");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(nuevoUsuario.getNombreUsuario());
        usuario.setRol(nuevoUsuario.getRolUsuario());
        usuario.setCodigo_sucursal(nuevoUsuario.getCodigo_sucursal());
        usuario.setCorreo(nuevoUsuario.getCorreo());
        usuario.setPassword(nuevoUsuario.getPassword());
        usuarioDAO.insertar(usuario);
    }

    @Override
    public List<UsuarioConsultadoDTO> consultarUsuarios() {
        List<Usuario> usuarios = usuarioDAO.consultarTodos();
        List<UsuarioConsultadoDTO> usuariosConsultadoDTO = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            usuariosConsultadoDTO.add(new UsuarioConsultadoDTO(String.valueOf(usuario.getIdUsuario()),usuario.getNombre(),usuario.getRol(),usuario.getCorreo()));
        }

        return usuariosConsultadoDTO;
    }

    @Override
    public void actualizarNombreUsuario(Integer idUsuario, String nombreUsuario) {
        usuarioDAO.actualizarNombreUsuarioPorId(idUsuario,nombreUsuario);
    }

    @Override
    public void actualizarRolUsuario(Integer idUsuario, String rolUsuario) {
        usuarioDAO.actualizarRolUsuarioPorId(idUsuario,rolUsuario);
    }

    @Override
    public void actualizarCorreoUsuario(Integer idUsuario, String correo) {
        if(usuarioDAO.consultarPorCorreo(correo)!=null){
            throw new UsuarioYaRegistradoException("Ya existe un Usuario con el correo proporcionado");
        }
        usuarioDAO.actualizarCorreoUsuarioPorId(idUsuario,correo);
    }

    @Override
    public void eliminarUsuario(Integer idUsuario) {
        usuarioDAO.eliminarPorId(idUsuario);
    }


}
