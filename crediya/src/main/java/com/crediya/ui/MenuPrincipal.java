package com.crediya.ui;

import java.util.List;

// Menú principal: abre cada módulo
public class MenuPrincipal extends Menu {

    // Submenús
    private final MenuEmpleados empleados;
    private final MenuClientes clientes;
    private final MenuPrestamos prestamos;
    private final MenuPagos pagos;
    private final MenuReportes reportes;

    // Constructor
    public MenuPrincipal(Consola consola, MenuEmpleados empleados, MenuClientes clientes,
                         MenuPrestamos prestamos, MenuPagos pagos, MenuReportes reportes) {
        super(consola);
        this.empleados = empleados;
        this.clientes = clientes;
        this.prestamos = prestamos;
        this.pagos = pagos;
        this.reportes = reportes;
    }

    // Título y opciones
    @Override
    protected String titulo() {
        return "CREDIYA S.A.S. - SISTEMA DE COBROS DE CARTERA";
    }

    @Override
    protected List<String> opciones() {
        return List.of("Empleados", "Clientes", "Prestamos", "Pagos", "Reportes");
    }

    // Aquí la opción 0 es Salir
    @Override
    protected String etiquetaSalida() {
        return "Salir";
    }

    // Cada opción abre su submenú
    @Override
    protected void procesar(int opcion) {
        switch (opcion) {
            case 1 -> empleados.ejecutar();
            case 2 -> clientes.ejecutar();
            case 3 -> prestamos.ejecutar();
            case 4 -> pagos.ejecutar();
            case 5 -> reportes.ejecutar();
            default -> opcionInvalida();
        }
    }
}
