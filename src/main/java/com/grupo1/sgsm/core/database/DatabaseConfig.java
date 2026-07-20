package com.grupo1.sgsm.core.database;

import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {
    private static final Properties properties = new Properties();

    static {
        try (InputStream is = DatabaseConfig.class
                .getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (is == null) {
                throw new RuntimeException("No se encontró application.properties");
            }
            properties.load(is);
        } catch (Exception e) {
            throw new RuntimeException("Error cargando configuración", e);
        }
    }

    public static String getUrl(String dbKey) {
        return properties.getProperty("db." + dbKey + ".url");
    }

    public static String getUser(String dbKey) {
        return properties.getProperty("db." + dbKey + ".user");
    }

    public static String getPassword(String dbKey) {
        return properties.getProperty("db." + dbKey + ".password");
    }
}

