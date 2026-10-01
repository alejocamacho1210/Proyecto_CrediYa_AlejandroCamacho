package com.crediya.util;

import java.math.BigDecimal;
import java.util.Locale;

public final class Formato {

    private Formato() {
    }

    public static String dinero(BigDecimal valor) {
        return String.format(Locale.US, "$%,.2f", valor);
    }

    public static String porcentaje(BigDecimal valor) {
        return String.format(Locale.US, "%.2f%%", valor);
    }

    public static String recortar(String texto, int maximo) {
        if (texto == null) {
            return "";
        }
        return texto.length() <= maximo ? texto : texto.substring(0, maximo - 1) + ".";
    }
}
