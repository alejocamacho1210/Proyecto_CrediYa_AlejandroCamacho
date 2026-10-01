package com.crediya.ui;

import com.crediya.exception.CrediYaException;

import java.util.List;

public abstract class Menu {

    protected final Consola consola;

    protected Menu(Consola consola) {
        this.consola = consola;
    }

    protected abstract String titulo();

    protected abstract List<String> opciones();

    protected abstract void procesar(int opcion);

    protected String etiquetaSalida() {
        return "Volver";
    }

    public void ejecutar() {
        int opcion;
        do {
            imprimir();
            opcion = consola.entero("Seleccione una opcion");
            if (opcion != 0) {
                try {
                    procesar(opcion);
                } catch (CrediYaException e) {
                    consola.mostrar("Error: " + e.getMessage());
                }
            }
        } while (opcion != 0);
    }

    private void imprimir() {
        consola.mostrar("");
        consola.mostrar("=== " + titulo() + " ===");
        List<String> lista = opciones();
        for (int i = 0; i < lista.size(); i++) {
            consola.mostrar((i + 1) + ". " + lista.get(i));
        }
        consola.mostrar("0. " + etiquetaSalida());
    }

    protected void opcionInvalida() {
        consola.mostrar("Opcion invalida.");
    }
}
