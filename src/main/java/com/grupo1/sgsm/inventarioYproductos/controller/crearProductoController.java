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

        // Simulación de registro exitoso
        System.out.println("Registrando: " + txtNombre.getText());
        mensajeConfirmacion.setText("Producto agregado al catálogo satisfactoriamente");
        mensajeConfirmacion.getStyleClass().add("mensaje-exito");

        // Aquí podrías agregar un método para limpiar el formulario tras registrar
    }

    @FXML
    void cancelarOperacion(ActionEvent event) {
        System.out.println("Operación cancelada.");
        // Lógica para regresar o limpiar
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Cargar Íconos (Ajusta los literales según tu versión de FontAwesome)
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