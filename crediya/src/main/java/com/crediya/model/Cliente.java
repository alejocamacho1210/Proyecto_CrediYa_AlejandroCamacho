package com.crediya.model;

// Persona a la que se le otorga un préstamo (hereda de Persona)
public class Cliente extends Persona {

    // Atributo propio
    private final String telefono;

    // Constructor
    public Cliente(String nombre, String documento, String correo, String telefono) {
        super(nombre, documento, correo);
        this.telefono = telefono;
    }

    // Getter
    public String getTelefono() {
        return telefono;
    }

    // Polimorfismo: tipo de persona
    @Override
    public String getTipo() {
        return "Cliente";
    }
}
