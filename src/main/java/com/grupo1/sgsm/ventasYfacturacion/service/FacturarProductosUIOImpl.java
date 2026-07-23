package com.grupo1.sgsm.ventasYfacturacion.service;

import com.grupo1.sgsm.inventarioYproductos.dto.ProductoConsultaDTO;
import com.grupo1.sgsm.inventarioYproductos.service.IProductoService;
import com.grupo1.sgsm.inventarioYproductos.service.IStockLocalService;
import com.grupo1.sgsm.inventarioYproductos.service.ProductoService;
import com.grupo1.sgsm.inventarioYproductos.service.StockLocalService;
import com.grupo1.sgsm.ventasYfacturacion.dao.DetalleFacturaDAO;
import com.grupo1.sgsm.ventasYfacturacion.dao.FacturaGyeDAO;
import com.grupo1.sgsm.ventasYfacturacion.dao.FacturaUIO_ContableDAO;
import com.grupo1.sgsm.ventasYfacturacion.dao.FacturaUIO_OperativoDAO;
import com.grupo1.sgsm.ventasYfacturacion.dto.DetalleFacturaDTO;
import com.grupo1.sgsm.ventasYfacturacion.dto.InfoProductoDTO;
import com.grupo1.sgsm.ventasYfacturacion.dto.NuevaFacturaDTO;

import java.util.List;

public class FacturarProductosUIOImpl implements IFacturarProductosUIO {

    private FacturaUIO_ContableDAO uioContableDAO= new FacturaGyeDAO();
    private FacturaUIO_OperativoDAO uioOperativoDAO = new ProductoService();
    private DetalleFacturaDAO detalleFacturaDAO = new DetalleFacturaDAO();
    private IStockLocalService stockLocalService = new StockLocalService();

    @Override
    public void facturarProductos(NuevaFacturaDTO nuevaFacturaDTO, List<DetalleFacturaDTO> detallesFacturaDTO) {

    }

    @Override
    public InfoProductoDTO agregarProductoCarrito(String codigoProducto) {
        return null;
    }

    @Override
    public List<ProductoConsultaDTO> productosParaCarrito() {
        return List.of();
    }
}
