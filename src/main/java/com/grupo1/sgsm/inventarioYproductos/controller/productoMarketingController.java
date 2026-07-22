package com.grupo1.sgsm.inventarioYproductos.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.ResourceBundle;

public class productoMarketingController implements Initializable {

    @FXML private TextField txtBuscar;

    // --- Tabla ---
    @FXML private TableView<ProductoMarketing> tbProductos;
    @FXML private TableColumn<ProductoMarketing, String> colCodigo;
    @FXML private TableColumn<ProductoMarketing, String> colNombre;
    @FXML private TableColumn<ProductoMarketing, String> colDescripcion;

    // --- Panel de Edición Derecho ---
    @FXML private Label lblEditCodigo;
    @FXML private Label lblEditNombre;
    @FXML private TextArea txtEditDescripcion;
    @FXML private Label lblMensajeConfirmacion;
    @FXML private Button btnGuardar;

    // --- Íconos ---
    @FXML private Label lblSearchIcon;
    @FXML private Label lblEditHeaderIcon;
    @FXML private Label lblBtnGuardarIcon;

    // Lista maestra para la búsqueda dinámica
    private ObservableList<ProductoMarketing> masterData = FXCollections.observableArrayList();
    private ProductoMarketing productoSeleccionado;

    // Método utilitario para Ikonli
    private FontIcon crearIcono(String iconLiteral, String styleClass) {
        FontIcon icon = new FontIcon(iconLiteral);
        icon.getStyleClass().add(styleClass);
        return icon;
    }


    private void cargarIconos() {
        lblSearchIcon.setGraphic(crearIcono("fa-search", "search-icon-font"));
        lblEditHeaderIcon.setGraphic(crearIcono("fa-tags", "")); // fa-tags queda bien para marketing
        lblBtnGuardarIcon.setGraphic(crearIcono("fa-save", ""));
    }

    private void configurarTabla() {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
    }

    private void cargarDetalle(ProductoMarketing producto) {
        this.productoSeleccionado = producto;

        lblEditCodigo.setText(producto.getCodigo());
        lblEditNombre.setText(producto.getNombre());
        txtEditDescripcion.setText(producto.getDescripcion());

        // Limpiamos mensajes anteriores si los hubiera
        lblMensajeConfirmacion.setText("");
        lblMensajeConfirmacion.getStyleClass().removeAll("mensaje-exito", "mensaje-error");
    }

    private void buscarProductoRealTime(String query) {
        if (query == null || query.isEmpty()) {
            tbProductos.setItems(masterData);
            return;
        }

        String lowerCaseFilter = query.toLowerCase();
        ObservableList<ProductoMarketing> filteredData = FXCollections.observableArrayList();

        for (ProductoMarketing p : masterData) {
            if (p.getCodigo().toLowerCase().contains(lowerCaseFilter) ||
                    p.getNombre().toLowerCase().contains(lowerCaseFilter)) {
                filteredData.add(p);
            }
        }
        tbProductos.setItems(filteredData);
    }

    @FXML
    void guardarCambios(ActionEvent event) {
        if (productoSeleccionado == null) {
            lblMensajeConfirmacion.setText("Seleccione un producto primero.");
            lblMensajeConfirmacion.getStyleClass().setAll("mensaje-confirmacion", "mensaje-error");
            return;
        }

        // Actualizamos el objeto seleccionado con el nuevo texto del TextArea
        productoSeleccionado.setDescripcion(txtEditDescripcion.getText());

        // Refrescamos la tabla para que se vea el cambio reflejado al instante
        tbProductos.refresh();

        // Lógica de BD iría aquí (UPDATE tabla_marketing SET descripcion = ... WHERE codigo = ...)
        System.out.println("Guardando descripción marketing para: " + productoSeleccionado.getCodigo());

        lblMensajeConfirmacion.setText("Descripción actualizada con éxito");
        lblMensajeConfirmacion.getStyleClass().setAll("mensaje-confirmacion", "mensaje-exito");
    }

    // --- Clase Auxiliar (Puedes usar la de tu modelo real) ---
    private void cargarDatosPrueba() {
        masterData.addAll(
                new ProductoMarketing("SM-SH-001", "UltraBoost Pro Runners", "Zapatillas de running de alto rendimiento con amortiguación avanzada..."),
                new ProductoMarketing("SM-AP-042", "AeroFit Training Tee", "Camiseta técnica transpirable ideal para entrenamientos intensos..."),
                new ProductoMarketing("SM-EQ-109", "ProGrip Yoga Mat", "Esterilla de yoga antideslizante con grosor extra..."),
                new ProductoMarketing("SM-AC-022", "HydraFlask 1L", "Botella de agua térmica de acero inoxidable...")
        );
        tbProductos.setItems(masterData);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarIconos();
        configurarTabla();

        // 1. Configurar Búsqueda en tiempo real
        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            buscarProductoRealTime(newValue);
        });

        // 2. MAGIA: Escuchar los clics en las filas de la tabla
        tbProductos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                cargarDetalle(newSelection);
            }
        });

        // (Opcional) Cargar datos de prueba
        cargarDatosPrueba();
    }

    // Clase Modelo Interna (Solo de ejemplo, bórrala si ya tienes tu clase)
    public static class ProductoMarketing {
        private String codigo;
        private String nombre;
        private String descripcion;

        public ProductoMarketing(String codigo, String nombre, String descripcion) {
            this.codigo = codigo; this.nombre = nombre; this.descripcion = descripcion;
        }
        public String getCodigo() { return codigo; }
        public String getNombre() { return nombre; }
        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    }
}