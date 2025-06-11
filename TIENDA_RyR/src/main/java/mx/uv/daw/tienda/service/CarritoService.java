package mx.uv.daw.tienda.service;

import mx.uv.daw.tienda.model.Carrito;
import java.util.List;

// Interfaz que define las operaciones básicas para manipular el carrito de compras.
public interface CarritoService {

        // Agrega un producto con una cantidad determinada al carrito de un usuario identificado por su email.
        void agregarItem(String email, Long idProducto, int cantidad);

        // Lista los items activos del carrito de un usuario.
        List<Carrito> listarItemsActivos(String email);

        // Elimina un producto del carrito del usuario.
        void eliminarItem(String email, Long idProducto);

        // Actualiza la cantidad de un producto específico en el carrito.
        void actualizarCantidad(String email, Long idProducto, int cantidad);

        // Vacía (elimina todos los items) el carrito de un usuario.
        void vaciarCarrito(String email);

        // Verifica si el usuario es administrador.
        boolean esUsuarioAdmin(String email);
}
