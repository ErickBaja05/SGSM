package com.grupo1.sgsm.core.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private DatabaseConnection() {}

    public static Connection getConnection(String dbKey) throws SQLException {
        Connection conn = null;

        if ("uio".equalsIgnoreCase(dbKey)) {
            conn = DriverManager.getConnection(
                    DatabaseConfig.getUrl("uio"),
                    DatabaseConfig.getUser("uio"),
                    DatabaseConfig.getPassword("uio")
            );
        } else if ("gye".equalsIgnoreCase(dbKey)) {
            conn = DriverManager.getConnection(
                    DatabaseConfig.getUrl("gye"),
                    DatabaseConfig.getUser("gye"),
                    DatabaseConfig.getPassword("gye")
            );
        } else {
            throw new IllegalArgumentException("Base de datos desconocida: " + dbKey);
        }

        // =========================================================================
        // CONFIGURACIÓN CRÍTICA PARA TRANSACCIONES DISTRIBUIDAS (LINKED SERVERS)
        // =========================================================================
        // Al ejecutar SET XACT_ABORT ON, le decimos a SQL Server que si una
        // transacción distribuida falla en cualquier punto, haga un ROLLBACK
        // completo automáticamente, garantizando la consistencia de los datos.
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("SET XACT_ABORT ON");
        } catch (SQLException e) {
            // Si falla al configurar el abort, es mejor no retornar la conexión
            conn.close();
            throw new SQLException("No se pudo configurar XACT_ABORT en la conexión", e);
        }

        return conn;
    }
}