package com.grupo1.sgsm.ventasYfacturacion.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.kordamp.ikonli.javafx.FontIcon;

import com.grupo1.sgsm.ventasYfacturacion.dto.FacturaOperativaDTO;
import com.grupo1.sgsm.ventasYfacturacion.service.IFacturacionService;
import com.grupo1.sgsm.ventasYfacturacion.service.FacturacionService;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class consultarFacturasUIOOperativoController implements Initializable {

    // --- Filtros ---
    @FXML private DatePicker dpFechaInicio;
    @FXML private DatePicker dpFechaFin;
    @FXML private Button btnFiltrar;

    // --- Tabla ---
    @FXML private TableView<FacturaOperativaDTO> tbFacturas;
    @FXML private TableColumn<FacturaOperativaDTO, String> colNumeroFactura;
    @FXML private TableColumn<FacturaOperativaDTO, String> colFechaEmision;
    @FXML private TableColumn<FacturaOperativaDTO, String> colCedulaCliente;

    // --- Paginación y Footer ---
    @FXML private Label lblPaginacion;
    @FXML private Label lblFacturaSeleccionada;
    @FXML private Button btnVerCompleta;

    // --- Íconos ---
    @FXML private Label lblIconFiltrar;
    @FXML private Label lblIconVer;

    // --- Variables de Estado ---
    private final IFacturacionService facturacionService = new FacturacionService();
    private final ObservableList<FacturaOperativaDTO> listaFacturas = FXCollections.observableArrayList();
    private FacturaOperativaDTO facturaActualSeleccionada;

    private FontIcon crearIcono(String iconLiteral, String styleClass) {
        FontIcon icon = new FontIcon(iconLiteral);
        icon.getStyleClass().add(styleClass);
        return icon;
    }

    private void cargarIconos() {
        lblIconFiltrar.setGraphic(crearIcono("fa-filter", "btn-primary-icon-font"));
        if (lblIconVer != null) {
            lblIconVer.setGraphic(crearIcono("fa-eye", "btn-primary-icon-font"));
        }
    }

    private void configurarTabla() {
        colNumeroFactura.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNumeroFactura()));
        colFechaEmision.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFechaEmision()));
        colCedulaCliente.setCellValueFactory(cellData -> {
            String cedula = cellData.getValue().getCedulaCliente();
            String nombre = cellData.getValue().getNombreCliente();
            if (nombre != null && !nombre.isEmpty() && !nombre.equalsIgnoreCase("Consumidor Final")) {
                return new SimpleStringProperty(cedula + " (" + nombre + ")");
            }
            return new SimpleStringProperty(cedula);
        });
    }

    @FXML
    void filtrarFacturas(ActionEvent event) {
        tbFacturas.getSelectionModel().clearSelection();
        cargarDatos();
    }

    @FXML
    void verInformacionCompleta(ActionEvent event) {
        if (facturaActualSeleccionada != null) {
            System.out.println("Abriendo información completa de la factura operativa: " + facturaActualSeleccionada.getNumeroFactura());
            // Nota: En la versión operativa no hay una vista de detalles específica implementada en FXML,
            // pero si se requiriera, se cargaría aquí.
        }
    }

    private void cargarDatos() {
        LocalDate inicio = dpFechaInicio.getValue();
        LocalDate fin = dpFechaFin.getValue();
        try {
            listaFacturas.setAll(facturacionService.obtenerFacturasOperativas(inicio, fin));
            tbFacturas.setItems(listaFacturas);
            lblPaginacion.setText("Mostrando " + listaFacturas.size() + " comprobantes");
        } catch (Exception e) {
            System.err.println("Error al cargar facturas operativas: " + e.getMessage());
            lblPaginacion.setText("Error al cargar comprobantes");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarIconos();
        configurarTabla();

        // Listener para detectar fila seleccionada y activar botón de detalles
        tbFacturas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                facturaActualSeleccionada = newSelection;
                if (btnVerCompleta != null) btnVerCompleta.setDisable(false);
                if (lblFacturaSeleccionada != null) {
                    lblFacturaSeleccionada.setText("Factura seleccionada: " + newSelection.getNumeroFactura());
                    lblFacturaSeleccionada.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #00796B;");
                }
            } else {
                facturaActualSeleccionada = null;
                if (btnVerCompleta != null) btnVerCompleta.setDisable(true);
                if (lblFacturaSeleccionada != null) {
                    lblFacturaSeleccionada.setText("Factura seleccionada: Ninguna");
                    lblFacturaSeleccionada.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #555555;");
                }
            }
        });

        cargarDatos();
    }
}