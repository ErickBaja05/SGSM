package com.grupo1.sgsm.administracion.gestionParametros.controller;

import com.grupo1.sgsm.administracion.gestionParametros.dto.SucursalDTO;
import com.grupo1.sgsm.administracion.gestionParametros.exception.CodigoSucursalNoValidoException;
import com.grupo1.sgsm.administracion.gestionParametros.exception.SucursalYaExisteException;
import com.grupo1.sgsm.administracion.gestionParametros.exception.TelefonoNoValidoException;
import com.grupo1.sgsm.administracion.gestionParametros.service.IParametrosService;
import com.grupo1.sgsm.administracion.gestionParametros.service.ParametrosServiceImpl;
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
    @FXML private TextField txtCiudad;

    // --- Elementos de UI ---
    @FXML private Label lblBtnRegistrarIcon;
    @FXML private Label mensajeConfirmacion;

    // --- Botones ---
    @FXML private Button btnCancelar;
    @FXML private Button btnRegistrar;

    private IParametrosService parametrosService = new ParametrosServiceImpl()  ;

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
        if (txtCodigo.getText().isEmpty() || txtNombre.getText().isEmpty() || txtDireccion.getText().isEmpty() || txtTelefono.getText().isEmpty() || txtCiudad.getText().isEmpty()) {
            mensajeConfirmacion.setText("Todos los campos son obligatorios");
            mensajeConfirmacion.getStyleClass().addAll("mensaje-error");
            return;
        }

        if(!txtTelefono.getText().matches("[0-9]+")){
            mensajeConfirmacion.setText("El telefono debe ser un numero");
            mensajeConfirmacion.getStyleClass().addAll("mensaje-error");
            return;
        }

        SucursalDTO nuevaSucursal = new SucursalDTO(txtCodigo.getText(),txtDireccion.getText(),txtNombre.getText(),txtCiudad.getText(),txtTelefono.getText());
       try{
            parametrosService.registrarSucursal(nuevaSucursal);
           mensajeConfirmacion.setText("Sucursal registrada exitosamente.");
           mensajeConfirmacion.getStyleClass().addAll("mensaje-exito");
           limpiarFormulario();
       }catch(CodigoSucursalNoValidoException | TelefonoNoValidoException | SucursalYaExisteException e ){
           mensajeConfirmacion.setText(e.getMessage());
           mensajeConfirmacion.getStyleClass().addAll("mensaje-error");
       }


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
    }
}