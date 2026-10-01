package com.crediya.ui;

import com.crediya.service.ReporteService;

import java.util.List;

public class MenuReportes extends Menu {

    private final ReporteService servicio;
    private final Presentador presentador;

    public MenuReportes(Consola consola, ReporteService servicio, Presentador presentador) {
        super(consola);
        this.servicio = servicio;
        this.presentador = presentador;
    }

    @Override
    protected String titulo() {
        return "MODULO DE REPORTES";
    }

    @Override
    protected List<String> opciones() {
        return List.of("Prestamos activos", "Prestamos pagados", "Prestamos vencidos", "Prestamos en mora",
                "Clientes morosos", "Saldo pendiente por cliente", "Colocacion por empleado",
                "Prestamos con saldo mayor a un valor", "Totales de cartera");
    }

    @Override
    protected void procesar(int opcion) {
        switch (opcion) {
            case 1 -> presentador.prestamos(servicio.prestamosActivos());
            case 2 -> presentador.prestamos(servicio.prestamosPagados());
            case 3 -> presentador.prestamos(servicio.prestamosVencidos());
            case 4 -> presentador.prestamos(servicio.prestamosEnMora());
            case 5 -> presentador.clientes(servicio.clientesMorosos());
            case 6 -> presentador.resumenesCliente(servicio.saldoPorCliente());
            case 7 -> presentador.resumenesEmpleado(servicio.colocacionPorEmpleado());
            case 8 -> presentador.prestamos(servicio.prestamosConSaldoMayorA(consola.decimal("Saldo minimo")));
            case 9 -> presentador.totales(servicio.totales());
            default -> opcionInvalida();
        }
    }
}
