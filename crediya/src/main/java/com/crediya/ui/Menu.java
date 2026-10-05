package com.crediya.ui;

import com.crediya.exception.CrediYaException;

import java.util.List;

// Ciclo común de los menús (Template Method)
public abstract class Menu {

    // Consola compartida con los menús hijos
    protected final Consola consola;

    // Constructor
    protected Menu(Consola consola) {
        this.consola = consola;
    }

    // Lo que define cada menú
    protected abstract String titulo();

    protected abstract List<String> opciones();

    protected abstract void procesar(int opcion);

    // Texto de la opción 0
    protected String etiquetaSalida() {
        return "Volver";
    }

    // Ciclo del menú: atrapa los errores del negocio y sigue funcionando
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

    // Muestra el título y las opciones numeradas
    private void imprimir() {
        consola.mostrar("");
        consola.mostrar("=== " + titulo() + " ===");
        List<String> lista = opciones();
        for (int i = 0; i < lista.size(); i++) {
            consola.mostrar((i + 1) + ". " + lista.get(i));
        }
        consola.mostrar("0. " + etiquetaSalida());
    }

    // Aviso para opciones fuera de rango
    protected void opcionInvalida() {
        consola.mostrar("Opcion invalida.");
    }
}
