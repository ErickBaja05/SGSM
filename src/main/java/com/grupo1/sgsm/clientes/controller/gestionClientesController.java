package com.grupo1.sgsm.clientes.controller;

import com.grupo1.sgsm.clientes.exception.ClienteReferenciadoException;
import com.grupo1.sgsm.clientes.service.ClientesService;
import com.grupo1.sgsm.clientes.dto.ClienteConsultaDTO; // DTO importado

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.kordamp.ikonli.javafx.FontIcon;

public class gestionClientesController {

    @FXML private TextField txtBuscar;

    // 1. Uso del DTO en lugar del modelo genérico Cliente
    @FXML private TableView<ClienteConsultaDTO> tbClientes;
    @FXML private TableColumn<ClienteConsultaDTO, String> colCedula;
    @FXML private TableColumn<ClienteConsultaDTO, String> colNombres;
    @FXML private TableColumn<ClienteConsultaDTO, String> colApellidos;
    @FXML private TableColumn<ClienteConsultaDTO, String> colCorreo;
    @FXML private TableColumn<ClienteConsultaDTO, String> colDireccion;
    @FXML private TableColumn<ClienteConsultaDTO, Void> colAccion;

    // Campos de Edición
    @FXML private TextField txtEditNombres;
    @FXML private TextField txtEditApellidos;
    @FXML private TextField txtEditCorreo;
    @FXML private TextField txtEditDireccion;

    // Íconos
    @FXML private Label lblSearchIcon;
    @FXML private Label lblEditHeaderIcon;
    @FXML private Label lblBtnEliminarIcon;
    @FXML private Label lblBtnGuardarIcon;

    private ClientesService clientesService;
    private ObservableList<ClienteConsultaDTO> masterData = FXCollections.observableArrayList();

    // Variable para guardar la cédula en memoria al momento de editar
    private String cedulaSeleccionada = null;

    private FontIcon crearIcono(String iconLiteral, String styleClass) {
        FontIcon icon = new FontIcon(iconLiteral);
        icon.getStyleClass().add(styleClass);
        return icon;
    }

    public gestionClientesController() {
        this.clientesService = new ClientesService();
    }

