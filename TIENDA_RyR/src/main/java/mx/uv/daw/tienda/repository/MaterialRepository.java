package mx.uv.daw.tienda.repository;

import mx.uv.daw.tienda.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad Material.
 * Extiende JpaRepository para proporcionar las operaciones CRUD básicas y consultas paginadas.
 */
@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {

    /**
     * Busca un material por su nombre exacto en la base de datos.
     *
     * @param nombre El nombre del material a buscar.
     * @return Optional que contiene el Material si existe, o vacío si no se encuentra.
     */
    Optional<Material> findByNombre(String nombre);
}
