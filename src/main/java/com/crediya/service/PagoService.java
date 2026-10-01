package com.crediya.service;

import com.crediya.exception.ValidacionException;
import com.crediya.model.EstadoPrestamo;
import com.crediya.model.Pago;
import com.crediya.model.Prestamo;
import com.crediya.repository.Repositorio;
import com.crediya.util.Formato;
import com.crediya.util.Validador;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PagoService {

    private static final BigDecimal MONTO_MAXIMO = new BigDecimal("9999999999.99");

    private final Repositorio<Pago> pagos;
    private final PrestamoService prestamoService;

    public PagoService(Repositorio<Pago> pagos, PrestamoService prestamoService) {
        this.pagos = pagos;
        this.prestamoService = prestamoService;
    }

    public Pago registrar(int prestamoId, BigDecimal monto, LocalDate fecha) {
        Validador.positivo(monto, "monto del abono", MONTO_MAXIMO);
        Validador.fecha(fecha, "fecha de pago");
        BigDecimal abono = monto.setScale(2, RoundingMode.HALF_UP);
        if (abono.signum() <= 0) {
            throw new ValidacionException("El abono debe ser mayor que cero");
        }

        Prestamo prestamo = prestamoService.buscarPorId(prestamoId);
        if (prestamo.getEstado() == EstadoPrestamo.PAGADO) {
            throw new ValidacionException("El prestamo " + prestamoId + " ya esta pagado");
        }
        if (fecha.isAfter(LocalDate.now())) {
            throw new ValidacionException("La fecha de pago no puede ser futura");
        }
        if (fecha.isBefore(prestamo.getFechaInicio())) {
            throw new ValidacionException("La fecha de pago no puede ser anterior al inicio del prestamo ("
                    + prestamo.getFechaInicio() + ")");
        }
        if (abono.compareTo(prestamo.getSaldoPendiente()) > 0) {
            throw new ValidacionException("El abono supera el saldo pendiente ("
                    + Formato.dinero(prestamo.getSaldoPendiente()) + ")");
        }

        Pago guardado = pagos.guardar(new Pago(prestamoId, fecha, abono));

        Prestamo actualizado = prestamoService.buscarPorId(prestamoId);
        if (actualizado.getSaldoPendiente().signum() == 0) {
            prestamoService.actualizarEstado(actualizado, EstadoPrestamo.PAGADO);
        }
        return guardado;
    }

    public List<Pago> historial(int prestamoId) {
        prestamoService.buscarPorId(prestamoId);
        return pagos.listar().stream()
                .filter(p -> p.getPrestamoId() == prestamoId)
                .sorted(Comparator.comparing(Pago::getFecha).thenComparingInt(Pago::getId))
                .collect(Collectors.toList());
    }

    public List<Pago> historialGeneral() {
        return pagos.listar().stream()
                .sorted(Comparator.comparing(Pago::getFecha).thenComparingInt(Pago::getId))
                .collect(Collectors.toList());
    }
}
