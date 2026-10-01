package com.crediya.repository.mysql;

import com.crediya.config.ConexionBD;
import com.crediya.model.Pago;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PagoRepositorioMySQL extends RepositorioMySQL<Pago> {

    public PagoRepositorioMySQL(ConexionBD conexion) {
        super(conexion, "Pago");
    }

    @Override
    protected String tabla() {
        return "pagos";
    }

    @Override
    protected String sqlInsertar() {
        return "INSERT INTO pagos (prestamo_id, fecha_pago, monto) VALUES (?, ?, ?)";
    }

    @Override
    protected String sqlActualizar() {
        return "UPDATE pagos SET prestamo_id = ?, fecha_pago = ?, monto = ? WHERE id = ?";
    }

    @Override
    protected int asignarParametros(PreparedStatement s, Pago p) throws SQLException {
        s.setInt(1, p.getPrestamoId());
        s.setDate(2, Date.valueOf(p.getFecha()));
        s.setBigDecimal(3, p.getMonto());
        return 3;
    }

    @Override
    protected Pago mapear(ResultSet r) throws SQLException {
        Pago pago = new Pago(
                r.getInt("prestamo_id"),
                r.getDate("fecha_pago").toLocalDate(),
                r.getBigDecimal("monto"));
        pago.setId(r.getInt("id"));
        return pago;
    }
}
