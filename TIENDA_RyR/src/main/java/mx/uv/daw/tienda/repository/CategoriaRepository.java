package mx.uv.daw.tienda.repository;

import mx.uv.daw.tienda.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    //Busca una categoría por su nombre exacto.
    Optional<Categoria> findByNombre(String nombre);
}
