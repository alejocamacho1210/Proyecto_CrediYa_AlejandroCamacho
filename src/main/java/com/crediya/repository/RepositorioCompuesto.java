package com.crediya.repository;

import com.crediya.model.Entidad;
import com.crediya.repository.archivo.RepositorioArchivo;

import java.util.List;
import java.util.Optional;

public class RepositorioCompuesto<T extends Entidad> implements Repositorio<T> {

    private final Repositorio<T> principal;
    private final RepositorioArchivo<T> espejo;

    public RepositorioCompuesto(Repositorio<T> principal, RepositorioArchivo<T> espejo) {
        this.principal = principal;
        this.espejo = espejo;
    }

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

    @Override
    public Optional<T> buscarPorId(int id) {
        return principal.buscarPorId(id);
    }

    @Override
    public List<T> listar() {
        return principal.listar();
    }

    public void sincronizar() {
        espejo.reemplazarTodo(principal.listar());
    }
}
