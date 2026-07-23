package com.grupo1.sgsm.inventarioYproductos.controller;

import javafx.beans.property.SimpleStringProperty;
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
import org.kordamp.ikonli.javafx.FontIcon;

import com.grupo1.sgsm.inventarioYproductos.dto.ProductoConsultaDTO;
import com.grupo1.sgsm.inventarioYproductos.service.IProductoService;
import com.grupo1.sgsm.inventarioYproductos.service.ProductoService;

import java.net.URL;
import java.util.ResourceBundle;

public class gestionCatalogoController implements Initializable {

    @FXML private TextField txtBuscar;

    // Configuración de Tabla
    @FXML private TableView<ProductoConsultaDTO> tbProductos;
    @FXML private TableColumn<ProductoConsultaDTO, String> colCodigo;
    @FXML private TableColumn<ProductoConsultaDTO, String> colNombre;
    @FXML private TableColumn<ProductoConsultaDTO, String> colMarca;
    @FXML private TableColumn<ProductoConsultaDTO, String> colCategoria;
    @FXML private TableColumn<ProductoConsultaDTO, String> colPrecio;
    @FXML private TableColumn<ProductoConsultaDTO, Void> colAccion;

    // Campos de Edición
    @FXML private TextField txtEditCodigo;
    @FXML private TextField txtEditNombre;
    @FXML private TextField txtEditMarca;
    @FXML private TextField txtEditCategoria;
    @FXML private TextField txtEditPrecio;

    // Íconos y Mensajes
    @FXML private Label lblSearchIcon;
    @FXML private Label lblEditHeaderIcon;
    @FXML private Label lblBtnEliminarIcon;
    @FXML private Label lblBtnGuardarIcon;
    @FXML private Label mensajeConfirmacion;

    private final IProductoService productoService = new ProductoService();
    private final ObservableList<ProductoConsultaDTO> masterData = FXCollections.observableArrayList();

    private FontIcon crearIcono(String iconLiteral, String styleClass) {
        FontIcon icon = new FontIcon(iconLiteral);
        icon.getStyleClass().add(styleClass);
        return icon;
    }

    private void configurarColumnaAccion() {
        colAccion.setCellFactory(param -> new TableCell<ProductoConsultaDTO, Void>() {
            private final Button btnAccion = new Button();

            {
                btnAccion.setGraphic(crearIcono("fa-pencil", "action-table-icon"));
                btnAccion.getStyleClass().add("btn-transparent");

                btnAccion.setOnAction(event -> {
                    ProductoConsultaDTO productoSeleccionado = getTableView().getItems().get(getIndex());
                    cargarDatosEdicion(productoSeleccionado);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnAccion);
                }
            }
        });
    }

    private void cargarDatosEdicion(ProductoConsultaDTO producto) {
        if (producto != null) {
            mensajeConfirmacion.setText("");
            txtEditCodigo.setText(producto.getCodigo());
            txtEditNombre.setText(producto.getNombre());
            txtEditMarca.setText(producto.getMarca());
            txtEditCategoria.setText(producto.getCategoria());
            txtEditPrecio.setText(String.valueOf(producto.getPrecio()));
        }
    }

    private void buscarProductoEnTiempoReal(String filtro) {
        if (filtro == null || filtro.isEmpty()) {
            tbProductos.setItems(masterData);
            return;
        }

        String lowerCaseFilter = filtro.toLowerCase();
        ObservableList<ProductoConsultaDTO> filteredData = FXCollections.observableArrayList();

        for (ProductoConsultaDTO prod : masterData) {
            String precioStr = String.valueOf(prod.getPrecio());
            String precioFormateado = "$ " + prod.getPrecio();

            if ((prod.getCodigo() != null && prod.getCodigo().toLowerCase().contains(lowerCaseFilter)) ||
                (prod.getNombre() != null && prod.getNombre().toLowerCase().contains(lowerCaseFilter)) ||
                (prod.getMarca() != null && prod.getMarca().toLowerCase().contains(lowerCaseFilter)) ||
                (prod.getCategoria() != null && prod.getCategoria().toLowerCase().contains(lowerCaseFilter)) ||
                (precioStr.contains(lowerCaseFilter)) ||
                (precioFormateado.toLowerCase().contains(lowerCaseFilter))) {
                filteredData.add(prod);
            }
        }
        tbProductos.setItems(filteredData);
    }

    @FXML
    void eliminarProducto(ActionEvent event) {
        if (txtEditCodigo.getText().isEmpty()) {
            return;
        }
        try {
            productoService.eliminarProducto(txtEditCodigo.getText());
            mensajeConfirmacion.setText("Producto eliminado exitosamente");
            mensajeConfirmacion.getStyleClass().setAll("mensaje-confirmacion", "mensaje-error");
            limpiarFormulario();
            cargarDatos();
        } catch (Exception e) {
            mensajeConfirmacion.setText(e.getMessage());
            mensajeConfirmacion.getStyleClass().setAll("mensaje-confirmacion", "mensaje-error");
        }
    }

    @FXML
    void guardarCambios(ActionEvent event) {
        if (txtEditCodigo.getText().isEmpty()) {
            return;
        }
        try {
            double precio = Double.parseDouble(txtEditPrecio.getText());
            ProductoConsultaDTO dto = new ProductoConsultaDTO(
                    txtEditCodigo.getText(),
                    txtEditNombre.getText(),
                    txtEditMarca.getText(),
                    txtEditCategoria.getText(),
                    precio
            );
            productoService.actualizarProducto(dto);
            mensajeConfirmacion.setText("Cambios guardados correctamente");
            mensajeConfirmacion.getStyleClass().setAll("mensaje-confirmacion", "mensaje-exito");
            cargarDatos();
        } catch (NumberFormatException e) {
            mensajeConfirmacion.setText("Precio inválido");
            mensajeConfirmacion.getStyleClass().setAll("mensaje-confirmacion", "mensaje-error");
        } catch (Exception e) {
            mensajeConfirmacion.setText(e.getMessage());
            mensajeConfirmacion.getStyleClass().setAll("mensaje-confirmacion", "mensaje-error");
        }
    }

    private void limpiarFormulario() {
        txtEditCodigo.clear();
        txtEditNombre.clear();
        txtEditMarca.clear();
        txtEditCategoria.clear();
        txtEditPrecio.clear();
    }

    private void cargarDatos() {
        masterData.setAll(productoService.obtenerTodosProductos());
        tbProductos.setItems(masterData);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // 1. Cargar Íconos
        lblSearchIcon.setGraphic(crearIcono("fa-search", "search-icon-font"));
        lblEditHeaderIcon.setGraphic(crearIcono("fa-pencil-square-o", "edit-header-icon-font"));
        lblBtnEliminarIcon.setGraphic(crearIcono("fa-trash-o", "btn-danger-icon-font"));
        lblBtnGuardarIcon.setGraphic(crearIcono("fa-save", "btn-primary-icon-font"));

        // 2. Configurar Columnas
        colCodigo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodigo()));
        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        colMarca.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMarca()));
        colCategoria.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategoria()));

        // Formato especial para añadir el símbolo "$" al precio en la tabla
        colPrecio.setCellValueFactory(cellData -> {
            return new SimpleStringProperty("$ " + cellData.getValue().getPrecio());
        });

        // 3. Configurar Columna de Acción (Botón de edición)
        configurarColumnaAccion();

        // 4. Configurar Búsqueda Dinámica
        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            buscarProductoEnTiempoReal(newValue);
        });

        // limpiar mensaje al inicio
        mensajeConfirmacion.setText("");

        // Cargar los datos desde el servicio
        cargarDatos();
    }
}