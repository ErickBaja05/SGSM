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
import javafx.scene.control.TextArea;
import org.kordamp.ikonli.javafx.FontIcon;

import com.grupo1.sgsm.administracion.gestionParametros.service.IParametrosService;
import com.grupo1.sgsm.administracion.gestionParametros.service.ParametrosServiceImpl;
import com.grupo1.sgsm.ventasYfacturacion.dto.DetalleFacturaDTO;
import com.grupo1.sgsm.ventasYfacturacion.service.IFacturasGYEService;
import com.grupo1.sgsm.ventasYfacturacion.service.FacturasGYEServiceImpl;

public class verDetallesFacturaGYEController {

    private IFacturasGYEService facturasGYEService = new FacturasGYEServiceImpl();

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
    @FXML private TableView<DetalleFacturaDTO> tbDetalle;
    @FXML private TableColumn<DetalleFacturaDTO, String> colCodigo;
    @FXML private TableColumn<DetalleFacturaDTO, String> colDescripcion;
    @FXML private TableColumn<DetalleFacturaDTO, String> colCantidad;
    @FXML private TableColumn<DetalleFacturaDTO, String> colPUnitario;
    @FXML private TableColumn<DetalleFacturaDTO, String> colSubtotal;

    // --- Totales ---
    @FXML private Label lblSubtotalNeto;
    @FXML private Label lblTituloIva;
    @FXML private Label lblIva;
    @FXML private Label lblTotalFacturado;

    private ObservableList<DetalleFacturaDTO> listaDetalles = FXCollections.observableArrayList();

    private FontIcon crearIcono(String iconLiteral, String styleClass) {
        FontIcon icon = new FontIcon(iconLiteral);
        icon.getStyleClass().add(styleClass);
        return icon;
    }

    @FXML
    public void initialize() {
        lblIconSede.setGraphic(crearIcono("fa-map-marker", ""));
        lblIconVolver.setGraphic(crearIcono("fa-arrow-left", ""));

        configurarTabla();
    }

    private void configurarTabla() {
        colCodigo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodigo()));
        colDescripcion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescripcion()));

        colCantidad.setCellValueFactory(data -> {
            int cant = data.getValue().getCantidad();
            return new SimpleStringProperty(cant < 10 ? "0" + cant : String.valueOf(cant));
        });

        colPUnitario.setCellValueFactory(data ->
                new SimpleStringProperty(String.format("$ %.2f", data.getValue().getPrecioUnitario())));

        colSubtotal.setCellValueFactory(data -> {
            double sub = data.getValue().getCantidad() * data.getValue().getPrecioUnitario();
            return new SimpleStringProperty(String.format("$ %.2f", sub));
        });

        colSubtotal.setStyle("-fx-font-weight: bold; -fx-alignment: CENTER;");
    }

    @FXML
    void volverAlListado(ActionEvent event) {
        System.out.println("Regresando a la lista de facturas GYE...");
        try {
            javafx.scene.layout.Pane contenedorPrincipal = (javafx.scene.layout.Pane) ((Node) event.getSource()).getScene().lookup("#contenedorPrincipal");
            Parent root = javafx.fxml.FXMLLoader.load(getClass().getResource("/ventasYfacturacion/fxml/consultarFacturasGYE.fxml"));
            contenedorPrincipal.getChildren().clear();
            contenedorPrincipal.getChildren().add(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private com.grupo1.sgsm.ventasYfacturacion.dao.FacturaGyeDAO facturaGyeDAO = new com.grupo1.sgsm.ventasYfacturacion.dao.FacturaGyeDAO();
    private com.grupo1.sgsm.ventasYfacturacion.model.FacturaGYE facturaGYEGuardada;

    public void cargarFactura(String numeroFactura, String cedula, String cliente, String fecha) {
        txtNumFactura.setText(numeroFactura);
        txtCedula.setText(cedula != null ? cedula : "");
        txtCliente.setText(cliente != null && !cliente.isEmpty() ? cliente : "Consumidor Final");
        txtFecha.setText(fecha != null ? fecha : "");
        txtSucursal.setText("GUAYAQUIL");

        try {
            facturaGYEGuardada = facturaGyeDAO.consultarPorNumero(numeroFactura);
            listaDetalles.setAll(facturasGYEService.obtenerDetallesFactura(numeroFactura));
            tbDetalle.setItems(listaDetalles);
            calcularTotales();
        } catch (Exception e) {
            System.err.println("Error al cargar detalles de la factura GYE " + numeroFactura + ": " + e.getMessage());
        }
    }

    private void calcularTotales() {
        double subtotal = 0;
        for (DetalleFacturaDTO d : listaDetalles) {
            subtotal += d.getCantidad() * d.getPrecioUnitario();
        }

        double valIva;
        double ivaMonto;

        if (facturaGYEGuardada != null && facturaGYEGuardada.getSubtotal() > 0 && facturaGYEGuardada.getIVA() > 0) {
            ivaMonto = facturaGYEGuardada.getIVA();
            valIva = (ivaMonto / facturaGYEGuardada.getSubtotal()) * 100.0;
        } else if (subtotal > 0) {
            valIva = facturasGYEService.obtenerIVA();
            ivaMonto = subtotal * (valIva / 100.0);
        } else {
            valIva = facturasGYEService.obtenerIVA();
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