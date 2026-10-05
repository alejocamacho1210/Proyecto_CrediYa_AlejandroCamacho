USE crediya_db;

-- Dos empleados
INSERT INTO empleados (nombre, documento, rol, correo, salario) VALUES
('Laura Gomez', '1001234567', 'Asesor de cobranza', 'laura.gomez@crediya.com', 2800000.00),
('Carlos Ruiz', '1007654321', 'Gerente de cartera', 'carlos.ruiz@crediya.com', 4500000.00);

-- Cuatro clientes
INSERT INTO clientes (nombre, documento, correo, telefono) VALUES
('Maria Fernanda Lopez', '52123456', 'maria.lopez@correo.com', '3001234567'),
('Jorge Andres Perez', '80987654', 'jorge.perez@correo.com', '3109876543'),
('Ana Sofia Martinez', '1020304050', 'ana.martinez@correo.com', '3205551234'),
('Luis Alberto Torres', '79456123', 'luis.torres@correo.com', '3157778899');

-- Préstamo 1: vencido | 2: en mora | 3: pagado | 4: al día
INSERT INTO prestamos (cliente_id, empleado_id, monto, interes, cuotas, fecha_inicio, estado) VALUES
(1, 1, 1000000.00, 2.00, 6, '2026-03-01', 'pendiente'),
(2, 1, 500000.00, 1.50, 12, '2026-07-15', 'pendiente'),
(3, 2, 300000.00, 2.00, 3, '2026-06-01', 'pagado'),
(4, 2, 800000.00, 1.00, 10, '2026-09-10', 'pendiente');

-- Abonos de los préstamos 1, 2 y 3
INSERT INTO pagos (prestamo_id, fecha_pago, monto) VALUES
(1, '2026-04-01', 200000.00),
(1, '2026-05-01', 200000.00),
(1, '2026-06-01', 200000.00),
(2, '2026-08-15', 49166.67),
(3, '2026-07-01', 106000.00),
(3, '2026-08-01', 106000.00),
(3, '2026-09-01', 106000.00);
