package com.grupo1.sgsm.ventasYfacturacion.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import org.kordamp.ikonli.javafx.FontIcon;

import com.grupo1.sgsm.administracion.gestionParametros.service.IParametrosService;
import com.grupo1.sgsm.administracion.gestionParametros.service.ParametrosServiceImpl;

public class verDetallesFacturaGYEController {

    private IParametrosService parametrosService = new ParametrosServiceImpl();

    // --- Iconos ---
    @FXML private Label lblIconSede;
    @FXML private Label lblIconVolver;

    // --- Datos Generales de la Factura ---
    @FXML private TextArea txtNumFactura;
    @FXML private TextArea txtCedula;
    @FXML private TextArea txtCliente;
    @FXML private TextArea txtFecha;
    @FXML private TextArea txtSucursal;

    // --- Tabla de Detalles ---
    @FXML private TableView<DetalleProducto> tbDetalle;
    @FXML private TableColumn<DetalleProducto, String> colCodigo;
    @FXML private TableColumn<DetalleProducto, String> colDescripcion;
    @FXML private TableColumn<DetalleProducto, String> colCantidad; // String para poder formatear con "0X"
    @FXML private TableColumn<DetalleProducto, String> colPUnitario;
    @FXML private TableColumn<DetalleProducto, String> colSubtotal;

    // --- Totales ---
    @FXML private Label lblSubtotalNeto;
    @FXML private Label lblTituloIva;
    @FXML private Label lblIva;
    @FXML private Label lblTotalFacturado;

    private ObservableList<DetalleProducto> listaDetalles = FXCollections.observableArrayList();

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

        configurarTabla();

        // (Opcional) Cargar datos de prueba para visualizar el diseño.
        // En producción, esto se llenará cuando llamen al método cargarFactura()
        cargarDatosPrueba();
    }

    private void configurarTabla() {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        // Formatear cantidad para que se vea como "01", "02" según el mockup
        colCantidad.setCellValueFactory(data -> {
            int cant = data.getValue().getCantidad();
            return new SimpleStringProperty(cant < 10 ? "0" + cant : String.valueOf(cant));
        });

        // Formatear precios con el signo de dólar
        colPUnitario.setCellValueFactory(data ->
                new SimpleStringProperty(String.format("$ %.2f", data.getValue().getPrecioUnitario())));

        // Formatear subtotal
        colSubtotal.setCellValueFactory(data -> {
            double sub = data.getValue().getCantidad() * data.getValue().getPrecioUnitario();
            return new SimpleStringProperty(String.format("$ %.2f", sub));
        });

        // Estilo en negrita para la columna de subtotal (como en el mockup)
        colSubtotal.setStyle("-fx-font-weight: bold; -fx-alignment: CENTER;");
    }

    @FXML
    void volverAlListado(ActionEvent event) {
        // Lógica de navegación hacia atrás.
        // Aquí deberías usar tu NavigationUtil o el FXMLLoader para regresar a la vista anterior
        // Por ejemplo:
        // NavigationUtil.loadView("consultarFacturasGYE.fxml", event);

        System.out.println("Regresando a la lista de facturas...");
    }

    /**
     * MÉTODO CLAVE: Llama a este método desde el controlador de "Consultar Facturas"
     * pasándole los datos de la factura que el usuario seleccionó.
     */
    public void cargarFactura(/* Aquí pones tu objeto Factura o el ID */ String numeroFactura, String cedula, String cliente, String fecha) {
        // 1. Llenar la cabecera
        txtNumFactura.setText(numeroFactura);
        txtCedula.setText(cedula);
        txtCliente.setText(cliente);
        txtFecha.setText(fecha);
        txtSucursal.setText("GUAYAQUIL");

        // 2. Aquí harías un SELECT a tu BD buscando los detalles de 'numeroFactura'
        // y llenarías la lista 'listaDetalles'

        // 3. Recalcular los totales
        // calcularTotales();
    }

    // --- Simulación de Datos ---
    private void cargarDatosPrueba() {
        txtNumFactura.setText("FAC-2023-\n08942"); // Simulando el salto de línea
        txtCedula.setText("1726354829");
        txtCliente.setText("ALEJANDRO\nMARTÍNEZ\nRIVAS");
        txtFecha.setText("24 OCT 2023 -\n14:25");
        txtSucursal.setText("GUAYAQUIL");

        listaDetalles.addAll(
                new DetalleProducto("SKU-88219", "Balón de Fútbol Profesional FIFA Pro", 2, 45.00),
                new DetalleProducto("SKU-10293", "Raqueta de Tenis Wilson Ultra V4", 1, 185.00),
                new DetalleProducto("SKU-55402", "Zapatillas Nike Air Zoom Pegasus 40", 1, 120.00),
                new DetalleProducto("SKU-33012", "Set de Pesas 20kg Hierro Fundido", 3, 35.00)
        );
        tbDetalle.setItems(listaDetalles);

        calcularTotales();
    }

    private void calcularTotales() {
        double subtotal = 0;
        for (DetalleProducto d : listaDetalles) {
            subtotal += d.getCantidad() * d.getPrecioUnitario();
        }
        double valIva = parametrosService.obtenerIVA();
        int porcIva = (int) Math.round(valIva);
        if (lblTituloIva != null) {
            lblTituloIva.setText("IVA (" + porcIva + "%)");
        }
        double iva = subtotal * (valIva / 100.0);
        double total = subtotal + iva;

        lblSubtotalNeto.setText(String.format("$ %.2f", subtotal));
        lblIva.setText(String.format("$ %.2f", iva));
        lblTotalFacturado.setText(String.format("$ %.2f", total));
    }

    // --- Clase Auxiliar Interna ---
    public static class DetalleProducto {
        private String codigo;
        private String descripcion;
        private int cantidad;
        private double precioUnitario;

        public DetalleProducto(String codigo, String descripcion, int cantidad, double precioUnitario) {
            this.codigo = codigo; this.descripcion = descripcion; this.cantidad = cantidad; this.precioUnitario = precioUnitario;
        }

        public String getCodigo() { return codigo; }
        public String getDescripcion() { return descripcion; }
        public int getCantidad() { return cantidad; }
        public double getPrecioUnitario() { return precioUnitario; }
    }
}