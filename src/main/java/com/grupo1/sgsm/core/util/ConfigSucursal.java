package com.grupo1.sgsm.core.util;

import java.io.InputStream;
import java.util.Properties;

public class ConfigSucursal {

    private static final Properties properties = new Properties();

    static {
        try (InputStream is = ConfigSucursal.class
                .getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (is != null) {
                properties.load(is);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error cargando configuración", e);
        }
    }

    public static String getSucursalActual() {
        String envSucursal = System.getenv("SUCURSAL");
        if (envSucursal != null && !envSucursal.isBlank()) {
            return envSucursal;
        }
        return properties.getProperty("default.sucursal", "UIO");
    }
}
