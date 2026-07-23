package com.grupo1.sgsm.administracion.gestionParametros.controller;

import com.grupo1.sgsm.administracion.gestionParametros.dto.SucursalDTO;
import com.grupo1.sgsm.administracion.gestionParametros.service.IParametrosService;
import com.grupo1.sgsm.administracion.gestionParametros.service.ParametrosServiceImpl;
import com.grupo1.sgsm.core.session.SesionActual;
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

    @FXML private TableView<SucursalDTO> tbSucursales;
    @FXML private TableColumn<SucursalDTO, String> colId;
    @FXML private TableColumn<SucursalDTO, String> colNombre;
    @FXML private TableColumn<SucursalDTO, String> colDireccion;
    @FXML private TableColumn<SucursalDTO, String> colTelefono;
    @FXML private TableColumn<SucursalDTO, Void> colAccion;

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

    private ObservableList<SucursalDTO> masterData = FXCollections.observableArrayList();
    private SucursalDTO sucursalSeleccionada;

    private IParametrosService parametrosService = new ParametrosServiceImpl();

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

        cargarDatos();
        lblMensajeIva.setWrapText(true);
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
        colId.setCellValueFactory(new PropertyValueFactory<>("codigo_sucursal"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));

        // Magia visual: Columna de Acción con el botón del lápiz
        colAccion.setCellFactory(param -> new TableCell<SucursalDTO, Void>() {
            private final Button btnEdit = new Button();

            {
                btnEdit.setGraphic(crearIcono("fa-pencil", "action-table-icon"));
                btnEdit.getStyleClass().add("btn-transparent");

                btnEdit.setOnAction(event -> {
                    SucursalDTO data = getTableView().getItems().get(getIndex());
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

    private void cargarDetalleEdicion(SucursalDTO sucursal) {
        this.sucursalSeleccionada = sucursal;

        lblEditTitle.setText("Edición de Sucursal: " + sucursal.getCodigo_sucursal());
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
            lblMensajeIva.setText("El valor de IVA no puede estar vacío");
            lblMensajeIva.getStyleClass().addAll("mensaje-error");
            return;
        }

        if(!nuevoIva.matches("[0-9]+")){
            lblMensajeIva.setText("Ingrese solo un número entero que simboliza el porcentaje");
            lblMensajeIva.getStyleClass().addAll("mensaje-error");
            return;
        }
        SesionActual.setValorIva(Double.parseDouble(nuevoIva));
        lblMensajeIva.setText("IVA Actualizado");
        lblMensajeIva.getStyleClass().add("mensaje-exito");
    }

    @FXML
    void guardarCambiosSucursal(ActionEvent event) {
        if (sucursalSeleccionada == null) return;

        sucursalSeleccionada.setNombre(txtEditNombre.getText());
        sucursalSeleccionada.setDireccion(txtEditDireccion.getText());
        sucursalSeleccionada.setTelefono(txtEditTelefono.getText());

        parametrosService.actualizarNombreSucursal(sucursalSeleccionada.getCodigo_sucursal(), sucursalSeleccionada.getNombre());
        parametrosService.actualizarDireccionSucursal(sucursalSeleccionada.getCodigo_sucursal(), sucursalSeleccionada.getDireccion());
        parametrosService.actualizarTelefonoSucursal(sucursalSeleccionada.getCodigo_sucursal(), sucursalSeleccionada.getTelefono());
        tbSucursales.refresh();


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
        ObservableList<SucursalDTO> filteredData = FXCollections.observableArrayList();
        for (SucursalDTO s : masterData) {
            if (s.getCodigo_sucursal().toLowerCase().contains(filter) || s.getNombre().toLowerCase().contains(filter)) {
                filteredData.add(s);
            }
        }
        tbSucursales.setItems(filteredData);
    }

    private void cargarDatos() {
        try {
            masterData.clear();
            masterData.addAll(parametrosService.consultarSucursales());
            tbSucursales.setItems(masterData);
        } catch (Exception e) {
            System.err.println("Error al cargar los datos de la base de datos: " + e.getMessage());
        }
    }


}