package com.crediya;

import com.crediya.app.Aplicacion;
import com.crediya.ui.EntradaFinalizadaException;

public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        try {
            Aplicacion.iniciar();
        } catch (EntradaFinalizadaException e) {
            System.out.println();
            System.out.println("Entrada finalizada. Hasta pronto.");
        }
    }
}
