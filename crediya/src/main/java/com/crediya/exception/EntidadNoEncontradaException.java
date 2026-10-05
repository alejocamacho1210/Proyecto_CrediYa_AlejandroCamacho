package com.crediya.exception;

// Se lanza cuando se busca un id que no existe
public class EntidadNoEncontradaException extends CrediYaException {

    private static final long serialVersionUID = 1L;

    public EntidadNoEncontradaException(String entidad, int id) {
        super(entidad + " con id " + id + " no existe");
    }
}
