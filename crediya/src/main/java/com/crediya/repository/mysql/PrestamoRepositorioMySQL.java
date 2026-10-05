package com.crediya.repository.mysql;

import com.crediya.config.ConexionBD;
import com.crediya.model.EstadoPrestamo;
import com.crediya.model.Prestamo;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Acceso a la tabla prestamos
public class PrestamoRepositorioMySQL extends RepositorioMySQL<Prestamo> {

    public PrestamoRepositorioMySQL(ConexionBD conexion) {
        super(conexion, "Prestamo");
    }

    // Tabla y sentencias SQL
    @Override
    protected String tabla() {
        return "prestamos";
    }

    @Override
    protected String sqlInsertar() {
        return "INSERT INTO prestamos (cliente_id, empleado_id, monto, interes, cuotas, fecha_inicio, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String sqlActualizar() {
        return "UPDATE prestamos SET cliente_id = ?, empleado_id = ?, monto = ?, interes = ?, cuotas = ?, "
                + "fecha_inicio = ?, estado = ? WHERE id = ?";
    }

    // Valores de cada ? (la fecha se pasa como java.sql.Date)
    @Override
    protected int asignarParametros(PreparedStatement s, Prestamo p) throws SQLException {
        s.setInt(1, p.getClienteId());
        s.setInt(2, p.getEmpleadoId());
        s.setBigDecimal(3, p.getMonto());
        s.setBigDecimal(4, p.getInteres());
        s.setInt(5, p.getCuotas());
        s.setDate(6, Date.valueOf(p.getFechaInicio()));
        s.setString(7, p.getEstado().getEtiqueta());
        return 7;
    }

    // Convierte una fila en objeto
    @Override
    protected Prestamo mapear(ResultSet r) throws SQLException {
        Prestamo prestamo = new Prestamo(
                r.getInt("cliente_id"),
                r.getInt("empleado_id"),
                r.getBigDecimal("monto"),
                r.getBigDecimal("interes"),
                r.getInt("cuotas"),
                r.getDate("fecha_inicio").toLocalDate());
        prestamo.setEstado(EstadoPrestamo.desdeTexto(r.getString("estado")));
        prestamo.setId(r.getInt("id"));
        return prestamo;
    }
}
