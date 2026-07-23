package com.grupo1.sgsm.ventasYfacturacion.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.kordamp.ikonli.javafx.FontIcon;

import com.grupo1.sgsm.ventasYfacturacion.dto.FacturaContableDTO;
import com.grupo1.sgsm.ventasYfacturacion.service.IFacturacionService;
import com.grupo1.sgsm.ventasYfacturacion.service.FacturacionService;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class consultarFacturaContableUIOController implements Initializable {

    // --- Filtros ---
    @FXML private DatePicker dpFechaInicio;
    @FXML private DatePicker dpFechaFin;

    // --- Tabla ---
    @FXML private TableView<FacturaContableDTO> tbFacturas;
    @FXML private TableColumn<FacturaContableDTO, String> colNumeroFactura;
    @FXML private TableColumn<FacturaContableDTO, String> colSucursal;
    @FXML private TableColumn<FacturaContableDTO, String> colSubtotal;
    @FXML private TableColumn<FacturaContableDTO, String> colIva;
    @FXML private TableColumn<FacturaContableDTO, String> colTotal;
    @FXML private TableColumn<FacturaContableDTO, String> colMetodoPago;

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
    private final IFacturacionService facturacionService = new FacturacionService();
    private final ObservableList<FacturaContableDTO> listaFacturas = FXCollections.observableArrayList();
    private FacturaContableDTO facturaSeleccionada;

    private FontIcon crearIcono(String iconLiteral, String styleClass) {
        FontIcon icon = new FontIcon(iconLiteral);
        icon.getStyleClass().add(styleClass);
        return icon;
    }

    private void cargarIconos() {
        lblIconConsultar.setGraphic(crearIcono("fa-search", "btn-primary-icon-font"));
        lblIconVer.setGraphic(crearIcono("fa-eye", ""));
    }

    private void configurarTabla() {
        colNumeroFactura.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNumeroFactura()));
        colSucursal.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSucursal()));
        colMetodoPago.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMetodoPago()));

        // Formateo de monedas
        colSubtotal.setCellValueFactory(data ->
                new SimpleStringProperty(String.format("$ %.2f", data.getValue().getSubtotal())));

        colIva.setCellValueFactory(data ->
                new SimpleStringProperty(String.format("$ %.2f", data.getValue().getIva())));

        colTotal.setCellValueFactory(data ->
                new SimpleStringProperty(String.format("$ %.2f", data.getValue().getTotal())));

        // Darle color verde al texto de la columna TOTAL
        colTotal.setCellFactory(param -> new TableCell<FacturaContableDTO, String>() {
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

        for (FacturaContableDTO fac : listaFacturas) {
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
        tbFacturas.getSelectionModel().clearSelection();
        cargarDatos();
    }

    @FXML
    void verInformacionCompleta(ActionEvent event) {
        if (facturaSeleccionada != null) {
            System.out.println("Cargando detalle de: " + facturaSeleccionada.getNumeroFactura());
            try {
                javafx.scene.layout.Pane contenedorPrincipal = (javafx.scene.layout.Pane) ((Node) event.getSource()).getScene().lookup("#contenedorPrincipal");

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventasYfacturacion/fxml/verDetallesFacturaUIOContable.fxml"));
                Parent root = loader.load();

                // Pasar la factura al controlador de detalles
                verDetallesFacturaUIOContableController controller = loader.getController();
                controller.cargarFacturaContable(facturaSeleccionada.getNumeroFactura(), facturaSeleccionada.getSucursal());

                contenedorPrincipal.getChildren().clear();
                contenedorPrincipal.getChildren().add(root);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void cargarDatos() {
        LocalDate inicio = dpFechaInicio.getValue();
        LocalDate fin = dpFechaFin.getValue();
        try {
            listaFacturas.setAll(facturacionService.obtenerFacturasContables(inicio, fin));
            tbFacturas.setItems(listaFacturas);
            calcularTotalesAcumulados();
        } catch (Exception e) {
            System.err.println("Error al cargar facturas contables: " + e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarIconos();
        configurarTabla();

        double valIva = facturacionService.obtenerIVA();
        int porcIva = (int) Math.round(valIva);
        if (colIva != null) {
            colIva.setText("IVA (" + porcIva + "%)");
        }

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

        cargarDatos();
    }
}