package com.crediya.repository;

import com.crediya.model.Entidad;

import java.util.List;
import java.util.Optional;

// Contrato de persistencia: lo implementan archivo, MySQL y el compuesto
public interface Repositorio<T extends Entidad> {

    // Inserta y devuelve la entidad con su id
    T guardar(T entidad);

    // Modifica una entidad existente
    void actualizar(T entidad);

    // Busca por id (vacío si no existe)
    Optional<T> buscarPorId(int id);

    // Devuelve todas las entidades
    List<T> listar();
}
