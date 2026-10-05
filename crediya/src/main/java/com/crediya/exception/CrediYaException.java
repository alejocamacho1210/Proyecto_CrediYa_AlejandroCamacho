package com.crediya.exception;

// Excepción base de todos los errores del sistema
public class CrediYaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    // Solo con mensaje
    public CrediYaException(String mensaje) {
        super(mensaje);
    }

    // Con mensaje y causa original
    public CrediYaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
