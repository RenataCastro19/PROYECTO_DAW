
package mx.uv.daw.tienda.repository;

import mx.uv.daw.tienda.model.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrdenRepository extends JpaRepository<Orden, Long> {
    List<Orden> findByUsuario_EmailOrderByFechaDesc(String email);
}
