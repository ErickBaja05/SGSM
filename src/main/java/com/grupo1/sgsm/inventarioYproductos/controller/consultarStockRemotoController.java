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

import java.net.URL;
import java.util.ResourceBundle;

public class consultarStockRemotoController implements Initializable {

    @FXML private TextField txtBuscar;
    @FXML private Label lblSearchIcon;
    @FXML private Label lblLocationIcon;
    @FXML private Label lblReloadIcon;
    @FXML private Button btnRecargar;

    // --- Tabla y Columnas ---
    @FXML private TableView<StockRemoto> tbStock;
    @FXML private TableColumn<StockRemoto, String> colCodigo;
    @FXML private TableColumn<StockRemoto, String> colNombre;
    @FXML private TableColumn<StockRemoto, Integer> colStock;
    @FXML private TableColumn<StockRemoto, String> colSucursal;

    // Lista maestra para la búsqueda dinámica
    private ObservableList<StockRemoto> masterData = FXCollections.observableArrayList();

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
        colStock.setCellFactory(param -> new TableCell<StockRemoto, Integer>() {
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
        ObservableList<StockRemoto> filteredData = FXCollections.observableArrayList();

        for (StockRemoto p : masterData) {
            if (p.getCodigo().toLowerCase().contains(lowerCaseFilter) ||
                    p.getNombre().toLowerCase().contains(lowerCaseFilter)) {
                filteredData.add(p);
            }
        }
        tbStock.setItems(filteredData);
    }

    @FXML
    void recargarDatos(ActionEvent event) {
        // Aquí lanzarías tu consulta SQL hacia el nodo remoto para traer los datos más frescos
        System.out.println("Sincronizando con el nodo de la sede Costa (GYE)...");

        // Simulamos un parpadeo de recarga limpiando y volviendo a poner la data
        tbStock.setItems(FXCollections.observableArrayList());

        // Simular un pequeño delay de red no bloquearía la UI si usas un Task,
        // pero para efectos visuales directos, volvemos a asignar:
        tbStock.setItems(masterData);
    }

    // --- Clase Auxiliar (Reemplaza con tu modelo real) ---
    private void cargarDatosRemotos() {
        masterData.clear();
        masterData.addAll(
                new StockRemoto("APP-SH-001", "Zapatillas Running Pro X1", 45, "GYE"),
                new StockRemoto("APP-TS-045", "Camiseta Técnica Entrenamiento", 112, "GYE"),
                new StockRemoto("EQ-BL-012", "Balón Fútbol Liga Premium", 3, "GYE"), // Este saldrá rojo
                new StockRemoto("ACC-BT-009", "Botella Agua Acero Inox 750ml", 28, "GYE")
        );
        tbStock.setItems(masterData);
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

        // Cargar datos (Simulación del nodo remoto GYE)
        cargarDatosRemotos();
    }

    // Clase Modelo Interna
    public static class StockRemoto {
        private String codigo;
        private String nombre;
        private int stock;
        private String sucursal;

        public StockRemoto(String codigo, String nombre, int stock, String sucursal) {
            this.codigo = codigo; this.nombre = nombre; this.stock = stock; this.sucursal = sucursal;
        }
        public String getCodigo() { return codigo; }
        public String getNombre() { return nombre; }
        public int getStock() { return stock; }
        public String getSucursal() { return sucursal; }
    }
}