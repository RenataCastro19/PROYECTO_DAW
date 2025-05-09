-- UNIQUE sobre nombres
ALTER TABLE material
  ADD CONSTRAINT uk_material_nombre UNIQUE (nombre_material);

ALTER TABLE categoria
  ADD CONSTRAINT uk_categoria_nombre UNIQUE (nombre_categoria);

ALTER TABLE producto
  ADD CONSTRAINT uk_producto_nombre UNIQUE (nombre_producto);

-- NOT NULL en columnas de llaves foráneas obligatorias
ALTER TABLE direccion
  ALTER COLUMN R_usuario SET NOT NULL;

ALTER TABLE producto
  ALTER COLUMN R_material SET NOT NULL,
  ALTER COLUMN R_categoria SET NOT NULL;

ALTER TABLE orden
  ALTER COLUMN R_usuario SET NOT NULL;

ALTER TABLE pagos
  ALTER COLUMN estado_pago SET NOT NULL;

-- CHECK más estrictos para cantidades
ALTER TABLE detalle_orden
  ADD CONSTRAINT chk_detalle_cantidad CHECK (cantidad_items >= 1);

ALTER TABLE carrito
  ADD CONSTRAINT chk_carrito_cantidad CHECK (cantidad >= 1);

-- para evitar que alguien cree órdenes sin total
ALTER TABLE orden
  ALTER COLUMN total_orden SET NOT NULL;
