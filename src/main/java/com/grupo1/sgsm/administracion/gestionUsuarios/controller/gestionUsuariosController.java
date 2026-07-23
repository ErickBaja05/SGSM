package com.grupo1.sgsm.administracion.gestionUsuarios.controller;

import com.grupo1.sgsm.administracion.gestionUsuarios.dto.UsuarioConsultadoDTO;
import com.grupo1.sgsm.administracion.gestionUsuarios.service.IUsuarioService;
import com.grupo1.sgsm.administracion.gestionUsuarios.service.UsuarioServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.ResourceBundle;

public class gestionUsuariosController implements Initializable {

    // --- Búsqueda y Totales ---
    @FXML private TextField txtBuscar;
    @FXML private Label lblTotalUsuarios;
    @FXML private Label lblTotalAdmins;

    // --- Tabla ---
    @FXML private TableView<UsuarioConsultadoDTO> tbUsuarios;
    @FXML private TableColumn<UsuarioConsultadoDTO, String> colId;
    @FXML private TableColumn<UsuarioConsultadoDTO, String> colUsuario;
    @FXML private TableColumn<UsuarioConsultadoDTO, String> colRol;
    @FXML private TableColumn<UsuarioConsultadoDTO, String> colCorreo;

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

    private ObservableList<UsuarioConsultadoDTO> masterData = FXCollections.observableArrayList();
    private UsuarioConsultadoDTO usuarioSeleccionado;

    // Instancia del servicio que conecta con tu DAO
    private final IUsuarioService usuarioService = new UsuarioServiceImpl();

    private FontIcon crearIcono(String iconLiteral, String styleClass) {
        FontIcon icon = new FontIcon(iconLiteral);
        icon.getStyleClass().add(styleClass);
        return icon;
    }

    private void cargarIconos() {
        lblSearchIcon.setGraphic(crearIcono("fa-search", "search-icon-font"));
        lblEditHeaderIcon.setGraphic(crearIcono("fa-pencil-square-o", ""));
        lblBtnModificarIcon.setGraphic(crearIcono("fa-save", "btn-primary-icon-font"));
        lblBtnEliminarIcon.setGraphic(crearIcono("fa-trash-o", "btn-danger-icon-font"));
    }

    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
    }

    private void cargarDetalleEdicion(UsuarioConsultadoDTO user) {
        usuarioSeleccionado = user;

        lblEditId.setText("ID: " + user.getIdUsuario());
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
        ObservableList<UsuarioConsultadoDTO> filteredData = FXCollections.observableArrayList();

        for (UsuarioConsultadoDTO u : masterData) {
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
        for (UsuarioConsultadoDTO u : masterData) {
            if (u.getRol() != null && u.getRol().equalsIgnoreCase("ADMINISTRADOR")) {
                admins++;
            }
        }
        lblTotalUsuarios.setText(String.valueOf(total));
        lblTotalAdmins.setText(String.format("%02d", admins));
    }

    // Método que reemplaza los datos quemados usando el Service
    private void cargarDatos() {
        try {
            masterData.clear();
            masterData.addAll(usuarioService.consultarUsuarios());
            tbUsuarios.setItems(masterData);
            actualizarEstadisticas();
        } catch (Exception e) {
            System.err.println("Error al cargar los datos de la base de datos: " + e.getMessage());
        }
    }

    @FXML
    void modificarUsuario(ActionEvent event) {
        if (usuarioSeleccionado == null) return;

        try {
            // Parseamos el ID para enviarlo a los métodos del service
            Integer idUsuario = Integer.parseInt(usuarioSeleccionado.getIdUsuario());

            String nuevoNombre = txtEditUsuario.getText();
            String nuevoCorreo = txtEditCorreo.getText();
            String nuevoRol = cmbEditRol.getValue();

            // Evaluamos y mandamos a actualizar solo lo que se modificó en el Service
            if (!nuevoNombre.equals(usuarioSeleccionado.getNombreUsuario())) {
                usuarioService.actualizarNombreUsuario(idUsuario, nuevoNombre);
            }
            if (!nuevoCorreo.equals(usuarioSeleccionado.getCorreo())) {
                usuarioService.actualizarCorreoUsuario(idUsuario, nuevoCorreo);
            }
            if (!nuevoRol.equals(usuarioSeleccionado.getRol())) {
                usuarioService.actualizarRolUsuario(idUsuario, nuevoRol);
            }

            // Recargamos la tabla desde la base de datos para asegurar consistencia
            cargarDatos();
            limpiarPanelEdicion();

            lblMensajeConfirmacion.setText("Usuario modificado con éxito");
            lblMensajeConfirmacion.getStyleClass().setAll("mensaje-confirmacion", "mensaje-exito");

        } catch (NumberFormatException e) {
            lblMensajeConfirmacion.setText("Error: Formato de ID inválido");
            lblMensajeConfirmacion.getStyleClass().setAll("mensaje-confirmacion", "mensaje-error");
        } catch (Exception e) {
            // Captura las excepciones lanzadas por el Service (ej. UsuarioYaRegistradoException)
            lblMensajeConfirmacion.setText(e.getMessage());
            lblMensajeConfirmacion.getStyleClass().setAll("mensaje-confirmacion", "mensaje-error");
        }
    }

    @FXML
    void eliminarUsuario(ActionEvent event) {
        if (usuarioSeleccionado == null) return;

        try {

            Integer idUsuario = Integer.parseInt(usuarioSeleccionado.getIdUsuario());
            usuarioService.eliminarUsuario(idUsuario);

            masterData.remove(usuarioSeleccionado);
            tbUsuarios.getSelectionModel().clearSelection();
            actualizarEstadisticas();
            limpiarPanelEdicion();

            lblMensajeConfirmacion.setText("Usuario removido de la vista (Implementar en DAO)");
            lblMensajeConfirmacion.getStyleClass().setAll("mensaje-confirmacion", "mensaje-error");
        } catch (Exception e) {
            lblMensajeConfirmacion.setText("Error al eliminar");
            lblMensajeConfirmacion.getStyleClass().setAll("mensaje-confirmacion", "mensaje-error");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarIconos();
        configurarTabla();

        cmbEditRol.setItems(FXCollections.observableArrayList("ADMINISTRADOR", "VENDEDOR", "LOGÍSTICA", "GERENTE"));

        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            buscarUsuarioRealTime(newValue);
        });

        tbUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                cargarDetalleEdicion(newSelection);
            } else {
                limpiarPanelEdicion();
            }
        });

        // Llamar a los datos reales
        cargarDatos();
    }
}