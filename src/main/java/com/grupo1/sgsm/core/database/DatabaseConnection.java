package com.grupo1.sgsm.core.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private DatabaseConnection() {}

    public static Connection getConnection(String dbKey) throws SQLException {
        if ("uio".equalsIgnoreCase(dbKey)) {
            return DriverManager.getConnection(
                    DatabaseConfig.getUrl("uio"),
                    DatabaseConfig.getUser("uio"),
                    DatabaseConfig.getPassword("uio")
            );
        } else if ("gye".equalsIgnoreCase(dbKey)) {
            return DriverManager.getConnection(
                    DatabaseConfig.getUrl("gye"),
                    DatabaseConfig.getUser("gye"),
                    DatabaseConfig.getPassword("gye")
            );
        } else {
            throw new IllegalArgumentException("Base de datos desconocida: " + dbKey);
        }
    }
}

