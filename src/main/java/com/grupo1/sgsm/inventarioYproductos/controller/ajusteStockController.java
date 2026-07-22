package com.grupo1.sgsm.inventarioYproductos.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
        String query = txtBuscar.getText();

        if (query.isEmpty()) {
            lblBusquedaMensaje.setText("Ingrese un código válido.");
            lblBusquedaMensaje.getStyleClass().setAll("mensaje-error");
            return;
        }

        // Lógica simulada de búsqueda en BD
        // Nota: Dependiendo de si estás en el nodo de Quito o Guayaquil,
        // aquí traerías el stock local.
        txtCodigo.setText(query.toUpperCase());
        txtNombre.setText("Zapatillas Running Pro X");
        txtCategoria.setText("Calzado Deportivo");
        txtSucursal.setText("UIO"); // O "GYE" dependiendo del nodo
        txtNuevoStock.setText("45");

        lblBusquedaMensaje.setText("Producto encontrado satisfactoriamente");
        lblBusquedaMensaje.getStyleClass().setAll("mensaje-confirmacion", "mensaje-exito");
        mensajeConfirmacion.setText("");
    }

    @FXML
    void guardarCambios(ActionEvent event) {
        if (txtCodigo.getText().isEmpty() || txtNuevoStock.getText().isEmpty()) {
            mensajeConfirmacion.setText("No hay datos para actualizar.");
            mensajeConfirmacion.getStyleClass().setAll("mensaje-error");
            return;
        }

        // Aquí iría tu lógica de negocio para despachar la consulta hacia
        // tu base de datos distribuida en SQL Server.
        System.out.println("Actualizando stock en sucursal " + txtSucursal.getText() + " a: " + txtNuevoStock.getText());

        mensajeConfirmacion.setText("Stock actualizado correctamente");
        mensajeConfirmacion.getStyleClass().setAll("mensaje-confirmacion", "mensaje-exito");
    }

    @FXML
    void cancelarOperacion(ActionEvent event) {
        limpiarFormulario();
        System.out.println("Ajuste cancelado.");
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

        // Configurar Focus
        configurarEfectoFocus(txtBuscar, boxBuscar);
        configurarEfectoFocus(txtNuevoStock, boxStock);

        limpiarFormulario();

    }
}
