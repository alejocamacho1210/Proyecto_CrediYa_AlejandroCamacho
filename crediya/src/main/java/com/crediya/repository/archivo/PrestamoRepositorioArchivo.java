package com.crediya.repository.archivo;

import com.crediya.model.EstadoPrestamo;
import com.crediya.model.Prestamo;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;

// Archivo prestamos.txt: id|clienteId|empleadoId|monto|interes|cuotas|fechaInicio|estado
public class PrestamoRepositorioArchivo extends RepositorioArchivo<Prestamo> {

    public PrestamoRepositorioArchivo(Path ruta) {
        super(ruta, "Prestamo");
    }

    // Objeto a línea de texto
    @Override
    protected String serializar(Prestamo p) {
        return String.join(SEPARADOR,
                String.valueOf(p.getId()),
                String.valueOf(p.getClienteId()),
                String.valueOf(p.getEmpleadoId()),
                p.getMonto().toPlainString(),
                p.getInteres().toPlainString(),
                String.valueOf(p.getCuotas()),
                p.getFechaInicio().toString(),
                p.getEstado().getEtiqueta());
    }

    // Línea de texto a objeto
    @Override
    protected Prestamo deserializar(String[] c) {
        Prestamo prestamo = new Prestamo(
                Integer.parseInt(c[1]),
                Integer.parseInt(c[2]),
                new BigDecimal(c[3]),
                new BigDecimal(c[4]),
                Integer.parseInt(c[5]),
                LocalDate.parse(c[6]));
        prestamo.setEstado(EstadoPrestamo.desdeTexto(c[7]));
        prestamo.setId(Integer.parseInt(c[0]));
        return prestamo;
    }
}
