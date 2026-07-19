package com.grupo1.sgsm.clientes.controller;

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
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon; // Ikonli

// IMPORTANTE: Importa aquí tu clase modelo Cliente
import com.grupo1.sgsm.clientes.model.Cliente;

public class gestionClientesController {

    @FXML private TextField txtBuscar;

    // Configuración de Tabla (Ajusta 'Cliente' al nombre de tu clase modelo)
    @FXML private TableView<Cliente> tbClientes;
    @FXML private TableColumn<Cliente, String> colCedula;
    @FXML private TableColumn<Cliente, String> colNombres;
    @FXML private TableColumn<Cliente, String> colApellidos;
    @FXML private TableColumn<Cliente, String> colCorreo;
    @FXML private TableColumn<Cliente, String> colDireccion;
    @FXML private TableColumn<Cliente, Void> colAccion; // Void porque no se asocia a un atributo del modelo

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

    private ObservableList<Cliente> masterData = FXCollections.observableArrayList();

    private FontIcon crearIcono(String iconLiteral, String styleClass) {
        FontIcon icon = new FontIcon(iconLiteral);
        icon.getStyleClass().add(styleClass);
        return icon;
    }

    @FXML
    public void initialize() {
        // 1. Cargar Íconos
        lblSearchIcon.setGraphic(crearIcono("fa-search", "search-icon-font"));
        lblEditHeaderIcon.setGraphic(crearIcono("fa-indent", "edit-header-icon-font"));
        lblBtnEliminarIcon.setGraphic(crearIcono("fa-trash-o", "btn-danger-icon-font"));
        lblBtnGuardarIcon.setGraphic(crearIcono("fa-save", "btn-primary-icon-font"));

        // 2. Configurar Columnas de la Tabla (Los Strings deben coincidir con los atributos de tu clase Cliente)
        colCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        colNombres.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));

        // 3. Configurar Columna de Acción (Botón inyectado)
        configurarColumnaAccion();

        // 4. Configurar Búsqueda en tiempo real
        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            buscarClienteEnTiempoReal(newValue);
        });

        // (Opcional) Cargar datos de prueba o tu consulta a BD
        // cargarDatos();
    }

    private void configurarColumnaAccion() {
        colAccion.setCellFactory(param -> new TableCell<Cliente, Void>() {
            private final Button btnAccion = new Button();

            {
                // Configurar el botón transparente y su ícono
                btnAccion.setGraphic(crearIcono("fa-pencil-square-o", "action-table-icon"));
                btnAccion.getStyleClass().add("btn-transparent");

                // Evento al hacer clic en el botón de la fila
                btnAccion.setOnAction(event -> {
                    Cliente clienteSeleccionado = getTableView().getItems().get(getIndex());
                    cargarDatosEdicion(clienteSeleccionado);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnAccion);
                }
            }
        });
    }

    private void cargarDatosEdicion(Cliente cliente) {
        if (cliente != null) {
            txtEditNombres.setText(cliente.getNombres());
            txtEditApellidos.setText(cliente.getApellidos());
            txtEditCorreo.setText(cliente.getCorreo());

            // Aquí puedes aplicar tu lógica de negocio para ver si puede editar la dirección
            txtEditDireccion.setText(cliente.getDireccion());
        }
    }

    private void buscarClienteEnTiempoReal(String filtro) {
        if (filtro == null || filtro.isEmpty()) {
            tbClientes.setItems(masterData);
            return;
        }

        String lowerCaseFilter = filtro.toLowerCase();
        ObservableList<Cliente> filteredData = FXCollections.observableArrayList();

        for (Cliente cliente : masterData) {
            if (cliente.getCedula().toLowerCase().contains(lowerCaseFilter) ||
                    cliente.getNombres().toLowerCase().contains(lowerCaseFilter) ||
                    cliente.getApellidos().toLowerCase().contains(lowerCaseFilter)) {
                filteredData.add(cliente);
            }
        }
        tbClientes.setItems(filteredData);
    }

    @FXML
    void eliminarCliente(ActionEvent event) {
        System.out.println("Eliminando cliente...");
    }

    @FXML
    void guardarCambios(ActionEvent event) {
        System.out.println("Guardando cambios de: " + txtEditNombres.getText());
    }
}