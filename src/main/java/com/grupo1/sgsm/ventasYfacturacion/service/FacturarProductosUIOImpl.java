package com.grupo1.sgsm.ventasYfacturacion.service;

import com.grupo1.sgsm.core.util.ConfigSucursal;
import com.grupo1.sgsm.inventarioYproductos.dto.InfoProductoDTO;
import com.grupo1.sgsm.inventarioYproductos.dto.ProductoConsultaDTO;
import com.grupo1.sgsm.inventarioYproductos.service.IProductoService;
import com.grupo1.sgsm.inventarioYproductos.service.IStockLocalService;
import com.grupo1.sgsm.inventarioYproductos.service.ProductoService;
import com.grupo1.sgsm.inventarioYproductos.service.StockLocalService;
import com.grupo1.sgsm.ventasYfacturacion.dao.DetalleFacturaDAO;
import com.grupo1.sgsm.ventasYfacturacion.dao.FacturaUIO_ContableDAO;
import com.grupo1.sgsm.ventasYfacturacion.dao.FacturaUIO_OperativoDAO;
import com.grupo1.sgsm.ventasYfacturacion.dto.DetalleFacturaDTOFacturacion;
import com.grupo1.sgsm.ventasYfacturacion.dto.NuevaFacturaDTO;
import com.grupo1.sgsm.ventasYfacturacion.exception.NoSePudoFacturarException;
import com.grupo1.sgsm.ventasYfacturacion.model.DetalleFactura;
import com.grupo1.sgsm.ventasYfacturacion.model.FacturaUIOContable;
import com.grupo1.sgsm.ventasYfacturacion.model.FacturaUIOOperativo;

import java.util.List;

public class FacturarProductosUIOImpl implements IFacturarProductosUIO {

    // DAOs
    private FacturaUIO_ContableDAO uioContableDAO = new FacturaUIO_ContableDAO();
    private FacturaUIO_OperativoDAO uioOperativoDAO = new FacturaUIO_OperativoDAO();
    private DetalleFacturaDAO detalleFacturaDAO = new DetalleFacturaDAO();

    // Services importados de otros módulos
    private IProductoService productoService = new ProductoService();
    private IStockLocalService stockLocalService = new StockLocalService();
    private com.grupo1.sgsm.administracion.gestionParametros.service.IParametrosService parametrosService =
            new com.grupo1.sgsm.administracion.gestionParametros.service.ParametrosServiceImpl();

    @Override
    public double obtenerIVA() {
        return parametrosService.obtenerIVA();
    }

    @Override
    public void facturarProductos(NuevaFacturaDTO nuevaFacturaDTO, List<DetalleFacturaDTOFacturacion> detallesFacturaDTO) {
        try {
            String nodoEjecucion = ConfigSucursal.getSucursalActual().toUpperCase();

            // 1. Mapear y persistir el fragmento CONTABLE
            FacturaUIOContable contable = new FacturaUIOContable();
            contable.setNumero_factura(nuevaFacturaDTO.getNumero_factura());
            contable.setCodigo_sucursal(nodoEjecucion);
            contable.setTotal(nuevaFacturaDTO.getTotal());
            contable.setMetodo_pago(nuevaFacturaDTO.getMetodo_pago());
            contable.setSubtotal(nuevaFacturaDTO.getSubtotal());
            contable.setIVA(nuevaFacturaDTO.getIVA());
            uioContableDAO.insertar(contable);

            // 2. Mapear y persistir el fragmento OPERATIVO
            FacturaUIOOperativo operativo = new FacturaUIOOperativo();
            operativo.setNumero_factura(nuevaFacturaDTO.getNumero_factura());
            operativo.setCodigo_sucursal(nodoEjecucion);
            operativo.setCedula_ciudadania(nuevaFacturaDTO.getCedula_ciudadania());
            operativo.setFecha_emision(nuevaFacturaDTO.getFecha_emision());
            uioOperativoDAO.insertar(operativo);

            // 3. Iterar sobre los detalles para guardarlos y reducir el stock
            for (DetalleFacturaDTOFacturacion dto : detallesFacturaDTO) {
                // A) Guardar el detalle de factura
                DetalleFactura detalle = new DetalleFactura();
                detalle.setNumero_factura(nuevaFacturaDTO.getNumero_factura()); // Hereda cabecera
                detalle.setCodigo_producto(dto.getCodigo_producto());
                detalle.setCodigo_sucursal(nodoEjecucion); // Hereda nodo de ejecución
                detalle.setCantidad(dto.getCantidad());
                detalle.setPrecio_unitario(dto.getPrecio_unitario());
                detalle.setSubtotal_producto(dto.getSubtotal_producto());
                detalleFacturaDAO.insertar(detalle);

                // B) Lógica de Negocio: Reducir Stock Local
                // Obtenemos el stock actual usando la interfaz de Inventario
                int stockActual = stockLocalService.obtenerStockLocalPorProducto(dto.getCodigo_producto());

                // Restamos la cantidad que el cliente está comprando
                int nuevoStock = stockActual - dto.getCantidad();

                // Actualizamos el inventario
                stockLocalService.actualizarStock(dto.getCodigo_producto(), nuevoStock);
            }

        } catch (Exception e) {
            // Si cualquier inserción o consulta falla, envolvemos el error
            // en una excepción personalizada para que el Controller lance una alerta limpia.
            throw new NoSePudoFacturarException("Error al registrar la factura en UIO: " + e.getMessage());
        }
    }

    @Override
    public InfoProductoDTO agregarProductoCarrito(String codigoProducto) {
        // Delega la consulta directamente al Service de Productos
        return productoService.consultarStockProducto(codigoProducto);
    }

    @Override
    public List<ProductoConsultaDTO> productosParaCarrito() {
        // Trae únicamente los productos que tengan stock > 0 listos para la venta
        return productoService.obtenerProductosConStockDisponible();
    }

    @Override
    public String obtenerSiguienteNumeroFactura() {
        String ultimo = uioContableDAO.consultarUltimoNumeroFactura();

        // Si la base está vacía y devuelve null, empezamos la secuencia para Quito (001)
        if (ultimo == null || ultimo.isBlank()) {
            return "001-001-000000001";
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