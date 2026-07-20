package com.grupo1.sgsm.core.database;

import java.sql.Connection;

public class NetworkChecker {
    public static boolean hayConexionUIO() {
        try (Connection conn = DatabaseConnection.getConnection("UIO")) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}


