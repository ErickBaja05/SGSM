package com.grupo1.sgsm.ventasYfacturacion.controller;

import com.grupo1.sgsm.ventasYfacturacion.model.DetalleVenta;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import com.grupo1.sgsm.inventarioYproductos.model.Producto;

import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.ResourceBundle;



import com.grupo1.sgsm.administracion.gestionParametros.service.IParametrosService;
import com.grupo1.sgsm.administracion.gestionParametros.service.ParametrosServiceImpl;

public class facturarProductosController implements Initializable {

    private IParametrosService parametrosService = new ParametrosServiceImpl();

    // --- SECCIÓN CLIENTE ---
    @FXML private TextField txtCedulaRuc;
    @FXML private TextField txtNombreCliente;
    @FXML private TextField txtApellidoCliente;

    // --- SECCIÓN RESUMEN ---
    @FXML private Label lblSubtotal;
    @FXML private Label lblTituloIva;
    @FXML private Label lblIva;
    @FXML private Label lblTotal;

    // --- SECCIÓN BÚSQUEDA PRODUCTO ---
    @FXML private TextField txtBuscarProducto;
    @FXML private TableView<Producto> tbProductosBuscar;
    @FXML private TableColumn<Producto, String> colBusqCod;
    @FXML private TableColumn<Producto, String> colBusqNombre;
    @FXML private TableColumn<Producto, String> colBusqPrecio;
    @FXML private TableColumn<Producto, Void> colBusqAccion;

    // --- SECCIÓN DETALLE FACTURA ---
    @FXML private TableView<DetalleVenta> tbDetalleFactura;
    @FXML private TableColumn<DetalleVenta, String> colDetCod;
    @FXML private TableColumn<DetalleVenta, String> colDetDesc;
    @FXML private TableColumn<DetalleVenta, DetalleVenta> colDetCant; // Recibe el objeto entero para el TextField
    @FXML private TableColumn<DetalleVenta, String> colDetPUnit;
    @FXML private TableColumn<DetalleVenta, String> colDetSubtotal;
    @FXML private TableColumn<DetalleVenta, Void> colDetAcciones;

    // --- ÍCONOS ---
    @FXML private Label lblIconNuevoCliente;
    @FXML private Label lblIconBuscarCliente;
    @FXML private Label lblIconBuscarProd;
    @FXML private Label lblIconEfectivo;
    @FXML private Label lblIconTarjeta;
    @FXML private Label lblIconTransf;
    @FXML private Label lblIconFacturar;

    // Listas de datos
    private ObservableList<Producto> masterProductos = FXCollections.observableArrayList();
    private ObservableList<DetalleVenta> detallesFactura = FXCollections.observableArrayList();

