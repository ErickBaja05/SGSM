package com.grupo1.sgsm.inventarioYproductos.controller;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;

import com.grupo1.sgsm.inventarioYproductos.dto.ConsultaStockRemotoDTO;
import com.grupo1.sgsm.inventarioYproductos.service.IStockRemotoService;
import com.grupo1.sgsm.inventarioYproductos.service.StockRemotoService;
import com.grupo1.sgsm.core.util.ConfigSucursal;

import java.net.URL;
import java.time.Instant;
import java.util.List;
import java.util.ResourceBundle;

public class consultarStockRemotoController implements Initializable {

    @FXML private TextField txtBuscar;
    @FXML private Label lblSearchIcon;
    @FXML private Label lblLocationIcon;
    @FXML private Label lblReloadIcon;
    @FXML private Button btnRecargar;

    // --- Etiquetas Dinámicas ---
    @FXML private Label lblTitulo;
    @FXML private Label lblSubtitulo;
    @FXML private Label lblInventarioTitulo;
    @FXML private Label lblNombreSede;
    @FXML private Label lblTipoSede;
    @FXML private Label lblCodigoInterno;
    @FXML private Label lblSincronizacion;

    // --- Tabla y Columnas ---
    @FXML private TableView<ConsultaStockRemotoDTO> tbStock;
    @FXML private TableColumn<ConsultaStockRemotoDTO, String> colCodigo;
    @FXML private TableColumn<ConsultaStockRemotoDTO, String> colNombre;
    @FXML private TableColumn<ConsultaStockRemotoDTO, Integer> colStock;
    @FXML private TableColumn<ConsultaStockRemotoDTO, String> colSucursal;

    private final IStockRemotoService stockRemotoService = new StockRemotoService();
    private ObservableList<ConsultaStockRemotoDTO> masterData = FXCollections.observableArrayList();

    // Variables de cronómetro en vivo
    private Timeline cronometroSincronizacion;
    private Instant tiempoUltimaSincronizacion;

    private FontIcon crearIcono(String iconLiteral, String styleClass) {
        FontIcon icon = new FontIcon(iconLiteral);
        icon.getStyleClass().add(styleClass);
        return icon;
    }

    private void configurarTabla() {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colSucursal.setCellValueFactory(new PropertyValueFactory<>("sucursal"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        colStock.setCellFactory(param -> new TableCell<ConsultaStockRemotoDTO, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    Label lblPildora = new Label(String.valueOf(item));
                    lblPildora.setStyle("-fx-padding: 4 12; -fx-background-radius: 12; -fx-font-weight: bold; -fx-font-size: 11px;");

                    if (item < 10) {
                        lblPildora.setStyle(lblPildora.getStyle() + "-fx-background-color: #FDF2F2; -fx-text-fill: #D32F2F;");
                    } else {
                        lblPildora.setStyle(lblPildora.getStyle() + "-fx-background-color: #E2E8F0; -fx-text-fill: #333333;");
                    }

                    HBox box = new HBox(lblPildora);
                    box.setStyle("-fx-alignment: CENTER;");

                    setGraphic(box);
                    setText(null);
                }
            }
        });
    }

    private void buscarProductoRealTime(String query) {
        if (query == null || query.isEmpty()) {
            tbStock.setItems(masterData);
            return;
        }

        String lowerCaseFilter = query.toLowerCase();
        ObservableList<ConsultaStockRemotoDTO> filteredData = FXCollections.observableArrayList();

        for (ConsultaStockRemotoDTO p : masterData) {
            if ((p.getCodigo() != null && p.getCodigo().toLowerCase().contains(lowerCaseFilter)) ||
                    (p.getNombre() != null && p.getNombre().toLowerCase().contains(lowerCaseFilter))) {
                filteredData.add(p);
            }
        }
        tbStock.setItems(filteredData);
    }

    @FXML
    void recargarDatos(ActionEvent event) {
        // Efecto visual: Vaciar temporalmente la tabla para dar retroalimentación clara de recarga
        tbStock.setItems(FXCollections.observableArrayList());

        // Esperar un instante (150ms) y volver a cargar los datos frescos
        PauseTransition pause = new PauseTransition(Duration.millis(150));
        pause.setOnFinished(e -> cargarDatosRemotos());
        pause.play();
    }

    private void cargarDatosRemotos() {
        try {
            List<ConsultaStockRemotoDTO> datosRemotos = stockRemotoService.consultarStockRemoto();
            masterData.clear();
            if (datosRemotos != null) {
                masterData.addAll(datosRemotos);
            }
            tbStock.setItems(masterData);

            // Reiniciar tiempo del cronómetro al refrescar
            tiempoUltimaSincronizacion = Instant.now();
            actualizarTextoCronometro();
        } catch (Exception e) {
            System.err.println("Error al cargar stock remoto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void configurarTextosDinamicos() {
        String sucursalLocal = ConfigSucursal.getSucursalActual().toUpperCase();
        String sucursalRemota = "UIO".equalsIgnoreCase(sucursalLocal) ? "GYE" : "UIO";
        String nombreCiudad = "GYE".equalsIgnoreCase(sucursalRemota) ? "Guayaquil" : "Quito";
        String tipoSede = "GYE".equalsIgnoreCase(sucursalRemota) ? "SEDE COSTA" : "SEDE SIERRA";

        if (lblTitulo != null) lblTitulo.setText("CONSULTAR STOCK " + sucursalRemota);
        if (lblSubtitulo != null) lblSubtitulo.setText("Existencias en tiempo real de la sede de " + nombreCiudad);
        if (lblInventarioTitulo != null) lblInventarioTitulo.setText("Inventario " + sucursalRemota);
        if (lblNombreSede != null) lblNombreSede.setText(nombreCiudad);
        if (lblTipoSede != null) lblTipoSede.setText(tipoSede);
        if (lblCodigoInterno != null) lblCodigoInterno.setText(sucursalRemota);
    }

    private void iniciarCronometro() {
        tiempoUltimaSincronizacion = Instant.now();
        actualizarTextoCronometro();

        if (cronometroSincronizacion != null) {
            cronometroSincronizacion.stop();
        }

        cronometroSincronizacion = new Timeline(
            new KeyFrame(Duration.seconds(1), event -> actualizarTextoCronometro())
        );
        cronometroSincronizacion.setCycleCount(Timeline.INDEFINITE);
        cronometroSincronizacion.play();
    }

    private void actualizarTextoCronometro() {
        if (lblSincronizacion == null || tiempoUltimaSincronizacion == null) {
            return;
        }

        long segundos = java.time.Duration.between(tiempoUltimaSincronizacion, Instant.now()).getSeconds();
        if (segundos < 60) {
            lblSincronizacion.setText("Hace " + segundos + " s");
        } else if (segundos < 3600) {
            long minutos = segundos / 60;
            lblSincronizacion.setText("Hace " + minutos + " min");
        } else {
            long horas = segundos / 3600;
            lblSincronizacion.setText("Hace " + horas + " h");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Cargar Íconos
        lblSearchIcon.setGraphic(crearIcono("fa-search", "search-icon-font"));
        lblLocationIcon.setGraphic(crearIcono("fa-map-marker", ""));
        lblReloadIcon.setGraphic(crearIcono("fa-refresh", "action-table-icon"));

        configurarTextosDinamicos();
        configurarTabla();

        // Configurar Búsqueda en tiempo real
        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            buscarProductoRealTime(newValue);
        });

        // Cargar datos reales desde el nodo remoto
        cargarDatosRemotos();

        // Iniciar cronómetro de última sincronización en vivo
        iniciarCronometro();
    }
}