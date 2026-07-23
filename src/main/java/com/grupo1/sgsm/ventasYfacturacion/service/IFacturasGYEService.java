package com.grupo1.sgsm.ventasYfacturacion.service;

import com.grupo1.sgsm.inventarioYproductos.dto.InfoProductoDTO;
import com.grupo1.sgsm.inventarioYproductos.dto.ProductoConsultaDTO;
import com.grupo1.sgsm.ventasYfacturacion.dto.DetalleFacturaDTO;
import com.grupo1.sgsm.ventasYfacturacion.dto.DetalleFacturaDTOFacturacion;
import com.grupo1.sgsm.ventasYfacturacion.dto.FacturaGYEConsultadaDTO;
import com.grupo1.sgsm.ventasYfacturacion.dto.NuevaFacturaDTO;

import java.time.LocalDate;
import java.util.List;

public interface IFacturasGYEService {
    List<FacturaGYEConsultadaDTO> consultarFacturas(LocalDate fechaInicio, LocalDate fechaFin);
    void facturarProductos(NuevaFacturaDTO nuevaFacturaDTO, List<DetalleFacturaDTOFacturacion> detallesFacturaDTO);
    InfoProductoDTO agregarProductoCarrito(String codigoProducto);
    List<ProductoConsultaDTO> productosParaCarrito();
    public String obtenerSiguienteNumeroFactura();


}
