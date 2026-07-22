package com.grupo1.sgsm.administracion.gestionUsuarios.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.kordamp.ikonli.javafx.FontIcon;

public class gestionUsuariosController {

    // --- Búsqueda y Totales ---
    @FXML private TextField txtBuscar;
    @FXML private Label lblTotalUsuarios;
    @FXML private Label lblTotalAdmins;

    // --- Tabla ---
    @FXML private TableView<Usuario> tbUsuarios;
    @FXML private TableColumn<Usuario, String> colId;
    @FXML private TableColumn<Usuario, String> colUsuario;
    @FXML private TableColumn<Usuario, String> colRol;
    @FXML private TableColumn<Usuario, String> colCorreo;

    // --- Panel de Edición ---
    @FXML private Label lblEditId;
    @FXML private TextField txtEditUsuario;
    @FXML private TextField txtEditCorreo;
    @FXML private ComboBox<String> cmbEditRol;
    @FXML private Label lblMensajeConfirmacion;

    // --- Botones ---
    @FXML private Button btnModificar;
    @FXML private Button btnEliminar;

    // --- Íconos ---
    @FXML private Label lblSearchIcon;
    @FXML private Label lblEditHeaderIcon;
    @FXML private Label lblBtnModificarIcon;
    @FXML private Label lblBtnEliminarIcon;

    private ObservableList<Usuario> masterData = FXCollections.observableArrayList();
    private Usuario usuarioSeleccionado;

    private FontIcon crearIcono(String iconLiteral, String styleClass) {
        FontIcon icon = new FontIcon(iconLiteral);
        icon.getStyleClass().add(styleClass);
        return icon;
    }

    @FXML
    public void initialize() {
        cargarIconos();
        configurarTabla();

        cmbEditRol.setItems(FXCollections.observableArrayList("ADMINISTRADOR", "VENDEDOR", "LOGÍSTICA", "GERENTE"));

        // Búsqueda Dinámica
        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            buscarUsuarioRealTime(newValue);
        });

        // Listener de Selección de Tabla
        tbUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                cargarDetalleEdicion(newSelection);
            } else {
                limpiarPanelEdicion();
            }
        });

        cargarDatosPrueba();
    }

    private void cargarIconos() {
        lblSearchIcon.setGraphic(crearIcono("fa-search", "search-icon-font"));
        lblEditHeaderIcon.setGraphic(crearIcono("fa-pencil-square-o", ""));
        lblBtnModificarIcon.setGraphic(crearIcono("fa-save", "btn-primary-icon-font"));
        lblBtnEliminarIcon.setGraphic(crearIcono("fa-trash-o", "btn-danger-icon-font"));
    }

    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
    }

    private void cargarDetalleEdicion(Usuario user) {
        usuarioSeleccionado = user;

        lblEditId.setText("ID: " + user.getId());
        txtEditUsuario.setText(user.getNombreUsuario());
        txtEditCorreo.setText(user.getCorreo());
        cmbEditRol.setValue(user.getRol());

        btnModificar.setDisable(false);
        btnEliminar.setDisable(false);
        lblMensajeConfirmacion.setText("");
    }

    private void limpiarPanelEdicion() {
        usuarioSeleccionado = null;
        lblEditId.setText("ID: -");
        txtEditUsuario.clear();
        txtEditCorreo.clear();
        cmbEditRol.getSelectionModel().clearSelection();

        btnModificar.setDisable(true);
        btnEliminar.setDisable(true);
        lblMensajeConfirmacion.setText("");
    }

    private void buscarUsuarioRealTime(String query) {
        if (query == null || query.isEmpty()) {
            tbUsuarios.setItems(masterData);
            return;
        }
        String lowerCaseFilter = query.toLowerCase();
        ObservableList<Usuario> filteredData = FXCollections.observableArrayList();
        for (Usuario u : masterData) {
            if (u.getNombreUsuario().toLowerCase().contains(lowerCaseFilter) ||
                    u.getRol().toLowerCase().contains(lowerCaseFilter)) {
                filteredData.add(u);
            }
        }
        tbUsuarios.setItems(filteredData);
    }

    private void actualizarEstadisticas() {
        int total = masterData.size();
        int admins = 0;
        for (Usuario u : masterData) {
            if (u.getRol().equalsIgnoreCase("ADMINISTRADOR")) {
                admins++;
            }
        }
        lblTotalUsuarios.setText(String.valueOf(total));
        lblTotalAdmins.setText(String.format("%02d", admins)); // Formato 04
    }

    @FXML
    void modificarUsuario(ActionEvent event) {
        if (usuarioSeleccionado == null) return;

        usuarioSeleccionado.setNombreUsuario(txtEditUsuario.getText());
        usuarioSeleccionado.setCorreo(txtEditCorreo.getText());
        usuarioSeleccionado.setRol(cmbEditRol.getValue());

        tbUsuarios.refresh();
        actualizarEstadisticas();

        lblMensajeConfirmacion.setText("Usuario modificado con éxito");
        lblMensajeConfirmacion.getStyleClass().setAll("mensaje-confirmacion", "mensaje-exito");
    }

    @FXML
    void eliminarUsuario(ActionEvent event) {
        if (usuarioSeleccionado == null) return;

        masterData.remove(usuarioSeleccionado);
        tbUsuarios.getSelectionModel().clearSelection();
        actualizarEstadisticas();

        lblMensajeConfirmacion.setText("Usuario eliminado");
        lblMensajeConfirmacion.getStyleClass().setAll("mensaje-confirmacion", "mensaje-error");
    }

    private void cargarDatosPrueba() {
        masterData.addAll(
                new Usuario("#USR-001", "Admin_SportMaster", "ADMINISTRADOR", "admin@sportmaster.com.ec"),
                new Usuario("#USR-002", "Ventas_Quito1", "VENDEDOR", "v.quito1@sportmaster.com.ec"),
                new Usuario("#USR-003", "Bodega_Surtidor", "LOGÍSTICA", "bodega@sportmaster.com.ec"),
                new Usuario("#USR-004", "Gerencia_GYE", "ADMINISTRADOR", "gye.admin@sportmaster.com.ec")
        );
        tbUsuarios.setItems(masterData);
        actualizarEstadisticas();
    }

    public static class Usuario {
        private String id;
        private String nombreUsuario;
        private String rol;
        private String correo;

        public Usuario(String id, String nombreUsuario, String rol, String correo) {
            this.id = id; this.nombreUsuario = nombreUsuario; this.rol = rol; this.correo = correo;
        }

        public String getId() { return id; }
        public String getNombreUsuario() { return nombreUsuario; }
        public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
        public String getRol() { return rol; }
        public void setRol(String rol) { this.rol = rol; }
        public String getCorreo() { return correo; }
        public void setCorreo(String correo) { this.correo = correo; }
    }
}