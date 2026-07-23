package com.grupo1.sgsm.inventarioYproductos.service;

import java.util.List;
import com.grupo1.sgsm.inventarioYproductos.dto.ConsultaStockLocalDTO;
import com.grupo1.sgsm.inventarioYproductos.dto.DetalleAjusteStockDTO;
import com.grupo1.sgsm.inventarioYproductos.dto.NuevoStockLocalDTO;

public interface IStockLocalService {
    void guardarStockLocal(NuevoStockLocalDTO nuevoStockLocal);
    List<ConsultaStockLocalDTO> consultarStockLocal();
    void actualizarStock(String codigoProducto, int nuevoStock);
    void eliminarStockLocal(String codigoProducto);
    DetalleAjusteStockDTO buscarProductoParaAjuste(String query);
    void ajustarStock(String codigoProducto, int nuevoStock, String codigoSucursal);
    int obtenerStockLocalPorProducto(String codigoProducto);
    ConsultaStockLocalDTO consultarStockLocalPorProducto(String codigoProducto);
}
