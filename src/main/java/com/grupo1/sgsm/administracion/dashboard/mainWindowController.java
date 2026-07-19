package com.grupo1.sgsm.administracion.dashboard;

import com.grupo1.sgsm.core.session.SesionActual;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.ResourceBundle;

public class mainWindowController implements Initializable {

    @FXML
    private VBox administracionBox;

    @FXML
    private Button btnAdministracion;

    @FXML
    private Button btnAjusteStock;

    @FXML
    private Button btnClientes;

    @FXML
    private Button btnConsultarCatalogo;

    @FXML
    private Button btnConsultarFacturas;

    @FXML
    private Button btnCrearProducto;

    @FXML
    private Button btnCrearUsuario;

    @FXML
    private Button btnFacturacion;

    @FXML
    private Button btnFacturarProductos;

    @FXML
    private Button btnGestionClientes;

    @FXML
    private Button btnGestionParametros;

    @FXML
    private Button btnGestionUsuarios;

    @FXML
    private Button btnGoMain;

    @FXML
    private Button btnLogOut;

    @FXML
    private Button btnMarketing;

    @FXML
    private Button btnNuevaVenta;

    @FXML
    private Button btnParametros;

    @FXML
    private Button btnProductos;

    @FXML
    private Button btnRegistrarCliente;

    @FXML
    private Button btnRegistrarSucursal;

    @FXML
    private Button btnStockLocal;

    @FXML
    private Button btnStockRemoto;

    @FXML
    private Button btnUsuarios;

    @FXML
    private VBox clientesBox;

    @FXML
    private Pane contenedorPrincipal;

    @FXML
    private VBox facturacionBox;

    @FXML
    private Label iconoUsuario;

    @FXML
    private Label lblLogBalon;

    @FXML
    private Label lblRol;

    @FXML
    private Label lblSede;

    @FXML
    private Label lblUsuario;

    @FXML
    private VBox menuBox;

    @FXML
    private VBox parametrosBox;

    @FXML
    private VBox parametrosSubMenu;

    @FXML
    private VBox productosBox;


    @FXML
    private VBox submenuClientes;

    @FXML
    private VBox submenuFacturacion;

    @FXML
    private VBox submenuProductos;

    @FXML
    private VBox usuariosBox;

    @FXML
    private VBox usuariosSubMenu;

    @FXML
    void abirGestionUsuarios(ActionEvent event) {

    }

    @FXML
    void abrirAjusteStock(ActionEvent event) {

    }

    @FXML
    void abrirConsultarCatálogo(ActionEvent event) {

    }

    @FXML
    void abrirConsultarFacturas(ActionEvent event) {

    }

    @FXML
    void abrirCrearProducto(ActionEvent event) {

    }

    @FXML
    void abrirCrearUsuario(ActionEvent event) {

    }

    @FXML
    void abrirFacturacion(ActionEvent event) {

    }

    @FXML
    void abrirGestionClientes(ActionEvent event) {

    }

    @FXML
    void abrirGestionParametros(ActionEvent event) {

    }

    @FXML
    void abrirMarketing(ActionEvent event) {

    }

    @FXML
    void abrirRegistrarClientes(ActionEvent event) {

    }

    @FXML
    void abrirRegistrarSucursal(ActionEvent event) {

    }

    @FXML
    void abrirStockLocal(ActionEvent event) {

    }

    @FXML
    void abrirStockOtraSede(ActionEvent event) {

    }

    @FXML
    void goMain(ActionEvent event) {

    }

    @FXML
    void logOut(ActionEvent event) {

    }

    @FXML
    void toggleAdministracion(ActionEvent event) {
        boolean abierto = usuariosBox.isVisible() && parametrosBox.isVisible();
        usuariosBox.setVisible(!abierto);
        usuariosBox.setManaged(!abierto);
        parametrosBox.setVisible(!abierto);
        parametrosBox.setManaged(!abierto);
    }

    @FXML
    void toggleClientes(ActionEvent event) {
        boolean abierto = submenuClientes.isVisible();
        submenuClientes.setVisible(!abierto);
        submenuClientes.setManaged(!abierto);
    }

