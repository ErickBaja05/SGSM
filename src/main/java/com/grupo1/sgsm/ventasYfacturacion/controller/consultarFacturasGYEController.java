package com.grupo1.sgsm.ventasYfacturacion.controller;

import com.grupo1.sgsm.ventasYfacturacion.dto.FacturaGYEConsultadaDTO;
import com.grupo1.sgsm.ventasYfacturacion.exception.FechasNoValidasException;
import com.grupo1.sgsm.ventasYfacturacion.service.FacturasGYEServiceImpl;
import com.grupo1.sgsm.ventasYfacturacion.service.IFacturasGYEService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class consultarFacturasGYEController implements Initializable {

    // --- Filtros ---
    @FXML private DatePicker dpFechaInicio;
    @FXML private DatePicker dpFechaFin;
    @FXML private Button btnFiltrar;

    // --- Tabla ---
    // Ahora usa directamente el DTO devuelto por el Service
    @FXML private TableView<FacturaGYEConsultadaDTO> tbFacturas;
    @FXML private TableColumn<FacturaGYEConsultadaDTO, String> colNumeroFactura;
    @FXML private TableColumn<FacturaGYEConsultadaDTO, String> colFechaEmision;
    @FXML private TableColumn<FacturaGYEConsultadaDTO, String> colCedulaCliente;

    // --- Paginación y Footer ---
    @FXML private Label lblFacturaSeleccionada;
    @FXML private Button btnVerCompleta;

    // --- Íconos ---
    @FXML private Label lblIconFiltrar;
    @FXML private Label lblIconVer;

    // Instancia del Service
    private IFacturasGYEService facturaGYEService = new FacturasGYEServiceImpl();

    // --- Variables de Estado ---
    private ObservableList<FacturaGYEConsultadaDTO> listaFacturas = FXCollections.observableArrayList();

    // AQUÍ SE GUARDA LA FACTURA SELECCIONADA PARA MANDARLA A LA OTRA VENTANA
    private FacturaGYEConsultadaDTO facturaActualSeleccionada;

    // --- Métodos Auxiliares de UI ---
    private FontIcon crearIcono(String iconLiteral, String styleClass) {
        FontIcon icon = new FontIcon(iconLiteral);
        icon.getStyleClass().add(styleClass);
        return icon;
    }

    private void cargarIconos() {
        lblIconFiltrar.setGraphic(crearIcono("fa-filter", "btn-primary-icon-font"));
        lblIconVer.setGraphic(crearIcono("fa-eye", "btn-primary-icon-font"));
    }

    private void configurarTabla() {
        // Importante: Estos nombres deben coincidir EXACTAMENTE con los getters de FacturaGYEConsultadaDTO
        // Por ejemplo, si el getter es getNumero_factura(), aquí debe ir "numero_factura"
        colNumeroFactura.setCellValueFactory(new PropertyValueFactory<>("numero_factura"));
        colFechaEmision.setCellValueFactory(new PropertyValueFactory<>("fecha_emision"));
        colCedulaCliente.setCellValueFactory(new PropertyValueFactory<>("cedula_cliente"));
    }

    @FXML
    void filtrarFacturas(ActionEvent event) {
        LocalDate fechaInicio = dpFechaInicio.getValue();
        LocalDate fechaFin = dpFechaFin.getValue();

        // 1. Validar que el usuario sí haya seleccionado ambas fechas
        if (fechaInicio == null || fechaFin == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos Vacíos", "Por favor, seleccione una fecha de inicio y una fecha de fin.");
            return;
        }

        try {
            // 2. Consumir el Service (Que internamente valida las fechas contra LocalDate.now())
            List<FacturaGYEConsultadaDTO> facturas = facturaGYEService.consultarFacturas(fechaInicio, fechaFin);

            // 3. Limpiar y llenar la ObservableList con los datos reales de SQL Server
            listaFacturas.setAll(facturas);

            // 4. Actualizar la tabla visualmente
            tbFacturas.setItems(listaFacturas);
            tbFacturas.getSelectionModel().clearSelection();

        } catch (FechasNoValidasException e) {
            // Atrapa la excepción personalizada de tu Service y la muestra bonito en pantalla
            mostrarAlerta(Alert.AlertType.ERROR, "Fechas Inválidas", e.getMessage());
        } catch (Exception e) {
            // Catch de seguridad por si el DAO falla por red (NetworkChecker, Linked Servers, etc.)
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Consulta", "No se pudo consultar las facturas. Verifique su conexión de red.");
            e.printStackTrace();
        }
    }

    @FXML
    void verInformacionCompleta(ActionEvent event) {
        if (facturaActualSeleccionada != null) {
            // Dependiendo del nombre del getter en tu DTO, puede ser getNumero_factura()
            System.out.println("Abriendo información completa de la factura: " + facturaActualSeleccionada.getNumero_factura());

            // Aquí puedes llamar a tu NavigationUtil o cargar el nuevo FXML
            // pasándole el objeto 'facturaActualSeleccionada' al controlador de destino.
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarIconos();
        configurarTabla();

        // MAGIA: Listener para detectar cuando se selecciona una fila en la tabla
        tbFacturas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Hay una fila seleccionada
                facturaActualSeleccionada = newSelection;
                btnVerCompleta.setDisable(false); // Habilitar botón

                // Actualizar Label con el nombre de propiedad correcto de tu DTO
                lblFacturaSeleccionada.setText("Factura seleccionada: " + newSelection.getNumero_factura());
                lblFacturaSeleccionada.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #00796B;");
            } else {
                // Se desmarcó la fila
                facturaActualSeleccionada = null;
                btnVerCompleta.setDisable(true); // Deshabilitar botón

                lblFacturaSeleccionada.setText("Factura seleccionada: Ninguna");
                lblFacturaSeleccionada.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #555555;");
            }
        });
    }

    // --- Método utilitario para no repetir código de alertas ---
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}