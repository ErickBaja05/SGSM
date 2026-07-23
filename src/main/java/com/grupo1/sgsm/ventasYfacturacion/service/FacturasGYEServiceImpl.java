package com.grupo1.sgsm.ventasYfacturacion.service;

import com.grupo1.sgsm.ventasYfacturacion.dto.FacturaGYEConsultadaDTO;
import com.grupo1.sgsm.ventasYfacturacion.dto.NuevaFacturaDTO;

import java.util.List;

public class FacturasGYEServiceImpl implements IFacturasGYEService {

    @Override
    public List<FacturaGYEConsultadaDTO> consultarFacturas() {
        return List.of();
    }

    @Override
    public void facturarProductos(NuevaFacturaDTO nuevaFacturaDTO) {

    }
}
