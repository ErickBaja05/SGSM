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
    private VBox submenuAdministracion;

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
        ocultarTodos();
        if (!abierto) {
            usuariosBox.setVisible(true);
            usuariosBox.setManaged(true);
            parametrosBox.setVisible(true);
            parametrosBox.setManaged(true);
        }

    }

    @FXML
    void toggleClientes(ActionEvent event) {

        boolean abierto = submenuClientes.isVisible();
        ocultarTodos();
        if (!abierto) {
            submenuClientes.setVisible(true);
            submenuClientes.setManaged(true);
        }

    }

    @FXML
    void toggleFacturacion(ActionEvent event) {

        boolean abierto = submenuFacturacion.isVisible();
        ocultarTodos();
        if (!abierto) {
            submenuFacturacion.setVisible(true);
            submenuFacturacion.setManaged(true);
        }

    }

    @FXML
    void toggleParametros(ActionEvent event) {

        boolean abierto = parametrosSubMenu.isVisible();
        ocultarGestionParametros();
        if (!abierto) {
            parametrosSubMenu.setVisible(true);
            parametrosSubMenu.setManaged(true);
        }

    }

    @FXML
    void toggleProductos(ActionEvent event) {
        boolean abierto = submenuProductos.isVisible();
        ocultarTodos();
        if (!abierto) {
            submenuProductos.setVisible(true);
            submenuProductos.setManaged(true);
        }
    }

    @FXML
    void toggleUsuarios(ActionEvent event) {

        boolean abierto = usuariosSubMenu.isVisible();
        ocultarGestionUsuarios();
        if (!abierto) {
            usuariosSubMenu.setVisible(true);
            usuariosSubMenu.setManaged(true);
        }

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

        // ICONO PARA EL USUARIO



        FontIcon icon0 = new FontIcon("fa-user-circle-o");
        icon0.getStyleClass().add("userMdIcon");

        iconoUsuario.setGraphic(icon0);


        // ICONO PARA BOTON LOG OUT
        FontIcon icon = new FontIcon("fa-angle-double-left");
        icon.getStyleClass().add("buttonsIcon");

        btnLogOut.setGraphic(icon);


        // ICONO PARA EL ICONO ALADO DEL TITULO
        FontIcon icon2 = new FontIcon("fa-soccer-ball-o");
        icon2.getStyleClass().add("soccerBallIcon");

        lblLogBalon.setGraphic(icon2);
        lblLogBalon.setText(null);

        // ---------------------------------------------------

        // ICONOS PARA LAS OPCIONES DE CLIENTES

        // BOTON PRINCIPAL CLIENTES
        FontIcon icon3 = new FontIcon("fa-users");
        icon3.getStyleClass().add("buttonsIcon");

        btnClientes.setGraphic(icon3);


        // BOTON REGISTRAR NUEVO CLIENTE

        FontIcon icon4 = new FontIcon("fa-address-book");
        icon4.getStyleClass().add("buttonsIcon");

        btnRegistrarCliente.setGraphic(icon4);


        // BOTON GESTION DE CLIENTES

        FontIcon icon5 = new FontIcon("fa-archive");
        icon5.getStyleClass().add("buttonsIcon");

        btnGestionClientes.setGraphic(icon5);

        // ---------------------------------------------------

        // ICONO PARA LAS OPCIONES DE PRODUCTOS

        // BOTON PRINCIPAL PRODUCTOS

        FontIcon icon6 = new FontIcon("fa-briefcase");
        icon6.getStyleClass().add("buttonsIcon");

        btnProductos.setGraphic(icon6);


        // BOTON REGISTRAR PRODUCTO

        FontIcon icon7 = new FontIcon("fa-plus-square-o");
        icon7.getStyleClass().add("buttonsIcon");

        btnCrearProducto.setGraphic(icon7);

        // BOTON CONSULTAR CATALOGO

        FontIcon icon8 = new FontIcon("fa-shopping-bag");
        icon8.getStyleClass().add("buttonsIcon");

        btnConsultarCatalogo.setGraphic(icon8);

        // BOTON INFORMACION MARKETING

        FontIcon icon9 = new FontIcon("fa-bar-chart-o");
        icon9.getStyleClass().add("buttonsIcon");

        btnMarketing.setGraphic(icon9);

        // BOTON CONSULTAR STOCK LOCAL

        FontIcon icon10 = new FontIcon("fa-map-pin");
        icon10.getStyleClass().add("buttonsIcon");

        btnStockLocal.setGraphic(icon10);

        // BOTON CONSULTAR STOCK REMOTO

        FontIcon icon11 = new FontIcon("fa-car");
        icon11.getStyleClass().add("buttonsIcon");

        btnStockRemoto.setGraphic(icon11);

        // BOTON AJUSTE DE STOCK

        FontIcon icon12 = new FontIcon("fa-edit");
        icon12.getStyleClass().add("buttonsIcon");

        btnAjusteStock.setGraphic(icon11);


        // ---------------------------------------------------

        // ICONOS PARA FACTURACION


        // BOTON PRINCIPAL FACTURACION

        FontIcon icon13 = new FontIcon("fa-money");
        icon13.getStyleClass().add("buttonsIcon");

        btnFacturacion.setGraphic(icon13);

        // BOTON FACTURAR PRODUCTO

        FontIcon icon14 = new FontIcon("fa-cart-plus");
        icon14.getStyleClass().add("buttonsIcon");

        btnFacturarProductos.setGraphic(icon14);


        // BOTON CONSULTAR FACTURA

        FontIcon icon15 = new FontIcon("fa-calendar-o");
        icon15.getStyleClass().add("buttonsIcon");

        btnConsultarFacturas.setGraphic(icon15);

        // ---------------------------------------------------


        // ICONOS PARA ADMINISTRACION


        // BOTON PRINCIPAL ADMINISTRACION

        FontIcon icon16 = new FontIcon("fa-black-tie");
        icon16.getStyleClass().add("buttonsIcon");
        btnAdministracion.setGraphic(icon16);

        // BOTON USUARIOS

        FontIcon icon17 = new FontIcon("fa-odnoklassniki");
        icon17.getStyleClass().add("buttonsIcon");
        btnUsuarios.setGraphic(icon17);

        // BOTON CREAR USUARIO

        btnCrearUsuario.setGraphic(icon7);


        // BOTON GESTION DE USUARIOS

        btnGestionUsuarios.setGraphic(icon12);

        // BOTON PARÁMETROS

        FontIcon icon18 = new FontIcon("fa-navicon");
        icon18.getStyleClass().add("buttonsIcon");
        btnParametros.setGraphic(icon18);

        // BOTON REGISTRAR NUEVA SUCURSAL

        FontIcon icon19 = new FontIcon("fa-shirtsinbulk");
        icon19.getStyleClass().add("buttonsIcon");
        btnRegistrarSucursal.setGraphic(icon19);

        // BOTON GESTIÓN DE PARÁMETROS

        FontIcon icon20 = new FontIcon("fa-sliders");
        icon20.getStyleClass().add("buttonsIcon");
        btnRegistrarSucursal.setGraphic(icon20);

        // ---------------------------------------------------

        // BOTON NUEVA VENTA

        btnNuevaVenta.setGraphic(icon7);


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

    void ocultarTodos() {
        submenuFacturacion.setVisible(false);
        submenuFacturacion.setManaged(false);
        submenuClientes.setVisible(false);
        submenuClientes.setManaged(false);
        submenuProductos.setVisible(false);
        submenuProductos.setManaged(false);

        ocultarGestionParametros();
        ocultarGestionUsuarios();
        parametrosBox.setVisible(false);
        parametrosBox.setManaged(false);
        usuariosBox.setVisible(false);
        usuariosBox.setManaged(false);
    }



    void ocultarGestionParametros(){
        parametrosSubMenu.setVisible(false);
        parametrosSubMenu.setManaged(false);
    }

    void ocultarGestionUsuarios(){
        usuariosSubMenu.setVisible(false);
        usuariosSubMenu.setManaged(false);
    }
}





