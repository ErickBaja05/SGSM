package com.grupo1.sgsm.inventarioYproductos.service;

import com.grupo1.sgsm.inventarioYproductos.dao.ProductoDAO;
import com.grupo1.sgsm.inventarioYproductos.dao.ProductoMarketingDAO;
import com.grupo1.sgsm.inventarioYproductos.dto.NuevoProductoDTO;
import com.grupo1.sgsm.inventarioYproductos.dto.ProductoConsultaDTO;
import com.grupo1.sgsm.inventarioYproductos.dto.ProductoMarketingDTO;
import com.grupo1.sgsm.inventarioYproductos.model.Producto;
import com.grupo1.sgsm.inventarioYproductos.model.ProductoMarketing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoService implements IProductoService {

    private final ProductoDAO productoDAO = new ProductoDAO();
    private final ProductoMarketingDAO productoMarketingDAO = new ProductoMarketingDAO();

    @Override
    public List<ProductoConsultaDTO> obtenerTodosProductos() {
        List<Producto> productos = productoDAO.consultarTodos();
        List<ProductoConsultaDTO> resultado = new ArrayList<>();

        for (Producto p : productos) {
            resultado.add(new ProductoConsultaDTO(
                    p.getCodigo(),
                    p.getNombre(),
                    p.getMarca(),
                    p.getCategoria(),
                    p.getPrecio()
            ));
        }
        return resultado;
    }

    @Override
    public void guardarNuevoProducto(NuevoProductoDTO nuevoProducto) {
        if (nuevoProducto.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio debe ser un número positivo.");
        }

        Producto existente = productoDAO.consultarPorCodigo(nuevoProducto.getCodigo());
        if (existente != null) {
            throw new RuntimeException("Ya existe un producto con el código " + nuevoProducto.getCodigo());
        }

        // 1. Insertar en producto_info
        Producto p = new Producto(
                nuevoProducto.getCodigo(),
                nuevoProducto.getNombre(),
                nuevoProducto.getMarca(),
                nuevoProducto.getCategoria(),
                0.0,
                nuevoProducto.getPrecio()
        );
        productoDAO.insertar(p);

        // 2. Insertar en producto_marketing
        ProductoMarketing pm = new ProductoMarketing();
        pm.setCodigo_producto(nuevoProducto.getCodigo());
        pm.setDescripcion(nuevoProducto.getDescripcion() != null ? nuevoProducto.getDescripcion() : "");
        productoMarketingDAO.insertar(pm);
    }

    @Override
    public void actualizarProducto(ProductoConsultaDTO dto) {
        Producto existente = productoDAO.consultarPorCodigo(dto.getCodigo());
        if (existente == null) {
            throw new RuntimeException("El producto no existe.");
        }

        existente.setNombre(dto.getNombre());
        existente.setMarca(dto.getMarca());
        existente.setCategoria(dto.getCategoria());
        existente.setPrecio(dto.getPrecio());

        productoDAO.actualizar(existente);
    }

    @Override
    public void eliminarProducto(String codigo) {
        Producto existente = productoDAO.consultarPorCodigo(codigo);
        if (existente != null) {
            // Eliminar de marketing primero
            try {
                productoMarketingDAO.eliminarPorCodigo(codigo);
            } catch (Exception e) {
                // Silenciar si no tenía registro de marketing
            }
            // Eliminar de info
            productoDAO.eliminar(codigo);
        }
    }

    @Override
    public List<ProductoMarketingDTO> obtenerProductosMarketing() {
        List<Producto> productos = productoDAO.consultarTodos();
        List<ProductoMarketing> marketings = productoMarketingDAO.consultarTodos();

        Map<String, String> mapDescripciones = new HashMap<>();
        for (ProductoMarketing pm : marketings) {
            if (pm.getCodigo_producto() != null) {
                mapDescripciones.put(pm.getCodigo_producto().trim(), pm.getDescripcion());
            }
        }

        List<ProductoMarketingDTO> resultado = new ArrayList<>();
        for (Producto p : productos) {
            String desc = mapDescripciones.get(p.getCodigo());
            if (desc == null) {
                desc = "";
            }
            resultado.add(new ProductoMarketingDTO(
                    p.getCodigo(),
                    p.getNombre(),
                    desc
            ));
        }
        return resultado;
    }

    @Override
    public void actualizarDescripcionMarketing(String codigo, String descripcion) {
        ProductoMarketing existente = productoMarketingDAO.consultarPorCodigo(codigo);
        if (existente != null) {
            productoMarketingDAO.actualizarDescripcionPorCodigo(codigo, descripcion);
        } else {
            ProductoMarketing pm = new ProductoMarketing();
            pm.setCodigo_producto(codigo);
            pm.setDescripcion(descripcion);
            productoMarketingDAO.insertar(pm);
        }
    }
}
