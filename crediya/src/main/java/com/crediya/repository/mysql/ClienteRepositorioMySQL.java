package com.crediya.repository.mysql;

import com.crediya.config.ConexionBD;
import com.crediya.model.Cliente;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Acceso a la tabla clientes
public class ClienteRepositorioMySQL extends RepositorioMySQL<Cliente> {

    public ClienteRepositorioMySQL(ConexionBD conexion) {
        super(conexion, "Cliente");
    }

    // Tabla y sentencias SQL
    @Override
    protected String tabla() {
        return "clientes";
    }

    @Override
    protected String sqlInsertar() {
        return "INSERT INTO clientes (nombre, documento, correo, telefono) VALUES (?, ?, ?, ?)";
    }

    @Override
    protected String sqlActualizar() {
        return "UPDATE clientes SET nombre = ?, documento = ?, correo = ?, telefono = ? WHERE id = ?";
    }

    // Valores de cada ? de la sentencia
    @Override
    protected int asignarParametros(PreparedStatement s, Cliente c) throws SQLException {
        s.setString(1, c.getNombre());
        s.setString(2, c.getDocumento());
        s.setString(3, c.getCorreo());
        s.setString(4, c.getTelefono());
        return 4;
    }

    // Convierte una fila en objeto
    @Override
    protected Cliente mapear(ResultSet r) throws SQLException {
        Cliente cliente = new Cliente(
                r.getString("nombre"),
                r.getString("documento"),
                r.getString("correo"),
                r.getString("telefono"));
        cliente.setId(r.getInt("id"));
        return cliente;
    }
}
