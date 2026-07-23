module com.grupo1.sgsm {
    // Módulos JavaFX
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome;

    // Otros módulos comunes
    requires java.sql;
    requires java.desktop;
    requires javafx.base;
    //requires com.grupo1.sgsm;


    // Abre los paquetes con controladores para FXML
    opens com.grupo1.sgsm.administracion.gestionUsuarios.controller to javafx.fxml;
    opens com.grupo1.sgsm.administracion.gestionParametros.controller to javafx.fxml;
   opens com.grupo1.sgsm.ventasYfacturacion.controller to javafx.fxml;
opens com.grupo1.sgsm.clientes.controller to javafx.fxml;
   opens com.grupo1.sgsm.inventarioYproductos.controller to javafx.fxml;
    opens com.grupo1.sgsm.administracion.dashboard to javafx.fxml;

    opens com.grupo1.sgsm.administracion.gestionUsuarios.dto to javafx.base;
    opens com.grupo1.sgsm.administracion.gestionParametros.dto to javafx.base;
//    opens com.grupo1.sgsm.ventasYfacturacion.dto to javafx.base;
    opens com.grupo1.sgsm.clientes.dto to javafx.base;
    opens com.grupo1.sgsm.inventarioYproductos.model to javafx.base;

    exports com.grupo1.sgsm.app;



}