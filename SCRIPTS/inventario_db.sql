CREATE DATABASE IF NOT EXISTS inventario_db;
USE inventario_db;

-- Tabla usuarios
CREATE TABLE usuarios (
    id_usuario BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(50) NOT NULL,
    contrasena VARCHAR(80) NOT NULL,
    rol ENUM('ADMINISTRADOR', 'ALMACENISTA') NOT NULL,
    estatus BIT(1) NOT NULL,
    PRIMARY KEY (id_usuario),
    UNIQUE KEY uk_correo (correo)
);

-- Tabla productos
CREATE TABLE productos (
    id_producto BIGINT NOT NULL AUTO_INCREMENT,
    tipo ENUM('MANGA_CORTA', 'MANGA_LARGA') NOT NULL,
    talla ENUM('CH', 'EG', 'G', 'M') NOT NULL,
    color ENUM('AZUL_MARINO', 'BLANCO', 'NARANJA', 'NEGRO', 'ROJO') NOT NULL,
    precio DECIMAL(5,2) NOT NULL,
    cantidad_actual INT NOT NULL,
    activo BIT(1) NOT NULL,
    fecha_creacion DATETIME(6) NOT NULL,
    fecha_actualizacion DATETIME(6) DEFAULT NULL,
    PRIMARY KEY (id_producto),
    CONSTRAINT chk_cantidad_positiva CHECK (cantidad_actual >= 0)
);

-- Tabla movimientos_inventario
CREATE TABLE movimientos_inventario (
    id BIGINT NOT NULL AUTO_INCREMENT,
    id_producto BIGINT NOT NULL,
    id_usuario BIGINT NOT NULL,
    tipo_movimiento ENUM('ENTRADA', 'SALIDA') NOT NULL,
    cantidad INT NOT NULL,
    fecha_hora DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    KEY idx_producto (id_producto),
    KEY idx_usuario (id_usuario),
    CONSTRAINT fk_movimiento_producto 
        FOREIGN KEY (id_producto) REFERENCES productos (id_producto),
    CONSTRAINT fk_movimiento_usuario 
        FOREIGN KEY (id_usuario) REFERENCES usuarios (id_usuario)
);