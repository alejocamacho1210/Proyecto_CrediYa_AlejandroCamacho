package com.crediya.ui;

// Se lanza cuando el teclado ya no entrega más texto
public class EntradaFinalizadaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EntradaFinalizadaException() {
        super("Fin de la entrada estandar");
    }
}
