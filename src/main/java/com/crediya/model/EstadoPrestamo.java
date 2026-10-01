package com.crediya.model;

import com.crediya.exception.ValidacionException;

public enum EstadoPrestamo {

    PENDIENTE("pendiente"),
    PAGADO("pagado");

    private final String etiqueta;

    EstadoPrestamo(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public static EstadoPrestamo desdeTexto(String texto) {
        if (texto != null) {
            for (EstadoPrestamo estado : values()) {
                if (estado.etiqueta.equalsIgnoreCase(texto.trim())) {
                    return estado;
                }
            }
        }
        throw new ValidacionException("Estado de prestamo invalido: " + texto);
    }
}
