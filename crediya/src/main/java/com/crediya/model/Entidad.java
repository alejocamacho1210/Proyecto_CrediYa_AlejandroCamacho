package com.crediya.model;

// Clase base: todo lo que se guarda tiene un id
public abstract class Entidad {

    // Vale 0 mientras no se ha guardado
    private int id;

    // Getter y setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
