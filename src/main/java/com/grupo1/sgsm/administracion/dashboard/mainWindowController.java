package com.grupo1.sgsm.administracion.dashboard;

import com.grupo1.sgsm.core.util.NavigationUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class mainWindowController {

    @FXML
    void logOut(ActionEvent event) throws IOException {
        String vista = "/administracion/fxml/login.fxml";
        Parent root = FXMLLoader.load(getClass().getResource(vista));
        NavigationUtil.changeScene(event, root);
    }

}
