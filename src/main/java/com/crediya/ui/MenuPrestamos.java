package com.crediya.ui;

import com.crediya.model.EstadoPrestamo;
import com.crediya.model.Prestamo;
import com.crediya.service.ClienteService;
import com.crediya.service.EmpleadoService;
import com.crediya.service.PrestamoService;
import com.crediya.util.Formato;

import java.time.LocalDate;
import java.util.List;

public class MenuPrestamos extends Menu {

    private final PrestamoService servicio;
    private final ClienteService clienteService;
    private final EmpleadoService empleadoService;
    private final Presentador presentador;

    public MenuPrestamos(Consola consola, PrestamoService servicio, ClienteService clienteService,
                         EmpleadoService empleadoService, Presentador presentador) {
        super(consola);
        this.servicio = servicio;
        this.clienteService = clienteService;
        this.empleadoService = empleadoService;
        this.presentador = presentador;
    }

    @Override
    protected String titulo() {
        return "MODULO DE PRESTAMOS";
    }

    @Override
    protected List<String> opciones() {
        return List.of("Crear prestamo", "Listar prestamos", "Consultar detalle de un prestamo",
                "Cambiar estado de un prestamo");
    }

    @Override
    protected void procesar(int opcion) {
        switch (opcion) {
            case 1 -> crear();
            case 2 -> presentador.prestamos(servicio.listar());
            case 3 -> presentador.detallePrestamo(servicio.buscarPorId(consola.entero("ID del prestamo")));
            case 4 -> cambiarEstado();
            default -> opcionInvalida();
        }
    }

    private void crear() {
        consola.mostrar("Clientes disponibles:");
        presentador.clientes(clienteService.listar());
        int clienteId = consola.entero("ID del cliente");
        consola.mostrar("Empleados disponibles:");
        presentador.empleados(empleadoService.listar());
        int empleadoId = consola.entero("ID del empleado");
        var monto = consola.decimal("Monto a prestar");
        var interes = consola.decimal("Interes mensual (%)");
        int cuotas = consola.entero("Numero de cuotas (meses)");
        LocalDate fecha = consola.fecha("Fecha de inicio", LocalDate.now());

        Prestamo prestamo = servicio.crear(clienteId, empleadoId, monto, interes, cuotas, fecha);
        consola.mostrar("Prestamo creado con ID " + prestamo.getId());
        consola.mostrar("Monto total con interes: " + Formato.dinero(prestamo.getMontoTotal()));
        consola.mostrar("Valor de la cuota mensual: " + Formato.dinero(prestamo.getValorCuota()));
    }

    private void cambiarEstado() {
        int id = consola.entero("ID del prestamo");
        consola.mostrar("1. pendiente");
        consola.mostrar("2. pagado");
        int seleccion = consola.entero("Nuevo estado");
        EstadoPrestamo estado = switch (seleccion) {
            case 1 -> EstadoPrestamo.PENDIENTE;
            case 2 -> EstadoPrestamo.PAGADO;
            default -> null;
        };
        if (estado == null) {
            opcionInvalida();
            return;
        }
        Prestamo prestamo = servicio.cambiarEstado(id, estado);
        consola.mostrar("Estado actualizado: prestamo " + prestamo.getId() + " ahora esta " + estado.getEtiqueta());
    }
}
