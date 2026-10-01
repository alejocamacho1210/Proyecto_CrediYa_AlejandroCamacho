package com.crediya.service;

import com.crediya.exception.EntidadNoEncontradaException;
import com.crediya.exception.ValidacionException;
import com.crediya.model.Empleado;
import com.crediya.repository.Repositorio;
import com.crediya.util.Validador;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class EmpleadoService {

    private static final BigDecimal SALARIO_MAXIMO = new BigDecimal("99999999.99");

    private final Repositorio<Empleado> repositorio;

    public EmpleadoService(Repositorio<Empleado> repositorio) {
        this.repositorio = repositorio;
    }

    public Empleado registrar(String nombre, String documento, String rol, String correo, BigDecimal salario) {
        String nombreValido = Validador.texto(nombre, "nombre", 80);
        String documentoValido = Validador.texto(documento, "documento", 30);
        String rolValido = Validador.texto(rol, "rol", 30);
        String correoValido = Validador.correo(correo, 80);
        BigDecimal salarioValido = Validador.positivo(salario, "salario", SALARIO_MAXIMO);

        boolean duplicado = repositorio.listar().stream()
                .anyMatch(e -> e.getDocumento().equalsIgnoreCase(documentoValido));
        if (duplicado) {
            throw new ValidacionException("Ya existe un empleado con el documento " + documentoValido);
        }
        return repositorio.guardar(new Empleado(nombreValido, documentoValido, rolValido, correoValido,
                salarioValido.setScale(2, java.math.RoundingMode.HALF_UP)));
    }

    public List<Empleado> listar() {
        return repositorio.listar();
    }

    public Empleado buscarPorId(int id) {
        return repositorio.buscarPorId(id).orElseThrow(() -> new EntidadNoEncontradaException("Empleado", id));
    }

    public List<Empleado> buscarPorNombre(String texto) {
        String criterio = texto == null ? "" : texto.trim().toLowerCase(Locale.ROOT);
        return repositorio.listar().stream()
                .filter(e -> e.getNombre().toLowerCase(Locale.ROOT).contains(criterio))
                .collect(Collectors.toList());
    }
}
