# 🛍️ TIENDA R&R — Tienda en Línea

Aplicación web de comercio electrónico desarrollada con **Spring Boot 3** y **PostgreSQL**, diseñada siguiendo una arquitectura en capas (Controller → Service → Repository → Model). Permite la gestión integral de productos, usuarios, órdenes de compra, pagos, envíos y reseñas.

---

## 📋 Tabla de Contenidos

- [Descripción General](#-descripción-general)
- [Tecnologías Utilizadas](#-tecnologías-utilizadas)
- [Arquitectura del Proyecto](#-arquitectura-del-proyecto)
- [Modelo de Datos](#-modelo-de-datos)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Requisitos Previos](#-requisitos-previos)
- [Instalación y Configuración](#-instalación-y-configuración)
- [Ejecución](#-ejecución)
- [Endpoints Disponibles](#-endpoints-disponibles)
- [Autores](#-autores)

---

## 📝 Descripción General

**TIENDA R&R** es una plataforma de e-commerce que cubre el flujo completo de compra en línea:

- **Catálogo de productos** con categorías y materiales.
- **Registro de usuarios** con roles diferenciados (Administrador / Cliente).
- **Carrito de compras** con estados de actividad.
- **Órdenes de compra** con detalle de productos, cantidades y precios unitarios.
- **Procesamiento de pagos** con estados (Pendiente, Confirmado, Rechazado).
- **Gestión de envíos** con seguimiento de estado (Pendiente → Preparando → Enviado → Entregado).
- **Direcciones de envío** asociadas a cada usuario.
- **Reseñas y calificaciones** de productos por parte de los clientes.

---

## 🛠️ Tecnologías Utilizadas

| Tecnología | Versión | Propósito |
|---|---|---|
| **Java** | 17 | Lenguaje principal |
| **Spring Boot** | 3.4.5 | Framework backend |
| **Spring Web** | — | Controladores REST y MVC |
| **Spring Data JPA** | — | Persistencia y acceso a datos |
| **Thymeleaf** | — | Motor de plantillas HTML |
| **PostgreSQL** | — | Base de datos relacional |
| **Maven** | — | Gestión de dependencias y build |
| **Hibernate** | — | ORM (mapeo objeto-relacional) |

---

## 🏗️ Arquitectura del Proyecto

El proyecto sigue una **arquitectura en capas** estándar de Spring Boot:

```
┌─────────────────────────────────────────────┐
│               🌐 Vistas (Thymeleaf)         │
│          templates/index.html               │
│          templates/registro.html            │
├─────────────────────────────────────────────┤
│            🎮 Controller                    │
│          TestController.java                │
├─────────────────────────────────────────────┤
│            ⚙️ Service                       │
│    Lógica de negocio para cada entidad      │
├─────────────────────────────────────────────┤
│            🗄️ Repository                    │
│   Interfaces JPA para acceso a datos        │
├─────────────────────────────────────────────┤
│            📦 Model (Entidades JPA)         │
│   Mapeo de tablas de la base de datos       │
├─────────────────────────────────────────────┤
│            🐘 PostgreSQL                    │
│   Base de datos: proyecto_tienda            │
└─────────────────────────────────────────────┘
```

---

## 🗃️ Modelo de Datos

El sistema cuenta con **11 entidades** interrelacionadas:

```
┌──────────────┐       ┌──────────────┐       ┌──────────────┐
│   Categoria  │       │   Material   │       │   Usuario    │
│──────────────│       │──────────────│       │──────────────│
│ id_categoria │       │ id_material  │       │ ID_usuario   │
│ nombre       │       │ nombre       │       │ nombre_comp. │
└──────┬───────┘       └──────┬───────┘       │ email        │
       │                      │               │ contrasenia  │
       │    ┌─────────────────┘               │ rol_usuario  │
       │    │                                 └──┬───┬───┬───┘
       ▼    ▼                                    │   │   │
┌──────────────┐     ┌───────────┐               │   │   │
│   Producto   │◄────┤  Reseña   │◄──────────────┘   │   │
│──────────────│     │───────────│                    │   │
│ id_producto  │     │ calif.    │   ┌───────────┐   │   │
│ nombre       │     │ comentario│   │ Direccion  │◄──┘   │
│ descripcion  │     │ fecha     │   │───────────│       │
│ precio       │     └───────────┘   │ calle     │       │
│ imagen       │                     │ ciudad    │       │
│ stock        │                     │ estado    │       │
└──┬───────┬───┘                     │ cod_postal│       │
   │       │                         └─────┬─────┘       │
   │       │                               │             │
   ▼       ▼                               │             ▼
┌─────────┐ ┌──────────────┐          ┌────┴─────┐  ┌─────────┐
│ Carrito │ │ DetalleOrden │          │  Envio   │  │  Orden  │
│─────────│ │──────────────│          │──────────│  │─────────│
│ cantidad│ │ cant_items   │          │ estado   │  │ fecha   │
│ estado  │ │ precio_unit. │          │ fecha_env│  │ total   │
└─────────┘ └──────┬───────┘          └──────────┘  └────┬────┘
                   │                                     │
                   │          ┌───────────┐              │
                   └──────────┤   Pago    │◄─────────────┘
                              │───────────│
                              │ monto     │
                              │ fecha_pago│
                              │ estado    │
                              └───────────┘
```

### Entidades principales

| Entidad | Descripción |
|---|---|
| `Usuario` | Usuarios del sistema con nombre, email y rol (ADMIN / CLIENTE) |
| `Producto` | Artículos a la venta con precio, stock, imagen, categoría y material |
| `Categoria` | Clasificación de productos |
| `Material` | Tipo de material del producto |
| `Carrito` | Relación usuario-producto con cantidad y estado (ACTIVO / COMPRADO) |
| `Orden` | Pedido de compra asociado a un usuario, con total y fechas |
| `DetalleOrden` | Líneas de detalle de cada orden (producto, cantidad, precio unitario) |
| `Pago` | Registro de pago de una orden (PENDIENTE / CONFIRMADO / RECHAZADO) |
| `Envio` | Seguimiento del envío (PENDIENTE → PREPARANDO → ENVIADO → ENTREGADO) |
| `Direccion` | Dirección de envío del usuario (calle, ciudad, estado, código postal) |
| `Resenia` | Calificación y comentario de un producto por parte de un usuario |

---

## 📂 Estructura del Proyecto

```
TIENDA_RyR/
├── pom.xml                              # Configuración Maven y dependencias
├── mvnw / mvnw.cmd                      # Maven Wrapper
├── src/
│   ├── main/
│   │   ├── java/org/example/tienda_ryr/
│   │   │   ├── TiendaRyRApplication.java        # Clase principal (Spring Boot)
│   │   │   ├── controller/
│   │   │   │   └── TestController.java           # Controlador REST de prueba
│   │   │   ├── model/
│   │   │   │   ├── Carrito.java                  # Entidad carrito de compras
│   │   │   │   ├── Categoria.java                # Entidad categoría
│   │   │   │   ├── DetalleOrden.java             # Entidad detalle de orden
│   │   │   │   ├── Direccion.java                # Entidad dirección de envío
│   │   │   │   ├── Envio.java                    # Entidad envío
│   │   │   │   ├── Material.java                 # Entidad material
│   │   │   │   ├── Orden.java                    # Entidad orden de compra
│   │   │   │   ├── Pago.java                     # Entidad pago
│   │   │   │   ├── Producto.java                 # Entidad producto
│   │   │   │   ├── Resenia.java                  # Entidad reseña
│   │   │   │   └── Usuario.java                  # Entidad usuario (+ NombreCompleto, RolUsuario)
│   │   │   ├── repository/
│   │   │   │   ├── CarritoRepository.java
│   │   │   │   ├── CategoriaRepository.java
│   │   │   │   ├── DetalleOrdenRepository.java
│   │   │   │   ├── DireccionRepository.java
│   │   │   │   ├── EnvioRepository.java
│   │   │   │   ├── MaterialRepository.java
│   │   │   │   ├── OrdenRepository.java
│   │   │   │   ├── PagoRepository.java
│   │   │   │   ├── ProductoRepository.java
│   │   │   │   ├── ReseniaRepository.java
│   │   │   │   └── UsuarioRepository.java
│   │   │   └── service/
│   │   │       ├── CarritoService.java
│   │   │       ├── CategoriaService.java
│   │   │       ├── DetalleOrdenService.java
│   │   │       ├── DireccionService.java
│   │   │       ├── EnvioService.java
│   │   │       ├── MaterialService.java
│   │   │       ├── OrdenService.java
│   │   │       ├── PagoService.java
│   │   │       ├── ProductoService.java
│   │   │       ├── ReseniaService.java
│   │   │       └── UsuarioService.java
│   │   └── resources/
│   │       ├── application.properties           # Configuración de la aplicación
│   │       ├── static/                          # Archivos estáticos (CSS, JS, imágenes)
│   │       └── templates/                       # Plantillas Thymeleaf
│   │           ├── index.html
│   │           └── registro.html
│   └── test/                                    # Tests unitarios
└── .gitignore
```

## 🚀 Ejecución

### Usando Maven Wrapper

```bash
./mvnw spring-boot:run
```

En Windows:

```cmd
mvnw.cmd spring-boot:run
```

### Desde el IDE

Ejecuta la clase `TiendaRyRApplication.java` como una aplicación Java.

La aplicación estará disponible en: **http://localhost:8080**

Al iniciar, verás en consola el mensaje  `Conexión exitosa a la base de datos.` si la configuración de PostgreSQL es correcta.

---

## 🔗 Endpoints Disponibles

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/` | Página principal (index.html) |
| `GET` | `/api/test` | Endpoint de prueba — retorna `¡API funcionando!` |

> **Nota:** El proyecto se encuentra en desarrollo activo. Se irán agregando más controladores y endpoints conforme avance la implementación.

