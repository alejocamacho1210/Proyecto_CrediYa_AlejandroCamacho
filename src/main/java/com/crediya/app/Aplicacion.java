package com.crediya.app;

import com.crediya.config.ConexionBD;
import com.crediya.exception.PersistenciaException;
import com.crediya.model.Cliente;
import com.crediya.model.Empleado;
import com.crediya.model.Pago;
import com.crediya.model.Prestamo;
import com.crediya.repository.RepositorioCompuesto;
import com.crediya.repository.archivo.ClienteRepositorioArchivo;
import com.crediya.repository.archivo.EmpleadoRepositorioArchivo;
import com.crediya.repository.archivo.PagoRepositorioArchivo;
import com.crediya.repository.archivo.PrestamoRepositorioArchivo;
import com.crediya.repository.mysql.ClienteRepositorioMySQL;
import com.crediya.repository.mysql.EmpleadoRepositorioMySQL;
import com.crediya.repository.mysql.PagoRepositorioMySQL;
import com.crediya.repository.mysql.PrestamoRepositorioMySQL;
import com.crediya.service.ClienteService;
import com.crediya.service.EmpleadoService;
import com.crediya.service.PagoService;
import com.crediya.service.PrestamoService;
import com.crediya.service.ReporteService;
import com.crediya.ui.Consola;
import com.crediya.ui.MenuClientes;
import com.crediya.ui.MenuEmpleados;
import com.crediya.ui.MenuPagos;
import com.crediya.ui.MenuPrestamos;
import com.crediya.ui.MenuPrincipal;
import com.crediya.ui.MenuReportes;
import com.crediya.ui.Presentador;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class Aplicacion {

    private static final Path DIRECTORIO_DATOS = Paths.get("datos");

    private Aplicacion() {
    }

    public static void iniciar() {
        Consola consola = new Consola();
        Repositorios repositorios = construirRepositorios(consola);
        consola.mostrar("Persistencia activa: " + repositorios.modo());

        EmpleadoService empleadoService = new EmpleadoService(repositorios.empleados());
        ClienteService clienteService = new ClienteService(repositorios.clientes());
        PrestamoService prestamoService = new PrestamoService(repositorios.prestamos(), repositorios.pagos(),
                clienteService, empleadoService);
        PagoService pagoService = new PagoService(repositorios.pagos(), prestamoService);
        ReporteService reporteService = new ReporteService(prestamoService, clienteService, empleadoService,
                pagoService);

        Presentador presentador = new Presentador(consola, clienteService, empleadoService);
        MenuPrincipal menu = new MenuPrincipal(consola,
                new MenuEmpleados(consola, empleadoService, presentador),
                new MenuClientes(consola, clienteService, prestamoService, presentador),
                new MenuPrestamos(consola, prestamoService, clienteService, empleadoService, presentador),
                new MenuPagos(consola, pagoService, prestamoService, presentador),
                new MenuReportes(consola, reporteService, presentador));
        menu.ejecutar();
        consola.mostrar("Hasta pronto.");
    }

    private static Repositorios construirRepositorios(Consola consola) {
        EmpleadoRepositorioArchivo empleadosArchivo =
                new EmpleadoRepositorioArchivo(DIRECTORIO_DATOS.resolve("empleados.txt"));
        ClienteRepositorioArchivo clientesArchivo =
                new ClienteRepositorioArchivo(DIRECTORIO_DATOS.resolve("clientes.txt"));
        PrestamoRepositorioArchivo prestamosArchivo =
                new PrestamoRepositorioArchivo(DIRECTORIO_DATOS.resolve("prestamos.txt"));
        PagoRepositorioArchivo pagosArchivo =
                new PagoRepositorioArchivo(DIRECTORIO_DATOS.resolve("pagos.txt"));

        ConexionBD conexion = ConexionBD.getInstancia();
        if (conexion.estaDisponible()) {
            try {
                RepositorioCompuesto<Empleado> empleados =
                        new RepositorioCompuesto<>(new EmpleadoRepositorioMySQL(conexion), empleadosArchivo);
                RepositorioCompuesto<Cliente> clientes =
                        new RepositorioCompuesto<>(new ClienteRepositorioMySQL(conexion), clientesArchivo);
                RepositorioCompuesto<Prestamo> prestamos =
                        new RepositorioCompuesto<>(new PrestamoRepositorioMySQL(conexion), prestamosArchivo);
                RepositorioCompuesto<Pago> pagos =
                        new RepositorioCompuesto<>(new PagoRepositorioMySQL(conexion), pagosArchivo);
                empleados.sincronizar();
                clientes.sincronizar();
                prestamos.sincronizar();
                pagos.sincronizar();
                return new Repositorios(empleados, clientes, prestamos, pagos, "MySQL + archivos de texto");
            } catch (PersistenciaException e) {
                consola.mostrar("No se pudo usar MySQL (" + e.getMessage() + ").");
            }
        } else {
            consola.mostrar("MySQL no esta disponible. Revise config.properties y que crediya_db exista.");
        }
        consola.mostrar("Se trabajara unicamente con archivos de texto en la carpeta datos/.");
        return new Repositorios(empleadosArchivo, clientesArchivo, prestamosArchivo, pagosArchivo,
                "solo archivos de texto");
    }
}
