package com.grupo1.sgsm.ventasYfacturacion.controller;

import com.grupo1.sgsm.clientes.dto.ClienteConsultaDTO;
import com.grupo1.sgsm.clientes.service.ClientesService;
import com.grupo1.sgsm.clientes.service.IClientesService;
import com.grupo1.sgsm.core.util.ConfigSucursal;
import com.grupo1.sgsm.inventarioYproductos.dto.ProductoConsultaDTO;
import com.grupo1.sgsm.ventasYfacturacion.dto.DetalleFacturaDTO;
import com.grupo1.sgsm.ventasYfacturacion.dto.NuevaFacturaDTO;
import com.grupo1.sgsm.ventasYfacturacion.service.FacturarProductosUIOImpl;
import com.grupo1.sgsm.ventasYfacturacion.service.FacturasGYEServiceImpl;
import com.grupo1.sgsm.ventasYfacturacion.service.IFacturarProductosUIO;
import com.grupo1.sgsm.ventasYfacturacion.service.IFacturasGYEService;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class facturarProductosController implements Initializable {

    // --- SECCIÓN CLIENTE ---
    @FXML private TextField txtCedulaRuc;
    @FXML private TextField txtNombreCliente;
    @FXML private TextField txtApellidoCliente;
    @FXML private Button btnBuscarCliente;

    // --- SECCIÓN RESUMEN ---
    @FXML private Label lblSubtotal;
    @FXML private Label lblIva;
    @FXML private Label lblTotal;

    // --- SECCIÓN BÚSQUEDA PRODUCTO ---
    @FXML private TextField txtBuscarProducto;
    @FXML private TableView<ProductoConsultaDTO> tbProductosBuscar;
    @FXML private TableColumn<ProductoConsultaDTO, String> colBusqCod;
    @FXML private TableColumn<ProductoConsultaDTO, String> colBusqNombre;
    @FXML private TableColumn<ProductoConsultaDTO, String> colBusqPrecio;
    @FXML private TableColumn<ProductoConsultaDTO, Void> colBusqAccion;

    // --- SECCIÓN DETALLE FACTURA ---
    @FXML private TableView<ItemCarrito> tbDetalleFactura;
    @FXML private TableColumn<ItemCarrito, String> colDetCod;
    @FXML private TableColumn<ItemCarrito, String> colDetDesc;
    @FXML private TableColumn<ItemCarrito, ItemCarrito> colDetCant;
    @FXML private TableColumn<ItemCarrito, String> colDetPUnit;
    @FXML private TableColumn<ItemCarrito, String> colDetSubtotal;
    @FXML private TableColumn<ItemCarrito, Void> colDetAcciones;

    // --- MÉTODOS DE PAGO ---
    @FXML private ToggleGroup tgMetodoPago;
    @FXML private ToggleButton btnEfectivo;
    @FXML private ToggleButton btnTarjeta;
    @FXML private ToggleButton btnTransf;

    // --- ÍCONOS ---
    @FXML private Label lblIconNuevoCliente;
    @FXML private Label lblIconBuscarCliente;
    @FXML private Label lblIconBuscarProd;
    @FXML private Label lblIconEfectivo;
    @FXML private Label lblIconTarjeta;
    @FXML private Label lblIconTransf;
    @FXML private Label lblIconFacturar;

    // --- SERVICES & ESTADO ---
    private IClientesService clientesService;
    private IFacturarProductosUIO serviceUIO;
    private IFacturasGYEService serviceGYE;
    private String sedeActual;

    // Variables para totales financieros
    private double subtotalGlobal = 0.0;
    private double ivaGlobal = 0.0;
    private double totalGlobal = 0.0;

    // Listas observables mapeadas a DTOs reales
    private ObservableList<ProductoConsultaDTO> masterProductos = FXCollections.observableArrayList();
    private ObservableList<ItemCarrito> detallesFactura = FXCollections.observableArrayList();

    // ==========================================
    // CLASE VIEW-MODEL PARA LA TABLA CARRITO
    // ==========================================
    public static class ItemCarrito {
        private ProductoConsultaDTO producto;
        private int cantidad;

        public ItemCarrito(ProductoConsultaDTO producto, int cantidad) {
            this.producto = producto;
            this.cantidad = cantidad;
        }

        public ProductoConsultaDTO getProducto() { return producto; }
        public int getCantidad() { return cantidad; }
        public void setCantidad(int cantidad) { this.cantidad = cantidad; }
        public double getSubtotal() { return producto.getPrecio() * cantidad; }
    }

    private FontIcon crearIcono(String iconLiteral, String styleClass) {
        FontIcon icon = new FontIcon(iconLiteral);
        icon.getStyleClass().add(styleClass);
        return icon;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarIconos();

        // Inicializar Servicios
        clientesService = new ClientesService();
        sedeActual = ConfigSucursal.getSucursalActual().toUpperCase();

        if ("UIO".equals(sedeActual)) {
            serviceUIO = new FacturarProductosUIOImpl();
        } else {
            serviceGYE = new FacturasGYEServiceImpl();
        }

        configurarTablaBusqueda();
        configurarTablaDetalle();

        // Cargar los productos reales del backend
        cargarProductosDesdeBD();

        // Listeners de búsqueda
        txtBuscarProducto.textProperty().addListener((obs, oldV, newV) -> buscarProductoRealTime(newV));
        btnBuscarCliente.setOnAction(e -> buscarClienteEnBD());
    }

    private void cargarIconos() {
        lblIconNuevoCliente.setGraphic(crearIcono("fa-user-plus", "btn-primary-icon-font"));
        lblIconBuscarCliente.setGraphic(crearIcono("fa-search", "btn-primary-icon-font"));
        lblIconBuscarProd.setGraphic(crearIcono("fa-search", "search-icon-font"));
        lblIconEfectivo.setGraphic(crearIcono("fa-money", "payment-icon-font"));
        lblIconTarjeta.setGraphic(crearIcono("fa-credit-card", "payment-icon-font"));
        lblIconTransf.setGraphic(crearIcono("fa-bank", "payment-icon-font"));
        lblIconFacturar.setGraphic(crearIcono("fa-print", "btn-facturar-icon-font"));
    }

    private void cargarProductosDesdeBD() {
        try {
            List<ProductoConsultaDTO> prodsBD;
            if ("UIO".equals(sedeActual)) {
                prodsBD = serviceUIO.productosParaCarrito();
            } else {
                prodsBD = serviceGYE.productosParaCarrito();
            }
            masterProductos.setAll(prodsBD);
            tbProductosBuscar.setItems(masterProductos);
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Conexión", "No se pudieron cargar los productos del inventario.");
        }
    }

    private void buscarClienteEnBD() {
        String cedulaBuscada = txtCedulaRuc.getText().trim();
        if (cedulaBuscada.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campo Vacío", "Ingrese una cédula o RUC para buscar.");
            return;
        }

        try {
            List<ClienteConsultaDTO> clientes = clientesService.consultarTodosLosClientes();
            boolean encontrado = false;

            for (ClienteConsultaDTO c : clientes) {
                if (c.getCedula().equals(cedulaBuscada)) {
                    txtNombreCliente.setText(c.getNombre());
                    txtApellidoCliente.setText(c.getApellidos());
                    encontrado = true;
                    break;
                }
            }

            if (!encontrado) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "No Encontrado", "El cliente no está registrado en el sistema. Utilice el botón 'Nuevo'.");
                txtNombreCliente.clear();
                txtApellidoCliente.clear();
            }
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Problema de red al buscar el cliente.");
        }
    }

    // ==========================================
    // TABLA "BUSCAR PRODUCTO"
    // ==========================================
    private void configurarTablaBusqueda() {
        colBusqCod.setCellValueFactory(new PropertyValueFactory<>("codigo_producto")); // Asegura que coincida con tu DTO
        colBusqNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colBusqPrecio.setCellValueFactory(data -> new SimpleStringProperty(String.format("$%.2f", data.getValue().getPrecio())));

        colBusqAccion.setCellFactory(param -> new TableCell<ProductoConsultaDTO, Void>() {
            private final Button btnAdd = new Button();
            {
                btnAdd.setGraphic(crearIcono("fa-shopping-cart", "btn-cart-icon"));
                btnAdd.getStyleClass().add("btn-cart");
                btnAdd.setOnAction(event -> {
                    ProductoConsultaDTO p = getTableView().getItems().get(getIndex());
                    agregarAlDetalle(p);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnAdd);
            }
        });
    }

    // ==========================================
    // TABLA "DETALLE FACTURA"
    // ==========================================
    private void configurarTablaDetalle() {
        tbDetalleFactura.setItems(detallesFactura);

        colDetCod.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProducto().getCodigo()));

        colDetDesc.setCellFactory(param -> new TableCell<ItemCarrito, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    ItemCarrito detalle = getTableRow().getItem();
                    VBox box = new VBox();
                    Label lblNombre = new Label(detalle.getProducto().getNombre());
                    lblNombre.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");

                    Label lblSub = new Label("Cat: " + detalle.getProducto().getCategoria());
                    lblSub.setStyle("-fx-font-size: 10px; -fx-text-fill: #888;");

                    box.getChildren().addAll(lblNombre, lblSub);
                    setGraphic(box);
                }
            }
        });

        colDetCant.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue()));
        colDetCant.setCellFactory(param -> new TableCell<ItemCarrito, ItemCarrito>() {
            private final TextField txtCant = new TextField();
            {
                txtCant.getStyleClass().add("table-textfield");
                txtCant.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().matches("\\d*") ? change : null));

                txtCant.textProperty().addListener((obs, oldV, newV) -> {
                    if (getTableRow() != null && getTableRow().getItem() != null) {
                        ItemCarrito det = getTableRow().getItem();
                        int nuevaCant = newV.isEmpty() ? 0 : Integer.parseInt(newV);
                        det.setCantidad(nuevaCant);
                        tbDetalleFactura.refresh();
                        calcularTotales();
                    }
                });
            }
            @Override
            protected void updateItem(ItemCarrito detalle, boolean empty) {
                super.updateItem(detalle, empty);
                if (empty || detalle == null) {
                    setGraphic(null);
                } else {
                    txtCant.setText(String.valueOf(detalle.getCantidad()));
                    setGraphic(txtCant);
                }
            }
        });

        colDetPUnit.setCellValueFactory(data -> new SimpleStringProperty(String.format("$%.2f", data.getValue().getProducto().getPrecio())));
        colDetSubtotal.setCellValueFactory(data -> new SimpleStringProperty(String.format("$%.2f", data.getValue().getSubtotal())));

        colDetAcciones.setCellFactory(param -> new TableCell<ItemCarrito, Void>() {
            private final Button btnDel = new Button();
            {
                btnDel.setGraphic(crearIcono("fa-trash-o", "btn-delete-icon"));
                btnDel.getStyleClass().add("btn-transparent");
                btnDel.setOnAction(event -> {
                    ItemCarrito det = getTableView().getItems().get(getIndex());
                    detallesFactura.remove(det);
                    calcularTotales();
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnDel);
            }
        });
    }

    private void agregarAlDetalle(ProductoConsultaDTO p) {
        for (ItemCarrito det : detallesFactura) {
            if (det.getProducto().getCodigo().equals(p.getCodigo())) {
                det.setCantidad(det.getCantidad() + 1);
                tbDetalleFactura.refresh();
                calcularTotales();
                return;
            }
        }
        detallesFactura.add(new ItemCarrito(p, 1));
        calcularTotales();
    }

    private void buscarProductoRealTime(String query) {
        if (query == null || query.isEmpty()) {
            tbProductosBuscar.setItems(masterProductos);
            return;
        }
        ObservableList<ProductoConsultaDTO> filtrados = FXCollections.observableArrayList();
        for (ProductoConsultaDTO p : masterProductos) {
            if (p.getNombre().toLowerCase().contains(query.toLowerCase()) ||
                    p.getCodigo().toLowerCase().contains(query.toLowerCase())) {
                filtrados.add(p);
            }
        }
        tbProductosBuscar.setItems(filtrados);
    }

    private void calcularTotales() {
        subtotalGlobal = 0;
        for (ItemCarrito d : detallesFactura) {
            subtotalGlobal += d.getSubtotal();
        }
        ivaGlobal = subtotalGlobal * 0.15;
        totalGlobal = subtotalGlobal + ivaGlobal;

        lblSubtotal.setText(String.format("$%.2f", subtotalGlobal));
        lblIva.setText(String.format("$%.2f", ivaGlobal));
        lblTotal.setText(String.format("$%.2f", totalGlobal));
    }

    // ==========================================
    // ORQUESTACIÓN: FACTURACIÓN EN EL BACKEND
    // ==========================================
    @FXML
    void generarFactura(ActionEvent event) {
        // 1. Validaciones
        if (txtCedulaRuc.getText().trim().isEmpty() || txtNombreCliente.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Falta Cliente", "Por favor, busque y seleccione un cliente válido.");
            return;
        }
        if (detallesFactura.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Carrito Vacío", "No puede facturar sin productos en el carrito.");
            return;
        }
        for (ItemCarrito item : detallesFactura) {
            if (item.getCantidad() <= 0) {
                mostrarAlerta(Alert.AlertType.WARNING, "Cantidad Inválida", "La cantidad de los productos debe ser mayor a cero.");
                return;
            }
        }

        // 2. Determinar Método de Pago
        ToggleButton btnPagoSeleccionado = (ToggleButton) tgMetodoPago.getSelectedToggle();
        String metodoPago = "Efectivo"; // Default
        if (btnPagoSeleccionado == btnTarjeta) {
            metodoPago = "Tarjeta de Crédito";
        } else if (btnPagoSeleccionado == btnTransf) {
            metodoPago = "Transferencia";
        }

        try {
            // 3. Obtener correlativo dinámico desde BD
            String numeroFactura;
            if ("UIO".equals(sedeActual)) {
                numeroFactura = serviceUIO.obtenerSiguienteNumeroFactura();
            } else {
                numeroFactura = serviceGYE.obtenerSiguienteNumeroFactura();
            }

            // 4. Mapear DTO Cabecera
            NuevaFacturaDTO nuevaFactura = new NuevaFacturaDTO(
                    numeroFactura,
                    txtCedulaRuc.getText().trim(),
                    sedeActual,
                    LocalDate.now(),
                    totalGlobal,
                    metodoPago,
                    subtotalGlobal,
                    ivaGlobal
            );

            // 5. Mapear DTOs Detalle
            List<DetalleFacturaDTO> listaDetalles = new ArrayList<>();
            for (ItemCarrito item : detallesFactura) {
                listaDetalles.add(new DetalleFacturaDTO(
                        item.getProducto().getCodigo(),
                        sedeActual,
                        item.getCantidad(),
                        item.getProducto().getPrecio(),
                        item.getSubtotal()
                ));
            }

            // 6. Inyectar en el Backend
            if ("UIO".equals(sedeActual)) {
                serviceUIO.facturarProductos(nuevaFactura, listaDetalles);
            } else {
                serviceGYE.facturarProductos(nuevaFactura, listaDetalles);
            }

            mostrarAlerta(Alert.AlertType.INFORMATION, "Factura Registrada", "La factura " + numeroFactura + " ha sido guardada exitosamente y el stock actualizado.");
            limpiarInterfaz();

        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error en Transacción", e.getMessage());
        }
    }

    private void limpiarInterfaz() {
        txtCedulaRuc.clear();
        txtNombreCliente.clear();
        txtApellidoCliente.clear();
        detallesFactura.clear();
        calcularTotales();
        tgMetodoPago.selectToggle(btnEfectivo);
        // Volver a cargar stock real (por las rebajas hechas)
        cargarProductosDesdeBD();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}