package com.crediya.service;

import com.crediya.model.Cliente;
import com.crediya.model.Empleado;
import com.crediya.model.EstadoPrestamo;
import com.crediya.model.Pago;
import com.crediya.model.Prestamo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ReporteService {

    public record ResumenCliente(Cliente cliente, long prestamosActivos, BigDecimal saldoPendiente) {
    }

    public record ResumenEmpleado(Empleado empleado, long prestamos, BigDecimal montoColocado) {
    }

    public record TotalesCartera(int totalPrestamos, long activos, long pagados, long vencidos, long enMora,
                                 BigDecimal carteraPendiente, BigDecimal totalRecaudado) {
    }

    private final PrestamoService prestamoService;
    private final ClienteService clienteService;
    private final EmpleadoService empleadoService;
    private final PagoService pagoService;

    public ReporteService(PrestamoService prestamoService, ClienteService clienteService,
                          EmpleadoService empleadoService, PagoService pagoService) {
        this.prestamoService = prestamoService;
        this.clienteService = clienteService;
        this.empleadoService = empleadoService;
        this.pagoService = pagoService;
    }

    public List<Prestamo> prestamosActivos() {
        return prestamoService.filtrar(p -> p.getEstado() == EstadoPrestamo.PENDIENTE);
    }

    public List<Prestamo> prestamosPagados() {
        return prestamoService.filtrar(p -> p.getEstado() == EstadoPrestamo.PAGADO);
    }

    public List<Prestamo> prestamosVencidos() {
        LocalDate hoy = LocalDate.now();
        return prestamoService.filtrar(p -> p.isVencido(hoy));
    }

    public List<Prestamo> prestamosEnMora() {
        LocalDate hoy = LocalDate.now();
        return prestamoService.filtrar(p -> p.isEnMora(hoy));
    }

    public List<Prestamo> prestamosConSaldoMayorA(BigDecimal minimo) {
        Predicate<Prestamo> criterio = p -> p.getEstado() == EstadoPrestamo.PENDIENTE
                && p.getSaldoPendiente().compareTo(minimo) > 0;
        return prestamoService.filtrar(criterio);
    }

    public List<Cliente> clientesMorosos() {
        Set<Integer> idsMorosos = prestamosEnMora().stream()
                .map(Prestamo::getClienteId)
                .collect(Collectors.toSet());
        return clienteService.listar().stream()
                .filter(c -> idsMorosos.contains(c.getId()))
                .collect(Collectors.toList());
    }

    public List<ResumenCliente> saldoPorCliente() {
        Map<Integer, List<Prestamo>> activosPorCliente = prestamosActivos().stream()
                .collect(Collectors.groupingBy(Prestamo::getClienteId));
        return clienteService.listar().stream()
                .filter(c -> activosPorCliente.containsKey(c.getId()))
                .map(c -> {
                    List<Prestamo> activos = activosPorCliente.get(c.getId());
                    BigDecimal saldo = activos.stream()
                            .map(Prestamo::getSaldoPendiente)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    return new ResumenCliente(c, activos.size(), saldo);
                })
                .sorted(Comparator.comparing(ResumenCliente::saldoPendiente).reversed())
                .collect(Collectors.toList());
    }

    public List<ResumenEmpleado> colocacionPorEmpleado() {
        Map<Integer, List<Prestamo>> porEmpleado = prestamoService.listar().stream()
                .collect(Collectors.groupingBy(Prestamo::getEmpleadoId));
        return empleadoService.listar().stream()
                .filter(e -> porEmpleado.containsKey(e.getId()))
                .map(e -> {
                    List<Prestamo> otorgados = porEmpleado.get(e.getId());
                    BigDecimal monto = otorgados.stream()
                            .map(Prestamo::getMonto)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    return new ResumenEmpleado(e, otorgados.size(), monto);
                })
                .sorted(Comparator.comparing(ResumenEmpleado::montoColocado).reversed())
                .collect(Collectors.toList());
    }

    public TotalesCartera totales() {
        LocalDate hoy = LocalDate.now();
        List<Prestamo> todos = prestamoService.listar();
        BigDecimal cartera = todos.stream()
                .filter(p -> p.getEstado() == EstadoPrestamo.PENDIENTE)
                .map(Prestamo::getSaldoPendiente)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal recaudado = pagoService.historialGeneral().stream()
                .map(Pago::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new TotalesCartera(
                todos.size(),
                todos.stream().filter(p -> p.getEstado() == EstadoPrestamo.PENDIENTE).count(),
                todos.stream().filter(p -> p.getEstado() == EstadoPrestamo.PAGADO).count(),
                todos.stream().filter(p -> p.isVencido(hoy)).count(),
                todos.stream().filter(p -> p.isEnMora(hoy)).count(),
                cartera,
                recaudado);
    }
}
