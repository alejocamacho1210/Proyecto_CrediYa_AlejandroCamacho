package com.crediya.tarea;

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

// Singleton: una sola instancia que entrega la conexión a MySQL (crediya_db)
public final class ConexionDB {

    // Valores por defecto si no hay configuración
    private static final String URL_POR_DEFECTO =
            "jdbc:mysql://localhost:3306/crediya_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final Path ARCHIVO_CONFIGURACION = Paths.get("config.properties");

    // Única instancia de la clase
    private static ConexionDB instancia;

    // Datos de conexión y la conexión abierta
    private final String url;
    private final String usuario;
    private final String clave;
    private Connection conexion;

    // Constructor privado: lee config.properties y variables de entorno
    private ConexionDB() {
        Properties propiedades = cargarPropiedades();
        this.url = resolver("CREDIYA_DB_URL", propiedades.getProperty("db.url"), URL_POR_DEFECTO);
        this.usuario = resolver("CREDIYA_DB_USER", propiedades.getProperty("db.user"), "root");
        this.clave = resolver("CREDIYA_DB_PASSWORD", propiedades.getProperty("db.password"), "");
    }

    // Acceso a la instancia única
    public static synchronized ConexionDB getInstancia() {
        if (instancia == null) {
            instancia = new ConexionDB();
        }
        return instancia;
    }

    // Devuelve la conexión; la abre solo si no existe o fue cerrada
    public synchronized Connection getConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            conexion = DriverManager.getConnection(url, usuario, clave);
        }
        return conexion;
    }

    // Cierra la conexión al terminar el programa
    public synchronized void cerrar() {
        if (conexion != null) {
            try {
                conexion.close();
            } catch (SQLException e) {
                System.out.println("No se pudo cerrar la conexión: " + e.getMessage());
            }
            conexion = null;
        }
    }

    // Lee config.properties si existe
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

    // Prioridad: variable de entorno, archivo y valor por defecto
    private static String resolver(String variableEntorno, String propiedad, String valorPorDefecto) {
        String entorno = System.getenv(variableEntorno);
        if (entorno != null && !entorno.isBlank()) {
            return entorno;
        }
        return propiedad != null ? propiedad.trim() : valorPorDefecto;
    }
}
