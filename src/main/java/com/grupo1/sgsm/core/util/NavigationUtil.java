package com.grupo1.sgsm.core.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public final class NavigationUtil {
    public static void changeScene(ActionEvent event, Parent root) {
        // Obtener Stage actual
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Cambiar escena
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }


    public static void openNewWindow(ActionEvent event, Parent root, String title) {
        // Crear nuevo Stage
        Stage stage = new Stage();
        stage.setTitle(title);

        stage.setScene(new Scene(root));
        stage.setResizable(false);

        // Modal
        stage.initModality(Modality.WINDOW_MODAL);

        // Ventana padre
        Stage parentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.initOwner(parentStage);

        // Mostrar y esperar
        stage.showAndWait();
    }
}
