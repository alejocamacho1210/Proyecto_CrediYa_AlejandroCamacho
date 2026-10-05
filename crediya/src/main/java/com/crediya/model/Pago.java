package com.crediya.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

// Abono realizado a un préstamo
public class Pago extends Entidad {

    // Atributos
    private final int prestamoId;
    private final LocalDate fecha;
    private final BigDecimal monto;

    // Constructor: el monto queda con 2 decimales
    public Pago(int prestamoId, LocalDate fecha, BigDecimal monto) {
        this.prestamoId = prestamoId;
        this.fecha = fecha;
        this.monto = monto.setScale(2, RoundingMode.HALF_UP);
    }

    // Getters
    public int getPrestamoId() {
        return prestamoId;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public BigDecimal getMonto() {
        return monto;
    }
}
