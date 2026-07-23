package com.grupo1.sgsm.ventasYfacturacion.service;

import com.grupo1.sgsm.inventarioYproductos.dto.InfoProductoDTO;
import com.grupo1.sgsm.inventarioYproductos.dto.ProductoConsultaDTO;
import com.grupo1.sgsm.ventasYfacturacion.dto.DetalleFacturaDTO;
import com.grupo1.sgsm.ventasYfacturacion.dto.DetalleFacturaDTOFacturacion;
import com.grupo1.sgsm.ventasYfacturacion.dto.NuevaFacturaDTO;

import java.util.List;

public interface IFacturarProductosUIO {
    void facturarProductos(NuevaFacturaDTO nuevaFacturaDTO, List<DetalleFacturaDTOFacturacion> detallesFacturaDTO);
    InfoProductoDTO agregarProductoCarrito(String codigoProducto);
    List<ProductoConsultaDTO> productosParaCarrito();
    String obtenerSiguienteNumeroFactura();
}
