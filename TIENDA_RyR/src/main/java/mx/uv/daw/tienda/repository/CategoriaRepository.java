package mx.uv.daw.tienda.repository;

import mx.uv.daw.tienda.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Interfaz de repositorio JPA para la entidad Categoria.
 * Extiende JpaRepository para ofrecer operaciones CRUD básicas.
 */
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    /**
     * Busca una categoría por su nombre exacto.
     *
     * @param nombre Nombre de la categoría a buscar
     * @return Optional con la categoría encontrada o vacío si no existe
     */
    Optional<Categoria> findByNombre(String nombre);
}
