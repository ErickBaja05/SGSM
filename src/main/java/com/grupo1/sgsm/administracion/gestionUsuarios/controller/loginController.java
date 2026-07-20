package com.grupo1.sgsm.administracion.gestionUsuarios.controller;

import com.grupo1.sgsm.administracion.gestionUsuarios.dto.UsuarioSesionDTO;
import com.grupo1.sgsm.administracion.gestionUsuarios.service.IUsuarioService;
import com.grupo1.sgsm.administracion.gestionUsuarios.service.UsuarioServiceImpl;
import com.grupo1.sgsm.core.session.SesionActual;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon; // Asegúrate de tener este import

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class loginController implements Initializable {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContrasena;
    @FXML private TextField txtContrasenaVisible;
    @FXML private Label lblMensaje;

    // Inyección de los Labels del FXML que contendrán los iconos
    @FXML private Label lblLogoIcon;
    @FXML private Label lblUserIcon;
    @FXML private Label lblPassIcon;
    @FXML private Label lblEyeIcon;
    @FXML private Label lblBtnIcon;

    private IUsuarioService usuarioService;


    // Tu método auxiliar para evitar duplicidad
    private FontIcon crearIcono(String iconLiteral, String styleClass) {
        FontIcon icon = new FontIcon(iconLiteral);
        icon.getStyleClass().add(styleClass);
        return icon;
    }


    @FXML
    public void toggleMostrarContrasena(MouseEvent event) {
        boolean isVisible = txtContrasenaVisible.isVisible();

        if (isVisible) {
            // Ocultar contraseña
            txtContrasenaVisible.setVisible(false);
            txtContrasenaVisible.setManaged(false);
            txtContrasena.setVisible(true);
            txtContrasena.setManaged(true);

            // Cambiar dinámicamente al ojito normal
            lblEyeIcon.setGraphic(crearIcono("fa-eye", "action-icon"));
        } else {
            // Mostrar contraseña
            txtContrasenaVisible.setVisible(true);
            txtContrasenaVisible.setManaged(true);
            txtContrasena.setVisible(false);
            txtContrasena.setManaged(false);

            // Cambiar dinámicamente al ojito tachado
            lblEyeIcon.setGraphic(crearIcono("fa-eye-slash", "action-icon"));
        }
    }

    @FXML
    public void login(ActionEvent event) {
        String usuario = txtUsuario.getText();
        String contrasena = txtContrasena.getText();
        lblMensaje.setText("");

        // ELIMINAR ESTAS DOS LINEAS Y DESCOMENTAR EL TRY CUANDO YA ESTEN USUARIOS EN LA BASE DE DATOS

        UsuarioSesionDTO usuariologin= new UsuarioSesionDTO(1,"Erick","ADMINISTRADOR","UIO");
        SesionActual.iniciarSesion(usuariologin);

        cargarDashboard(event);
//        try{
//            UsuarioSesionDTO usuarioLogin = usuarioService.login(usuario, contrasena);
//            SesionActual.iniciarSesion(usuarioLogin);
 //              cargarDashboard(event);

//        }catch(Exception e){
//            lblMensaje.setText(e.getMessage());
//        }

    }

    private void cargarDashboard(ActionEvent event) {
        try {
            String vista = "/administracion/fxml/dashboard.fxml";
            Parent root = FXMLLoader.load(getClass().getResource(vista));

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.setTitle("SPORT MASTER ECUADOR");
            stage.setMaximized(true);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            lblMensaje.setText("Error al cargar");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inyectamos los iconos directamente como gráficos de los Labels
        // Nota: Ajusta los nombres "fa-..." a la versión exacta de FontAwesome que tengas en tu POM
        lblLogoIcon.setGraphic(crearIcono("fa-futbol-o", "logo-icon")); // O "fa-basketball-ball" si usas una versión más reciente
        lblUserIcon.setGraphic(crearIcono("fa-user", "input-icon"));
        lblPassIcon.setGraphic(crearIcono("fa-lock", "input-icon"));
        lblEyeIcon.setGraphic(crearIcono("fa-eye", "action-icon"));
        lblBtnIcon.setGraphic(crearIcono("fa-sign-in", "btn-icon"));

        // Binding bidireccional para que lo que escribas en un campo se refleje en el otro
        if (txtContrasena != null && txtContrasenaVisible != null) {
            txtContrasenaVisible.textProperty().bindBidirectional(txtContrasena.textProperty());
        }

        usuarioService = new UsuarioServiceImpl();

    }
}