package com.crediya.tarea;

import com.crediya.exception.CrediYaException;
import com.crediya.exception.ValidacionException;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

// Gestiona los pagos en memoria (ArrayList), los guarda en MySQL y genera el reporte con Stream API
public class GestorPagos {

    // Lista en memoria donde se almacenan los pagos
    private final List<Pago> pagos = new ArrayList<>();

    // Agrega un pago a la lista; los errores se capturan con try/catch y se muestran
    public void registrarPago(Pago pago) {
        try {
            validar(pago);
            if (pago.getId() == 0) {
                pago.setId(pagos.stream().mapToInt(Pago::getId).max().orElse(0) + 1);
            }
            pagos.add(pago);
            System.out.println("Pago registrado correctamente.");
        } catch (CrediYaException e) {
            System.out.println("Error al registrar el pago: " + e.getMessage());
        }
    }

    // Muestra en consola todos los pagos registrados
    public void listarPagos() {
        System.out.println("=== LISTADO DE PAGOS ===");
        if (pagos.isEmpty()) {
            System.out.println("No hay pagos registrados.");
            return;
        }
        pagos.forEach(System.out::println);
    }

    // Reporte con Stream API y lambda: solo los pagos con monto mayor al valor ingresado
    public void mostrarPagosMayoresA(double valor) {
        String texto = valor == Math.rint(valor) ? String.valueOf((long) valor) : String.valueOf(valor);
        System.out.println("=== PAGOS MAYORES A " + texto + " ===");
        List<Pago> mayores = pagos.stream()
                .filter(p -> p.getMonto() > valor)
                .collect(Collectors.toList());
        if (mayores.isEmpty()) {
            System.out.println("No hay pagos mayores a " + texto);
            return;
        }
        mayores.forEach(p -> System.out.println("Pago ID " + p.getId() + " - Monto: " + p.getMonto()));
    }

    // Inserta el pago en la tabla pagos de crediya_db con JDBC y la conexión Singleton
    public boolean guardarPagoEnDB(Pago pago) {
        String sql = "INSERT INTO pagos (prestamo_id, fecha_pago, monto) VALUES (?, ?, ?)";
        try {
            validar(pago);
            Connection conexion = ConexionDB.getInstancia().getConexion();
            try (PreparedStatement sentencia = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                sentencia.setInt(1, pago.getPrestamoId());
                sentencia.setDate(2, Date.valueOf(pago.getFechaPago()));
                sentencia.setBigDecimal(3, BigDecimal.valueOf(pago.getMonto()));
                sentencia.executeUpdate();
                try (ResultSet claves = sentencia.getGeneratedKeys()) {
                    if (claves.next()) {
                        pago.setId(claves.getInt(1));
                    }
                }
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error al guardar el pago en MySQL: " + e.getMessage());
            return false;
        } catch (CrediYaException e) {
            System.out.println("Error al guardar el pago: " + e.getMessage());
            return false;
        }
    }

    // Copia de solo lectura de los pagos en memoria
    public List<Pago> getPagos() {
        return Collections.unmodifiableList(pagos);
    }

    // Validaciones comunes: pago no nulo, fecha presente y monto positivo
    private void validar(Pago pago) {
        if (pago == null) {
            throw new ValidacionException("El pago no puede ser nulo");
        }
        if (pago.getFechaPago() == null) {
            throw new ValidacionException("La fecha de pago no puede ser nula");
        }
        if (pago.getMonto() <= 0) {
            throw new ValidacionException("El monto debe ser mayor que cero");
        }
    }
}
