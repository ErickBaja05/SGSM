package com.grupo1.sgsm.inventarioYproductos.service;

import com.grupo1.sgsm.inventarioYproductos.dao.InventarioDAO;
import com.grupo1.sgsm.inventarioYproductos.dao.ProductoInfoDAO;
import com.grupo1.sgsm.inventarioYproductos.dao.ProductoMarketingDAO;
import com.grupo1.sgsm.inventarioYproductos.dto.InfoProductoDTO;
import com.grupo1.sgsm.inventarioYproductos.dto.NuevoProductoDTO;
import com.grupo1.sgsm.inventarioYproductos.dto.ProductoConsultaDTO;
import com.grupo1.sgsm.inventarioYproductos.dto.ProductoMarketingDTO;
import com.grupo1.sgsm.inventarioYproductos.model.Inventario;
import com.grupo1.sgsm.inventarioYproductos.model.Producto;
import com.grupo1.sgsm.inventarioYproductos.model.ProductoMarketing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoService implements IProductoService {

    private final ProductoInfoDAO productoInfoDAO = new ProductoInfoDAO();
    private final ProductoMarketingDAO productoMarketingDAO = new ProductoMarketingDAO();
    private final InventarioDAO inventarioDAO = new InventarioDAO();

    @Override
    public List<ProductoConsultaDTO> obtenerTodosProductos() {
        List<Producto> productos = productoInfoDAO.consultarTodos();
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

        Producto existente = productoInfoDAO.consultarPorCodigo(nuevoProducto.getCodigo());
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
        productoInfoDAO.insertar(p);

        // 2. Insertar en producto_marketing
        ProductoMarketing pm = new ProductoMarketing();
        pm.setCodigo_producto(nuevoProducto.getCodigo());
        pm.setDescripcion(nuevoProducto.getDescripcion() != null ? nuevoProducto.getDescripcion() : "");
        productoMarketingDAO.insertar(pm);
    }

    @Override
    public void actualizarProducto(ProductoConsultaDTO dto) {
        Producto existente = productoInfoDAO.consultarPorCodigo(dto.getCodigo());
        if (existente == null) {
            throw new RuntimeException("El producto no existe.");
        }

        existente.setNombre(dto.getNombre());
        existente.setMarca(dto.getMarca());
        existente.setCategoria(dto.getCategoria());
        existente.setPrecio(dto.getPrecio());

        productoInfoDAO.actualizar(existente);
    }

    @Override
    public void eliminarProducto(String codigo) {
        Producto existente = productoInfoDAO.consultarPorCodigo(codigo);
        if (existente != null) {
            // Eliminar de marketing primero
            try {
                productoMarketingDAO.eliminarPorCodigo(codigo);
            } catch (Exception e) {
                // Silenciar si no tenía registro de marketing
            }
            // Eliminar de info
            productoInfoDAO.eliminar(codigo);
        }
    }

    @Override
    public List<ProductoMarketingDTO> obtenerProductosMarketing() {
        List<Producto> productos = productoInfoDAO.consultarTodos();
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

    @Override
    public InfoProductoDTO consultarStockProducto(String codigoProducto) {
        Producto producto = productoInfoDAO.consultarPorCodigo(codigoProducto);
        String categoria = (producto != null && producto.getCategoria() != null) ? producto.getCategoria() : "";

        int stock = 0;
        try {
            Inventario inv = inventarioDAO.consultarPorProducto(codigoProducto);
            if (inv != null) {
                stock = inv.getStock();
            }
        } catch (Exception e) {
            System.err.println("Advertencia al consultar inventario para " + codigoProducto + ": " + e.getMessage());
        }

        return new InfoProductoDTO(String.valueOf(stock), categoria);
    }

    @Override
    public List<ProductoConsultaDTO> obtenerProductosConStockDisponible() {
        List<Inventario> inventarios = inventarioDAO.consultarTodos();
        Map<String, Integer> mapStock = new HashMap<>();
        if (inventarios != null) {
            for (Inventario inv : inventarios) {
                if (inv.getCodigo_producto() != null && inv.getStock() != null) {
                    mapStock.put(inv.getCodigo_producto().trim(), inv.getStock());
                }
            }
        }

        List<Producto> productos = productoInfoDAO.consultarTodos();
        List<ProductoConsultaDTO> resultado = new ArrayList<>();

        if (productos != null) {
            for (Producto p : productos) {
                String cod = (p.getCodigo() != null) ? p.getCodigo().trim() : "";
                int stock = mapStock.getOrDefault(cod, 0);
                if (stock > 0) {
                    resultado.add(new ProductoConsultaDTO(
                            p.getCodigo(),
                            p.getNombre(),
                            p.getMarca(),
                            p.getCategoria(),
                            p.getPrecio()
                    ));
                }
            }
        }
        return resultado;
    }
}
