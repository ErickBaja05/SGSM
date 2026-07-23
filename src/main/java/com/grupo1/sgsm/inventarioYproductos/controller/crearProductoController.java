package com.grupo1.sgsm.inventarioYproductos.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon;

import com.grupo1.sgsm.inventarioYproductos.dto.NuevoProductoDTO;
import com.grupo1.sgsm.inventarioYproductos.service.IProductoService;
import com.grupo1.sgsm.inventarioYproductos.service.ProductoService;

import java.net.URL;
import java.util.ResourceBundle;

public class crearProductoController implements Initializable {

    // Contenedores para efecto Focus
    @FXML private HBox boxCodigo;
    @FXML private HBox boxPrecio;

    // Campos de formulario
    @FXML private TextField txtCodigo;
    @FXML private ComboBox<String> cmbCategoria;
    @FXML private TextField txtNombre;
    @FXML private TextField txtMarca;
    @FXML private TextArea txtDescripcion;
    @FXML private TextField txtPrecio;

    // Íconos
    @FXML private Label lblHeaderIcon;
    @FXML private Label lblCodigoIcon;
    @FXML private Label lblPrecioIcon;
    @FXML private Label lblBtnRegistrarIcon;

    // Label de Confirmación
    @FXML private Label mensajeConfirmacion;

    // Botones
    @FXML private Button btnRegistrar;
    @FXML private Button btnCancelar;

    private final IProductoService productoService = new ProductoService();

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
    void registrarProducto(ActionEvent event) {
        // Limpiar mensaje anterior
        mensajeConfirmacion.setText("");
        mensajeConfirmacion.getStyleClass().removeAll("mensaje-error", "mensaje-exito");

        // Validación básica
        if (txtCodigo.getText().isEmpty() || txtNombre.getText().isEmpty() || txtPrecio.getText().isEmpty()) {
            mensajeConfirmacion.setText("Por favor, llene los campos obligatorios.");
            mensajeConfirmacion.getStyleClass().add("mensaje-error");
            return;
        }

        try {
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            NuevoProductoDTO nuevoDTO = new NuevoProductoDTO(
                    txtCodigo.getText().trim(),
                    txtNombre.getText().trim(),
                    txtMarca.getText().trim(),
                    cmbCategoria.getValue() != null ? cmbCategoria.getValue() : "",
                    txtDescripcion.getText() != null ? txtDescripcion.getText().trim() : "",
                    precio
            );

            productoService.guardarNuevoProducto(nuevoDTO);

            mensajeConfirmacion.setText("Producto agregado al catálogo satisfactoriamente");
            mensajeConfirmacion.getStyleClass().add("mensaje-exito");
            limpiarFormulario();
        } catch (NumberFormatException e) {
            mensajeConfirmacion.setText("El precio debe ser un valor numérico válido.");
            mensajeConfirmacion.getStyleClass().add("mensaje-error");
        } catch (Exception e) {
            mensajeConfirmacion.setText(e.getMessage());
            mensajeConfirmacion.getStyleClass().add("mensaje-error");
        }
    }

    @FXML
    void cancelarOperacion(ActionEvent event) {
        limpiarFormulario();
        mensajeConfirmacion.setText("");
        mensajeConfirmacion.getStyleClass().removeAll("mensaje-error", "mensaje-exito");
    }

    private void limpiarFormulario() {
        txtCodigo.clear();
        txtNombre.clear();
        txtMarca.clear();
        txtDescripcion.clear();
        txtPrecio.clear();
        cmbCategoria.setValue(null);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Cargar Íconos
        lblHeaderIcon.setGraphic(crearIcono("fa-archive", "header-icon-font"));
        lblCodigoIcon.setGraphic(crearIcono("fa-barcode", "input-icon-font"));
        lblPrecioIcon.setGraphic(crearIcono("fa-usd", "input-icon-font"));
        lblBtnRegistrarIcon.setGraphic(crearIcono("fa-save", "btn-primary-icon-font"));

        // Llenar ComboBox de Categorías
        cmbCategoria.setItems(FXCollections.observableArrayList(
                "Calzado Deportivo",
                "Ropa Deportiva",
                "Accesorios",
                "Equipamiento",
                "Nutrición"
        ));

        // Configurar Focus para los contenedores con HBox
        configurarEfectoFocus(txtCodigo, boxCodigo);
        configurarEfectoFocus(txtPrecio, boxPrecio);
    }
}