package com.crediya.ui;

import com.crediya.model.Empleado;
import com.crediya.service.EmpleadoService;

import java.util.List;

// Módulo de empleados
public class MenuEmpleados extends Menu {

    // Dependencias
    private final EmpleadoService servicio;
    private final Presentador presentador;

    // Constructor
    public MenuEmpleados(Consola consola, EmpleadoService servicio, Presentador presentador) {
        super(consola);
        this.servicio = servicio;
        this.presentador = presentador;
    }

    // Título y opciones
    @Override
    protected String titulo() {
        return "MODULO DE EMPLEADOS";
    }

    @Override
    protected List<String> opciones() {
        return List.of("Registrar empleado", "Listar empleados", "Consultar empleado por ID",
                "Buscar empleados por nombre");
    }

    // Cada opción llama a su servicio o método
    @Override
    protected void procesar(int opcion) {
        switch (opcion) {
            case 1 -> registrar();
            case 2 -> presentador.empleados(servicio.listar());
            case 3 -> presentador.empleados(List.of(servicio.buscarPorId(consola.entero("ID del empleado"))));
            case 4 -> presentador.empleados(servicio.buscarPorNombre(consola.texto("Nombre a buscar")));
            default -> opcionInvalida();
        }
    }

    // Pide los datos y registra al empleado
    private void registrar() {
        String nombre = consola.texto("Nombre");
        String documento = consola.texto("Documento");
        String rol = consola.texto("Rol");
        String correo = consola.texto("Correo");
        var salario = consola.decimal("Salario");
        Empleado empleado = servicio.registrar(nombre, documento, rol, correo, salario);
        consola.mostrar("Empleado registrado con ID " + empleado.getId());
    }
}
