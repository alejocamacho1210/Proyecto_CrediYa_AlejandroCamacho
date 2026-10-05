USE crediya_db;

-- Tabla pagos de la tarea (ya la crea crediya_db.sql; esta sentencia solo la crea si faltara)
CREATE TABLE IF NOT EXISTS pagos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    prestamo_id INT NOT NULL,
    fecha_pago DATE NOT NULL,
    monto DECIMAL(12,2) NOT NULL,
    CONSTRAINT fk_pagos_prestamo FOREIGN KEY (prestamo_id) REFERENCES prestamos(id),
    CONSTRAINT chk_pagos_monto CHECK (monto > 0)
) ENGINE=InnoDB;

-- Verificar el pago que inserta el programa de la tarea
SELECT * FROM pagos WHERE fecha_pago = '2025-10-09';

-- Borrar el pago de prueba (opcional, para dejar los datos como estaban)
DELETE FROM pagos WHERE fecha_pago = '2025-10-09';
