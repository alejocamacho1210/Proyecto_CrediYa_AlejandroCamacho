package com.crediya.service;

import com.crediya.exception.EntidadNoEncontradaException;
import com.crediya.exception.ValidacionException;
import com.crediya.model.Cliente;
import com.crediya.repository.Repositorio;
import com.crediya.util.Validador;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

// Reglas de negocio de los clientes
public class ClienteService {

    // Repositorio recibido por constructor (inyección de dependencias)
    private final Repositorio<Cliente> repositorio;

    public ClienteService(Repositorio<Cliente> repositorio) {
        this.repositorio = repositorio;
    }

    // Valida los datos, evita documentos repetidos y guarda
    public Cliente registrar(String nombre, String documento, String correo, String telefono) {
        // Validación de cada campo
        String nombreValido = Validador.texto(nombre, "nombre", 80);
        String documentoValido = Validador.texto(documento, "documento", 30);
        String correoValido = Validador.correo(correo, 80);
        String telefonoValido = Validador.telefono(telefono);

        // El documento no puede repetirse
        boolean duplicado = repositorio.listar().stream()
                .anyMatch(c -> c.getDocumento().equalsIgnoreCase(documentoValido));
        if (duplicado) {
            throw new ValidacionException("Ya existe un cliente con el documento " + documentoValido);
        }
        return repositorio.guardar(new Cliente(nombreValido, documentoValido, correoValido, telefonoValido));
    }

    // Consultas
    public List<Cliente> listar() {
        return repositorio.listar();
    }

    public Cliente buscarPorId(int id) {
        return repositorio.buscarPorId(id).orElseThrow(() -> new EntidadNoEncontradaException("Cliente", id));
    }

    // Busca por texto dentro del nombre
    public List<Cliente> buscarPorNombre(String texto) {
        String criterio = texto == null ? "" : texto.trim().toLowerCase(Locale.ROOT);
        return repositorio.listar().stream()
                .filter(c -> c.getNombre().toLowerCase(Locale.ROOT).contains(criterio))
                .collect(Collectors.toList());
    }
}
