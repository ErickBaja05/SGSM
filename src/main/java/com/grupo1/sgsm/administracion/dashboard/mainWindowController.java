package com.grupo1.sgsm.administracion.dashboard;

import com.grupo1.sgsm.administracion.gestionUsuarios.dto.UsuarioSesionDTO;
import com.grupo1.sgsm.core.database.NetworkChecker;
import com.grupo1.sgsm.core.session.SesionActual;
import com.grupo1.sgsm.core.util.ConfigSucursal;
import com.grupo1.sgsm.core.util.NavigationUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class mainWindowController implements Initializable {

    @FXML private BorderPane rootPane;
    @FXML private VBox sidebar;
    @FXML private StackPane contenedorPrincipal;

    @FXML private VBox administracionBox;
    @FXML private Button btnAdministracion;
    @FXML private Button btnAjusteStock;
    @FXML private Button btnClientes;
    @FXML private Button btnConsultarCatalogo;
    @FXML private Button btnConsultarFacturas;
    @FXML private Button btnCrearProducto;
    @FXML private Button btnCrearUsuario;
    @FXML private Button btnFacturacion;
    @FXML private Button btnFacturarProductos;
    @FXML private Button btnGestionClientes;
    @FXML private Button btnGestionParametros;
    @FXML private Button btnGestionUsuarios;
    @FXML private Button btnGoMain;
    @FXML private Button btnLogOut;
    @FXML private Button btnMarketing;
    @FXML private Button btnNuevaVenta;
    @FXML private Button btnParametros;
    @FXML private Button btnProductos;
    @FXML private Button btnRegistrarCliente;
    @FXML private Button btnRegistrarSucursal;
    @FXML private Button btnStockLocal;
    @FXML private Button btnStockRemoto;
    @FXML private Button btnUsuarios;

    @FXML private VBox clientesBox;
    @FXML private VBox facturacionBox;
    @FXML private Label iconoUsuario;
    @FXML private Label lblLogBalon;
    @FXML private Label lblRol;
    @FXML private Label lblSede;
    @FXML private Label lblUsuario;
    @FXML private VBox menuBox;
    @FXML private VBox parametrosBox;
    @FXML private VBox parametrosSubMenu;
    @FXML private VBox productosBox;
    @FXML private VBox submenuClientes;
    @FXML private VBox submenuFacturacion;
    @FXML private VBox submenuProductos;
    @FXML private VBox usuariosBox;
    @FXML private VBox usuariosSubMenu;



    @FXML void abirGestionUsuarios(ActionEvent event) {}
    @FXML void abrirAjusteStock(ActionEvent event) {
        cargarVista("/inventarioYproductos/fxml/ajusteStock.fxml");
    }
    @FXML void abrirConsultarCatálogo(ActionEvent event) {
        cargarVista("/inventarioYproductos/fxml/gestionCatalogo.fxml");
    }
    @FXML void abrirConsultarFacturas(ActionEvent event) {}
    @FXML void abrirCrearProducto(ActionEvent event) {
        cargarVista("/inventarioYproductos/fxml/crearNuevoProducto.fxml");
    }
    @FXML void abrirCrearUsuario(ActionEvent event) {}
    @FXML void abrirFacturacion(ActionEvent event) {
        cargarVista(("/ventasYFacturacion/fxml/facturarProductos.fxml"));
    }
    @FXML void abrirGestionClientes(ActionEvent event) {

        if(verificarConectividad()){
            cargarVista("/clientes/fxml/gestionClientes.fxml");
            return;
        }
        cargarVista("/administracion/fxml/errorConexion.fxml");

    }
    @FXML void abrirGestionParametros(ActionEvent event) {}
    @FXML void abrirMarketing(ActionEvent event) {}
    @FXML void abrirRegistrarClientes(ActionEvent event) {
        cargarVista("/clientes/fxml/registrarClientes.fxml");
    }
    @FXML void abrirRegistrarSucursal(ActionEvent event) {}
    @FXML void abrirStockLocal(ActionEvent event) {}
    @FXML void abrirStockOtraSede(ActionEvent event) {}
    @FXML void goMain(ActionEvent event) throws IOException {

        String vista = "/administracion/fxml/dashboard.fxml";

        Parent root = FXMLLoader.load(getClass().getResource(vista));
        NavigationUtil.changeScene(event,root);
    }
    @FXML void logOut(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hasta pronto");
        alert.setHeaderText("Sesión cerrada con éxito");
        alert.setContentText("La sesión se cerró con éxito, deberá volver a iniciar sesión para acceder a cualquier funcionalidad.");
        alert.showAndWait();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/administracion/fxml/login.fxml"));
        Parent root = loader.load();
        NavigationUtil.changeScene(event,root);
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

    // Método auxiliar para evitar duplicidad de nodos de íconos en JavaFX
    private FontIcon crearIcono(String iconLiteral, String styleClass) {
        FontIcon icon = new FontIcon(iconLiteral);
        icon.getStyleClass().add(styleClass);
        return icon;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        lblUsuario.setText(
                SesionActual.getUsuario().getNombre_us().toUpperCase()
        );

        lblRol.setText(SesionActual.getUsuario().getRol().toUpperCase());
        lblSede.setText(SesionActual.getUsuario().getCodigo_sucursal().toUpperCase());


        // CONFIGURAR QUE ROL Y DE QUE SEDE PUEDE O NO VER LOS BOTONES

//        if(!usuarioActual.getRol().equals("ADMINISTRADOR")){
//            btnAdministracion.setVisible(false);
//        }
//
//        if(usuarioActual.getRol().equals("AUDITOR")){
//            btnClientes.setVisible(false);
//            btnProductos.setVisible(false);
//            btnFacturarProductos.setVisible(false);
//        }
//
//        if(usuarioActual.getRol().equals("CAJERO")){
//
//        }


        // ICONOS PRINCIPALES
        iconoUsuario.setGraphic(crearIcono("fa-user-circle-o", "userMdIcon"));
        btnLogOut.setGraphic(crearIcono("fa-angle-double-left", "navDownIcon"));
        btnGoMain.setGraphic(crearIcono("fa-home", "navDownIcon")); // Añadí la casita al botón de Main
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

        // APLICACION DE ESTILOS HOVER
        submenuFacturacion.setOnMouseEntered(e -> btnFacturacion.getStyleClass().add("navBtn-activo"));
        submenuFacturacion.setOnMouseExited(e -> btnFacturacion.getStyleClass().remove("navBtn-activo"));

        submenuClientes.setOnMouseEntered(e -> btnClientes.getStyleClass().add("navBtn-activo"));
        submenuClientes.setOnMouseExited(e -> btnClientes.getStyleClass().remove("navBtn-activo"));

        submenuProductos.setOnMouseEntered(e -> btnProductos.getStyleClass().add("navBtn-activo"));
        submenuProductos.setOnMouseExited(e -> btnProductos.getStyleClass().remove("navBtn-activo"));

        parametrosSubMenu.setOnMouseEntered(e -> btnParametros.getStyleClass().add("navBtn-activo"));
        parametrosSubMenu.setOnMouseExited(e -> btnParametros.getStyleClass().remove("navBtn-activo"));

        usuariosSubMenu.setOnMouseEntered(e -> btnUsuarios.getStyleClass().add("navBtn-activo"));
        usuariosSubMenu.setOnMouseExited(e -> btnUsuarios.getStyleClass().remove("navBtn-activo"));

        usuariosBox.setOnMouseEntered(e -> btnAdministracion.getStyleClass().add("navBtn-activo"));
        usuariosBox.setOnMouseExited(e -> btnAdministracion.getStyleClass().remove("navBtn-activo"));

        parametrosBox.setOnMouseEntered(e -> btnAdministracion.getStyleClass().add("navBtn-activo"));
        parametrosBox.setOnMouseExited(e -> btnAdministracion.getStyleClass().remove("navBtn-activo"));


    }

    private void cargarVista(String rutaFxml) {
        try {
            Parent vista = FXMLLoader.load(getClass().getResource(rutaFxml));
            contenedorPrincipal.getChildren().clear();
            contenedorPrincipal.getChildren().add(vista);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            contenedorPrincipal.getChildren().clear();
            contenedorPrincipal.getChildren().add(crearPlaceholder("Vista aún no implementada:\n" + rutaFxml));
        }
    }

    private boolean verificarConectividad(){
        if(ConfigSucursal.getSucursalActual().equalsIgnoreCase("UIO")){
            return NetworkChecker.hayConexionGYE();
        }
        return NetworkChecker.hayConexionUIO();
    }

    private Label crearPlaceholder(String mensaje) {
        Label aviso = new Label(mensaje);
        aviso.getStyleClass().add("placeholder-label");
        return aviso;
    }
}