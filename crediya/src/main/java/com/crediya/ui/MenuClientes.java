package com.crediya.ui;

import com.crediya.model.Cliente;
import com.crediya.service.ClienteService;
import com.crediya.service.PrestamoService;

import java.util.List;

// Módulo de clientes
public class MenuClientes extends Menu {

    // Dependencias
    private final ClienteService servicio;
    private final PrestamoService prestamoService;
    private final Presentador presentador;

    // Constructor
    public MenuClientes(Consola consola, ClienteService servicio, PrestamoService prestamoService,
                        Presentador presentador) {
        super(consola);
        this.servicio = servicio;
        this.prestamoService = prestamoService;
        this.presentador = presentador;
    }

    // Título y opciones
    @Override
    protected String titulo() {
        return "MODULO DE CLIENTES";
    }

    @Override
    protected List<String> opciones() {
        return List.of("Registrar cliente", "Listar clientes", "Consultar cliente por ID",
                "Buscar clientes por nombre", "Consultar prestamos de un cliente");
    }

    // Cada opción llama a su servicio o método
    @Override
    protected void procesar(int opcion) {
        switch (opcion) {
            case 1 -> registrar();
            case 2 -> presentador.clientes(servicio.listar());
            case 3 -> presentador.clientes(List.of(servicio.buscarPorId(consola.entero("ID del cliente"))));
            case 4 -> presentador.clientes(servicio.buscarPorNombre(consola.texto("Nombre a buscar")));
            case 5 -> presentador.prestamos(prestamoService.listarPorCliente(consola.entero("ID del cliente")));
            default -> opcionInvalida();
        }
    }

    // Pide los datos y registra al cliente
    private void registrar() {
        String nombre = consola.texto("Nombre");
        String documento = consola.texto("Documento");
        String correo = consola.texto("Correo");
        String telefono = consola.texto("Telefono");
        Cliente cliente = servicio.registrar(nombre, documento, correo, telefono);
        consola.mostrar("Cliente registrado con ID " + cliente.getId());
    }
}
