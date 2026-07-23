package com.grupo1.sgsm.administracion.gestionUsuarios.controller;

import com.grupo1.sgsm.administracion.gestionParametros.exception.CorreoNoValidoException;
import com.grupo1.sgsm.administracion.gestionUsuarios.dto.NuevoUsuarioDTO;
import com.grupo1.sgsm.administracion.gestionUsuarios.exception.PasswordInseguroException;
import com.grupo1.sgsm.administracion.gestionUsuarios.exception.UsuarioYaRegistradoException;
import com.grupo1.sgsm.administracion.gestionUsuarios.service.IUsuarioService;
import com.grupo1.sgsm.administracion.gestionUsuarios.service.UsuarioServiceImpl;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.ResourceBundle;

public class crearUsuarioController implements Initializable {

    // --- Inputs del Formulario ---
    @FXML private TextField txtUsuario;
    @FXML private ComboBox<String> cmbRol;
    @FXML private ComboBox<String> cmbSucursal;
    @FXML private PasswordField txtPassword;
    @FXML private TextField txtPasswordVisible;
    @FXML private PasswordField txtConfirmPassword;
    @FXML private TextField txtEmail;

    // --- Contenedores y Efectos ---
    @FXML private HBox boxPass;

    // --- Íconos ---
    @FXML private Label lblBreadcrumbIcon;
    @FXML private Label lblIconUser;
    @FXML private Label lblIconRole;
    @FXML private Label lblIconPass;
    @FXML private Label lblIconSucursal;
    @FXML private Label lblIconPassConf;
    @FXML private Label lblIconEmail;
    @FXML private Label lblIconInfo;
    @FXML private Label lblEyeIcon;
    @FXML private Label lblBtnGuardarIcon;

    // --- Mensajes y Botones ---
    @FXML private Label mensajeConfirmacion;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    private IUsuarioService usuarioService;

    private FontIcon crearIcono(String iconLiteral, String styleClass) {
        FontIcon icon = new FontIcon(iconLiteral);
        icon.getStyleClass().add(styleClass);
        return icon;
    }

    private void cargarIconos() {
        lblBreadcrumbIcon.setGraphic(crearIcono("fa-user-plus", ""));
        lblIconUser.setGraphic(crearIcono("fa-user-circle-o", ""));
        lblIconRole.setGraphic(crearIcono("fa-id-badge", ""));
        lblIconPass.setGraphic(crearIcono("fa-lock", ""));
        lblIconSucursal.setGraphic(crearIcono("fa-building-o", ""));
        lblIconPassConf.setGraphic(crearIcono("fa-unlock-alt", ""));
        lblIconEmail.setGraphic(crearIcono("fa-at", ""));
        lblIconInfo.setGraphic(crearIcono("fa-info-circle", ""));

        lblEyeIcon.setGraphic(crearIcono("fa-eye", "action-icon"));
        lblBtnGuardarIcon.setGraphic(crearIcono("fa-user-plus", "btn-primary-icon-font"));
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
    void togglePasswordVisibility(MouseEvent event) {
        boolean isVisible = txtPasswordVisible.isVisible();
        if (isVisible) {
            txtPasswordVisible.setVisible(false);
            txtPasswordVisible.setManaged(false);
            txtPassword.setVisible(true);
            txtPassword.setManaged(true);
            lblEyeIcon.setGraphic(crearIcono("fa-eye", "action-icon"));
        } else {
            txtPasswordVisible.setVisible(true);
            txtPasswordVisible.setManaged(true);
            txtPassword.setVisible(false);
            txtPassword.setManaged(false);
            lblEyeIcon.setGraphic(crearIcono("fa-eye-slash", "action-icon"));
        }
    }

    @FXML
    void crearUsuario(ActionEvent event) {
        mensajeConfirmacion.setText("");
        mensajeConfirmacion.getStyleClass().removeAll("mensaje-error", "mensaje-exito");

        // Validaciones básicas
        if (txtUsuario.getText().isEmpty() || txtPassword.getText().isEmpty() || txtEmail.getText().isEmpty() || cmbRol.getValue() == null) {
            mensajeConfirmacion.setText("Por favor, complete todos los campos obligatorios.");
            mensajeConfirmacion.getStyleClass().addAll("mensaje-error");
            return;
        }

        if (!txtPassword.getText().equals(txtConfirmPassword.getText())) {
            mensajeConfirmacion.setText("Las contraseñas no coinciden. Verifíquelas e intente de nuevo.");
            mensajeConfirmacion.getStyleClass().addAll("mensaje-error");
            return;
        }


        NuevoUsuarioDTO nuevoUsuario = new NuevoUsuarioDTO(txtUsuario.getText(),cmbRol.getValue(),txtPassword.getText(),cmbSucursal.getValue(),txtEmail.getText());

        try{
            usuarioService.crearUsuario(nuevoUsuario);
            mensajeConfirmacion.setText("Usuario creado exitosamente.");
            mensajeConfirmacion.getStyleClass().addAll("mensaje-exito");
        }catch (UsuarioYaRegistradoException | PasswordInseguroException | CorreoNoValidoException e){
            mensajeConfirmacion.setText(e.getMessage());
            mensajeConfirmacion.getStyleClass().addAll("mensaje-error");
        }

    }

    @FXML
    void cancelarOperacion(ActionEvent event) {

        txtUsuario.clear();
        txtPassword.clear();
        txtConfirmPassword.clear();
        txtEmail.clear();
        cmbRol.getSelectionModel().clearSelection();
        cmbSucursal.getSelectionModel().clearSelection();
        mensajeConfirmacion.setText("");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarIconos();

        // Cargar combos (puedes adaptarlo para que venga de BD)
        cmbRol.setItems(FXCollections.observableArrayList("ADMINISTRADOR", "AUDITOR", "CAJERO", "MARKETING"));
        cmbSucursal.setItems(FXCollections.observableArrayList("UIO", "GYE"));

        // Binding bidireccional para el "ojito" de la contraseña
        if (txtPassword != null && txtPasswordVisible != null) {
            txtPasswordVisible.textProperty().bindBidirectional(txtPassword.textProperty());
        }

        // Configurar efecto Focus nativo
        configurarEfectoFocus(txtPassword, boxPass);
        configurarEfectoFocus(txtPasswordVisible, boxPass);

        usuarioService = new UsuarioServiceImpl();
        mensajeConfirmacion.setWrapText(true);
    }
}