package com.crediya.ui;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

// Lectura del teclado y escritura en pantalla
public class Consola {

    // Lector del teclado
    private final Scanner entrada = new Scanner(System.in);

    // Salida
    public void mostrar(String texto) {
        System.out.println(texto);
    }

    public void imprimir(String formato, Object... argumentos) {
        System.out.printf(formato, argumentos);
    }

    // Entrada: cada método repite la pregunta hasta recibir un dato válido
    public String texto(String etiqueta) {
        System.out.print(etiqueta + ": ");
        if (!entrada.hasNextLine()) {
            throw new EntradaFinalizadaException();
        }
        return entrada.nextLine().trim();
    }

    // Entero
    public int entero(String etiqueta) {
        while (true) {
            String valor = texto(etiqueta);
            try {
                return Integer.parseInt(valor);
            } catch (NumberFormatException e) {
                mostrar("Ingrese un numero entero valido.");
            }
        }
    }

    // Decimal (acepta coma o punto)
    public BigDecimal decimal(String etiqueta) {
        while (true) {
            String valor = texto(etiqueta).replace(",", ".");
            try {
                return new BigDecimal(valor);
            } catch (NumberFormatException e) {
                mostrar("Ingrese un numero valido (ejemplo: 1500000 o 2.5).");
            }
        }
    }

    // Fecha AAAA-MM-DD; con Enter usa el valor por defecto
    public LocalDate fecha(String etiqueta, LocalDate porDefecto) {
        while (true) {
            String valor = texto(etiqueta + " (AAAA-MM-DD, Enter = " + porDefecto + ")");
            if (valor.isEmpty()) {
                return porDefecto;
            }
            try {
                return LocalDate.parse(valor);
            } catch (DateTimeParseException e) {
                mostrar("Fecha invalida. Use el formato AAAA-MM-DD.");
            }
        }
    }
}
