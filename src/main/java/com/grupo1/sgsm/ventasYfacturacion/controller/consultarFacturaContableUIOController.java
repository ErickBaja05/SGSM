package com.grupo1.sgsm.ventasYfacturacion.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.ResourceBundle;

public class consultarFacturaContableUIOController implements Initializable {

    // --- Filtros ---
    @FXML private DatePicker dpFechaInicio;
    @FXML private DatePicker dpFechaFin;

    // --- Tabla ---
    @FXML private TableView<FacturaContable> tbFacturas;
    @FXML private TableColumn<FacturaContable, String> colNumeroFactura;
    @FXML private TableColumn<FacturaContable, String> colSucursal;
    @FXML private TableColumn<FacturaContable, String> colSubtotal;
    @FXML private TableColumn<FacturaContable, String> colIva;
    @FXML private TableColumn<FacturaContable, String> colTotal;
    @FXML private TableColumn<FacturaContable, String> colMetodoPago;

    // --- Totales Acumulados ---
    @FXML private Label lblTotalSubtotal;
    @FXML private Label lblTotalIva;
    @FXML private Label lblTotalGeneral;

    // --- Botones e Íconos ---
    @FXML private Button btnConsultar;
    @FXML private Button btnVerInformacion;
    @FXML private Label lblIconConsultar;
    @FXML private Label lblIconVer;

    // --- Estado ---
    private ObservableList<FacturaContable> listaFacturas = FXCollections.observableArrayList();
    private FacturaContable facturaSeleccionada;

    private FontIcon crearIcono(String iconLiteral, String styleClass) {
        FontIcon icon = new FontIcon(iconLiteral);
        icon.getStyleClass().add(styleClass);
        return icon;
    }


    private void cargarIconos() {
        lblIconConsultar.setGraphic(crearIcono("fa-search", "btn-primary-icon-font"));
        lblIconVer.setGraphic(crearIcono("fa-eye", "")); // Se hereda estilo en línea
    }

    private void configurarTabla() {
        colNumeroFactura.setCellValueFactory(new PropertyValueFactory<>("numeroFactura"));
        colSucursal.setCellValueFactory(new PropertyValueFactory<>("sucursal"));
        colMetodoPago.setCellValueFactory(new PropertyValueFactory<>("metodoPago"));

        // Formateo de monedas
        colSubtotal.setCellValueFactory(data ->
                new SimpleStringProperty(String.format("$ %.2f", data.getValue().getSubtotal())));

        colIva.setCellValueFactory(data ->
                new SimpleStringProperty(String.format("$ %.2f", data.getValue().getIva())));

        colTotal.setCellValueFactory(data ->
                new SimpleStringProperty(String.format("$ %.2f", data.getValue().getTotal())));

        // Darle color verde al texto de la columna TOTAL
        colTotal.setCellFactory(param -> new TableCell<FacturaContable, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: #00796B; -fx-font-weight: bold; -fx-alignment: CENTER;");
                }
            }
        });
    }

    private void calcularTotalesAcumulados() {
        double sumSubtotal = 0;
        double sumIva = 0;
        double sumTotal = 0;

        for (FacturaContable fac : listaFacturas) {
            sumSubtotal += fac.getSubtotal();
            sumIva += fac.getIva();
            sumTotal += fac.getTotal();
        }

        lblTotalSubtotal.setText(String.format("$ %.2f", sumSubtotal));
        lblTotalIva.setText(String.format("$ %.2f", sumIva));
        lblTotalGeneral.setText(String.format("$ %.2f", sumTotal));
    }

    @FXML
    void consultarFacturas(ActionEvent event) {
        System.out.println("Consultando base de datos financiera...");
        // Al recargar datos, limpiamos la selección para que el botón se deshabilite
        tbFacturas.getSelectionModel().clearSelection();

        // Recalcular barra de totales
        calcularTotalesAcumulados();
    }

    @FXML
    void verInformacionCompleta(ActionEvent event) {
        if (facturaSeleccionada != null) {
            System.out.println("Abriendo detalle de: " + facturaSeleccionada.getNumeroFactura());
            // Lógica para abrir nueva ventana con la factura seleccionada
        }
    }

    // --- Simulación de Datos ---
    private void cargarDatosPrueba() {
        listaFacturas.addAll(
                new FacturaContable("FAC-2024-001245", "Quito - Norte", 1250.00, 187.50, "Tarjeta de Crédito"),
                new FacturaContable("FAC-2024-001246", "Guayaquil - Samborondón", 840.00, 126.00, "Transferencia Bancaria"),
                new FacturaContable("FAC-2024-001247", "Quito - Norte", 310.50, 46.58, "Efectivo")
        );
        tbFacturas.setItems(listaFacturas);
        calcularTotalesAcumulados();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarIconos();
        configurarTabla();

        // Listener para habilitar/deshabilitar el botón de "Ver Información"
        tbFacturas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                facturaSeleccionada = newSelection;
                btnVerInformacion.setDisable(false);
            } else {
                facturaSeleccionada = null;
                btnVerInformacion.setDisable(true);
            }
        });

        // (Opcional) Cargar datos de prueba
        cargarDatosPrueba();
    }

    // --- Clase Modelo Interna ---
    public static class FacturaContable {
        private String numeroFactura;
        private String sucursal;
        private double subtotal;
        private double iva;
        private double total;
        private String metodoPago;

        public FacturaContable(String numeroFactura, String sucursal, double subtotal, double iva, String metodoPago) {
            this.numeroFactura = numeroFactura;
            this.sucursal = sucursal;
            this.subtotal = subtotal;
            this.iva = iva;
            this.total = subtotal + iva;
            this.metodoPago = metodoPago;
        }

        public String getNumeroFactura() { return numeroFactura; }
        public String getSucursal() { return sucursal; }
        public double getSubtotal() { return subtotal; }
        public double getIva() { return iva; }
        public double getTotal() { return total; }
        public String getMetodoPago() { return metodoPago; }
    }
}