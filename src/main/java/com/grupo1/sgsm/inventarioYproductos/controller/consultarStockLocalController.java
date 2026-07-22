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

import java.net.URL;
import java.util.ResourceBundle;

public class consultarStockLocalController implements Initializable {

    @FXML private TextField txtBuscar;
    @FXML private Label lblSearchIcon;

    // --- Tabla y Columnas ---
    @FXML private TableView<StockLocal> tbStock;
    @FXML private TableColumn<StockLocal, String> colCodigo;
    @FXML private TableColumn<StockLocal, String> colNombre;
    @FXML private TableColumn<StockLocal, Integer> colStock;
    @FXML private TableColumn<StockLocal, String> colSucursal;

    // Lista maestra para la búsqueda dinámica
    private ObservableList<StockLocal> masterData = FXCollections.observableArrayList();

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
        colStock.setCellFactory(param -> new TableCell<StockLocal, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    Label lblPildora = new Label(String.valueOf(item));
                    lblPildora.setStyle("-fx-padding: 4 12; -fx-background-radius: 12; -fx-font-weight: bold; -fx-font-size: 11px;");

                    // Si el stock es menor a 10, lo pintamos de rojo/rosado como en el mockup
                    if (item < 10) {
                        lblPildora.setStyle(lblPildora.getStyle() + "-fx-background-color: #FDF2F2; -fx-text-fill: #D32F2F;");
                    } else {
                        // Stock normal: fondo gris, texto oscuro
                        lblPildora.setStyle(lblPildora.getStyle() + "-fx-background-color: #E2E8F0; -fx-text-fill: #333333;");
                    }

                    // Centramos el label dentro de la celda
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
        ObservableList<StockLocal> filteredData = FXCollections.observableArrayList();

        for (StockLocal p : masterData) {
            if (p.getCodigo().toLowerCase().contains(lowerCaseFilter) ||
                    p.getNombre().toLowerCase().contains(lowerCaseFilter)) {
                filteredData.add(p);
            }
        }
        tbStock.setItems(filteredData);
    }

    // --- Clase Auxiliar (Reemplaza con tu modelo real) ---
    private void cargarDatosPrueba() {
        masterData.addAll(
                new StockLocal("SM-SHO-001", "Zapatillas de Running X1", 45, "UIO"),
                new StockLocal("SM-APP-104", "Camiseta Técnica Pro (Talla M)", 120, "UIO"),
                new StockLocal("SM-ACC-055", "Mochila Deportiva 30L", 3, "UIO"), // Este saldrá rojo
                new StockLocal("SM-EQU-221", "Balón de Fútbol Sala N°4", 28, "UIO"),
                new StockLocal("SM-SHO-089", "Botines Césped Sintético Evo", 15, "UIO")
        );
        tbStock.setItems(masterData);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Cargar ícono de lupa
        lblSearchIcon.setGraphic(crearIcono("fa-search", "search-icon-font"));

        configurarTabla();

        // Configurar Búsqueda en tiempo real
        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            buscarProductoRealTime(newValue);
        });

        // Cargar datos (Esto se reemplaza con tu consulta SQL a la sede UIO)
        cargarDatosPrueba();
    }

    // Clase Modelo Interna
    public static class StockLocal {
        private String codigo;
        private String nombre;
        private int stock;
        private String sucursal;

        public StockLocal(String codigo, String nombre, int stock, String sucursal) {
            this.codigo = codigo; this.nombre = nombre; this.stock = stock; this.sucursal = sucursal;
        }
        public String getCodigo() { return codigo; }
        public String getNombre() { return nombre; }
        public int getStock() { return stock; }
        public String getSucursal() { return sucursal; }
    }
}