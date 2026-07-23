package com.grupo1.sgsm.ventasYfacturacion.service;

import com.grupo1.sgsm.inventarioYproductos.dto.ProductoConsultaDTO;
import com.grupo1.sgsm.inventarioYproductos.service.IProductoService;
import com.grupo1.sgsm.inventarioYproductos.service.IStockLocalService;
import com.grupo1.sgsm.inventarioYproductos.service.ProductoService;
import com.grupo1.sgsm.inventarioYproductos.service.StockLocalService;
import com.grupo1.sgsm.ventasYfacturacion.dao.DetalleFacturaDAO;
import com.grupo1.sgsm.ventasYfacturacion.dao.FacturaGyeDAO;
import com.grupo1.sgsm.ventasYfacturacion.dto.DetalleFacturaDTO;
import com.grupo1.sgsm.ventasYfacturacion.dto.FacturaGYEConsultadaDTO;
import com.grupo1.sgsm.ventasYfacturacion.dto.InfoProductoDTO;
import com.grupo1.sgsm.ventasYfacturacion.dto.NuevaFacturaDTO;
import com.grupo1.sgsm.ventasYfacturacion.exception.FechasNoValidasException;
import com.grupo1.sgsm.ventasYfacturacion.exception.NoSePudoFacturarException;
import com.grupo1.sgsm.ventasYfacturacion.model.DetalleFactura;
import com.grupo1.sgsm.ventasYfacturacion.model.FacturaGYE;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FacturasGYEServiceImpl implements IFacturasGYEService {

    private FacturaGyeDAO facturaGyeDAO = new FacturaGyeDAO();
    private IProductoService productoService = new ProductoService();
    private DetalleFacturaDAO detalleFacturaDAO = new DetalleFacturaDAO();
    @SuppressWarnings("unused")
    private IStockLocalService stockLocalService = new StockLocalService();

    @Override
    public List<FacturaGYEConsultadaDTO> consultarFacturas(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio != null && fechaFin != null) {
            if (fechaInicio.isAfter(fechaFin) || fechaInicio.isAfter(LocalDate.now())) {
                throw new FechasNoValidasException("Ingrese un rango de fechas valido");
            }
        }

        List<FacturaGYEConsultadaDTO> consultar = new ArrayList<>();
        List<FacturaGYE> facturas = facturaGyeDAO.consultarPorRangoFechas(fechaInicio, fechaFin);
        for (FacturaGYE facturaGYE : facturas) {
            consultar.add(new FacturaGYEConsultadaDTO(facturaGYE.getNumero_factura(), String.valueOf(facturaGYE.getFecha_emision()), facturaGYE.getCedula_ciudadania()));
        }
        return consultar;
    }

    @Override
    public void facturarProductos(NuevaFacturaDTO nuevaFacturaDTO, List<DetalleFacturaDTO> detallesFacturaDTO) {

        try {
            FacturaGYE facturaGYE = new FacturaGYE();
            facturaGYE.setNumero_factura(nuevaFacturaDTO.getNumero_factura());
            facturaGYE.setFecha_emision(nuevaFacturaDTO.getFecha_emision());
            facturaGYE.setIVA(nuevaFacturaDTO.getIVA());
            facturaGYE.setFecha_emision(nuevaFacturaDTO.getFecha_emision());
            facturaGYE.setCodigo_sucursal(nuevaFacturaDTO.getCodigo_sucursal());
            facturaGYE.setSubtotal(nuevaFacturaDTO.getSubtotal());
            facturaGYE.setTotal(nuevaFacturaDTO.getTotal());
            facturaGYE.setCedula_ciudadania(nuevaFacturaDTO.getCedula_ciudadania());

            facturaGyeDAO.insertar(facturaGYE);

            for (DetalleFacturaDTO detalleFacturaDTO : detallesFacturaDTO) {
                DetalleFactura detalle = new DetalleFactura();
                detalle.setCodigo_producto(detalleFacturaDTO.getCodigo_producto());
                detalle.setCantidad(detalleFacturaDTO.getCantidad());
                detalle.setPrecio_unitario(detalleFacturaDTO.getPrecio_unitario());
                detalle.setNumero_factura(facturaGYE.getNumero_factura());
                detalle.setCodigo_sucursal(facturaGYE.getCodigo_sucursal());
                detalle.setSubtotal_producto(detalleFacturaDTO.getSubtotal_producto());
                detalleFacturaDAO.insertar(detalle);
            }
        } catch (Exception e) {
            throw new NoSePudoFacturarException("Error desconocido, no se pudo facturar");
        }

        // TODA LA LOGICA PARA AJUSTAR STOCK A LOS PRODUCTOS
        // for(DetalleFacturaDTO detalleFacturaDTO : detallesFacturaDTO) {
        //     stockLocalService.consultarStockLocal();
        //     stockLocalService.actualizarStock(detalleFacturaDTO.getCodigo_producto(), detalleFacturaDTO.getCantidad());
        // }
    }

    @Override
    public InfoProductoDTO agregarProductoCarrito(String codigoProducto) {
        // LINEA DEL SERVICE DE PRODUCTO PARA CONSULTAR UN STOCK
        // InfoProductoDTO info = productoService.consultarStockProducto(codigoProducto)
        return new InfoProductoDTO("10", "de prueba");
    }

    @Override
    public List<ProductoConsultaDTO> productosParaCarrito() {
        return productoService.obtenerProductosConStockDisponible();
    }
}
