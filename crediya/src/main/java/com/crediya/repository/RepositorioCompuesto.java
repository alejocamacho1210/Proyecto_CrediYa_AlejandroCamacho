package com.crediya.repository;

import com.crediya.model.Entidad;
import com.crediya.repository.archivo.RepositorioArchivo;

import java.util.List;
import java.util.Optional;

// Guarda en MySQL y mantiene el archivo de texto como espejo
public class RepositorioCompuesto<T extends Entidad> implements Repositorio<T> {

    // principal = MySQL (fuente de verdad), espejo = archivo
    private final Repositorio<T> principal;
    private final RepositorioArchivo<T> espejo;

    // Constructor
    public RepositorioCompuesto(Repositorio<T> principal, RepositorioArchivo<T> espejo) {
        this.principal = principal;
        this.espejo = espejo;
    }

    // Escritura: primero MySQL y luego se sincroniza el archivo
    @Override
    public T guardar(T entidad) {
        T guardada = principal.guardar(entidad);
        sincronizar();
        return guardada;
    }

    @Override
    public void actualizar(T entidad) {
        principal.actualizar(entidad);
        sincronizar();
    }

    // Lectura: siempre desde MySQL
    @Override
    public Optional<T> buscarPorId(int id) {
        return principal.buscarPorId(id);
    }

    @Override
    public List<T> listar() {
        return principal.listar();
    }

    // Reescribe el archivo con lo que hay en MySQL
    public void sincronizar() {
        espejo.reemplazarTodo(principal.listar());
    }
}
