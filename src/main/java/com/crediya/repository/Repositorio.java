package com.crediya.repository;

import com.crediya.model.Entidad;

import java.util.List;
import java.util.Optional;

public interface Repositorio<T extends Entidad> {

    T guardar(T entidad);

    void actualizar(T entidad);

    Optional<T> buscarPorId(int id);

    List<T> listar();
}
