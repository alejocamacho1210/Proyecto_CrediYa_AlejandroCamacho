package com.crediya.model;

import com.crediya.exception.ValidacionException;

// Estados posibles de un préstamo
public enum EstadoPrestamo {

    // Cada valor guarda la etiqueta que va a archivo y MySQL
    PENDIENTE("pendiente"),
    PAGADO("pagado");

    private final String etiqueta;

    EstadoPrestamo(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    // Convierte un texto leído de archivo o MySQL al enum
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
