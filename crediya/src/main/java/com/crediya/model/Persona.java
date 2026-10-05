package com.crediya.model;

// Datos comunes de empleados y clientes (herencia)
public abstract class Persona extends Entidad {

    // Atributos
    private final String nombre;
    private final String documento;
    private final String correo;

    // Constructor: solo lo usan las clases hijas
    protected Persona(String nombre, String documento, String correo) {
        this.nombre = nombre;
        this.documento = documento;
        this.correo = correo;
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public String getDocumento() {
        return documento;
    }

    public String getCorreo() {
        return correo;
    }

    // Polimorfismo: cada hija define su tipo
    public abstract String getTipo();

    // Formato de impresión
    @Override
    public String toString() {
        return String.format("[%s #%d] %s | Doc: %s | %s", getTipo(), getId(), nombre, documento, correo);
    }
}
