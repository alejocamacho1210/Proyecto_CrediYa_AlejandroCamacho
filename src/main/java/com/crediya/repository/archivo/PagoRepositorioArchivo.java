package com.crediya.repository.archivo;

import com.crediya.model.Pago;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;

public class PagoRepositorioArchivo extends RepositorioArchivo<Pago> {

    public PagoRepositorioArchivo(Path ruta) {
        super(ruta, "Pago");
    }

    @Override
    protected String serializar(Pago p) {
        return String.join(SEPARADOR,
                String.valueOf(p.getId()),
                String.valueOf(p.getPrestamoId()),
                p.getFecha().toString(),
                p.getMonto().toPlainString());
    }

    @Override
    protected Pago deserializar(String[] c) {
        Pago pago = new Pago(Integer.parseInt(c[1]), LocalDate.parse(c[2]), new BigDecimal(c[3]));
        pago.setId(Integer.parseInt(c[0]));
        return pago;
    }
}
