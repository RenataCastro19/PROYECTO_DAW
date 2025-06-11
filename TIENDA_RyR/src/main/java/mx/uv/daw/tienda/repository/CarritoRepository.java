package mx.uv.daw.tienda.repository;

import mx.uv.daw.tienda.model.Carrito;
import mx.uv.daw.tienda.model.Carrito.CarritoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
// permite acceder a la base de datos para operaciones CRUD
@Repository
public interface    CarritoRepository extends JpaRepository<Carrito, CarritoId> {

    List<Carrito> findByUsuario_EmailAndEstado(String email, Carrito.EstadoCarrito estado);
    void deleteAllByUsuario_EmailAndEstado(String email, Carrito.EstadoCarrito estado);
}
