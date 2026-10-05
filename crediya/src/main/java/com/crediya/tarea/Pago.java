package com.crediya.tarea;

import com.crediya.exception.ValidacionException;

import java.time.LocalDate;

// Pago de la tarea: atributos privados, constructores, getters/setters con validación y toString
public final class Pago {

    // Atributos
    private int id;
    private int prestamoId;
    private LocalDate fechaPago;
    private double monto;

    // Constructores
    public Pago() {
    }

    public Pago(int prestamoId, LocalDate fechaPago, double monto) {
        setPrestamoId(prestamoId);
        setFechaPago(fechaPago);
        setMonto(monto);
    }

    public Pago(int id, int prestamoId, LocalDate fechaPago, double monto) {
        setId(id);
        setPrestamoId(prestamoId);
        setFechaPago(fechaPago);
        setMonto(monto);
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getPrestamoId() {
        return prestamoId;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public double getMonto() {
        return monto;
    }

    // Setters con validación
    public void setId(int id) {
        if (id < 0) {
            throw new ValidacionException("El id del pago no puede ser negativo");
        }
        this.id = id;
    }

    public void setPrestamoId(int prestamoId) {
        if (prestamoId <= 0) {
            throw new ValidacionException("El id del préstamo debe ser mayor que cero");
        }
        this.prestamoId = prestamoId;
    }

    public void setFechaPago(LocalDate fechaPago) {
        if (fechaPago == null) {
            throw new ValidacionException("La fecha de pago no puede ser nula");
        }
        this.fechaPago = fechaPago;
    }

    public void setMonto(double monto) {
        if (Double.isNaN(monto) || Double.isInfinite(monto) || monto <= 0) {
            throw new ValidacionException("El monto debe ser un número mayor que cero");
        }
        this.monto = monto;
    }

    // Texto con el formato pedido en el enunciado
    @Override
    public String toString() {
        return "ID: " + id + " - Préstamo: " + prestamoId + " - Fecha: " + fechaPago + " - Monto: " + monto;
    }
}
