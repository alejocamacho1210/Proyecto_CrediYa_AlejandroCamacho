package com.crediya.util;

import java.math.BigDecimal;
import java.util.Locale;

// Formato de dinero, porcentajes y textos para las tablas
public final class Formato {

    // Clase de utilidad: no se instancia
    private Formato() {
    }

    // Ejemplo: $1,120,000.00
    public static String dinero(BigDecimal valor) {
        return String.format(Locale.US, "$%,.2f", valor);
    }

    // Ejemplo: 2.00%
    public static String porcentaje(BigDecimal valor) {
        return String.format(Locale.US, "%.2f%%", valor);
    }

    // Acorta textos largos para que las columnas queden alineadas
    public static String recortar(String texto, int maximo) {
        if (texto == null) {
            return "";
        }
        return texto.length() <= maximo ? texto : texto.substring(0, maximo - 1) + ".";
    }
}
