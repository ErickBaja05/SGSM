package com.grupo1.sgsm.ventasYfacturacion.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class consultarFacturasUIOOperativoController implements Initializable {

    // --- Filtros ---
    @FXML private DatePicker dpFechaInicio;
    @FXML private DatePicker dpFechaFin;
    @FXML private Button btnFiltrar;

    // --- Tabla ---
    @FXML private TableView<FacturaResumen> tbFacturas;
    @FXML private TableColumn<FacturaResumen, String> colNumeroFactura;
    @FXML private TableColumn<FacturaResumen, String> colFechaEmision;
    @FXML private TableColumn<FacturaResumen, String> colCedulaCliente;

    // --- Paginación y Footer ---
    @FXML private Label lblPaginacion;


    // --- Íconos ---
    @FXML private Label lblIconFiltrar;


    // --- Variables de Estado ---
    private ObservableList<FacturaResumen> listaFacturas = FXCollections.observableArrayList();

    // AQUÍ SE GUARDA LA FACTURA SELECCIONADA PARA MANDARLA A LA OTRA VENTANA
    private FacturaResumen facturaActualSeleccionada;

    private FontIcon crearIcono(String iconLiteral, String styleClass) {
        FontIcon icon = new FontIcon(iconLiteral);
        icon.getStyleClass().add(styleClass);
        return icon;
    }

    private void cargarIconos() {
        lblIconFiltrar.setGraphic(crearIcono("fa-filter", "btn-primary-icon-font"));

    }

    private void configurarTabla() {
        colNumeroFactura.setCellValueFactory(new PropertyValueFactory<>("numeroFactura"));
        colFechaEmision.setCellValueFactory(new PropertyValueFactory<>("fechaEmision"));
        colCedulaCliente.setCellValueFactory(new PropertyValueFactory<>("cedulaCliente"));
    }

    @FXML
    void filtrarFacturas(ActionEvent event) {
        LocalDate fechaInicio = dpFechaInicio.getValue();
        LocalDate fechaFin = dpFechaFin.getValue();

        // Lógica de BD: Aquí harías tu consulta SELECT con los parámetros de fecha.
        System.out.println("Filtrando facturas desde " + fechaInicio + " hasta " + fechaFin);

        // Al volver a cargar datos, es buena práctica limpiar la selección actual
        tbFacturas.getSelectionModel().clearSelection();
    }

    @FXML
    void verInformacionCompleta(ActionEvent event) {
        if (facturaActualSeleccionada != null) {
            System.out.println("Abriendo información completa de la factura: " + facturaActualSeleccionada.getNumeroFactura());

            // Aquí puedes llamar a tu NavigationUtil o cargar el nuevo FXML
            // pasándole el objeto 'facturaActualSeleccionada' al controlador de destino.
        }
    }

    // --- Datos de Prueba ---
    private void cargarDatosPrueba() {
        listaFacturas.addAll(
                new FacturaResumen("FAC-001-002-000456123", "15/10/2023", "1726543210"),
                new FacturaResumen("FAC-001-002-000456124", "15/10/2023", "0912345678"),
                new FacturaResumen("FAC-001-002-000456125", "14/10/2023", "1718273645"),
                new FacturaResumen("FAC-001-002-000456126", "14/10/2023", "0809876543"),
                new FacturaResumen("FAC-001-002-000456127", "13/10/2023", "1314151617")
        );
        tbFacturas.setItems(listaFacturas);
        lblPaginacion.setText("Mostrando " + listaFacturas.size() + " de 156 comprobantes");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarIconos();
        configurarTabla();


        // (Opcional) Cargar datos de prueba al iniciar si así lo deseas
        cargarDatosPrueba();
    }

    // --- Modelo Auxiliar ---
    public static class FacturaResumen {
        private String numeroFactura;
        private String fechaEmision;
        private String cedulaCliente;

        public FacturaResumen(String numeroFactura, String fechaEmision, String cedulaCliente) {
            this.numeroFactura = numeroFactura;
            this.fechaEmision = fechaEmision;
            this.cedulaCliente = cedulaCliente;
        }

        public String getNumeroFactura() { return numeroFactura; }
        public String getFechaEmision() { return fechaEmision; }
        public String getCedulaCliente() { return cedulaCliente; }
    }
}