package com.crediya.exception;

public class EntidadNoEncontradaException extends CrediYaException {

    private static final long serialVersionUID = 1L;

    public EntidadNoEncontradaException(String entidad, int id) {
        super(entidad + " con id " + id + " no existe");
    }
}
