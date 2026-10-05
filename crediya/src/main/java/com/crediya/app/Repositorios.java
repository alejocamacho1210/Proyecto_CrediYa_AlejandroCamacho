package com.crediya.app;

import com.crediya.model.Cliente;
import com.crediya.model.Empleado;
import com.crediya.model.Pago;
import com.crediya.model.Prestamo;
import com.crediya.repository.Repositorio;

// Agrupa los cuatro repositorios y el modo de persistencia activo
public record Repositorios(Repositorio<Empleado> empleados,
                           Repositorio<Cliente> clientes,
                           Repositorio<Prestamo> prestamos,
                           Repositorio<Pago> pagos,
                           String modo) {
}
