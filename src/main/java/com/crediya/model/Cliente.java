package com.crediya.model;

public class Cliente extends Persona {

    private final String telefono;

    public Cliente(String nombre, String documento, String correo, String telefono) {
        super(nombre, documento, correo);
        this.telefono = telefono;
    }

    public String getTelefono() {
        return telefono;
    }

    @Override
    public String getTipo() {
        return "Cliente";
    }
}
