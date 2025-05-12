package mx.uv.daw.tienda.service;

import mx.uv.daw.tienda.model.Carrito;
import java.util.List;

public interface CarritoService {
        void agregarItem(String email, Long idProducto, int cantidad);
        List<Carrito> listarItemsActivos(String email);
        void eliminarItem(String email, Long idProducto);
        void actualizarCantidad(String email, Long idProducto, int cantidad);
        void vaciarCarrito(String email);

}
