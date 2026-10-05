package com.crediya.ui;

import com.crediya.model.Cliente;
import com.crediya.model.Empleado;
import com.crediya.model.Pago;
import com.crediya.model.Prestamo;
import com.crediya.service.ClienteService;
import com.crediya.service.EmpleadoService;
import com.crediya.service.ReporteService;
import com.crediya.util.Formato;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Imprime tablas y detalles; no tiene lógica de negocio
public class Presentador {

    // Atributos
    private final Consola consola;
    private final ClienteService clienteService;
    private final EmpleadoService empleadoService;

    // Constructor
    public Presentador(Consola consola, ClienteService clienteService, EmpleadoService empleadoService) {
        this.consola = consola;
        this.clienteService = clienteService;
        this.empleadoService = empleadoService;
    }

    // Tablas de empleados y clientes
    public void empleados(List<Empleado> lista) {
        if (vacia(lista)) {
            return;
        }
        consola.imprimir("%-4s %-24s %-14s %-16s %-28s %16s%n", "ID", "NOMBRE", "DOCUMENTO", "ROL", "CORREO", "SALARIO");
        lista.forEach(e -> consola.imprimir("%-4d %-24s %-14s %-16s %-28s %16s%n",
                e.getId(), Formato.recortar(e.getNombre(), 24), Formato.recortar(e.getDocumento(), 14),
                Formato.recortar(e.getRol(), 16), Formato.recortar(e.getCorreo(), 28), Formato.dinero(e.getSalario())));
    }

    // Tabla de clientes
    public void clientes(List<Cliente> lista) {
        if (vacia(lista)) {
            return;
        }
        consola.imprimir("%-4s %-24s %-14s %-28s %-16s%n", "ID", "NOMBRE", "DOCUMENTO", "CORREO", "TELEFONO");
        lista.forEach(c -> consola.imprimir("%-4d %-24s %-14s %-28s %-16s%n",
                c.getId(), Formato.recortar(c.getNombre(), 24), Formato.recortar(c.getDocumento(), 14),
                Formato.recortar(c.getCorreo(), 28), Formato.recortar(c.getTelefono(), 16)));
    }

    // Tabla de préstamos con la situación (vencido, en mora, al día)
    public void prestamos(List<Prestamo> lista) {
        if (vacia(lista)) {
            return;
        }
        Map<Integer, String> nombres = clienteService.listar().stream()
                .collect(Collectors.toMap(Cliente::getId, Cliente::getNombre));
        LocalDate hoy = LocalDate.now();
        consola.imprimir("%-4s %-20s %15s %7s %6s %15s %14s %15s %-10s %-10s %-8s%n",
                "ID", "CLIENTE", "MONTO", "INT/MES", "CUOTAS", "TOTAL", "CUOTA", "SALDO", "ESTADO", "VENCE", "SITUAC.");
        lista.forEach(p -> consola.imprimir("%-4d %-20s %15s %7s %6d %15s %14s %15s %-10s %-10s %-8s%n",
                p.getId(), Formato.recortar(nombres.getOrDefault(p.getClienteId(), "?"), 20),
                Formato.dinero(p.getMonto()), Formato.porcentaje(p.getInteres()), p.getCuotas(),
                Formato.dinero(p.getMontoTotal()), Formato.dinero(p.getValorCuota()),
                Formato.dinero(p.getSaldoPendiente()), p.getEstado().getEtiqueta(), p.getFechaVencimiento(),
                situacion(p, hoy)));
    }

