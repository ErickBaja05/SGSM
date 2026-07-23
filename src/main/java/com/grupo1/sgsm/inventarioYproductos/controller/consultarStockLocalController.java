package com.grupo1.sgsm.inventarioYproductos.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon;

import com.grupo1.sgsm.inventarioYproductos.dto.ConsultaStockLocalDTO;
import com.grupo1.sgsm.inventarioYproductos.service.IStockLocalService;
import com.grupo1.sgsm.inventarioYproductos.service.StockLocalService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class consultarStockLocalController implements Initializable {

    @FXML
    private TextField txtBuscar;
    @FXML
    private Label lblSearchIcon;
    @FXML
    private Label lblSubtitulo;

    // --- Tabla y Columnas ---
    @FXML
    private TableView<ConsultaStockLocalDTO> tbStock;
    @FXML
    private TableColumn<ConsultaStockLocalDTO, String> colCodigo;
    @FXML
    private TableColumn<ConsultaStockLocalDTO, String> colNombre;
    @FXML
    private TableColumn<ConsultaStockLocalDTO, Integer> colStock;
    @FXML
    private TableColumn<ConsultaStockLocalDTO, String> colSucursal;

    private IStockLocalService stockLocalService;
    // Lista maestra para la búsqueda dinámica
    private ObservableList<ConsultaStockLocalDTO> masterData = FXCollections.observableArrayList();

    public consultarStockLocalController() {
        this.stockLocalService = new StockLocalService();
    }

    private FontIcon crearIcono(String iconLiteral, String styleClass) {
        FontIcon icon = new FontIcon(iconLiteral);
        icon.getStyleClass().add(styleClass);
        return icon;
    }

    private void cargarDatos() {
        try {
            List<ConsultaStockLocalDTO> datos = stockLocalService.consultarStockLocal();
            if (datos != null) {
                masterData.setAll(datos);
            } else {
                masterData.clear();
            }
            tbStock.setItems(masterData);
        } catch (Exception e) {
            System.err.println("Error al cargar datos de stock local: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void configurarTabla() {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo_producto"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre_producto"));
        colSucursal.setCellValueFactory(new PropertyValueFactory<>("codigo_sucursal"));

        // Mapear el valor del stock
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        // Magia visual: Crear las "píldoras" de stock y pintar de rojo si es bajo (< 10)
        colStock.setCellFactory(param -> new TableCell<ConsultaStockLocalDTO, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    Label lblPildora = new Label(String.valueOf(item));
                    lblPildora.setStyle(
                            "-fx-padding: 4 12; -fx-background-radius: 12; -fx-font-weight: bold; -fx-font-size: 11px;");

                    if (item < 10) {
                        lblPildora.setStyle(
                                lblPildora.getStyle() + "-fx-background-color: #FDF2F2; -fx-text-fill: #D32F2F;");
                    } else {
                        lblPildora.setStyle(
                                lblPildora.getStyle() + "-fx-background-color: #E2E8F0; -fx-text-fill: #333333;");
                    }

                    HBox box = new HBox(lblPildora);
                    box.setStyle("-fx-alignment: CENTER;");

                    setGraphic(box);
                    setText(null);
                }
            }
        });
    }

    private void buscarProductoRealTime(String query) {
        if (query == null || query.isEmpty()) {
            tbStock.setItems(masterData);
            return;
        }

        String lowerCaseFilter = query.toLowerCase();
        ObservableList<ConsultaStockLocalDTO> filteredData = FXCollections.observableArrayList();

        for (ConsultaStockLocalDTO p : masterData) {
            if ((p.getCodigo_producto() != null && p.getCodigo_producto().toLowerCase().contains(lowerCaseFilter)) ||
                    (p.getNombre_producto() != null
                            && p.getNombre_producto().toLowerCase().contains(lowerCaseFilter))) {
                filteredData.add(p);
            }
        }
        tbStock.setItems(filteredData);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Cargar ícono de lupa
        lblSearchIcon.setGraphic(crearIcono("fa-search", "search-icon-font"));

        String sucursalLocal = com.grupo1.sgsm.core.util.ConfigSucursal.getSucursalActual().toUpperCase();
        if (lblSubtitulo != null) {
            lblSubtitulo.setText("Revisa la disponibilidad de productos en la sucursal " + sucursalLocal + ".");
        }

        configurarTabla();

        // Configurar Búsqueda en tiempo real
        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            buscarProductoRealTime(newValue);
        });

        // Cargar datos reales desde el servicio
        cargarDatos();
    }
}