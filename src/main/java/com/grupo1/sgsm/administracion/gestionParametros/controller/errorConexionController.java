package com.grupo1.sgsm.administracion.gestionParametros.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.kordamp.ikonli.javafx.FontIcon;

public class errorConexionController {

    @FXML private Label lblErrorIcon;
    @FXML private Button btnReintentar;

    // Método auxiliar para evitar duplicidad de nodos
    private FontIcon crearIcono(String iconLiteral, String styleClass) {
        FontIcon icon = new FontIcon(iconLiteral);
        icon.getStyleClass().add(styleClass);
        return icon;
    }

    @FXML
    public void initialize() {
        // En FontAwesome 'fa-chain-broken' o 'fa-ban' funcionan perfecto para esto
        lblErrorIcon.setGraphic(crearIcono("fa-chain-broken", "error-giant-icon-font"));
    }

    @FXML
    void reintentarConexion(ActionEvent event) {
        // Aquí dispararías nuevamente tu script de conexión a SQL Server
        // para verificar si el nodo remoto en Guayaquil o Quito ya está levantado.
        System.out.println("Intentando restablecer conexión con la base de datos remota...");
    }
}
