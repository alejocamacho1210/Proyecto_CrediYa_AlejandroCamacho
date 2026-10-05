package com.crediya.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

// Préstamo: guarda los datos y calcula total, cuota, saldo y mora
public class Prestamo extends Entidad {

    // Constante para pasar porcentajes a fracción
    private static final BigDecimal CIEN = BigDecimal.valueOf(100);

    // Atributos
    private final int clienteId;
    private final int empleadoId;
    private final BigDecimal monto;
    private final BigDecimal interes;
    private final int cuotas;
    private final LocalDate fechaInicio;
    private EstadoPrestamo estado;
    // Pagos del préstamo (no se guarda en MySQL, se carga aparte)
    private List<Pago> pagos = new ArrayList<>();

    // Constructor: nace en estado pendiente
    public Prestamo(int clienteId, int empleadoId, BigDecimal monto, BigDecimal interes, int cuotas, LocalDate fechaInicio) {
        this.clienteId = clienteId;
        this.empleadoId = empleadoId;
        this.monto = monto.setScale(2, RoundingMode.HALF_UP);
        this.interes = interes.setScale(2, RoundingMode.HALF_UP);
        this.cuotas = cuotas;
        this.fechaInicio = fechaInicio;
        this.estado = EstadoPrestamo.PENDIENTE;
    }

    // Getters
    public int getClienteId() {
        return clienteId;
    }

    public int getEmpleadoId() {
        return empleadoId;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public BigDecimal getInteres() {
        return interes;
    }

    public int getCuotas() {
        return cuotas;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    // Estado y pagos
    public EstadoPrestamo getEstado() {
        return estado;
    }

    public void setEstado(EstadoPrestamo estado) {
        this.estado = estado;
    }

    public List<Pago> getPagos() {
        return List.copyOf(pagos);
    }

    public void setPagos(List<Pago> pagos) {
        this.pagos = new ArrayList<>(pagos);
    }

    // Cálculos financieros: total con interés, cuota, pagado y saldo
    public BigDecimal getMontoTotal() {
        BigDecimal intereses = monto.multiply(interes)
                .multiply(BigDecimal.valueOf(cuotas))
                .divide(CIEN, 2, RoundingMode.HALF_UP);
        return monto.add(intereses);
    }

    public BigDecimal getValorCuota() {
        return getMontoTotal().divide(BigDecimal.valueOf(cuotas), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTotalPagado() {
        return pagos.stream()
                .map(Pago::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getSaldoPendiente() {
        return getMontoTotal().subtract(getTotalPagado()).max(BigDecimal.ZERO);
    }

    // Vencimiento y mora
    public LocalDate getFechaVencimiento() {
        return fechaInicio.plusMonths(cuotas);
    }

    // Cuotas que ya debieron pagarse a la fecha
    public int cuotasExigibles(LocalDate hoy) {
        long transcurridos = ChronoUnit.MONTHS.between(fechaInicio, hoy);
        return (int) Math.max(0, Math.min(cuotas, transcurridos));
    }

    // Monto que debería llevar pagado a la fecha
    public BigDecimal montoEsperado(LocalDate hoy) {
        return getValorCuota()
                .multiply(BigDecimal.valueOf(cuotasExigibles(hoy)))
                .min(getMontoTotal());
    }

    // Vencido: pendiente y ya pasó la fecha final
    public boolean isVencido(LocalDate hoy) {
        return estado == EstadoPrestamo.PENDIENTE && hoy.isAfter(getFechaVencimiento());
    }

    // En mora: pendiente y pagó menos de lo exigible
    public boolean isEnMora(LocalDate hoy) {
        return estado == EstadoPrestamo.PENDIENTE && getTotalPagado().compareTo(montoEsperado(hoy)) < 0;
    }
}
