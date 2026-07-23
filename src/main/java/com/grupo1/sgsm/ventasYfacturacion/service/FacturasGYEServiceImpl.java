package com.grupo1.sgsm.ventasYfacturacion.service;

import com.grupo1.sgsm.inventarioYproductos.dto.InfoProductoDTO;
import com.grupo1.sgsm.inventarioYproductos.dto.ProductoConsultaDTO;
import com.grupo1.sgsm.inventarioYproductos.service.IProductoService;
import com.grupo1.sgsm.inventarioYproductos.service.IStockLocalService;
import com.grupo1.sgsm.inventarioYproductos.service.ProductoService;
import com.grupo1.sgsm.inventarioYproductos.service.StockLocalService;
import com.grupo1.sgsm.ventasYfacturacion.dao.DetalleFacturaDAO;
import com.grupo1.sgsm.ventasYfacturacion.dao.FacturaGyeDAO;
import com.grupo1.sgsm.ventasYfacturacion.dto.DetalleFacturaDTO;
import com.grupo1.sgsm.ventasYfacturacion.dto.FacturaGYEConsultadaDTO;
import com.grupo1.sgsm.ventasYfacturacion.dto.NuevaFacturaDTO;
import com.grupo1.sgsm.ventasYfacturacion.exception.FechasNoValidasException;
import com.grupo1.sgsm.ventasYfacturacion.exception.NoSePudoFacturarException;
import com.grupo1.sgsm.ventasYfacturacion.model.DetalleFactura;
import com.grupo1.sgsm.ventasYfacturacion.model.FacturaGYE;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FacturasGYEServiceImpl implements IFacturasGYEService {

    // DAOs
    private FacturaGyeDAO facturaGyeDAO = new FacturaGyeDAO();
    private DetalleFacturaDAO detalleFacturaDAO = new DetalleFacturaDAO();

    // Services importados de otros módulos
    private IProductoService productoService = new ProductoService();
    private IStockLocalService stockLocalService = new StockLocalService();

    @Override
    public List<FacturaGYEConsultadaDTO> consultarFacturas(LocalDate fechaInicio, LocalDate fechaFin) {
        if(fechaInicio.isAfter(fechaFin) || fechaInicio.isAfter(LocalDate.now())) {
            throw new FechasNoValidasException("Ingrese un rango de fechas valido");
        }

        List<FacturaGYEConsultadaDTO> consultar = new ArrayList<>();
        List<FacturaGYE> facturas = facturaGyeDAO.consultarPorRangoFechas(fechaInicio, fechaFin);

        for (FacturaGYE facturaGYE : facturas) {
            // Se corrigió el mapeo para enviar la fecha correctamente en lugar de repetir el número de factura
            consultar.add(new FacturaGYEConsultadaDTO(
                    facturaGYE.getNumero_factura(),
                    String.valueOf(facturaGYE.getFecha_emision()),
                    facturaGYE.getCedula_ciudadania()
            ));
        }
        return consultar;
    }

    @Override
    public void facturarProductos(NuevaFacturaDTO nuevaFacturaDTO, List<DetalleFacturaDTO> detallesFacturaDTO) {
        try {
            // 1. Mapear y persistir la cabecera de la factura (Tabla unificada en GYE)
            FacturaGYE facturaGYE = new FacturaGYE();
            facturaGYE.setNumero_factura(nuevaFacturaDTO.getNumero_factura());
            facturaGYE.setCedula_ciudadania(nuevaFacturaDTO.getCedula_ciudadania());
            facturaGYE.setCodigo_sucursal(nuevaFacturaDTO.getCodigo_sucursal());
            facturaGYE.setFecha_emision(nuevaFacturaDTO.getFecha_emision());
            facturaGYE.setTotal(nuevaFacturaDTO.getTotal());
            facturaGYE.setMetodo_pago(nuevaFacturaDTO.getMetodo_pago());
            facturaGYE.setSubtotal(nuevaFacturaDTO.getSubtotal());
            facturaGYE.setIVA(nuevaFacturaDTO.getIVA());

            facturaGyeDAO.insertar(facturaGYE);

            // 2. Iterar sobre los detalles para guardarlos y reducir el stock
            for (DetalleFacturaDTO dto : detallesFacturaDTO) {
                // A) Guardar el detalle de factura
                DetalleFactura detalle = new DetalleFactura();
                detalle.setNumero_factura(nuevaFacturaDTO.getNumero_factura()); // Hereda de la cabecera[cite: 25]
                detalle.setCodigo_producto(dto.getCodigo_producto());
                detalle.setCodigo_sucursal(nuevaFacturaDTO.getCodigo_sucursal()); // Hereda de la cabecera[cite: 25]
                detalle.setCantidad(dto.getCantidad());
                detalle.setPrecio_unitario(dto.getPrecio_unitario());
                detalle.setSubtotal_producto(dto.getSubtotal_producto());

                detalleFacturaDAO.insertar(detalle);

                // B) Lógica de Negocio: Reducir Stock Local en la sucursal de la Costa
                int stockActual = stockLocalService.obtenerStockLocalPorProducto(dto.getCodigo_producto());
                int nuevoStock = stockActual - dto.getCantidad();

                stockLocalService.actualizarStock(dto.getCodigo_producto(), nuevoStock);
            }

        } catch (Exception e) {
            throw new NoSePudoFacturarException("Error al registrar la factura en GYE: " + e.getMessage());
        }
    }

    @Override
    public InfoProductoDTO agregarProductoCarrito(String codigoProducto) {
        // Delega la consulta directamente al Service de Productos
        return productoService.consultarStockProducto(codigoProducto);
    }

    @Override
    public List<ProductoConsultaDTO> productosParaCarrito() {
        // Trae únicamente los productos que tengan stock > 0 listos para la venta[cite: 27]
        return productoService.obtenerProductosConStockDisponible();
    }

    @Override
    public String obtenerSiguienteNumeroFactura() {
        String ultimo = facturaGyeDAO.consultarUltimoNumeroFactura();

        // Si la base está vacía y devuelve null, empezamos la secuencia para Guayaquil (002)
        if (ultimo == null || ultimo.isBlank()) {
            return "002-001-000000001";
        }

        return generarSiguienteNumeroFactura(ultimo);
    }

    private String generarSiguienteNumeroFactura(String ultimoNumero) {
        String[] partes = ultimoNumero.split("-");
        String correlativoStr = partes[2];
        int correlativo = Integer.parseInt(correlativoStr);
        correlativo++;

        String prefijo = partes[0] + "-" + partes[1] + "-";
        int longitudPrefijo = prefijo.length();
        int longitudTotal = 17;
        int longitudCorrelativo = longitudTotal - longitudPrefijo;

        String nuevoCorrelativo = String.format("%0" + longitudCorrelativo + "d", correlativo);
        return prefijo + nuevoCorrelativo;
    }
}