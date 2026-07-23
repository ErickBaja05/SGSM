package com.grupo1.sgsm.ventasYfacturacion.service;

import com.grupo1.sgsm.ventasYfacturacion.dto.FacturaGYEConsultadaDTO;
import com.grupo1.sgsm.ventasYfacturacion.dto.NuevaFacturaDTO;

import java.time.LocalDate;
import java.util.List;

public interface IFacturasGYEService {
    List<FacturaGYEConsultadaDTO> consultarFacturas(LocalDate fechaInicio, LocalDate fechaFin);
    void facturarProductos(NuevaFacturaDTO nuevaFacturaDTO);


}
