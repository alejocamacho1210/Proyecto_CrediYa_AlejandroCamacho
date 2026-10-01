CREATE DATABASE IF NOT EXISTS crediya_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE crediya_db;

CREATE TABLE IF NOT EXISTS empleados (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL,
    documento VARCHAR(30) NOT NULL UNIQUE,
    rol VARCHAR(30) NOT NULL,
    correo VARCHAR(80) NOT NULL,
    salario DECIMAL(10,2) NOT NULL,
    CONSTRAINT chk_empleados_salario CHECK (salario > 0)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL,
    documento VARCHAR(30) NOT NULL UNIQUE,
    correo VARCHAR(80) NOT NULL,
    telefono VARCHAR(20) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS prestamos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT NOT NULL,
    empleado_id INT NOT NULL,
    monto DECIMAL(12,2) NOT NULL,
    interes DECIMAL(5,2) NOT NULL,
    cuotas INT NOT NULL,
    fecha_inicio DATE NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'pendiente',
    CONSTRAINT fk_prestamos_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id),
    CONSTRAINT fk_prestamos_empleado FOREIGN KEY (empleado_id) REFERENCES empleados(id),
    CONSTRAINT chk_prestamos_monto CHECK (monto > 0),
    CONSTRAINT chk_prestamos_interes CHECK (interes >= 0),
    CONSTRAINT chk_prestamos_cuotas CHECK (cuotas > 0),
    CONSTRAINT chk_prestamos_estado CHECK (estado IN ('pendiente', 'pagado'))
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS pagos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    prestamo_id INT NOT NULL,
    fecha_pago DATE NOT NULL,
    monto DECIMAL(12,2) NOT NULL,
    CONSTRAINT fk_pagos_prestamo FOREIGN KEY (prestamo_id) REFERENCES prestamos(id),
    CONSTRAINT chk_pagos_monto CHECK (monto > 0)
) ENGINE=InnoDB;

CREATE INDEX idx_prestamos_cliente ON prestamos(cliente_id);
CREATE INDEX idx_prestamos_empleado ON prestamos(empleado_id);
CREATE INDEX idx_prestamos_estado ON prestamos(estado);
CREATE INDEX idx_pagos_prestamo ON pagos(prestamo_id);