    private FontIcon crearIcono(String iconLiteral, String styleClass) {
        FontIcon icon = new FontIcon(iconLiteral);
        icon.getStyleClass().add(styleClass);
        return icon;
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

    // ==========================================
    // CONFIGURACIÓN DE LA TABLA "BUSCAR PRODUCTO"
    // ==========================================
    private void configurarTablaBusqueda() {
        colBusqCod.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colBusqNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colBusqPrecio.setCellValueFactory(data -> new SimpleStringProperty(String.format("$%.2f", data.getValue().getPrecio())));

        // Botón de carrito de compras
        colBusqAccion.setCellFactory(param -> new TableCell<Producto, Void>() {
            private final Button btnAdd = new Button();
            {
                btnAdd.setGraphic(crearIcono("fa-shopping-cart", "btn-cart-icon"));
                btnAdd.getStyleClass().add("btn-cart");
                btnAdd.setOnAction(event -> {
                    Producto p = getTableView().getItems().get(getIndex());
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
    // CONFIGURACIÓN DE LA TABLA "DETALLE FACTURA"
    // ==========================================
    private void configurarTablaDetalle() {
        tbDetalleFactura.setItems(detallesFactura);

        colDetCod.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProducto().getCodigo()));

        // Celda personalizada para mostrar Nombre y abajo un texto extra (Stock/Cat)
        colDetDesc.setCellValueFactory(new PropertyValueFactory<>("productoNombre")); // Retorna dummy, se dibuja en CellFactory
        colDetDesc.setCellFactory(param -> new TableCell<DetalleVenta, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    DetalleVenta detalle = getTableRow().getItem();
                    VBox box = new VBox();
                    Label lblNombre = new Label(detalle.getProducto().getNombre());
                    lblNombre.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");

                    Label lblSub = new Label("Stock: " + detalle.getProducto().getStock() + " | Cat: " + detalle.getProducto().getCategoria());
                    lblSub.setStyle("-fx-font-size: 10px; -fx-text-fill: #888;");

                    box.getChildren().addAll(lblNombre, lblSub);
                    setGraphic(box);
                }
            }
        });

        // La magia de la cantidad editable (solo números)
        colDetCant.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue()));
        colDetCant.setCellFactory(param -> new TableCell<DetalleVenta, DetalleVenta>() {
            private final TextField txtCant = new TextField();
            {
                txtCant.getStyleClass().add("table-textfield");
                // Filtro para aceptar solo números
                txtCant.setTextFormatter(new TextFormatter<>(change ->
                        change.getControlNewText().matches("\\d*") ? change : null));

                // Listener cuando cambia el texto
                txtCant.textProperty().addListener((obs, oldV, newV) -> {
                    if (getTableRow() != null && getTableRow().getItem() != null) {
                        DetalleVenta det = getTableRow().getItem();
                        int nuevaCant = newV.isEmpty() ? 0 : Integer.parseInt(newV);
                        det.setCantidad(nuevaCant);
                        // Refrescar la tabla para actualizar subtotales
                        tbDetalleFactura.refresh();
                        calcularTotales();
                    }
                });
            }
            @Override
            protected void updateItem(DetalleVenta detalle, boolean empty) {
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

        // El subtotal de la fila
        colDetSubtotal.setCellValueFactory(data -> {
            double sub = data.getValue().getCantidad() * data.getValue().getProducto().getPrecio();
            return new SimpleStringProperty(String.format("$%.2f", sub));
        });

        // Botón Eliminar Fila (Basurero)
        colDetAcciones.setCellFactory(param -> new TableCell<DetalleVenta, Void>() {
            private final Button btnDel = new Button();
            {
                btnDel.setGraphic(crearIcono("fa-trash-o", "btn-delete-icon"));
                btnDel.getStyleClass().add("btn-transparent");
                btnDel.setOnAction(event -> {
                    DetalleVenta det = getTableView().getItems().get(getIndex());
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

    private void agregarAlDetalle(Producto p) {
        // Verifica si ya existe en la factura
        for (DetalleVenta det : detallesFactura) {
            if (det.getProducto().getCodigo().equals(p.getCodigo())) {
                det.setCantidad(det.getCantidad() + 1);
                tbDetalleFactura.refresh();
                calcularTotales();
                return;
            }
        }
        // Si no existe, lo agrega
        detallesFactura.add(new DetalleVenta(p, 1));
        calcularTotales();
    }

    private void buscarProductoRealTime(String query) {
        if (query == null || query.isEmpty()) {
            tbProductosBuscar.setItems(masterProductos);
            return;
        }
        ObservableList<Producto> filtrados = FXCollections.observableArrayList();
        for (Producto p : masterProductos) {
            if (p.getNombre().toLowerCase().contains(query.toLowerCase()) ||
                    p.getCodigo().toLowerCase().contains(query.toLowerCase())) {
                filtrados.add(p);
            }
        }
        tbProductosBuscar.setItems(filtrados);
    }

    private void calcularTotales() {
        double subtotal = 0;
        for (DetalleVenta d : detallesFactura) {
            subtotal += d.getCantidad() * d.getProducto().getPrecio();
        }
        double valIva = parametrosService.obtenerIVA();
        int porcIva = (int) Math.round(valIva);
        if (lblTituloIva != null) {
            lblTituloIva.setText("IVA (" + porcIva + "%)");
        }
        double iva = subtotal * (valIva / 100.0);
        double total = subtotal + iva;

        lblSubtotal.setText(String.format("$%.2f", subtotal));
        lblIva.setText(String.format("$%.2f", iva));
        lblTotal.setText(String.format("$%.2f", total));
    }

    @FXML
    void generarFactura(ActionEvent event) {
        System.out.println("Enviando factura a BD...");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarIconos();
        configurarTablaBusqueda();
        configurarTablaDetalle();

        double valIva = parametrosService.obtenerIVA();
        int porcIva = (int) Math.round(valIva);
        if (lblTituloIva != null) {
            lblTituloIva.setText("IVA (" + porcIva + "%)");
        }

        // Búsqueda en tiempo real
        txtBuscarProducto.textProperty().addListener((obs, oldV, newV) -> {
            buscarProductoRealTime(newV);
        });

        // Simulación de carga (Esto lo borrarás cuando conectes tu BD)
        masterProductos.addAll(
                new Producto("1042", "Balón de Fútbol Profesional FIFA Q Pro", "Nike", "12", 12.00,14.66),
                new Producto("8891", "Zapatillas Running Pro Elite V2", "Adidas", "5", 12.00,13.66),
                new Producto("4520", "Camiseta Deportiva Transpirable Azul", "Marathon", "6", 12.00,14.66)
        );
        tbProductosBuscar.setItems(masterProductos);
    }
}