package com.crediya.exception;

// Fallos al leer o escribir en archivos o MySQL
public class PersistenciaException extends CrediYaException {

    private static final long serialVersionUID = 1L;

    // Solo con mensaje
    public PersistenciaException(String mensaje) {
        super(mensaje);
    }

    // Con mensaje y causa original
    public PersistenciaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
