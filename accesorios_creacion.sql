

create table material(
	ID_material serial primary key,
	nombre_material varchar(100) not null
	);
create table usuario(
	ID_usuario serial primary key,
	nombre_usuario varchar(100) not null,
	ap_pat varchar(100) not null,
	ap_mat varchar(100) not null,
	email varchar(100) unique not null,
	contraseña text not null,
-- aqui no se si ponerle el rol
	rol_usuario varchar(20) check (rol_usuario in ('admin', 'cliente')) not null
);
create table categoria (
    ID_categoria serial primary key,
    nombre_categoria varchar(100) not null
);
create table producto (
	ID_producto serial primary key,
	nombre_producto varchar(100) not null,
	descripcion_producto text,
	precio_producto decimal(10,2) not null,
	imagen_producto text,
	stock int not  null default 0,
	ID_material int references material (ID_material) on delete set null,
	ID_categoria int references categoria(ID_categoria) on delete set null
);

create table orden (
	ID_orden serial primary key,
	ID_usuario int references usuario(ID_usuario) on delete cascade ,
	fecha timestamp default current_timestamp,
	estado varchar(20) check (estado in ('pendiente', 'enviado', 'entregado')) not null,
	total_orden decimal(10,2) not null
);
create table detalle_orden(
	ID_detalle serial primary key,
	ID_orden int references orden(ID_orden) on delete cascade,
	ID_producto int references producto(ID_producto) on delete cascade,
	cantidad_items int not null,
	precio_unitario decimal(10,2) not null

);
create table carrito(
	ID_carrito serial primary key,
	ID_usuario int references usuario(ID_usuario) on delete cascade ,
	ID_producto int references producto(ID_producto) on delete cascade,
	cantidad int not null

);
create table pagos(
	ID_pago serial primary key,
	ID_orden int references orden(ID_orden) on delete cascade,
	monto_total decimal(10,2) not null,
	fecha_pago timestamp default current_timestamp,
	metodo_pago varchar(50) check (metodo_pago in ('tarjeta', 'paypal', 'transferencia')) not null
);

create table resena(
ID_resena serial primary key,
ID_usuario int references usuario(ID_usuario) on delete cascade ,
ID_producto int references producto(ID_producto) on delete cascade,
calificacion int check (calificacion between 1 and 5) not null,
comentario text,
fecha_resena timestamp default current_timestamp

);

create table direccion (
    ID_direccion serial primary key,
    ID_usuario int references usuario(ID_usuario) on delete cascade,
    calle varchar(255) not null,
    ciudad varchar(100) not null,
    estado varchar(100) not null,
    codigo_postal varchar(20) not null
);


create table envios(
ID_envio serial primary key,
ID_orden int references orden(ID_orden) on delete cascade,
ID_direccion int references direccion(ID_direccion) on delete cascade,
estado varchar(20) check(estado in('en proceso', 'en camino', 'entregado'))not null,
fecha_envio timestamp default current_timestamp
);
