package com.crediya.ui;

import com.crediya.model.Pago;
import com.crediya.model.Prestamo;
import com.crediya.service.PagoService;
import com.crediya.service.PrestamoService;
import com.crediya.util.Formato;

import java.time.LocalDate;
import java.util.List;

public class MenuPagos extends Menu {

    private final PagoService servicio;
    private final PrestamoService prestamoService;
    private final Presentador presentador;

    public MenuPagos(Consola consola, PagoService servicio, PrestamoService prestamoService,
                     Presentador presentador) {
        super(consola);
        this.servicio = servicio;
        this.prestamoService = prestamoService;
        this.presentador = presentador;
    }

    @Override
    protected String titulo() {
        return "MODULO DE PAGOS";
    }

    @Override
    protected List<String> opciones() {
        return List.of("Registrar abono a un prestamo", "Historico de pagos de un prestamo",
                "Historico general de pagos", "Consultar saldo de un prestamo");
    }

    @Override
    protected void procesar(int opcion) {
        switch (opcion) {
            case 1 -> registrar();
            case 2 -> presentador.pagos(servicio.historial(consola.entero("ID del prestamo")));
            case 3 -> presentador.pagos(servicio.historialGeneral());
            case 4 -> saldo();
            default -> opcionInvalida();
        }
    }

    private void registrar() {
        int prestamoId = consola.entero("ID del prestamo");
        Prestamo antes = prestamoService.buscarPorId(prestamoId);
        consola.mostrar("Saldo pendiente: " + Formato.dinero(antes.getSaldoPendiente())
                + " | Cuota mensual: " + Formato.dinero(antes.getValorCuota()));
        var monto = consola.decimal("Monto del abono");
        LocalDate fecha = consola.fecha("Fecha de pago", LocalDate.now());

        Pago pago = servicio.registrar(prestamoId, monto, fecha);
        Prestamo despues = prestamoService.buscarPorId(prestamoId);
        consola.mostrar("Abono registrado con ID " + pago.getId());
        consola.mostrar("Nuevo saldo pendiente: " + Formato.dinero(despues.getSaldoPendiente())
                + " | Estado: " + despues.getEstado().getEtiqueta());
    }

    private void saldo() {
        Prestamo prestamo = prestamoService.buscarPorId(consola.entero("ID del prestamo"));
        consola.mostrar("Monto total:      " + Formato.dinero(prestamo.getMontoTotal()));
        consola.mostrar("Total pagado:     " + Formato.dinero(prestamo.getTotalPagado()));
        consola.mostrar("Saldo pendiente:  " + Formato.dinero(prestamo.getSaldoPendiente()));
        consola.mostrar("Estado:           " + prestamo.getEstado().getEtiqueta());
    }
}
