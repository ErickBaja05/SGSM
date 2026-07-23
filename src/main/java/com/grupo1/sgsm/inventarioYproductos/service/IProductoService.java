package com.grupo1.sgsm.inventarioYproductos.service;

import com.grupo1.sgsm.inventarioYproductos.dto.NuevoProductoDTO;
import com.grupo1.sgsm.inventarioYproductos.dto.ProductoConsultaDTO;
import com.grupo1.sgsm.inventarioYproductos.dto.ProductoMarketingDTO;
import java.util.List;

public interface IProductoService {
    List<ProductoConsultaDTO> obtenerTodosProductos();
    void guardarNuevoProducto(NuevoProductoDTO nuevoProducto);
    void actualizarProducto(ProductoConsultaDTO producto);
    void eliminarProducto(String codigo);
    List<ProductoMarketingDTO> obtenerProductosMarketing();
    void actualizarDescripcionMarketing(String codigo, String descripcion);
}
