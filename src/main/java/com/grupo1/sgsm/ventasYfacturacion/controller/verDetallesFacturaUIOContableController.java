package com.grupo1.sgsm.ventasYfacturacion.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.kordamp.ikonli.javafx.FontIcon;

import com.grupo1.sgsm.ventasYfacturacion.dto.DetalleFacturaDTO;
import com.grupo1.sgsm.ventasYfacturacion.service.IFacturacionService;
import com.grupo1.sgsm.ventasYfacturacion.service.FacturacionService;

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
    @FXML private TableView<DetalleFacturaDTO> tbDetalle;
    @FXML private TableColumn<DetalleFacturaDTO, String> colCodigo;
    @FXML private TableColumn<DetalleFacturaDTO, String> colDescripcion;
    @FXML private TableColumn<DetalleFacturaDTO, Integer> colCantidad;
    @FXML private TableColumn<DetalleFacturaDTO, String> colPUnitario;
    @FXML private TableColumn<DetalleFacturaDTO, String> colSubtotal;

    // --- Totales ---
    @FXML private Label lblSubtotalNeto;
    @FXML private Label lblTituloIva;
    @FXML private Label lblIva;
    @FXML private Label lblTotalFacturado;

    private final IFacturacionService facturacionService = new FacturacionService();
    private final ObservableList<DetalleFacturaDTO> listaDetalles = FXCollections.observableArrayList();

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
    }

    private void configurarTabla() {
        colCodigo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodigo()));
        colDescripcion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescripcion()));
        colCantidad.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getCantidad()).asObject());

        // Formatear precios con el signo de dólar
        colPUnitario.setCellValueFactory(data ->
                new SimpleStringProperty(String.format("$ %.2f", data.getValue().getPrecioUnitario())));

        // Formatear subtotal
        colSubtotal.setCellValueFactory(data -> {
            double sub = data.getValue().getCantidad() * data.getValue().getPrecioUnitario();
            return new SimpleStringProperty(String.format("$ %.2f", sub));
        });
        colSubtotal.setStyle("-fx-font-weight: bold; -fx-alignment: CENTER;");
    }

    @FXML
    void volverAlListado(ActionEvent event) {
        System.out.println("Regresando a la lista de facturas de UIO...");
        try {
            javafx.scene.layout.Pane contenedorPrincipal = (javafx.scene.layout.Pane) ((Node) event.getSource()).getScene().lookup("#contenedorPrincipal");
            String vista = "/ventasYfacturacion/fxml/consultarFacturaContableUIO.fxml";
            if (com.grupo1.sgsm.core.session.SesionActual.getUsuario() != null) {
                String rol = com.grupo1.sgsm.core.session.SesionActual.getUsuario().getRol();
                if ("CAJERO".equalsIgnoreCase(rol) || "ADMINISTRACION".equalsIgnoreCase(rol)) {
                    vista = "/ventasYfacturacion/fxml/consultarFacturasUIOOperativo.fxml";
                }
            }
            Parent root = javafx.fxml.FXMLLoader.load(getClass().getResource(vista));
            contenedorPrincipal.getChildren().clear();
            contenedorPrincipal.getChildren().add(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * MÉTODO CLAVE: Se llama desde consultarFacturaContableUIOController
     */
    private com.grupo1.sgsm.ventasYfacturacion.dto.FacturaContableDTO facturaGuardada;

    /**
     * MÉTODO CLAVE: Se llama desde consultarFacturaContableUIOController
     */
    public void cargarFacturaContable(String numeroFactura, String sucursal) {
        txtNumFactura.setText(numeroFactura);
        txtSucursal.setText(sucursal);

        try {
            facturaGuardada = facturacionService.obtenerFacturaContablePorNumero(numeroFactura);
            listaDetalles.setAll(facturacionService.obtenerDetallesFactura(numeroFactura));
            tbDetalle.setItems(listaDetalles);
            calcularTotales();
        } catch (Exception e) {
            System.err.println("Error al cargar detalles de la factura " + numeroFactura + ": " + e.getMessage());
        }
    }

    private void calcularTotales() {
        double subtotal = 0;
        for (DetalleFacturaDTO d : listaDetalles) {
            subtotal += d.getCantidad() * d.getPrecioUnitario();
        }

        double valIva;
        double ivaMonto;

        if (facturaGuardada != null && facturaGuardada.getSubtotal() > 0 && facturaGuardada.getIva() > 0) {
            ivaMonto = facturaGuardada.getIva();
            valIva = (ivaMonto / facturaGuardada.getSubtotal()) * 100.0;
        } else if (subtotal > 0) {
            valIva = facturacionService.obtenerIVA();
            ivaMonto = subtotal * (valIva / 100.0);
        } else {
            valIva = facturacionService.obtenerIVA();
            ivaMonto = 0;
        }

        int porcIva = (int) Math.round(valIva);
        if (lblTituloIva != null) {
            lblTituloIva.setText("IVA (" + porcIva + "%)");
        }
        double total = subtotal + ivaMonto;

        lblSubtotalNeto.setText(String.format("$ %.2f", subtotal));
        lblIva.setText(String.format("$ %.2f", ivaMonto));
        lblTotalFacturado.setText(String.format("$ %.2f", total));
    }
}