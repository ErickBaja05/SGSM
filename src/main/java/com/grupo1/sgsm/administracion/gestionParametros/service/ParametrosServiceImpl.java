package com.grupo1.sgsm.administracion.gestionParametros.service;

import com.grupo1.sgsm.administracion.gestionParametros.dao.SucursalDAO;
import com.grupo1.sgsm.administracion.gestionParametros.dto.SucursalDTO;
import com.grupo1.sgsm.administracion.gestionParametros.exception.CodigoSucursalNoValidoException;
import com.grupo1.sgsm.administracion.gestionParametros.exception.SucursalYaExisteException;
import com.grupo1.sgsm.administracion.gestionParametros.exception.TelefonoNoValidoException;
import com.grupo1.sgsm.administracion.gestionParametros.model.Sucursal;
import com.grupo1.sgsm.core.session.SesionActual;

import java.util.ArrayList;
import java.util.List;

public class ParametrosServiceImpl implements IParametrosService{

    private SucursalDAO sucursalDAO = new SucursalDAO();

    @Override
    public void registrarSucursal(SucursalDTO nuevaSucursal) {

        if(sucursalDAO.consultarPorCodigoSucursal(nuevaSucursal.getCodigo_sucursal())!=null){
            throw new SucursalYaExisteException("Ya existe una sucursal con el código proporcionado");
        }

        if(nuevaSucursal.getCodigo_sucursal().length() != 3){
            throw new CodigoSucursalNoValidoException("El código de sucursal debe tener exactamente 3 caracteres");
        }

        if(nuevaSucursal.getTelefono().length() != 10){
            throw new TelefonoNoValidoException("El número de telefono debe tener exactamente 10 caracteres");
        }

        Sucursal sucursal = new Sucursal();
        sucursal.setCodigo_sucursal(nuevaSucursal.getCodigo_sucursal());
        sucursal.setNombre(nuevaSucursal.getNombre());
        sucursal.setTelefono(nuevaSucursal.getTelefono());
        sucursal.setDireccion(nuevaSucursal.getDireccion());
        sucursal.setCiudad(nuevaSucursal.getCiudad());

        sucursalDAO.registrarSucursal(sucursal);

    }

    @Override
    public void actualizarIVA(Double valorIVA) {

        Double nuevoIva = valorIVA / 100;
        SesionActual.setValorIva(valorIVA);

    }

    @Override
    public void actualizarNombreSucursal(String codigo_sucursal, String nombreSucursal) {

        sucursalDAO.actualizarNombrePorCodigoSucursal(codigo_sucursal,nombreSucursal);

    }

    @Override
    public void actualizarTelefonoSucursal(String codigo_sucursal, String telefonoSucursal) {
        sucursalDAO.actualizarNombrePorCodigoSucursal(codigo_sucursal,telefonoSucursal);
    }

    @Override
    public void actualizarDireccionSucursal( String codigo_sucursal, String direccionSucursal) {
        sucursalDAO.actualizarDireccionPorCodigoSucursal(codigo_sucursal,direccionSucursal);
    }

    @Override
    public List<SucursalDTO> consultarSucursales() {


        List<SucursalDTO> sucursales = new ArrayList<>();
        List<Sucursal> consultadas = sucursalDAO.consultarTodasLasSucursales();

        for(Sucursal suc : consultadas){
            sucursales.add(new SucursalDTO(suc.getCodigo_sucursal(),suc.getDireccion(),suc.getNombre(),suc.getCiudad(),suc.getTelefono()));
        }
        return sucursales;
    }
}
