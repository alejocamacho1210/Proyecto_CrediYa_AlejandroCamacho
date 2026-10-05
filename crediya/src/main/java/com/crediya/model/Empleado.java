package com.crediya.model;

import java.math.BigDecimal;

// Trabajador de CrediYa (hereda de Persona)
public class Empleado extends Persona {

    // Atributos propios
    private final String rol;
    private final BigDecimal salario;

    // Constructor
    public Empleado(String nombre, String documento, String rol, String correo, BigDecimal salario) {
        super(nombre, documento, correo);
        this.rol = rol;
        this.salario = salario;
    }

    // Getters
    public String getRol() {
        return rol;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    // Polimorfismo: tipo de persona
    @Override
    public String getTipo() {
        return "Empleado";
    }
}
