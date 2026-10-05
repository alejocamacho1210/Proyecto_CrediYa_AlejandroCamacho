package com.crediya.repository.mysql;

import com.crediya.config.ConexionBD;
import com.crediya.model.Empleado;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Acceso a la tabla empleados
public class EmpleadoRepositorioMySQL extends RepositorioMySQL<Empleado> {

    public EmpleadoRepositorioMySQL(ConexionBD conexion) {
        super(conexion, "Empleado");
    }

    // Tabla y sentencias SQL
    @Override
    protected String tabla() {
        return "empleados";
    }

    @Override
    protected String sqlInsertar() {
        return "INSERT INTO empleados (nombre, documento, rol, correo, salario) VALUES (?, ?, ?, ?, ?)";
    }

    @Override
    protected String sqlActualizar() {
        return "UPDATE empleados SET nombre = ?, documento = ?, rol = ?, correo = ?, salario = ? WHERE id = ?";
    }

    // Valores de cada ? de la sentencia
    @Override
    protected int asignarParametros(PreparedStatement s, Empleado e) throws SQLException {
        s.setString(1, e.getNombre());
        s.setString(2, e.getDocumento());
        s.setString(3, e.getRol());
        s.setString(4, e.getCorreo());
        s.setBigDecimal(5, e.getSalario());
        return 5;
    }

    // Convierte una fila en objeto
    @Override
    protected Empleado mapear(ResultSet r) throws SQLException {
        Empleado empleado = new Empleado(
                r.getString("nombre"),
                r.getString("documento"),
                r.getString("rol"),
                r.getString("correo"),
                r.getBigDecimal("salario"));
        empleado.setId(r.getInt("id"));
        return empleado;
    }
}
