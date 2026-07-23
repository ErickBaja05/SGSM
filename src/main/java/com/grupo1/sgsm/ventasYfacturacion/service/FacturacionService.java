package com.grupo1.sgsm.ventasYfacturacion.service;

import com.grupo1.sgsm.ventasYfacturacion.dao.FacturaUIO_ContableDAO;
import com.grupo1.sgsm.ventasYfacturacion.dao.FacturaUIO_OperativoDAO;
import com.grupo1.sgsm.ventasYfacturacion.dto.DetalleFacturaDTO;
import com.grupo1.sgsm.ventasYfacturacion.dto.FacturaContableDTO;
import com.grupo1.sgsm.ventasYfacturacion.dto.FacturaOperativaDTO;

import java.time.LocalDate;
import java.util.List;

public class FacturacionService implements IFacturacionService {

    private final FacturaUIO_OperativoDAO facturaOperativoDAO = new FacturaUIO_OperativoDAO();
    private final FacturaUIO_ContableDAO facturaContableDAO = new FacturaUIO_ContableDAO();

    @Override
    public List<FacturaOperativaDTO> obtenerFacturasOperativas(LocalDate inicio, LocalDate fin) {
        return facturaOperativoDAO.consultarFacturasOperativas(inicio, fin);
    }

    @Override
    public List<FacturaContableDTO> obtenerFacturasContables(LocalDate inicio, LocalDate fin) {
        return facturaContableDAO.consultarFacturasContables(inicio, fin);
    }

    @Override
    public List<DetalleFacturaDTO> obtenerDetallesFactura(String numeroFactura) {
        if (numeroFactura == null || numeroFactura.trim().isEmpty()) {
            throw new IllegalArgumentException("El número de factura no puede ser nulo o vacío");
        }
        return facturaContableDAO.consultarDetallesFactura(numeroFactura.trim());
    }
}
