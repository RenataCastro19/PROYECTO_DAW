package mx.uv.daw.tienda.repository;

import mx.uv.daw.tienda.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    Optional<Producto> findByNombre(String nombre);

    /**
     * Busca productos cuyo nombre o descripción contenga el término dado,
     * ignorando mayúsculas y minúsculas.
     */
    List<Producto> findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCase(
            String nombreTerm,
            String descTerm
    );

    List<Producto> findByCategoria_Id(Long categoriaId);
}