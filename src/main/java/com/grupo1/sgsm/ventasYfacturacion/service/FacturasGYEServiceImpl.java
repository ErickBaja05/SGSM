package com.grupo1.sgsm.ventasYfacturacion.service;

import com.grupo1.sgsm.ventasYfacturacion.dao.FacturaGyeDAO;
import com.grupo1.sgsm.ventasYfacturacion.dto.FacturaGYEConsultadaDTO;
import com.grupo1.sgsm.ventasYfacturacion.dto.NuevaFacturaDTO;
import com.grupo1.sgsm.ventasYfacturacion.exception.FechasNoValidasException;
import com.grupo1.sgsm.ventasYfacturacion.model.FacturaGYE;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FacturasGYEServiceImpl implements IFacturasGYEService {

    private FacturaGyeDAO facturaGyeDAO = new FacturaGyeDAO();

    @Override
    public List<FacturaGYEConsultadaDTO> consultarFacturas(LocalDate fechaInicio, LocalDate fechaFin) {
        if(fechaInicio.isAfter(fechaFin) || fechaInicio.isAfter(LocalDate.now())) {
            throw new FechasNoValidasException("Ingrese un rango de fechas valido");
        }

        List<FacturaGYEConsultadaDTO> consultar = new ArrayList<>();
        List<FacturaGYE> facturas = facturaGyeDAO.consultarPorRangoFechas(fechaInicio, fechaFin);
        for (FacturaGYE facturaGYE : facturas) {
            consultar.add(new FacturaGYEConsultadaDTO(facturaGYE.getNumero_factura(),String.valueOf(facturaGYE.getFecha_emision()),facturaGYE.getCedula_ciudadania()));
        }
        return consultar;
    }

    @Override
    public void facturarProductos(NuevaFacturaDTO nuevaFacturaDTO) {

    }
}
