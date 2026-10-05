package com.crediya.tarea;

import com.crediya.exception.CrediYaException;

import java.time.LocalDate;

// Programa de la tarea "Gestión y Reporte de Pagos": reproduce la ejecución esperada del enunciado
public final class MainPagos {

    private MainPagos() {
    }

    // Pasar el valor del reporte como argumento
    public static void main(String[] args) {
        double valorReporte = leerValor(args);
        GestorPagos gestor = new GestorPagos();

        // 1. Registro en memoria
        System.out.println("=== REGISTRO DE PAGOS ===");
        Pago pago = new Pago(2, LocalDate.of(2025-10-09), 300000.0);
        gestor.registrarPago(pago);

        // 2. Listado
        System.out.println();
        gestor.listarPagos();

        // 3. Reporte con Stream API
        System.out.println();
        gestor.mostrarPagosMayoresA(valorReporte);

        // 4. Validaciones: monto negativo y fecha nula
        System.out.println();
        System.out.println("=== VALIDACIONES ===");
        probarMontoNegativo(gestor);
        probarFechaNula(gestor);
        gestor.registrarPago(null);

        // 5. Persistencia en MySQL
        System.out.println();
        System.out.println("=== GUARDADO EN MYSQL ===");
        if (gestor.guardarPagoEnDB(pago)) {
            System.out.println("Pago guardado en MySQL con id " + pago.getId());
        }
        ConexionDB.getInstancia().cerrar();
    }

    // Intenta crear un pago con monto negativo
    private static void probarMontoNegativo(GestorPagos gestor) {
        try {
            gestor.registrarPago(new Pago(3, LocalDate.now(), -500.0));
        } catch (CrediYaException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Intenta crear un pago con fecha nula
    private static void probarFechaNula(GestorPagos gestor) {
        try {
            gestor.registrarPago(new Pago(3, null, 1000.0));
        } catch (CrediYaException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Lee el valor del reporte desde los argumentos
    private static double leerValor(String[] args) {
        if (args.length > 0) {
            try {
                return Double.parseDouble(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido, se usa 200000");
            }
        }
        return 200000;
    }
}
