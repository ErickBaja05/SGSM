package com.grupo1.sgsm.ventasYfacturacion.service;

import com.grupo1.sgsm.ventasYfacturacion.dto.DetalleFacturaDTO;
import com.grupo1.sgsm.ventasYfacturacion.dto.FacturaContableDTO;
import com.grupo1.sgsm.ventasYfacturacion.dto.FacturaOperativaDTO;

import java.time.LocalDate;
import java.util.List;

public interface IFacturacionService {
    List<FacturaOperativaDTO> obtenerFacturasOperativas(LocalDate inicio, LocalDate fin);
    List<FacturaContableDTO> obtenerFacturasContables(LocalDate inicio, LocalDate fin);
    List<DetalleFacturaDTO> obtenerDetallesFactura(String numeroFactura);
    FacturaContableDTO obtenerFacturaContablePorNumero(String numeroFactura);
    double obtenerIVA();
}
