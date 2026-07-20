package com.grupo1.sgsm.core.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connectionUIO;
    private static Connection connectionGYE;

    private DatabaseConnection() {}

    public static Connection getConnection(String dbKey) throws SQLException {
        if ("uio".equalsIgnoreCase(dbKey)) {
            if (connectionUIO == null || connectionUIO.isClosed()) {
                connectionUIO = DriverManager.getConnection(
                        DatabaseConfig.getUrl("uio"),
                        DatabaseConfig.getUser("uio"),
                        DatabaseConfig.getPassword("uio")
                );
            }
            return connectionUIO;
        } else if ("gye".equalsIgnoreCase(dbKey)) {
            if (connectionGYE == null || connectionGYE.isClosed()) {
                connectionGYE = DriverManager.getConnection(
                        DatabaseConfig.getUrl("gye"),
                        DatabaseConfig.getUser("gye"),
                        DatabaseConfig.getPassword("gye")
                );
            }
            return connectionGYE;
        } else {
            throw new IllegalArgumentException("Base de datos desconocida: " + dbKey);
        }
    }
}

