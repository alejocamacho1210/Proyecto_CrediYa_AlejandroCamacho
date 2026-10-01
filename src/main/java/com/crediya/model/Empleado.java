package com.crediya.model;

import java.math.BigDecimal;

public class Empleado extends Persona {

    private final String rol;
    private final BigDecimal salario;

    public Empleado(String nombre, String documento, String rol, String correo, BigDecimal salario) {
        super(nombre, documento, correo);
        this.rol = rol;
        this.salario = salario;
    }

    public String getRol() {
        return rol;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    @Override
    public String getTipo() {
        return "Empleado";
    }
}
