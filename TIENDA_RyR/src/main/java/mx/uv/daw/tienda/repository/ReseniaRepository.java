package mx.uv.daw.tienda.repository;

import mx.uv.daw.tienda.model.Resenia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReseniaRepository extends JpaRepository<Resenia, Long> {
    // Busca todas las reseñas de un producto por su id
    List<Resenia> findByProductoId(Long productoId);

    List<Resenia> findByUsuario_Email(String email);
}
