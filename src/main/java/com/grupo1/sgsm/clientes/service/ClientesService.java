package com.grupo1.sgsm.clientes.service;

import com.grupo1.sgsm.clientes.dao.ClienteContactoDAO;
import com.grupo1.sgsm.clientes.dao.ClienteFacturacionDAO;
import com.grupo1.sgsm.clientes.dto.ClienteConsultaDTO;
import com.grupo1.sgsm.clientes.dto.NuevoClienteDTO;
import com.grupo1.sgsm.clientes.exception.ClienteReferenciadoException;
import com.grupo1.sgsm.clientes.exception.ClienteYaExisteException;
import com.grupo1.sgsm.clientes.model.ClienteContacto;
import com.grupo1.sgsm.clientes.model.ClienteFacturacion;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientesService implements IClientesService {

    private final ClienteFacturacionDAO clienteFacturacionDAO = new ClienteFacturacionDAO();
    private final ClienteContactoDAO clienteContactoDAO = new ClienteContactoDAO();

    @Override
    public void guardarCliente(NuevoClienteDTO nuevoCliente) {

        ClienteFacturacion clienteConsulta = clienteFacturacionDAO.consultarPorCedula(nuevoCliente.getCedula());
        if(clienteConsulta == null) {
            ClienteFacturacion nuevoClienteFacturacion = new ClienteFacturacion();
            nuevoClienteFacturacion.setCedula_ciudadania(nuevoCliente.getCedula());
            nuevoClienteFacturacion.setPrimer_nombre(nuevoCliente.getPrimerNombre());
            // CORRECCIÓN: Se asigna el segundo nombre correctamente en lugar de repetir el apellido
            nuevoClienteFacturacion.setSegundo_nombre(nuevoCliente.getSegundoNombre());
            nuevoClienteFacturacion.setPrimer_apellido(nuevoCliente.getPrimerApellido());
            nuevoClienteFacturacion.setSegundo_apellido(nuevoCliente.getSegundoApellido());

            clienteFacturacionDAO.insertar(nuevoClienteFacturacion);

            ClienteContacto nuevoClienteContacto = new ClienteContacto();
            nuevoClienteContacto.setCedula_ciudadania(nuevoCliente.getCedula());
            nuevoClienteContacto.setDireccion(nuevoCliente.getDireccion());
            nuevoClienteContacto.setCod_sucursal_registro(nuevoCliente.getCodSucursalRegistro());
            nuevoClienteContacto.setCorreo_electronico(nuevoCliente.getCorreoElectronico());

            clienteContactoDAO.insertar(nuevoClienteContacto);

        } else {
            throw new ClienteYaExisteException("Ya existe un cliente con el número de cédula proporcionado");
        }
    }

    @Override
    public List<ClienteConsultaDTO> consultarTodosLosClientes() {

        List<ClienteContacto> informacionClientes = clienteContactoDAO.consultarTodos();
        List<ClienteFacturacion> facturacionClientes = clienteFacturacionDAO.consultarTodos();

        Map<String, ClienteContacto> mapaContactos = new HashMap<>();

        for (ClienteContacto contacto : informacionClientes) {
            mapaContactos.put(contacto.getCedula_ciudadania(), contacto);
        }

        List<ClienteConsultaDTO> listaClienteConsultaDTO = new ArrayList<>();

        for (ClienteFacturacion datosFacturacion : facturacionClientes) {

            ClienteConsultaDTO clienteConsulta = new ClienteConsultaDTO();
            clienteConsulta.setCedula(datosFacturacion.getCedula_ciudadania());
            clienteConsulta.setNombre(datosFacturacion.getPrimer_nombre() + " " + datosFacturacion.getSegundo_nombre());
            clienteConsulta.setApellidos(datosFacturacion.getPrimer_apellido() + " " + datosFacturacion.getSegundo_apellido());

            ClienteContacto contacto = mapaContactos.get(datosFacturacion.getCedula_ciudadania());

            if (contacto != null) {
                if (contacto.getDireccion() != null && !contacto.getDireccion().trim().isEmpty()) {
                    clienteConsulta.setDireccion(contacto.getDireccion());
                } else {
                    clienteConsulta.setDireccion("NO DISPONIBLE");
                }

                if (contacto.getCorreo_electronico() != null && !contacto.getCorreo_electronico().trim().isEmpty()) {
                    clienteConsulta.setCorreo(contacto.getCorreo_electronico());
                } else {
                    clienteConsulta.setCorreo("NO DISPONIBLE");
                }
            } else {
                clienteConsulta.setDireccion("NO DISPONIBLE");
                clienteConsulta.setCorreo("NO DISPONIBLE");
            }
            listaClienteConsultaDTO.add(clienteConsulta);
        }
        return listaClienteConsultaDTO;
    }

    @Override
    public void actualizarCliente(String cedula, String nuevoCorreo, String nuevoDireccion) {
        ClienteContacto clienteAActualizar = clienteContactoDAO.consultarPorCedula(cedula);
        if(clienteAActualizar != null){
            clienteContactoDAO.actualizarCorreoPorCedula(clienteAActualizar.getCedula_ciudadania(), nuevoCorreo);
            clienteContactoDAO.actualizarDireccionPorCedula(clienteAActualizar.getCedula_ciudadania(), nuevoDireccion);
        }
    }

    @Override
    public void eliminarCliente(String cedula) {

        ClienteFacturacion clienteAeliminar = clienteFacturacionDAO.consultarPorCedula(cedula);

        if(clienteAeliminar != null){

            try {
                clienteContactoDAO.eliminarPorCedula(cedula);

                // 2. LUEGO eliminamos el registro padre (Facturación)
                clienteFacturacionDAO.eliminarPorCedula(cedula);
            }catch (RuntimeException e){
                throw new ClienteReferenciadoException("No se puede eliminar el Cliente porque tiene facturas asociadas");
            }

        }
        // 1. PRIMERO eliminamos el registro hijo (Contacto) para no violar la restricción de la FK


    }
}