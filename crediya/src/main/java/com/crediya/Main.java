package com.crediya;

import com.crediya.app.Aplicacion;
import com.crediya.ui.EntradaFinalizadaException;

// Punto de entrada: arranca la aplicación
public final class Main {

    // Constructor privado: la clase no se instancia
    private Main() {
    }

    // Inicia el sistema y cierra limpio si se acaba la entrada de teclado
    public static void main(String[] args) {
        try {
            Aplicacion.iniciar();
        } catch (EntradaFinalizadaException e) {
            System.out.println();
            System.out.println("Entrada finalizada. Hasta pronto.");
        }
    }
}
