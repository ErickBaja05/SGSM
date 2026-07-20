package com.grupo1.sgsm.core.database;

import java.sql.Connection;

public class NetworkChecker {
    public static boolean hayConexionUIO() {
        try (Connection conn = DatabaseConnection.getConnection("UIO")) {
            // Valida que el nodo remoto realmente responde (timeout de 3 segundos)
            return conn.isValid(3);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean hayConexionGYE() {
        try (Connection conn = DatabaseConnection.getConnection("GYE")) {
            // Valida que el nodo remoto realmente responde (timeout de 3 segundos)
            return conn.isValid(3);
        } catch (Exception e) {
            return false;
        }
    }
}