    // Detalle completo de un préstamo
    public void detallePrestamo(Prestamo p) {
        String cliente = clienteService.buscarPorId(p.getClienteId()).getNombre();
        String empleado = empleadoService.buscarPorId(p.getEmpleadoId()).getNombre();
        LocalDate hoy = LocalDate.now();
        consola.mostrar("Prestamo #" + p.getId());
        consola.mostrar("  Cliente:            " + cliente + " (id " + p.getClienteId() + ")");
        consola.mostrar("  Empleado:           " + empleado + " (id " + p.getEmpleadoId() + ")");
        consola.mostrar("  Monto prestado:     " + Formato.dinero(p.getMonto()));
        consola.mostrar("  Interes mensual:    " + Formato.porcentaje(p.getInteres()));
        consola.mostrar("  Cuotas:             " + p.getCuotas());
        consola.mostrar("  Monto total:        " + Formato.dinero(p.getMontoTotal()));
        consola.mostrar("  Valor cuota:        " + Formato.dinero(p.getValorCuota()));
        consola.mostrar("  Total pagado:       " + Formato.dinero(p.getTotalPagado()));
        consola.mostrar("  Saldo pendiente:    " + Formato.dinero(p.getSaldoPendiente()));
        consola.mostrar("  Fecha de inicio:    " + p.getFechaInicio());
        consola.mostrar("  Fecha vencimiento:  " + p.getFechaVencimiento());
        consola.mostrar("  Estado:             " + p.getEstado().getEtiqueta());
        consola.mostrar("  Situacion:          " + situacion(p, hoy));
    }

    // Tabla de pagos
    public void pagos(List<Pago> lista) {
        if (vacia(lista)) {
            return;
        }
        consola.imprimir("%-4s %-10s %-12s %16s%n", "ID", "PRESTAMO", "FECHA", "MONTO");
        lista.forEach(p -> consola.imprimir("%-4d %-10d %-12s %16s%n",
                p.getId(), p.getPrestamoId(), p.getFecha(), Formato.dinero(p.getMonto())));
    }

    // Tablas de los reportes
    public void resumenesCliente(List<ReporteService.ResumenCliente> lista) {
        if (vacia(lista)) {
            return;
        }
        consola.imprimir("%-4s %-26s %-14s %10s %18s%n", "ID", "CLIENTE", "DOCUMENTO", "ACTIVOS", "SALDO PENDIENTE");
        lista.forEach(r -> consola.imprimir("%-4d %-26s %-14s %10d %18s%n",
                r.cliente().getId(), Formato.recortar(r.cliente().getNombre(), 26),
                Formato.recortar(r.cliente().getDocumento(), 14), r.prestamosActivos(),
                Formato.dinero(r.saldoPendiente())));
    }

    public void resumenesEmpleado(List<ReporteService.ResumenEmpleado> lista) {
        if (vacia(lista)) {
            return;
        }
        consola.imprimir("%-4s %-26s %-16s %10s %18s%n", "ID", "EMPLEADO", "ROL", "PRESTAMOS", "MONTO COLOCADO");
        lista.forEach(r -> consola.imprimir("%-4d %-26s %-16s %10d %18s%n",
                r.empleado().getId(), Formato.recortar(r.empleado().getNombre(), 26),
                Formato.recortar(r.empleado().getRol(), 16), r.prestamos(), Formato.dinero(r.montoColocado())));
    }

    // Totales de la cartera
    public void totales(ReporteService.TotalesCartera t) {
        consola.mostrar("Total de prestamos:    " + t.totalPrestamos());
        consola.mostrar("Prestamos activos:     " + t.activos());
        consola.mostrar("Prestamos pagados:     " + t.pagados());
        consola.mostrar("Prestamos vencidos:    " + t.vencidos());
        consola.mostrar("Prestamos en mora:     " + t.enMora());
        consola.mostrar("Cartera pendiente:     " + Formato.dinero(t.carteraPendiente()));
        consola.mostrar("Total recaudado:       " + Formato.dinero(t.totalRecaudado()));
    }

    // Auxiliares: situación del préstamo y aviso de lista vacía
    private String situacion(Prestamo p, LocalDate hoy) {
        if (p.isVencido(hoy)) {
            return "VENCIDO";
        }
        if (p.isEnMora(hoy)) {
            return "EN MORA";
        }
        return p.getEstado().getEtiqueta().equals("pagado") ? "-" : "AL DIA";
    }

    private boolean vacia(List<?> lista) {
        if (lista.isEmpty()) {
            consola.mostrar("No hay resultados.");
            return true;
        }
        return false;
    }
}