    @FXML
    void toggleFacturacion(ActionEvent event) {
        boolean abierto = submenuFacturacion.isVisible();
        submenuFacturacion.setVisible(!abierto);
        submenuFacturacion.setManaged(!abierto);
    }

    @FXML
    void toggleParametros(ActionEvent event) {
        boolean abierto = parametrosSubMenu.isVisible();
        parametrosSubMenu.setVisible(!abierto);
        parametrosSubMenu.setManaged(!abierto);
    }

    @FXML
    void toggleProductos(ActionEvent event) {
        boolean abierto = submenuProductos.isVisible();
        submenuProductos.setVisible(!abierto);
        submenuProductos.setManaged(!abierto);
    }

    @FXML
    void toggleUsuarios(ActionEvent event) {
        boolean abierto = usuariosSubMenu.isVisible();
        usuariosSubMenu.setVisible(!abierto);
        usuariosSubMenu.setManaged(!abierto);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if(SesionActual.haySesion()){
            lblUsuario.setText(
                    SesionActual.getUsuario().getNombre_us().toUpperCase()
            );
        }
        lblRol.setText("ADMINISTRADOR");
//        if(SesionActual.getUsuario().getPerfil_us().equals("VE")){
//
//            btnAdministracion.setVisible(false);
//            btnContactoProveedores.setVisible(false);
//            btnRegistrarProducto.setVisible(false);
//            btnModificarProducto.setVisible(false);
//            btnReportePsicotropicos.setVisible(false);
//            btnRegistrarProducto5.setVisible(false);
//            btnProductosBajaRotacion.setVisible(false);
//            btnProductosProximosCaducar.setVisible(false);
//            btnAvanceCaja.setVisible(false);
//            btnArqueoCaja.setVisible(false);
//            btngenerarReporteRentabilidad.setVisible(false);
//            btnVerFacturas.setVisible(false);
//            btnPromocionesExistentes.setVisible(false);
//            btnGenerarReporteVentas.setVisible(false);
//            btnReporteARCSA.setVisible(false);
//
//
//            btnAdministracion.setManaged(false);
//            btnContactoProveedores.setManaged(false);
//            btnRegistrarProducto.setManaged(false);
//            btnModificarProducto.setManaged(false);
//            btnReportePsicotropicos.setManaged(false);
//            btnRegistrarProducto5.setManaged(false);
//            btnProductosBajaRotacion.setManaged(false);
//            btnProductosProximosCaducar.setManaged(false);
//            btnAvanceCaja.setManaged(false);
//            btnArqueoCaja.setManaged(false);
//            btngenerarReporteRentabilidad.setManaged(false);
//            btnVerFacturas.setManaged(false);
//            btnPromocionesExistentes.setManaged(false);
//            btnGenerarReporteVentas.setManaged(false);
//            btnReporteARCSA.setManaged(false);
//
//            lblPerfilUsuario.setText("AUXILIAR");
//
//        }

        // ICONOS PRINCIPALES
        iconoUsuario.setGraphic(crearIcono("fa-user-circle-o", "userMdIcon"));
        btnLogOut.setGraphic(crearIcono("fa-angle-double-left", "buttonsIcon"));
        lblLogBalon.setGraphic(crearIcono("fa-soccer-ball-o", "soccerBallIcon"));
        lblLogBalon.setText(null);

        // CLIENTES
        btnClientes.setGraphic(crearIcono("fa-users", "buttonsIcon"));
        btnRegistrarCliente.setGraphic(crearIcono("fa-address-book", "buttonsIcon"));
        btnGestionClientes.setGraphic(crearIcono("fa-archive", "buttonsIcon"));

        // PRODUCTOS
        btnProductos.setGraphic(crearIcono("fa-briefcase", "buttonsIcon"));
        btnCrearProducto.setGraphic(crearIcono("fa-plus-square-o", "buttonsIcon"));
        btnConsultarCatalogo.setGraphic(crearIcono("fa-shopping-bag", "buttonsIcon"));
        btnMarketing.setGraphic(crearIcono("fa-bar-chart-o", "buttonsIcon"));
        btnStockLocal.setGraphic(crearIcono("fa-map-pin", "buttonsIcon"));
        btnStockRemoto.setGraphic(crearIcono("fa-car", "buttonsIcon"));
        btnAjusteStock.setGraphic(crearIcono("fa-edit", "buttonsIcon"));

        // FACTURACIÓN
        btnFacturacion.setGraphic(crearIcono("fa-money", "buttonsIcon"));
        btnFacturarProductos.setGraphic(crearIcono("fa-cart-plus", "buttonsIcon"));
        btnConsultarFacturas.setGraphic(crearIcono("fa-calendar-o", "buttonsIcon"));

        // ADMINISTRACIÓN Y USUARIOS
        btnAdministracion.setGraphic(crearIcono("fa-black-tie", "buttonsIcon"));
        btnUsuarios.setGraphic(crearIcono("fa-odnoklassniki", "buttonsIcon"));
        btnCrearUsuario.setGraphic(crearIcono("fa-plus-square-o", "buttonsIcon"));
        btnGestionUsuarios.setGraphic(crearIcono("fa-edit", "buttonsIcon"));

        // PARÁMETROS
        btnParametros.setGraphic(crearIcono("fa-navicon", "buttonsIcon"));
        btnRegistrarSucursal.setGraphic(crearIcono("fa-shirtsinbulk", "buttonsIcon"));
        btnGestionParametros.setGraphic(crearIcono("fa-sliders", "buttonsIcon"));

        // BOTON NUEVA VENTA
        btnNuevaVenta.setGraphic(crearIcono("fa-plus-square-o", "buttonsIcon"));
        btnGoMain.setGraphic(crearIcono("fa-home", "buttonsIcon"));


        usuariosBox.setVisible(false);
        usuariosBox.setManaged(false);
        parametrosBox.setVisible(false);
        parametrosBox.setManaged(false);




        // APLICACION DE ESTILOS

        submenuFacturacion.setOnMouseEntered(e -> {
            btnFacturacion.getStyleClass().add("navBtn-activo");
        });

        submenuFacturacion.setOnMouseExited(e -> {
            btnFacturacion.getStyleClass().remove("navBtn-activo");
        });

        submenuClientes.setOnMouseEntered(e -> {
            btnClientes.getStyleClass().add("navBtn-activo");
        });

        submenuClientes.setOnMouseExited(e -> {
            btnClientes.getStyleClass().remove("navBtn-activo");
        });

        submenuProductos.setOnMouseEntered(e -> {
            btnProductos.getStyleClass().add("navBtn-activo");
        });

        submenuProductos.setOnMouseExited(e -> {
            btnProductos.getStyleClass().remove("navBtn-activo");
        });



        parametrosSubMenu.setOnMouseEntered(e -> {
            btnParametros.getStyleClass().add("navBtn-activo");
        });

        parametrosSubMenu.setOnMouseExited(e -> {
            btnParametros.getStyleClass().remove("navBtn-activo");
        });
        usuariosSubMenu.setOnMouseEntered(e -> {
            btnUsuarios.getStyleClass().add("navBtn-activo");
        });

        usuariosSubMenu.setOnMouseExited(e -> {
            btnUsuarios.getStyleClass().remove("navBtn-activo");
        });

        usuariosBox.setOnMouseEntered(e -> {
            btnAdministracion.getStyleClass().add("navBtn-activo");
        });
        usuariosBox.setOnMouseExited(e -> {
            btnAdministracion.getStyleClass().remove("navBtn-activo");
        });

        parametrosBox.setOnMouseEntered(e -> {
            btnAdministracion.getStyleClass().add("navBtn-activo");
        });
        parametrosBox.setOnMouseExited(e -> {
            btnAdministracion.getStyleClass().remove("navBtn-activo");
        });

    }

    // Método auxiliar para evitar duplicidad de nodos de íconos en JavaFX
    private FontIcon crearIcono(String iconLiteral, String styleClass) {
        FontIcon icon = new FontIcon(iconLiteral);
        icon.getStyleClass().add(styleClass);
        return icon;
    }


}





