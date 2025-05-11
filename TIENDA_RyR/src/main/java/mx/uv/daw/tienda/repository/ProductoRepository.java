package mx.uv.daw.tienda.repository;

import mx.uv.daw.tienda.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad Producto.
 * Extiende JpaRepository para proporcionar operaciones CRUD básicas.
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    /**
     * Busca un producto por su nombre exacto.
     *
     * @param nombre Nombre del producto a buscar
     * @return Optional que contiene el Producto si existe, o vacío si no
     */
    Optional<Producto> findByNombre(String nombre);
}
