package com.crediya.util;

import com.crediya.exception.ValidacionException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.regex.Pattern;

// Validaciones de datos; todas lanzan ValidacionException
public final class Validador {

    // Patrones de correo y teléfono
    private static final Pattern CORREO = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");
    private static final Pattern TELEFONO = Pattern.compile("^[0-9+\\- ]{7,20}$");

    // Clase de utilidad: no se instancia
    private Validador() {
    }

    // Texto obligatorio con largo máximo
    public static String texto(String valor, String campo, int maximo) {
        if (valor == null || valor.isBlank()) {
            throw new ValidacionException("El campo " + campo + " es obligatorio");
        }
        String limpio = valor.trim();
        if (limpio.length() > maximo) {
            throw new ValidacionException("El campo " + campo + " no puede superar " + maximo + " caracteres");
        }
        return limpio;
    }

    // Correo con formato válido
    public static String correo(String valor, int maximo) {
        String limpio = texto(valor, "correo", maximo);
        if (!CORREO.matcher(limpio).matches()) {
            throw new ValidacionException("El correo '" + limpio + "' no tiene un formato valido");
        }
        return limpio;
    }

    // Teléfono con formato válido
    public static String telefono(String valor) {
        String limpio = texto(valor, "telefono", 20);
        if (!TELEFONO.matcher(limpio).matches()) {
            throw new ValidacionException("El telefono debe tener entre 7 y 20 digitos (se permite + - y espacios)");
        }
        return limpio;
    }

    // Número mayor que cero y con tope
    public static BigDecimal positivo(BigDecimal valor, String campo, BigDecimal maximo) {
        if (valor == null || valor.signum() <= 0) {
            throw new ValidacionException("El campo " + campo + " debe ser mayor que cero");
        }
        if (valor.compareTo(maximo) > 0) {
            throw new ValidacionException("El campo " + campo + " no puede superar " + maximo.toPlainString());
        }
        return valor;
    }

    // Porcentaje entre 0 y 100
    public static BigDecimal porcentaje(BigDecimal valor, String campo) {
        if (valor == null || valor.signum() < 0 || valor.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new ValidacionException("El campo " + campo + " debe estar entre 0 y 100");
        }
        return valor;
    }

    // Entero dentro de un rango
    public static int enteroEnRango(int valor, String campo, int minimo, int maximo) {
        if (valor < minimo || valor > maximo) {
            throw new ValidacionException("El campo " + campo + " debe estar entre " + minimo + " y " + maximo);
        }
        return valor;
    }

    // Fecha obligatoria
    public static LocalDate fecha(LocalDate valor, String campo) {
        if (valor == null) {
            throw new ValidacionException("El campo " + campo + " es obligatorio");
        }
        return valor;
    }
}
