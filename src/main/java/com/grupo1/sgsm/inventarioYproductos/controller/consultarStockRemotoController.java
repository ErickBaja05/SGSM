package com.grupo1.sgsm.inventarioYproductos.controller;

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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon;

import com.grupo1.sgsm.inventarioYproductos.dto.ConsultaStockRemotoDTO;
import com.grupo1.sgsm.inventarioYproductos.service.IStockRemotoService;
import com.grupo1.sgsm.inventarioYproductos.service.StockRemotoService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class consultarStockRemotoController implements Initializable {

    @FXML private TextField txtBuscar;
    @FXML private Label lblSearchIcon;
    @FXML private Label lblLocationIcon;
    @FXML private Label lblReloadIcon;
    @FXML private Button btnRecargar;

    // --- Tabla y Columnas ---
    @FXML private TableView<ConsultaStockRemotoDTO> tbStock;
    @FXML private TableColumn<ConsultaStockRemotoDTO, String> colCodigo;
    @FXML private TableColumn<ConsultaStockRemotoDTO, String> colNombre;
    @FXML private TableColumn<ConsultaStockRemotoDTO, Integer> colStock;
    @FXML private TableColumn<ConsultaStockRemotoDTO, String> colSucursal;

    private final IStockRemotoService stockRemotoService = new StockRemotoService();
    // Lista maestra para la búsqueda dinámica
    private ObservableList<ConsultaStockRemotoDTO> masterData = FXCollections.observableArrayList();

    private FontIcon crearIcono(String iconLiteral, String styleClass) {
        FontIcon icon = new FontIcon(iconLiteral);
        icon.getStyleClass().add(styleClass);
        return icon;
    }

    private void configurarTabla() {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colSucursal.setCellValueFactory(new PropertyValueFactory<>("sucursal"));

        // Mapear el valor del stock
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        // Magia visual: Crear las "píldoras" de stock y pintar de rojo si es bajo
        colStock.setCellFactory(param -> new TableCell<ConsultaStockRemotoDTO, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    Label lblPildora = new Label(String.valueOf(item));
                    lblPildora.setStyle("-fx-padding: 4 12; -fx-background-radius: 12; -fx-font-weight: bold; -fx-font-size: 11px;");

                    // Alerta roja si el stock remoto es menor a 10
                    if (item < 10) {
                        lblPildora.setStyle(lblPildora.getStyle() + "-fx-background-color: #FDF2F2; -fx-text-fill: #D32F2F;");
                    } else {
                        // Stock normal: fondo gris, texto oscuro
                        lblPildora.setStyle(lblPildora.getStyle() + "-fx-background-color: #E2E8F0; -fx-text-fill: #333333;");
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
        ObservableList<ConsultaStockRemotoDTO> filteredData = FXCollections.observableArrayList();

        for (ConsultaStockRemotoDTO p : masterData) {
            if ((p.getCodigo() != null && p.getCodigo().toLowerCase().contains(lowerCaseFilter)) ||
                    (p.getNombre() != null && p.getNombre().toLowerCase().contains(lowerCaseFilter))) {
                filteredData.add(p);
            }
        }
        tbStock.setItems(filteredData);
    }

    @FXML
    void recargarDatos(ActionEvent event) {
        cargarDatosRemotos();
    }

    private void cargarDatosRemotos() {
        try {
            List<ConsultaStockRemotoDTO> datosRemotos = stockRemotoService.consultarStockRemoto();
            masterData.clear();
            if (datosRemotos != null) {
                masterData.addAll(datosRemotos);
            }
            tbStock.setItems(masterData);
        } catch (Exception e) {
            System.err.println("Error al cargar stock remoto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Cargar Íconos
        lblSearchIcon.setGraphic(crearIcono("fa-search", "search-icon-font"));
        lblLocationIcon.setGraphic(crearIcono("fa-map-marker", ""));
        lblReloadIcon.setGraphic(crearIcono("fa-refresh", "action-table-icon"));

        configurarTabla();

        // Configurar Búsqueda en tiempo real
        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            buscarProductoRealTime(newValue);
        });

        // Cargar datos reales desde el nodo remoto
        cargarDatosRemotos();
    }
}