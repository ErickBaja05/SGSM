package com.grupo1.sgsm.administracion.gestionParametros.service;

import com.grupo1.sgsm.administracion.gestionParametros.dto.SucursalDTO;

import java.util.List;

public interface IParametrosService {

    void registrarSucursal(SucursalDTO nuevaSucursal);
    void actualizarIVA(Double valorIVA);
    Double obtenerIVA();
    void actualizarNombreSucursal(String codigo_sucursal,String nombreSucursal);
    void actualizarTelefonoSucursal(String codigo_sucursal, String telefonoSucursal);
    void actualizarDireccionSucursal(String codigo_sucursal,String direccionSucursal);
    List<SucursalDTO> consultarSucursales();


}
