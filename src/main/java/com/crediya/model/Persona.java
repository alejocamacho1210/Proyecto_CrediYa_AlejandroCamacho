package com.crediya.model;

public abstract class Persona extends Entidad {

    private final String nombre;
    private final String documento;
    private final String correo;

    protected Persona(String nombre, String documento, String correo) {
        this.nombre = nombre;
        this.documento = documento;
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDocumento() {
        return documento;
    }

    public String getCorreo() {
        return correo;
    }

    public abstract String getTipo();

    @Override
    public String toString() {
        return String.format("[%s #%d] %s | Doc: %s | %s", getTipo(), getId(), nombre, documento, correo);
    }
}
