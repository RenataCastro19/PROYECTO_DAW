package mx.uv.daw.tienda.repository;

import mx.uv.daw.tienda.model.Carrito;
import mx.uv.daw.tienda.model.Carrito.CarritoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface    CarritoRepository extends JpaRepository<Carrito, CarritoId> {
    /**
     * Devuelve todos los ítems de carrito con estado ACTIVO para el usuario identificado por su email.
     */
    List<Carrito> findByUsuario_EmailAndEstado(String email, Carrito.EstadoCarrito estado);
    void deleteAllByUsuario_EmailAndEstado(String email, Carrito.EstadoCarrito estado);
}
