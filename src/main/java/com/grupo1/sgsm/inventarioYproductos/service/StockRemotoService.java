package com.grupo1.sgsm.inventarioYproductos.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.grupo1.sgsm.inventarioYproductos.dao.InventarioDAO;
import com.grupo1.sgsm.inventarioYproductos.dao.ProductoInfoDAO;
import com.grupo1.sgsm.inventarioYproductos.dto.ConsultaStockRemotoDTO;
import com.grupo1.sgsm.inventarioYproductos.model.Inventario;
import com.grupo1.sgsm.inventarioYproductos.model.Producto;

public class StockRemotoService implements IStockRemotoService {

    private final InventarioDAO inventarioDAO = new InventarioDAO();
    private final ProductoInfoDAO productoInfoDAO = new ProductoInfoDAO();

    @Override
    public List<ConsultaStockRemotoDTO> consultarStockRemoto() {
        List<Inventario> inventariosRemotos = inventarioDAO.consultarRemoto();

        Map<String, String> mapaProductos = new HashMap<>();
        try {
            List<Producto> productos = productoInfoDAO.consultarTodos();
            if (productos != null) {
                for (Producto p : productos) {
                    if (p.getCodigo() != null) {
                        mapaProductos.put(p.getCodigo().trim(), p.getNombre());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Advertencia al consultar catálogo de productos para stock remoto: " + e.getMessage());
        }

        List<ConsultaStockRemotoDTO> listaDTO = new ArrayList<>();
        if (inventariosRemotos != null) {
            for (Inventario inv : inventariosRemotos) {
                String codProd = inv.getCodigo_producto() != null ? inv.getCodigo_producto().trim() : "";
                String nombre = mapaProductos.getOrDefault(codProd, "Producto " + codProd);
                String codSucursal = (inv.getCodigo_sucursal() != null && !inv.getCodigo_sucursal().isEmpty())
                        ? inv.getCodigo_sucursal().toUpperCase()
                        : "REMOTO";
                listaDTO.add(new ConsultaStockRemotoDTO(codProd, nombre, inv.getStock(), codSucursal));
            }
        }
        return listaDTO;
    }
}