    @FXML
    public void initialize() {
        lblSearchIcon.setGraphic(crearIcono("fa-search", "search-icon-font"));
        lblEditHeaderIcon.setGraphic(crearIcono("fa-indent", "edit-header-icon-font"));
        lblBtnEliminarIcon.setGraphic(crearIcono("fa-trash-o", "btn-danger-icon-font"));
        lblBtnGuardarIcon.setGraphic(crearIcono("fa-save", "btn-primary-icon-font"));

        // Propiedades adaptadas a los getters de ClienteConsultaDTO
        colCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        colNombres.setCellValueFactory(new PropertyValueFactory<>("nombre")); // Es getNombre() en el DTO
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));

        configurarColumnaAccion();

        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            buscarClienteEnTiempoReal(newValue);
        });

        cargarDatos();
    }

    private void cargarDatos() {
        // Obtenemos los datos desde el service y llenamos la tabla
        masterData.setAll(clientesService.consultarTodosLosClientes());
        tbClientes.setItems(masterData);
    }

    private void configurarColumnaAccion() {
        colAccion.setCellFactory(param -> new TableCell<ClienteConsultaDTO, Void>() {
            private final Button btnAccion = new Button();

            {
                btnAccion.setGraphic(crearIcono("fa-pencil-square-o", "action-table-icon"));
                btnAccion.getStyleClass().add("btn-transparent");

                btnAccion.setOnAction(event -> {
                    ClienteConsultaDTO clienteSeleccionado = getTableView().getItems().get(getIndex());
                    cargarDatosEdicion(clienteSeleccionado);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    ClienteConsultaDTO cliente = getTableView().getItems().get(getIndex());

                    // REGLA DE NEGOCIO: Si son de otra sucursal, no aparece el botón
                    if ("NO DISPONIBLE".equals(cliente.getCorreo()) || "NO DISPONIBLE".equals(cliente.getDireccion())) {
                        setGraphic(null);
                    } else {
                        setGraphic(btnAccion);
                    }
                }
            }
        });
    }

    private void cargarDatosEdicion(ClienteConsultaDTO cliente) {
        if (cliente != null) {
            // Guardamos la cédula para el update
            this.cedulaSeleccionada = cliente.getCedula();

            // Llenamos los textfields (Deshabilitar Nombres/Apellidos en la UI si no se deben editar)
            txtEditNombres.setText(cliente.getNombre());
            txtEditApellidos.setText(cliente.getApellidos());
            txtEditCorreo.setText(cliente.getCorreo());
            txtEditDireccion.setText(cliente.getDireccion());
        }
    }

    private void buscarClienteEnTiempoReal(String filtro) {
        if (filtro == null || filtro.isEmpty()) {
            tbClientes.setItems(masterData);
            return;
        }

        String lowerCaseFilter = filtro.toLowerCase();
        ObservableList<ClienteConsultaDTO> filteredData = FXCollections.observableArrayList();

        // Búsqueda adaptada a las propiedades de ClienteConsultaDTO
        for (ClienteConsultaDTO cliente : masterData) {
            if (cliente.getCedula().toLowerCase().contains(lowerCaseFilter) ||
                    cliente.getNombre().toLowerCase().contains(lowerCaseFilter) ||
                    cliente.getApellidos().toLowerCase().contains(lowerCaseFilter)) {
                filteredData.add(cliente);
            }
        }
        tbClientes.setItems(filteredData);
    }


    @FXML
    void eliminarCliente(ActionEvent event) {
        if (cedulaSeleccionada != null) {

            // VALIDACIÓN DE RED
            if (!com.grupo1.sgsm.core.database.NetworkChecker.hayConexionUIO()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Problema de Red");
                alert.setHeaderText("Sin conexión con Matriz");
                alert.setContentText("No se puede eliminar el cliente en este momento. Verifique su conexión con UIO.");
                alert.showAndWait();
                return;
            }

            try{
                clientesService.eliminarCliente(cedulaSeleccionada);
                // Limpiamos la memoria de la interfaz y refrescamos la tabla
                cedulaSeleccionada = null;
                limpiarCamposEdicion();
                cargarDatos();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Éxito");
                alert.setHeaderText("Cambios guardados con éxito");
                alert.setContentText("Cliente eliminado exitosamente");
                alert.showAndWait(); // Te faltaba el .showAndWait() para que la alerta se vea jeje
            }catch(ClienteReferenciadoException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Cambios no efectuados");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Cambios no efectuados");
            alert.setContentText("Por favor, seleccione un cliente en la tabla primero (clic en el botón de acción)");
            alert.showAndWait();
        }
    }

    @FXML
    void guardarCambios(ActionEvent event) {
        if (cedulaSeleccionada != null) {

            // VALIDACIÓN DE RED
            if (!com.grupo1.sgsm.core.database.NetworkChecker.hayConexionUIO()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Problema de Red");
                alert.setHeaderText("Sin conexión con Matriz");
                alert.setContentText("No se pueden actualizar los datos en este momento. Verifique su conexión con UIO.");
                alert.showAndWait();
                return;
            }

            String nuevoCorreo = txtEditCorreo.getText();
            String nuevaDireccion = txtEditDireccion.getText();

            clientesService.actualizarCliente(cedulaSeleccionada, nuevoCorreo, nuevaDireccion);

            // Refrescar tabla y limpiar variables de memoria
            cargarDatos();
            cedulaSeleccionada = null;
            limpiarCamposEdicion();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Éxito");
            alert.setHeaderText("Cambios guardados con éxito");
            alert.setContentText("Información guardada correctamente");
            alert.showAndWait(); // Mostrar la alerta
        }
    }

    private void limpiarCamposEdicion() {
        txtEditNombres.clear();
        txtEditApellidos.clear();
        txtEditCorreo.clear();
        txtEditDireccion.clear();
    }
}