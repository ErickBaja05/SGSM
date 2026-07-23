package com.grupo1.sgsm.inventarioYproductos.controller;

import javafx.beans.property.SimpleStringProperty;
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

import com.grupo1.sgsm.inventarioYproductos.dto.ProductoMarketingDTO;
import com.grupo1.sgsm.inventarioYproductos.service.IProductoService;
import com.grupo1.sgsm.inventarioYproductos.service.ProductoService;

import java.net.URL;
import java.util.ResourceBundle;

public class productoMarketingController implements Initializable {

    @FXML private TextField txtBuscar;

    // --- Tabla ---
    @FXML private TableView<ProductoMarketingDTO> tbProductos;
    @FXML private TableColumn<ProductoMarketingDTO, String> colCodigo;
    @FXML private TableColumn<ProductoMarketingDTO, String> colNombre;
    @FXML private TableColumn<ProductoMarketingDTO, String> colDescripcion;

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

    private final IProductoService productoService = new ProductoService();
    private final ObservableList<ProductoMarketingDTO> masterData = FXCollections.observableArrayList();
    private ProductoMarketingDTO productoSeleccionado;

    private FontIcon crearIcono(String iconLiteral, String styleClass) {
        FontIcon icon = new FontIcon(iconLiteral);
        icon.getStyleClass().add(styleClass);
        return icon;
    }

    private void cargarIconos() {
        lblSearchIcon.setGraphic(crearIcono("fa-search", "search-icon-font"));
        lblEditHeaderIcon.setGraphic(crearIcono("fa-tags", ""));
        lblBtnGuardarIcon.setGraphic(crearIcono("fa-save", ""));
    }

    private void configurarTabla() {
        colCodigo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodigo()));
        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        colDescripcion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescripcion()));
    }

    private void cargarDetalle(ProductoMarketingDTO producto) {
        this.productoSeleccionado = producto;

        lblEditCodigo.setText(producto.getCodigo());
        lblEditNombre.setText(producto.getNombre());
        txtEditDescripcion.setText(producto.getDescripcion());

        lblMensajeConfirmacion.setText("");
        lblMensajeConfirmacion.getStyleClass().removeAll("mensaje-exito", "mensaje-error");
    }

    private void buscarProductoRealTime(String query) {
        if (query == null || query.isEmpty()) {
            tbProductos.setItems(masterData);
            return;
        }

        String lowerCaseFilter = query.toLowerCase();
        ObservableList<ProductoMarketingDTO> filteredData = FXCollections.observableArrayList();

        for (ProductoMarketingDTO p : masterData) {
            if ((p.getCodigo() != null && p.getCodigo().toLowerCase().contains(lowerCaseFilter)) ||
                (p.getNombre() != null && p.getNombre().toLowerCase().contains(lowerCaseFilter)) ||
                (p.getDescripcion() != null && p.getDescripcion().toLowerCase().contains(lowerCaseFilter))) {
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

        try {
            String nuevaDesc = txtEditDescripcion.getText() != null ? txtEditDescripcion.getText().trim() : "";
            productoService.actualizarDescripcionMarketing(productoSeleccionado.getCodigo(), nuevaDesc);

            productoSeleccionado.setDescripcion(nuevaDesc);
            tbProductos.refresh();

            lblMensajeConfirmacion.setText("Descripción actualizada con éxito");
            lblMensajeConfirmacion.getStyleClass().setAll("mensaje-confirmacion", "mensaje-exito");
        } catch (Exception e) {
            lblMensajeConfirmacion.setText(e.getMessage());
            lblMensajeConfirmacion.getStyleClass().setAll("mensaje-confirmacion", "mensaje-error");
        }
    }

    private void cargarDatos() {
        masterData.setAll(productoService.obtenerProductosMarketing());
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

        // 2. Escuchar los clics en las filas de la tabla
        tbProductos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                cargarDetalle(newSelection);
            }
        });

        // Cargar datos desde base de datos
        cargarDatos();
    }
}