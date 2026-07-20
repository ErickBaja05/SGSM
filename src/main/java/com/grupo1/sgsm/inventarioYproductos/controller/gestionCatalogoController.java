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
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon;

// IMPORTANTE: Importa aquí tu clase modelo Producto
import com.grupo1.sgsm.inventarioYproductos.model.Producto;

import java.net.URL;
import java.util.ResourceBundle;

public class gestionCatalogoController implements Initializable {

    @FXML private TextField txtBuscar;

    // Configuración de Tabla
    @FXML private TableView<Producto> tbProductos;
    @FXML private TableColumn<Producto, String> colCodigo;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, String> colMarca;
    @FXML private TableColumn<Producto, String> colCategoria;
    @FXML private TableColumn<Producto, String> colPrecio;
    @FXML private TableColumn<Producto, Void> colAccion;

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

    private ObservableList<Producto> masterData = FXCollections.observableArrayList();

    private FontIcon crearIcono(String iconLiteral, String styleClass) {
        FontIcon icon = new FontIcon(iconLiteral);
        icon.getStyleClass().add(styleClass);
        return icon;
    }

    private void configurarColumnaAccion() {
        colAccion.setCellFactory(param -> new TableCell<Producto, Void>() {
            private final Button btnAccion = new Button();

            {
                // Inyectamos el lapicito (fa-pencil)
                btnAccion.setGraphic(crearIcono("fa-pencil", "action-table-icon"));
                btnAccion.getStyleClass().add("btn-transparent");

                btnAccion.setOnAction(event -> {
                    Producto productoSeleccionado = getTableView().getItems().get(getIndex());
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

    private void cargarDatosEdicion(Producto producto) {
        if (producto != null) {
            // Limpiar mensajes anteriores
            mensajeConfirmacion.setText("");

            // Llenar formulario
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
        ObservableList<Producto> filteredData = FXCollections.observableArrayList();

        for (Producto prod : masterData) {
            if (prod.getCodigo().toLowerCase().contains(lowerCaseFilter) ||
                    prod.getNombre().toLowerCase().contains(lowerCaseFilter)) {
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
        System.out.println("Eliminando producto: " + txtEditCodigo.getText());
        mensajeConfirmacion.setText("Producto eliminado exitosamente");
        mensajeConfirmacion.getStyleClass().setAll("mensaje-confirmacion", "mensaje-error"); // rojo para eliminar
    }

    @FXML
    void guardarCambios(ActionEvent event) {
        if (txtEditCodigo.getText().isEmpty()) {
            return;
        }
        System.out.println("Actualizando producto: " + txtEditNombre.getText());
        mensajeConfirmacion.setText("Cambios guardados correctamente");
        mensajeConfirmacion.getStyleClass().setAll("mensaje-confirmacion", "mensaje-exito"); // verde corporativo
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // 1. Cargar Íconos
        lblSearchIcon.setGraphic(crearIcono("fa-search", "search-icon-font"));
        lblEditHeaderIcon.setGraphic(crearIcono("fa-pencil-square-o", "edit-header-icon-font"));
        lblBtnEliminarIcon.setGraphic(crearIcono("fa-trash-o", "btn-danger-icon-font"));
        lblBtnGuardarIcon.setGraphic(crearIcono("fa-save", "btn-primary-icon-font"));

        // 2. Configurar Columnas
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));

        // Formato especial para añadir el símbolo "$" al precio en la tabla
        colPrecio.setCellValueFactory(cellData -> {
            // Asumiendo que getPrecio() devuelve un Double o String
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

    }
}