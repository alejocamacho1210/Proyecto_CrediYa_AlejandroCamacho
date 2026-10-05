package com.crediya.service;

import com.crediya.exception.EntidadNoEncontradaException;
import com.crediya.exception.ValidacionException;
import com.crediya.model.EstadoPrestamo;
import com.crediya.model.Pago;
import com.crediya.model.Prestamo;
import com.crediya.repository.Repositorio;
import com.crediya.util.Validador;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

// Reglas de negocio de los préstamos
public class PrestamoService {

    // Límites que respetan las columnas de MySQL
    private static final BigDecimal MONTO_MAXIMO = new BigDecimal("9999999999.99");
    private static final int CUOTAS_MAXIMAS = 360;

    // Dependencias recibidas por constructor
    private final Repositorio<Prestamo> prestamos;
    private final Repositorio<Pago> pagos;
    private final ClienteService clienteService;
    private final EmpleadoService empleadoService;

    // Constructor
    public PrestamoService(Repositorio<Prestamo> prestamos, Repositorio<Pago> pagos,
                           ClienteService clienteService, EmpleadoService empleadoService) {
        this.prestamos = prestamos;
        this.pagos = pagos;
        this.clienteService = clienteService;
        this.empleadoService = empleadoService;
    }

    // Crea un préstamo validando cliente, empleado y datos
    public Prestamo crear(int clienteId, int empleadoId, BigDecimal monto, BigDecimal interes,
                          int cuotas, LocalDate fechaInicio) {
        // Cliente y empleado deben existir
        clienteService.buscarPorId(clienteId);
        empleadoService.buscarPorId(empleadoId);
        // Validación de monto, interés, cuotas y fecha
        Validador.positivo(monto, "monto", MONTO_MAXIMO);
        Validador.porcentaje(interes, "interes");
        Validador.enteroEnRango(cuotas, "cuotas", 1, CUOTAS_MAXIMAS);
        Validador.fecha(fechaInicio, "fecha de inicio");
        return prestamos.guardar(new Prestamo(clienteId, empleadoId, monto, interes, cuotas, fechaInicio));
    }

    // Lista los préstamos cargando los pagos de cada uno
    public List<Prestamo> listar() {
        List<Prestamo> todos = prestamos.listar();
        Map<Integer, List<Pago>> pagosPorPrestamo = pagos.listar().stream()
                .collect(Collectors.groupingBy(Pago::getPrestamoId));
        todos.forEach(p -> p.setPagos(pagosPorPrestamo.getOrDefault(p.getId(), List.of())));
        return todos;
    }

    // Filtra con una condición lambda (base de los reportes)
    public List<Prestamo> filtrar(Predicate<Prestamo> criterio) {
        return listar().stream().filter(criterio).collect(Collectors.toList());
    }

    // Busca un préstamo con sus pagos
    public Prestamo buscarPorId(int id) {
        Prestamo prestamo = prestamos.buscarPorId(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Prestamo", id));
        prestamo.setPagos(pagos.listar().stream()
                .filter(pago -> pago.getPrestamoId() == id)
                .collect(Collectors.toList()));
        return prestamo;
    }

    // Préstamos asociados a un cliente
    public List<Prestamo> listarPorCliente(int clienteId) {
        clienteService.buscarPorId(clienteId);
        return filtrar(p -> p.getClienteId() == clienteId);
    }

    // Cambio manual de estado con sus reglas
    public Prestamo cambiarEstado(int id, EstadoPrestamo nuevoEstado) {
        Prestamo prestamo = buscarPorId(id);
        if (prestamo.getEstado() == nuevoEstado) {
            throw new ValidacionException("El prestamo ya esta en estado " + nuevoEstado.getEtiqueta());
        }
        boolean saldoCubierto = prestamo.getSaldoPendiente().signum() == 0;
        if (nuevoEstado == EstadoPrestamo.PAGADO && !saldoCubierto) {
            throw new ValidacionException("No se puede marcar como pagado: aun hay saldo pendiente");
        }
        if (nuevoEstado == EstadoPrestamo.PENDIENTE && saldoCubierto) {
            throw new ValidacionException("No se puede marcar como pendiente: el saldo ya esta cubierto");
        }
        actualizarEstado(prestamo, nuevoEstado);
        return prestamo;
    }

    // Guarda el nuevo estado
    public void actualizarEstado(Prestamo prestamo, EstadoPrestamo estado) {
        prestamo.setEstado(estado);
        prestamos.actualizar(prestamo);
    }
}
