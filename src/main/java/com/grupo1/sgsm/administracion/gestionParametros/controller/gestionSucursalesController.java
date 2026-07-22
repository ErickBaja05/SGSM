package com.grupo1.sgsm.administracion.gestionParametros.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.kordamp.ikonli.javafx.FontIcon;

public class gestionSucursalesController {

    // --- Tarjeta IVA ---
    @FXML private TextField txtIva;
    @FXML private Label lblMensajeIva;
    @FXML private Label lblIconIva;
    @FXML private Label lblIconActualizarIva;

    // --- Tarjeta Directorio ---
    @FXML private TextField txtBuscarSucursal;
    @FXML private Label lblIconDirectorio;
    @FXML private Label lblIconBuscar;

    @FXML private TableView<Sucursal> tbSucursales;
    @FXML private TableColumn<Sucursal, String> colId;
    @FXML private TableColumn<Sucursal, String> colNombre;
    @FXML private TableColumn<Sucursal, String> colDireccion;
    @FXML private TableColumn<Sucursal, String> colTelefono;
    @FXML private TableColumn<Sucursal, Void> colAccion;

    // --- Tarjeta Edición ---
    @FXML private Label lblEditTitle;
    @FXML private TextField txtEditNombre;
    @FXML private TextField txtEditTelefono;
    @FXML private TextField txtEditDireccion;
    @FXML private Button btnDescartar;
    @FXML private Button btnGuardarSucursal;
    @FXML private Label lblMensajeSucursal;

    @FXML private Label lblIconEditHeader;
    @FXML private Label lblIconGuardarSucursal;

    private ObservableList<Sucursal> masterData = FXCollections.observableArrayList();
    private Sucursal sucursalSeleccionada;

    private FontIcon crearIcono(String iconLiteral, String styleClass) {
        FontIcon icon = new FontIcon(iconLiteral);
        icon.getStyleClass().add(styleClass);
        return icon;
    }

    @FXML
    public void initialize() {
        cargarIconos();
        configurarTabla();

        // Búsqueda Dinámica
        txtBuscarSucursal.textProperty().addListener((observable, oldValue, newValue) -> {
            buscarSucursalRealTime(newValue);
        });

        cargarDatosPrueba();
    }

    private void cargarIconos() {
        lblIconIva.setGraphic(crearIcono("fa-percent", ""));
        lblIconActualizarIva.setGraphic(crearIcono("fa-refresh", "btn-primary-icon-font"));

        lblIconDirectorio.setGraphic(crearIcono("fa-building", ""));
        lblIconBuscar.setGraphic(crearIcono("fa-search", "search-icon-font"));

        lblIconEditHeader.setGraphic(crearIcono("fa-pencil", ""));
        lblIconGuardarSucursal.setGraphic(crearIcono("fa-save", "btn-primary-icon-font"));
    }

    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));

        // Magia visual: Columna de Acción con el botón del lápiz
        colAccion.setCellFactory(param -> new TableCell<Sucursal, Void>() {
            private final Button btnEdit = new Button();

            {
                btnEdit.setGraphic(crearIcono("fa-pencil", "action-table-icon"));
                btnEdit.getStyleClass().add("btn-transparent");

                btnEdit.setOnAction(event -> {
                    Sucursal data = getTableView().getItems().get(getIndex());
                    cargarDetalleEdicion(data);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnEdit);
                }
            }
        });
    }

    private void cargarDetalleEdicion(Sucursal sucursal) {
        this.sucursalSeleccionada = sucursal;

        lblEditTitle.setText("Edición de Sucursal: " + sucursal.getId());
        txtEditNombre.setText(sucursal.getNombre());
        txtEditDireccion.setText(sucursal.getDireccion());
        txtEditTelefono.setText(sucursal.getTelefono());

        btnDescartar.setDisable(false);
        btnGuardarSucursal.setDisable(false);
        lblMensajeSucursal.setText("");
    }

    private void limpiarFormularioEdicion() {
        this.sucursalSeleccionada = null;
        lblEditTitle.setText("Edición de Sucursal: -");
        txtEditNombre.clear();
        txtEditDireccion.clear();
        txtEditTelefono.clear();

        btnDescartar.setDisable(true);
        btnGuardarSucursal.setDisable(true);
        lblMensajeSucursal.setText("");
    }

    @FXML
    void actualizarIva(ActionEvent event) {
        lblMensajeIva.setText("");
        lblMensajeIva.getStyleClass().removeAll("mensaje-error", "mensaje-exito");

        String nuevoIva = txtIva.getText();
        if (nuevoIva.isEmpty()) {
            lblMensajeIva.setText("Ingrese un valor");
            lblMensajeIva.getStyleClass().add("mensaje-error");
            return;
        }

        System.out.println("Actualizando IVA general a: " + nuevoIva);
        lblMensajeIva.setText("IVA Actualizado");
        lblMensajeIva.getStyleClass().add("mensaje-exito");
    }

    @FXML
    void guardarCambiosSucursal(ActionEvent event) {
        if (sucursalSeleccionada == null) return;

        sucursalSeleccionada.setNombre(txtEditNombre.getText());
        sucursalSeleccionada.setDireccion(txtEditDireccion.getText());
        sucursalSeleccionada.setTelefono(txtEditTelefono.getText());

        tbSucursales.refresh();
        System.out.println("Guardando cambios para la sucursal: " + sucursalSeleccionada.getId());

        lblMensajeSucursal.setText("Cambios guardados correctamente");
        lblMensajeSucursal.getStyleClass().setAll("mensaje-confirmacion", "mensaje-exito");
    }

    @FXML
    void descartarEdicion(ActionEvent event) {
        System.out.println("Edición descartada.");
        limpiarFormularioEdicion();
    }

    private void buscarSucursalRealTime(String query) {
        if (query == null || query.isEmpty()) {
            tbSucursales.setItems(masterData);
            return;
        }
        String filter = query.toLowerCase();
        ObservableList<Sucursal> filteredData = FXCollections.observableArrayList();
        for (Sucursal s : masterData) {
            if (s.getId().toLowerCase().contains(filter) || s.getNombre().toLowerCase().contains(filter)) {
                filteredData.add(s);
            }
        }
        tbSucursales.setItems(filteredData);
    }

    private void cargarDatosPrueba() {
        masterData.addAll(
                new Sucursal("SM-001", "Matriz Quito North", "Av. Amazonas N45-12 y Gaspar de Villaroel", "02-2456-789"),
                new Sucursal("SM-002", "Guayaquil Terminal", "C.C. Terminal Terrestre, Local 124", "04-2134-567"),
                new Sucursal("SM-003", "Cuenca El Ejido", "Av. Solano 4-15 y Tadeo Torres", "07-4109-876")
        );
        tbSucursales.setItems(masterData);
    }

    // --- Clase Auxiliar ---
    public static class Sucursal {
        private String id;
        private String nombre;
        private String direccion;
        private String telefono;

        public Sucursal(String id, String nombre, String direccion, String telefono) {
            this.id = id; this.nombre = nombre; this.direccion = direccion; this.telefono = telefono;
        }

        public String getId() { return id; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getDireccion() { return direccion; }
        public void setDireccion(String direccion) { this.direccion = direccion; }
        public String getTelefono() { return telefono; }
        public void setTelefono(String telefono) { this.telefono = telefono; }
    }
}