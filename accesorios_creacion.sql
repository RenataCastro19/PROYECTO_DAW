-- BD JOYERIA PROYECTO FINAL DAW

-- Tabla de materiales
CREATE TABLE material (
    ID_material SERIAL PRIMARY KEY,
    nombre_material VARCHAR(100) NOT NULL
);
CREATE TYPE nombre_completo AS (
    nombre VARCHAR(100),
    ap_pat VARCHAR(100),
    ap_mat VARCHAR(100)
);

-- Tabla de usuarios
CREATE TABLE usuario (
    ID_usuario SERIAL PRIMARY KEY,
    nombre_completo nombre_completo NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    contrasenia TEXT NOT NULL,
    rol_usuario VARCHAR(20) CHECK (rol_usuario IN ('admin', 'cliente')) NOT NULL
);


-- Tabla de direcciones
CREATE TABLE direccion (
    ID_direccion SERIAL PRIMARY KEY,
    R_usuario INT REFERENCES usuario(ID_usuario) ON DELETE CASCADE,
    calle VARCHAR(255) NOT NULL,
    ciudad VARCHAR(100) NOT NULL,
    estado VARCHAR(100) NOT NULL,
    codigo_postal VARCHAR(20) NOT NULL
);

-- Tabla de categorías de productos
CREATE TABLE categoria (
    ID_categoria SERIAL PRIMARY KEY,
    nombre_categoria VARCHAR(100) NOT NULL
);

-- Tabla de productos
CREATE TABLE producto (
    ID_producto SERIAL PRIMARY KEY,
    nombre_producto VARCHAR(100) NOT NULL,
    descripcion_producto TEXT,
    precio_producto DECIMAL(10,2) NOT NULL CHECK (precio_producto >= 0),
    imagen_producto TEXT,
    stock INT NOT NULL DEFAULT 0 CHECK (stock >= 0),
    R_material INT REFERENCES material (ID_material) ON DELETE SET NULL,
    R_categoria INT REFERENCES categoria (ID_categoria) ON DELETE SET NULL
);

-- Tabla de órdenes
CREATE TABLE orden (
    ID_orden SERIAL PRIMARY KEY,
    R_usuario INT REFERENCES usuario(ID_usuario) ON DELETE CASCADE,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_orden DECIMAL(10,2) NOT NULL,
    R_usuario_modifico INT REFERENCES usuario(ID_usuario),
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de detalle de orden
CREATE TABLE detalle_orden (
    R_orden INT REFERENCES orden(ID_orden) ON DELETE CASCADE,
    R_producto INT REFERENCES producto(ID_producto) ON DELETE CASCADE,
    cantidad_items INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (R_orden, R_producto)
);

-- Tabla de carrito
CREATE TABLE carrito (
    R_usuario INT REFERENCES usuario(ID_usuario) ON DELETE CASCADE,
    R_producto INT REFERENCES producto(ID_producto) ON DELETE CASCADE,
    cantidad INT NOT NULL,
    estado VARCHAR(20) CHECK (estado IN ('activo', 'comprado')) DEFAULT 'activo',
    PRIMARY KEY (R_usuario, R_producto)
);

-- Tabla de pagos
CREATE TABLE pagos (
    ID_pago SERIAL PRIMARY KEY,
    R_orden INT REFERENCES orden(ID_orden) ON DELETE CASCADE,
    monto_total DECIMAL(10,2) NOT NULL,
    fecha_pago TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    comprobante_pago TEXT,
    estado_pago VARCHAR(20) DEFAULT 'pendiente' CHECK (estado_pago IN ('pendiente', 'confirmado', 'rechazado'))
);

-- Tabla de reseñas
CREATE TABLE resenia (
    ID_resenia SERIAL PRIMARY KEY,
    R_usuario INT REFERENCES usuario(ID_usuario) ON DELETE CASCADE,
    R_producto INT REFERENCES producto(ID_producto) ON DELETE CASCADE,
    calificacion INT CHECK (calificacion BETWEEN 1 AND 5) NOT NULL,
    comentario TEXT,
    fecha_resenia TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de envíos
CREATE TABLE envios (
    ID_envio SERIAL PRIMARY KEY,
    R_orden INT REFERENCES orden(ID_orden) ON DELETE CASCADE,
    R_direccion INT REFERENCES direccion(ID_direccion) ON DELETE CASCADE,
    estado VARCHAR(20) CHECK(estado IN ('pendiente', 'preparando', 'enviado', 'entregado')) NOT NULL,
    fecha_envio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_entrega_estimada DATE
);