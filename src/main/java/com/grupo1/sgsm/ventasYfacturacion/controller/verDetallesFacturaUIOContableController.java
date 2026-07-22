package com.grupo1.sgsm.ventasYfacturacion.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.kordamp.ikonli.javafx.FontIcon;

public class verDetallesFacturaUIOContableController {

    // --- Iconos ---
    @FXML private Label lblIconSede;
    @FXML private Label lblIconVolver;
    @FXML private Label lblIconFactura;
    @FXML private Label lblIconSucursal;

    // --- Cajas de Texto ---
    @FXML private TextField txtNumFactura;
    @FXML private TextField txtSucursal;

    // --- Tabla de Detalles ---
    @FXML private TableView<DetalleContable> tbDetalle;
    @FXML private TableColumn<DetalleContable, String> colCodigo;
    @FXML private TableColumn<DetalleContable, String> colDescripcion;
    @FXML private TableColumn<DetalleContable, Integer> colCantidad;
    @FXML private TableColumn<DetalleContable, String> colPUnitario;
    @FXML private TableColumn<DetalleContable, String> colSubtotal;

    // --- Totales ---
    @FXML private Label lblSubtotalNeto;
    @FXML private Label lblIva;
    @FXML private Label lblTotalFacturado;

    private ObservableList<DetalleContable> listaDetalles = FXCollections.observableArrayList();

    private FontIcon crearIcono(String iconLiteral, String styleClass) {
        FontIcon icon = new FontIcon(iconLiteral);
        icon.getStyleClass().add(styleClass);
        return icon;
    }

    @FXML
    public void initialize() {
        // Cargar Íconos
        lblIconSede.setGraphic(crearIcono("fa-map-marker", ""));
        lblIconVolver.setGraphic(crearIcono("fa-arrow-left", ""));
        lblIconFactura.setGraphic(crearIcono("fa-hashtag", ""));
        lblIconSucursal.setGraphic(crearIcono("fa-building-o", ""));

        configurarTabla();

        // Cargar datos de simulación (Esto lo llamarás desde la otra pantalla en producción)
        cargarDatosPrueba();
    }

    private void configurarTabla() {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

        // Formatear precios con el signo de dólar
        colPUnitario.setCellValueFactory(data ->
                new SimpleStringProperty(String.format("$ %.2f", data.getValue().getPrecioUnitario())));

        // Formatear subtotal y aplicar negrita
        colSubtotal.setCellValueFactory(data -> {
            double sub = data.getValue().getCantidad() * data.getValue().getPrecioUnitario();
            return new SimpleStringProperty(String.format("$ %.2f", sub));
        });
        colSubtotal.setStyle("-fx-font-weight: bold; -fx-alignment: CENTER;");
    }

    @FXML
    void volverAlListado(ActionEvent event) {
        System.out.println("Regresando a la lista de facturas contables de UIO...");
        // NavigationUtil.loadView("consultarFacturaContableUIO.fxml", event);
    }

    /**
     * MÉTODO CLAVE: Se llama desde consultarFacturaContableUIOController
     */
    public void cargarFacturaContable(String numeroFactura, String sucursal) {
        txtNumFactura.setText(numeroFactura);
        txtSucursal.setText(sucursal);

        // Aquí harías tu consulta a BD para traer los productos de esa factura
        // listaDetalles.clear();
        // listaDetalles.addAll(...);

        // calcularTotales();
    }

    // --- Simulación de Datos del Mockup ---
    private void cargarDatosPrueba() {
        txtNumFactura.setText("FAC-001-010-0004582");
        txtSucursal.setText("QUITO NORTE - EL CONDADO");

        listaDetalles.addAll(
                new DetalleContable("AD-TRN-V2-BLK", "Zapatos de Entrenamiento Adidas Tech Response V2 - Negro/Gris", 2, 84.99),
                new DetalleContable("NK-DRF-SH-WHT", "Camiseta Nike Dri-FIT Entrenamiento - Blanco Hombre", 3, 32.50),
                new DetalleContable("UA-CP-GL-01", "Guantes de Compresión Under Armour - Talla L", 1, 24.00),
                new DetalleContable("PS-AC-WA-500", "Cilindro Agua 500ml Sport Master - Aluminio", 2, 12.00)
        );
        tbDetalle.setItems(listaDetalles);

        calcularTotales();
    }

    private void calcularTotales() {
        double subtotal = 0;
        for (DetalleContable d : listaDetalles) {
            subtotal += d.getCantidad() * d.getPrecioUnitario();
        }
        double iva = subtotal * 0.12;
        double total = subtotal + iva;

        lblSubtotalNeto.setText(String.format("$ %.2f", subtotal));
        lblIva.setText(String.format("$ %.2f", iva));
        lblTotalFacturado.setText(String.format("$ %.2f", total));
    }

    // --- Clase Auxiliar Interna ---
    public static class DetalleContable {
        private String codigo;
        private String descripcion;
        private int cantidad;
        private double precioUnitario;

        public DetalleContable(String codigo, String descripcion, int cantidad, double precioUnitario) {
            this.codigo = codigo; this.descripcion = descripcion; this.cantidad = cantidad; this.precioUnitario = precioUnitario;
        }

        public String getCodigo() { return codigo; }
        public String getDescripcion() { return descripcion; }
        public int getCantidad() { return cantidad; }
        public double getPrecioUnitario() { return precioUnitario; }
    }
}