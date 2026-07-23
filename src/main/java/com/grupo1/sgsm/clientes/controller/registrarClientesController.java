package com.grupo1.sgsm.clientes.controller;

import com.grupo1.sgsm.clientes.dto.NuevoClienteDTO;
import com.grupo1.sgsm.clientes.service.ClientesService;
import com.grupo1.sgsm.clientes.service.IClientesService;
import com.grupo1.sgsm.core.util.ConfigSucursal;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.ResourceBundle;

public class registrarClientesController implements Initializable {

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

    // Para el mensaje
    @FXML private Label mensajeConfirmacion;

    private IClientesService clientesService;

    // Método auxiliar para Ikonli
    private FontIcon crearIcono(String iconLiteral, String styleClass) {
        FontIcon icon = new FontIcon(iconLiteral);
        icon.getStyleClass().add(styleClass);
        return icon;
    }

    private void mostrarError(String mensaje) {
        mensajeConfirmacion.setText(mensaje);
        mensajeConfirmacion.getStyleClass().setAll("mensaje-error");
    }

    /**
     * Aplica la clase CSS "focused-box" al HBox padre cuando el TextField gana foco.
     */
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
    void registrarCliente(ActionEvent event) {
        // 1. Validación de campos vacíos
        if (txtCedula.getText().isBlank() || txtNombres.getText().isBlank() || txtApellidos.getText().isBlank()
                || txtCorreo.getText().isBlank() || txtTelefono.getText().isBlank()
                || txtDireccion.getText().isBlank() || txtSucursal.getText().isBlank()) {
            mostrarError("Debes llenar todos los campos obligatorios.");
            return;
        }

        String cedula = txtCedula.getText().trim();
        String nombres = txtNombres.getText().trim();
        String apellidos = txtApellidos.getText().trim();
        String correo = txtCorreo.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String sucursal = txtSucursal.getText().trim();

        // 2. Validación de Cédula (10 dígitos, 2 primeros entre 00 y 24)
        if (!cedula.matches("^\\d{10}$")) {
            mostrarError("La cédula debe contener exactamente 10 dígitos numéricos.");
            return;
        }
        int provincia = Integer.parseInt(cedula.substring(0, 2));
        if (provincia < 0 || provincia > 24) {
            mostrarError("Los dos primeros dígitos de la cédula deben estar entre 00 y 24.");
            return;
        }

        // 3. Validación de Nombres y Apellidos (mínimo dos nombres y dos apellidos con mayúscula inicial)
        String[] partesNombres = nombres.split("\\s+");
        String[] partesApellidos = apellidos.split("\\s+");

        boolean nombresValidos = partesNombres.length >= 2;
        if (nombresValidos) {
            for (String parte : partesNombres) {
                if (!parte.matches("^[A-ZÁÉÍÓÚÑ][a-záéíóúñA-ZÁÉÍÓÚÑ]*$")) {
                    nombresValidos = false;
                    break;
                }
            }
        }

        boolean apellidosValidos = partesApellidos.length >= 2;
        if (apellidosValidos) {
            for (String parte : partesApellidos) {
                if (!parte.matches("^[A-ZÁÉÍÓÚÑ][a-záéíóúñA-ZÁÉÍÓÚÑ]*$")) {
                    apellidosValidos = false;
                    break;
                }
            }
        }

        if (!nombresValidos || !apellidosValidos) {
            mostrarError("Se debe ingresar dos nombres y dos apellidos");
            return;
        }

        // 5. Validación de Correo Electrónico (Regex)
        String regexCorreo = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!correo.matches(regexCorreo)) {
            mostrarError("El correo electrónico ingresado no tiene un formato válido (ej. usuario@dominio.com).");
            return;
        }

        // 6. Validación de Celular (10 dígitos y empieza con 09)
        if (!telefono.matches("^09\\d{8}$")) {
            mostrarError("El número de celular debe contener 10 dígitos numéricos y comenzar con '09'.");
            return;
        }

        // 7. VALIDACIÓN DE RED ANTES DE ESCRIBIR
        if (!com.grupo1.sgsm.core.database.NetworkChecker.hayConexionUIO()) {
            mostrarError("Error: Sin conexión con la Matriz (UIO). No se puede registrar.");
            return;
        }

        // Extracción segura de nombres y apellidos
        String primer_nombre = partesNombres[0];
        String segundo_nombre = String.join(" ", java.util.Arrays.copyOfRange(partesNombres, 1, partesNombres.length));

        String primer_apellido = partesApellidos[0];
        String segundo_apellido = String.join(" ", java.util.Arrays.copyOfRange(partesApellidos, 1, partesApellidos.length));

        NuevoClienteDTO nuevoClienteDTO = new NuevoClienteDTO(
                cedula, primer_nombre, segundo_nombre,
                primer_apellido, segundo_apellido, correo,
                telefono, direccion, sucursal
        );

        try {
            clientesService.guardarCliente(nuevoClienteDTO);
            mensajeConfirmacion.setText("Cliente registrado exitosamente.");
            mensajeConfirmacion.getStyleClass().setAll("mensaje-exito");
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    void cancelarOperacion(ActionEvent event) {
        txtCedula.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtCorreo.setText("");
        txtTelefono.setText("");
        txtDireccion.setText("");
        mensajeConfirmacion.setText("");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Carga de Íconos
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

        clientesService = new ClientesService();

        // Muestra la sucursal donde se está realizando el registro (nodo local)
        txtSucursal.setText(ConfigSucursal.getSucursalActual().toUpperCase());
        txtSucursal.setDisable(true);
    }
}