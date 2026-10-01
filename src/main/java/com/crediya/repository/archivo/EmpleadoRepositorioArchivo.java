package com.crediya.repository.archivo;

import com.crediya.model.Empleado;

import java.math.BigDecimal;
import java.nio.file.Path;

public class EmpleadoRepositorioArchivo extends RepositorioArchivo<Empleado> {

    public EmpleadoRepositorioArchivo(Path ruta) {
        super(ruta, "Empleado");
    }

    @Override
    protected String serializar(Empleado e) {
        return String.join(SEPARADOR,
                String.valueOf(e.getId()),
                limpiar(e.getNombre()),
                limpiar(e.getDocumento()),
                limpiar(e.getRol()),
                limpiar(e.getCorreo()),
                e.getSalario().toPlainString());
    }

    @Override
    protected Empleado deserializar(String[] c) {
        Empleado empleado = new Empleado(c[1], c[2], c[3], c[4], new BigDecimal(c[5]));
        empleado.setId(Integer.parseInt(c[0]));
        return empleado;
    }
}
