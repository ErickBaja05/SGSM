package com.grupo1.sgsm.inventarioYproductos.controller;

import com.grupo1.sgsm.inventarioYproductos.dto.DetalleAjusteStockDTO;
import com.grupo1.sgsm.inventarioYproductos.service.IStockLocalService;
import com.grupo1.sgsm.inventarioYproductos.service.StockLocalService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.ResourceBundle;

public class ajusteStockController implements Initializable {

    // Contenedores Focus
    @FXML private HBox boxBuscar;
    @FXML private HBox boxStock;

    // Campos de Búsqueda
    @FXML private TextField txtBuscar;
    @FXML private Label lblBusquedaMensaje;

    // Campos de Detalle (Solo lectura)
    @FXML private TextField txtCodigo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtCategoria;
    @FXML private TextField txtSucursal;

    // Campo de Stock
    @FXML private TextField txtNuevoStock;

    // Íconos
    @FXML private Label lblSearchIcon;
    @FXML private Label lblSucursalIcon;
    @FXML private Label lblAsignacionIcon;
    @FXML private Label lblBtnGuardarIcon;

    // Mensaje Final
    @FXML private Label mensajeConfirmacion;

    private final IStockLocalService stockLocalService = new StockLocalService();

    private FontIcon crearIcono(String iconLiteral, String styleClass) {
        FontIcon icon = new FontIcon(iconLiteral);
        icon.getStyleClass().add(styleClass);
        return icon;
    }

    private void configurarEfectoFocus(TextField textField, HBox contenedor) {
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                if (!contenedor.getStyleClass().contains("focused-box")) {
                    contenedor.getStyleClass().add("focused-box");
                }
            } else {
                contenedor.getStyleClass().remove("focused-box");
            }
        });
    }

    @FXML
    void buscarProducto(ActionEvent event) {
        String query = txtBuscar.getText() != null ? txtBuscar.getText().trim() : "";

        if (query.isEmpty()) {
            lblBusquedaMensaje.setText("Ingrese el nombre o código del producto.");
            lblBusquedaMensaje.getStyleClass().setAll("mensaje-error");
            return;
        }

        try {
            DetalleAjusteStockDTO detalle = stockLocalService.buscarProductoParaAjuste(query);
            if (detalle != null) {  
                txtCodigo.setText(detalle.getCodigo_producto());
                txtNombre.setText(detalle.getNombre());
                txtCategoria.setText(detalle.getCategoria());
                txtSucursal.setText(detalle.getCodigo_sucursal());
                txtNuevoStock.setText(String.valueOf(detalle.getStock()));

                lblBusquedaMensaje.setText("Producto encontrado satisfactoriamente");
                lblBusquedaMensaje.getStyleClass().setAll("mensaje-confirmacion", "mensaje-exito");
                mensajeConfirmacion.setText("");
            } else {
                lblBusquedaMensaje.setText("No se encontró ningún producto con ese código o nombre.");
                lblBusquedaMensaje.getStyleClass().setAll("mensaje-error");

                txtCodigo.clear();
                txtNombre.clear();
                txtCategoria.clear();
                txtSucursal.clear();
                txtNuevoStock.clear();
                mensajeConfirmacion.setText("");
            }
        } catch (Exception e) {
            lblBusquedaMensaje.setText("Error al consultar la base de datos.");
            lblBusquedaMensaje.getStyleClass().setAll("mensaje-error");
        }
    }

    @FXML
    void guardarCambios(ActionEvent event) {
        String codigoProducto = txtCodigo.getText();
        String sucursal = txtSucursal.getText();
        String stockTexto = txtNuevoStock.getText() != null ? txtNuevoStock.getText().trim() : "";

        if (codigoProducto == null || codigoProducto.isEmpty()) {
            mensajeConfirmacion.setText("Primero busque y seleccione un producto.");
            mensajeConfirmacion.getStyleClass().setAll("mensaje-error");

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Atención");
            alert.setHeaderText("Sin producto seleccionado");
            alert.setContentText("Por favor, busque un producto por nombre o código antes de guardar.");
            alert.showAndWait();
            return;
        }

        if (stockTexto.isEmpty()) {
            mensajeConfirmacion.setText("Ingrese el valor del nuevo stock.");
            mensajeConfirmacion.getStyleClass().setAll("mensaje-error");
            return;
        }

        int nuevoStock;
        try {
            nuevoStock = Integer.parseInt(stockTexto);
            if (nuevoStock < 0) {
                throw new NumberFormatException("El stock no puede ser negativo");
            }
        } catch (NumberFormatException e) {
            mensajeConfirmacion.setText("Ingrese un número entero válido para el stock.");
            mensajeConfirmacion.getStyleClass().setAll("mensaje-error");
            return;
        }

        try {
            stockLocalService.ajustarStock(codigoProducto, nuevoStock, sucursal);

            mensajeConfirmacion.setText("Stock actualizado correctamente");
            mensajeConfirmacion.getStyleClass().setAll("mensaje-confirmacion", "mensaje-exito");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Éxito");
            alert.setHeaderText("Cambios guardados con éxito");
            alert.setContentText(
                    "Se actualizó el stock del producto " + codigoProducto + " a " + nuevoStock + " unidades.");
            alert.showAndWait();

        } catch (Exception e) {
            mensajeConfirmacion.setText("Error al guardar en la base de datos.");
            mensajeConfirmacion.getStyleClass().setAll("mensaje-error");

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No se pudo guardar el cambio");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void cancelarOperacion(ActionEvent event) {
        limpiarFormulario();
    }

    private void limpiarFormulario() {
        txtBuscar.clear();
        txtCodigo.clear();
        txtNombre.clear();
        txtCategoria.clear();
        txtSucursal.clear();
        txtNuevoStock.clear();

        lblBusquedaMensaje.setText("");
        mensajeConfirmacion.setText("");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Cargar Íconos
        lblSearchIcon.setGraphic(crearIcono("fa-search", "input-icon-font"));
        lblSucursalIcon.setGraphic(crearIcono("fa-building-o", "input-icon-font"));
        lblAsignacionIcon.setGraphic(crearIcono("fa-clipboard", "section-icon-font"));
        lblBtnGuardarIcon.setGraphic(crearIcono("fa-save", "btn-primary-icon-font"));

        // Permitir clic en el ícono de búsqueda
        lblSearchIcon.setOnMouseClicked(e -> buscarProducto(null));

        // Configurar Focus
        configurarEfectoFocus(txtBuscar, boxBuscar);
        configurarEfectoFocus(txtNuevoStock, boxStock);

        limpiarFormulario();
    }
}
