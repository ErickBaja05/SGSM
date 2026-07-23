package com.grupo1.sgsm.inventarioYproductos.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.grupo1.sgsm.administracion.gestionUsuarios.dto.UsuarioSesionDTO;
import com.grupo1.sgsm.core.session.SesionActual;
import com.grupo1.sgsm.core.util.ConfigSucursal;
import com.grupo1.sgsm.inventarioYproductos.dao.InventarioDAO;
import com.grupo1.sgsm.inventarioYproductos.dao.ProductoInfoDAO;
import com.grupo1.sgsm.inventarioYproductos.dto.NuevoStockLocalDTO;
import com.grupo1.sgsm.inventarioYproductos.dto.ConsultaStockLocalDTO;
import com.grupo1.sgsm.inventarioYproductos.dto.DetalleAjusteStockDTO;
import com.grupo1.sgsm.inventarioYproductos.model.Inventario;
import com.grupo1.sgsm.inventarioYproductos.model.Producto;

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
            List<Producto> productos = productoInfoDAO.consultarTodos();
            if (productos != null) {
                for (Producto p : productos) {
                    if (p.getCodigo() != null) {
                        mapaProductos.put(p.getCodigo().trim(), p.getNombre());
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

    @Override
    public DetalleAjusteStockDTO buscarProductoParaAjuste(String query) {
        if (query == null || query.trim().isEmpty()) {
            return null;
        }
        String q = query.trim();

        Producto productoEncontrado = null;
        try {
            List<Producto> productos = productoInfoDAO.consultarTodos();
            if (productos != null) {
                for (Producto p : productos) {
                    if ((p.getCodigo() != null && p.getCodigo().equalsIgnoreCase(q)) ||
                            (p.getNombre() != null && p.getNombre().equalsIgnoreCase(q))) {
                        productoEncontrado = p;
                        break;
                    }
                }
                if (productoEncontrado == null) {
                    for (Producto p : productos) {
                        if ((p.getCodigo() != null
                                && p.getCodigo().toLowerCase().contains(q.toLowerCase())) ||
                                (p.getNombre() != null
                                        && p.getNombre().toLowerCase().contains(q.toLowerCase()))) {
                            productoEncontrado = p;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al consultar la base de datos de productos.", e);
        }

        if (productoEncontrado == null) {
            return null;
        }

        String codigoProd = productoEncontrado.getCodigo();
        String nombre = productoEncontrado.getNombre();
        String categoria = productoEncontrado.getCategoria() != null ? productoEncontrado.getCategoria() : "General";

        String sucursal = obtenerSucursalSesion();
        int stock = 0;

        try {
            Inventario inv = inventarioDAO.consultarPorProducto(codigoProd);
            if (inv != null) {
                stock = inv.getStock();
                if (inv.getCodigo_sucursal() != null && !inv.getCodigo_sucursal().trim().isEmpty()) {
                    sucursal = inv.getCodigo_sucursal().trim().toUpperCase();
                }
            }
        } catch (Exception e) {
            System.err.println("Advertencia al consultar inventario del producto: " + e.getMessage());
        }

        return new DetalleAjusteStockDTO(codigoProd, nombre, categoria, sucursal, stock);
    }

    @Override
    public void ajustarStock(String codigoProducto, int nuevoStock, String codigoSucursal) {
        Inventario invExistente = inventarioDAO.consultarPorProducto(codigoProducto);
        if (invExistente == null) {
            Inventario nuevoInv = new Inventario();
            String sucursalFinal = (codigoSucursal != null && !codigoSucursal.trim().isEmpty())
                    ? codigoSucursal.trim().toUpperCase()
                    : obtenerSucursalSesion();
            nuevoInv.setCodigo_sucursal(sucursalFinal);
            nuevoInv.setCodigo_producto(codigoProducto);
            nuevoInv.setStock(nuevoStock);
            inventarioDAO.insertar(nuevoInv);
        } else {
            inventarioDAO.actualizarStock(codigoProducto, nuevoStock);
        }
    }

    @Override
    public int obtenerStockLocalPorProducto(String codigoProducto) {
        if (codigoProducto == null || codigoProducto.trim().isEmpty()) {
            return 0;
        }
        Inventario inv = inventarioDAO.consultarPorProducto(codigoProducto.trim());
        return (inv != null && inv.getStock() != null) ? inv.getStock() : 0;
    }

    @Override
    public ConsultaStockLocalDTO consultarStockLocalPorProducto(String codigoProducto) {
        if (codigoProducto == null || codigoProducto.trim().isEmpty()) {
            return null;
        }
        String codTrim = codigoProducto.trim();
        Inventario inv = inventarioDAO.consultarPorProducto(codTrim);
        if (inv == null) {
            return null;
        }

        String nombre = "Producto " + codTrim;
        try {
            Producto p = productoInfoDAO.consultarPorCodigo(codTrim);
            if (p != null && p.getNombre() != null) {
                nombre = p.getNombre();
            }
        } catch (Exception e) {
            System.err.println("Advertencia al obtener nombre de producto: " + e.getMessage());
        }

        String codSucursal = (inv.getCodigo_sucursal() != null && !inv.getCodigo_sucursal().isEmpty())
                ? inv.getCodigo_sucursal().toUpperCase()
                : obtenerSucursalSesion();

        return new ConsultaStockLocalDTO(codTrim, nombre, inv.getStock(), codSucursal);
    }

    private String obtenerSucursalSesion() {
        UsuarioSesionDTO usuario = SesionActual.getUsuario();
        if (usuario != null && usuario.getCodigo_sucursal() != null && !usuario.getCodigo_sucursal().isEmpty()) {
            return usuario.getCodigo_sucursal();
        }
        return ConfigSucursal.getSucursalActual();
    }
}
