package com.grupo1.sgsm.inventarioYproductos.service;

import java.util.List;
import com.grupo1.sgsm.inventarioYproductos.dto.ConsultaStockLocalDTO;
import com.grupo1.sgsm.inventarioYproductos.dto.NuevoStockLocalDTO;

public interface IStockLocalService {
    void guardarStockLocal(NuevoStockLocalDTO nuevoStockLocal);
    List<ConsultaStockLocalDTO> consultarStockLocal();
    void actualizarStock(String codigoProducto, int nuevoStock);
    void eliminarStockLocal(String codigoProducto);
}
