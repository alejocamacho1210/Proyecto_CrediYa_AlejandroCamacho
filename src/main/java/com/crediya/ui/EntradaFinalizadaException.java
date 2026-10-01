package com.crediya.ui;

public class EntradaFinalizadaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EntradaFinalizadaException() {
        super("Fin de la entrada estandar");
    }
}
