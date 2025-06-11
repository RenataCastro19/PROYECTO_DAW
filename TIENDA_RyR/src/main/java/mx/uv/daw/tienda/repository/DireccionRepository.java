
package mx.uv.daw.tienda.repository;

import mx.uv.daw.tienda.model.Direccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DireccionRepository extends JpaRepository<Direccion, Long> {
//no tiene nada pq no se debe editar el metodo por defecto del jpa
}
