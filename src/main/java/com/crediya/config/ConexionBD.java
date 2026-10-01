package com.crediya.config;

import com.crediya.exception.PersistenciaException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class ConexionBD {

    private static final String URL_POR_DEFECTO =
            "jdbc:mysql://localhost:3306/crediya_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USUARIO_POR_DEFECTO = "root";
    private static final String CLAVE_POR_DEFECTO = "";
    private static final Path ARCHIVO_CONFIGURACION = Paths.get("config.properties");
    private static final ConexionBD INSTANCIA = new ConexionBD();

    private final String url;
    private final String usuario;
    private final String clave;

    private ConexionBD() {
        Properties propiedades = cargarPropiedades();
        this.url = resolver("CREDIYA_DB_URL", propiedades.getProperty("db.url"), URL_POR_DEFECTO);
        this.usuario = resolver("CREDIYA_DB_USER", propiedades.getProperty("db.user"), USUARIO_POR_DEFECTO);
        this.clave = resolver("CREDIYA_DB_PASSWORD", propiedades.getProperty("db.password"), CLAVE_POR_DEFECTO);
    }

    public static ConexionBD getInstancia() {
        return INSTANCIA;
    }

    public Connection obtener() {
        try {
            return DriverManager.getConnection(url, usuario, clave);
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo conectar a MySQL: " + e.getMessage(), e);
        }
    }

    public boolean estaDisponible() {
        try (Connection conexion = obtener()) {
            return conexion.isValid(3);
        } catch (SQLException | PersistenciaException e) {
            return false;
        }
    }

    private static Properties cargarPropiedades() {
        Properties propiedades = new Properties();
        if (Files.exists(ARCHIVO_CONFIGURACION)) {
            try (InputStream entrada = Files.newInputStream(ARCHIVO_CONFIGURACION)) {
                propiedades.load(entrada);
            } catch (IOException e) {
                throw new PersistenciaException("No se pudo leer config.properties", e);
            }
        }
        return propiedades;
    }

    private static String resolver(String variableEntorno, String propiedad, String valorPorDefecto) {
        String entorno = System.getenv(variableEntorno);
        if (entorno != null && !entorno.isBlank()) {
            return entorno;
        }
        if (propiedad != null) {
            return propiedad.trim();
        }
        return valorPorDefecto;
    }
}
