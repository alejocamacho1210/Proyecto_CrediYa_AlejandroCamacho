package com.crediya.repository.mysql;

import com.crediya.config.ConexionBD;
import com.crediya.exception.EntidadNoEncontradaException;
import com.crediya.exception.PersistenciaException;
import com.crediya.model.Entidad;
import com.crediya.repository.Repositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// JDBC genérico para las cuatro tablas (Template Method)
public abstract class RepositorioMySQL<T extends Entidad> implements Repositorio<T> {

    // Atributos
    private final ConexionBD conexion;
    private final String nombreEntidad;

    // Constructor
    protected RepositorioMySQL(ConexionBD conexion, String nombreEntidad) {
        this.conexion = conexion;
        this.nombreEntidad = nombreEntidad;
    }

    // Piezas que define cada tabla: SQL, parámetros y conversión de filas
    protected abstract String tabla();

    protected abstract String sqlInsertar();

    protected abstract String sqlActualizar();

    protected abstract int asignarParametros(PreparedStatement sentencia, T entidad) throws SQLException;

    protected abstract T mapear(ResultSet resultado) throws SQLException;

    // INSERT y recuperación del id generado
    @Override
    public T guardar(T entidad) {
        try (Connection c = conexion.obtener();
             PreparedStatement sentencia = c.prepareStatement(sqlInsertar(), Statement.RETURN_GENERATED_KEYS)) {
            asignarParametros(sentencia, entidad);
            sentencia.executeUpdate();
            try (ResultSet claves = sentencia.getGeneratedKeys()) {
                if (claves.next()) {
                    entidad.setId(claves.getInt(1));
                }
            }
            return entidad;
        } catch (SQLException e) {
            throw new PersistenciaException("Error al guardar " + nombreEntidad + ": " + e.getMessage(), e);
        }
    }

    // UPDATE por id
    @Override
    public void actualizar(T entidad) {
        try (Connection c = conexion.obtener();
             PreparedStatement sentencia = c.prepareStatement(sqlActualizar())) {
            int ultimo = asignarParametros(sentencia, entidad);
            sentencia.setInt(ultimo + 1, entidad.getId());
            if (sentencia.executeUpdate() == 0) {
                throw new EntidadNoEncontradaException(nombreEntidad, entidad.getId());
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al actualizar " + nombreEntidad + ": " + e.getMessage(), e);
        }
    }

    // SELECT por id
    @Override
    public Optional<T> buscarPorId(int id) {
        String sql = "SELECT * FROM " + tabla() + " WHERE id = ?";
        try (Connection c = conexion.obtener();
             PreparedStatement sentencia = c.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            try (ResultSet resultado = sentencia.executeQuery()) {
                return resultado.next() ? Optional.of(mapear(resultado)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al consultar " + nombreEntidad + ": " + e.getMessage(), e);
        }
    }

    // SELECT de todos los registros
    @Override
    public List<T> listar() {
        String sql = "SELECT * FROM " + tabla() + " ORDER BY id";
        List<T> entidades = new ArrayList<>();
        try (Connection c = conexion.obtener();
             PreparedStatement sentencia = c.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {
            while (resultado.next()) {
                entidades.add(mapear(resultado));
            }
            return entidades;
        } catch (SQLException e) {
            throw new PersistenciaException("Error al listar " + nombreEntidad + ": " + e.getMessage(), e);
        }
    }
}
