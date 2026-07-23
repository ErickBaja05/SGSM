package com.grupo1.sgsm.inventarioYproductos.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.grupo1.sgsm.inventarioYproductos.dao.InventarioDAO;
import com.grupo1.sgsm.inventarioYproductos.dao.ProductoInfoDAO;
import com.grupo1.sgsm.inventarioYproductos.dto.NuevoStockLocalDTO;
import com.grupo1.sgsm.inventarioYproductos.dto.ConsultaStockLocalDTO;
import com.grupo1.sgsm.inventarioYproductos.model.Inventario;
import com.grupo1.sgsm.inventarioYproductos.model.ProductoInfo;

public class StockLocalService implements IStockLocalService {

    private final InventarioDAO inventarioDAO = new InventarioDAO();
    private final ProductoInfoDAO productoInfoDAO = new ProductoInfoDAO();

    @Override
    public void guardarStockLocal(NuevoStockLocalDTO nuevoStockLocal) {
        Inventario inv = new Inventario();
        inv.setCodigo_sucursal(nuevoStockLocal.getCodigo_sucursal());
        inv.setCodigo_producto(nuevoStockLocal.getCodigo_producto());
        inv.setStock(nuevoStockLocal.getStock());
        inventarioDAO.insertar(inv);
    }

    @Override
    public List<ConsultaStockLocalDTO> consultarStockLocal() {
        List<Inventario> inventarios = inventarioDAO.consultarTodos();
        
        Map<String, String> mapaProductos = new HashMap<>();
        try {
            List<ProductoInfo> productos = productoInfoDAO.consultarTodos();
            if (productos != null) {
                for (ProductoInfo p : productos) {
                    if (p.getCodigo_producto() != null) {
                        mapaProductos.put(p.getCodigo_producto().trim(), p.getNombre());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Advertencia al consultar información complementaria de productos: " + e.getMessage());
        }

        List<ConsultaStockLocalDTO> listaDTO = new ArrayList<>();
        if (inventarios != null) {
            for (Inventario inv : inventarios) {
                String codProd = inv.getCodigo_producto() != null ? inv.getCodigo_producto().trim() : "";
                String nombre = mapaProductos.getOrDefault(codProd, "Producto " + codProd);
                String codSucursal = (inv.getCodigo_sucursal() != null && !inv.getCodigo_sucursal().isEmpty())
                        ? inv.getCodigo_sucursal().toUpperCase()
                        : "UIO";
                listaDTO.add(new ConsultaStockLocalDTO(codProd, nombre, inv.getStock(), codSucursal));
            }
        }
        return listaDTO;
    }

    @Override
    public void actualizarStock(String codigoProducto, int nuevoStock) {
        inventarioDAO.actualizarStock(codigoProducto, nuevoStock);
    }

    @Override
    public void eliminarStockLocal(String codigoProducto) {
        inventarioDAO.eliminarPorProducto(codigoProducto);
    }
}
