package com.grupo1.sgsm.clientes.service;

import com.grupo1.sgsm.clientes.dto.ClienteConsultaDTO;
import com.grupo1.sgsm.clientes.dto.NuevoClienteDTO;

import java.util.List;

public interface IClientesService {
    void guardarCliente(NuevoClienteDTO nuevoCliente);
    List<ClienteConsultaDTO> consultarTodosLosClientes();
    void actualizarCliente(String cedula, String nuevoCorreo, String nuevoDireccion);
    void eliminarCliente(String cedula);

}
