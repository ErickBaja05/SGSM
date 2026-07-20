package com.grupo1.sgsm.clientes.controller;

import com.grupo1.sgsm.clientes.dto.NuevoClienteDTO;
import com.grupo1.sgsm.clientes.exception.ClienteYaExisteException;
import com.grupo1.sgsm.clientes.service.ClientesService;
import com.grupo1.sgsm.clientes.service.IClientesService;
import com.grupo1.sgsm.core.session.SesionActual;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon; // Import de Ikonli

public class registrarClientesController {

    // Contenedores HBox que necesitan el efecto de Focus
    @FXML private HBox boxCedula;
    @FXML private HBox boxCorreo;
    @FXML private HBox boxTelefono;
    @FXML private HBox boxSucursal;

    // Campos de Texto
    @FXML private TextField txtCedula;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtSucursal;

    // Labels para íconos
    @FXML private Label lblHeaderIcon;
    @FXML private Label lblCedulaIcon;
    @FXML private Label lblCorreoIcon;
    @FXML private Label lblTelefonoIcon;
    @FXML private Label lblSucursalIcon;
    @FXML private Label lblBtnRegistrarIcon;

    // Botones
    @FXML private Button btnRegistrar;
    @FXML private Button btnCancelar;

    // Pare el mensaje

    @FXML private Label mensajeConfirmacion;

    private IClientesService clientesService;
    // Método auxiliar para Ikonli
    private FontIcon crearIcono(String iconLiteral, String styleClass) {
        FontIcon icon = new FontIcon(iconLiteral);
        icon.getStyleClass().add(styleClass);
        return icon;
    }

    @FXML
    public void initialize() {
        // Carga de Íconos (ajusta los literales "fa-" según la versión de FontAwesome en tu POM)
        lblHeaderIcon.setGraphic(crearIcono("fa-user-plus", "header-icon-font"));
        lblCedulaIcon.setGraphic(crearIcono("fa-id-card-o", "input-icon-font"));
        lblCorreoIcon.setGraphic(crearIcono("fa-envelope-o", "input-icon-font"));
        lblTelefonoIcon.setGraphic(crearIcono("fa-phone", "input-icon-font"));
        lblSucursalIcon.setGraphic(crearIcono("fa-building-o", "input-icon-font"));
        lblBtnRegistrarIcon.setGraphic(crearIcono("fa-save", "btn-primary-icon-font"));

        // Configurar los efectos de Focus en los contenedores
        configurarEfectoFocus(txtCedula, boxCedula);
        configurarEfectoFocus(txtCorreo, boxCorreo);
        configurarEfectoFocus(txtTelefono, boxTelefono);
        // Sucursal no necesita focus porque es editable="false", pero se puede agregar si deseas.

        clientesService = new ClientesService();
        txtSucursal.setText(SesionActual.getUsuario().getCodigo_sucursal().toUpperCase());
    }

    /**
     * Aplica la clase CSS "focused-box" al HBox padre cuando el TextField gana foco.
     */
    private void configurarEfectoFocus(TextField textField, HBox contenedor) {
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                // Ganó el foco
                if (!contenedor.getStyleClass().contains("focused-box")) {
                    contenedor.getStyleClass().add("focused-box");
                }
            } else {
                // Perdió el foco
                contenedor.getStyleClass().remove("focused-box");
            }
        });
    }

    @FXML
    void registrarCliente(ActionEvent event) {
        // Validación de campos vacíos
        if(txtCedula.getText().isEmpty() || txtApellidos.getText().isEmpty() || txtCorreo.getText().isEmpty() || txtTelefono.getText().isEmpty() || txtDireccion.getText().isEmpty() || txtSucursal.getText().isEmpty()) {
            mensajeConfirmacion.setText("Debes llenar todos los campos");
            return;
        }

        // VALIDACIÓN DE RED ANTES DE ESCRIBIR
        if (!com.grupo1.sgsm.core.database.NetworkChecker.hayConexionUIO()) {
            mensajeConfirmacion.setText("Error: Sin conexión con la Matriz (UIO). No se puede registrar.");
            return;
        }

        // Extracción segura de nombres y apellidos (evita ArrayIndexOutOfBoundsException)
        String[] nombresArray = txtNombres.getText().trim().split("\\s+");
        String primer_nombre = nombresArray[0];
        String segundo_nombre = nombresArray.length > 1 ? nombresArray[1] : "";

        String[] apellidosArray = txtApellidos.getText().trim().split("\\s+");
        String primer_apellido = apellidosArray[0];
        String segundo_apellido = apellidosArray.length > 1 ? apellidosArray[1] : ""; // Corregido: ya no usa txtCorreo

        NuevoClienteDTO nuevoClienteDTO = new NuevoClienteDTO(
                txtCedula.getText(), primer_nombre, segundo_nombre,
                primer_apellido, segundo_apellido, txtCorreo.getText(),
                txtTelefono.getText(), txtDireccion.getText(), txtSucursal.getText()
        );

        try{
            clientesService.guardarCliente(nuevoClienteDTO);
            mensajeConfirmacion.setText("Cliente registrado exitosamente.");
        } catch(Exception e){
            mensajeConfirmacion.setText(e.getMessage());
        }
    }

    @FXML
    void cancelarOperacion(ActionEvent event) {
        // Lógica para limpiar el form o volver
        System.out.println("Operación cancelada.");
    }
}