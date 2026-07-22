package com.grupo1.sgsm.administracion.gestionParametros.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.kordamp.ikonli.javafx.FontIcon;

public class registrarSucursalController {

    // --- Entradas del Formulario ---
    @FXML private TextField txtCodigo;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtNombre;
    @FXML private TextField txtTelefono;

    // --- Elementos de UI ---
    @FXML private Label lblBtnRegistrarIcon;
    @FXML private Label mensajeConfirmacion;

    // --- Botones ---
    @FXML private Button btnCancelar;
    @FXML private Button btnRegistrar;

    private FontIcon crearIcono(String iconLiteral, String styleClass) {
        FontIcon icon = new FontIcon(iconLiteral);
        icon.getStyleClass().add(styleClass);
        return icon;
    }

    @FXML
    public void initialize() {
        // Cargar ícono del botón principal (fa-cog o fa-cogs combinan bien con "infraestructura")
        lblBtnRegistrarIcon.setGraphic(crearIcono("fa-cogs", "btn-primary-icon-font"));
    }

    @FXML
    void registrarSucursal(ActionEvent event) {
        // Limpiar estilos de mensajes anteriores
        mensajeConfirmacion.setText("");
        mensajeConfirmacion.getStyleClass().removeAll("mensaje-error", "mensaje-exito");

        // Validación básica
        if (txtCodigo.getText().isEmpty() || txtNombre.getText().isEmpty() || txtDireccion.getText().isEmpty()) {
            mensajeConfirmacion.setText("Por favor, complete los campos obligatorios.");
            mensajeConfirmacion.getStyleClass().add("mensaje-error");
            return;
        }

        // Lógica para enviar los datos a la BD distribuida iría aquí.
        System.out.println("Registrando nueva sucursal: " + txtCodigo.getText() + " - " + txtNombre.getText());

        // Mensaje de éxito
        mensajeConfirmacion.setText("Sucursal registrada exitosamente.");
        mensajeConfirmacion.getStyleClass().add("mensaje-exito");

        // Opcional: limpiar los campos después de guardar
        // limpiarFormulario();
    }

    @FXML
    void cancelarOperacion(ActionEvent event) {
        System.out.println("Operación de registro cancelada.");
        limpiarFormulario();
    }

    private void limpiarFormulario() {
        txtCodigo.clear();
        txtNombre.clear();
        txtDireccion.clear();
        txtTelefono.clear();
        mensajeConfirmacion.setText("");
    }
}