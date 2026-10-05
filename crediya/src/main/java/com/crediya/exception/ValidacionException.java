package com.crediya.exception;

// Datos inválidos ingresados por el usuario
public class ValidacionException extends CrediYaException {

    private static final long serialVersionUID = 1L;

    public ValidacionException(String mensaje) {
        super(mensaje);
    }
}
