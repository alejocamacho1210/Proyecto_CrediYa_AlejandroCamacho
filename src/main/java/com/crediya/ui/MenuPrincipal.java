package com.crediya.ui;

import java.util.List;

public class MenuPrincipal extends Menu {

    private final MenuEmpleados empleados;
    private final MenuClientes clientes;
    private final MenuPrestamos prestamos;
    private final MenuPagos pagos;
    private final MenuReportes reportes;

    public MenuPrincipal(Consola consola, MenuEmpleados empleados, MenuClientes clientes,
                         MenuPrestamos prestamos, MenuPagos pagos, MenuReportes reportes) {
        super(consola);
        this.empleados = empleados;
        this.clientes = clientes;
        this.prestamos = prestamos;
        this.pagos = pagos;
        this.reportes = reportes;
    }

    @Override
    protected String titulo() {
        return "CREDIYA S.A.S. - SISTEMA DE COBROS DE CARTERA";
    }

    @Override
    protected List<String> opciones() {
        return List.of("Empleados", "Clientes", "Prestamos", "Pagos", "Reportes");
    }

    @Override
    protected String etiquetaSalida() {
        return "Salir";
    }

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
