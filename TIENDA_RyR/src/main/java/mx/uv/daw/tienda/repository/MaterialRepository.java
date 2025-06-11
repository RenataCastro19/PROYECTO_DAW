package mx.uv.daw.tienda.repository;

import mx.uv.daw.tienda.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {

    Optional<Material> findByNombre(String nombre);
}
