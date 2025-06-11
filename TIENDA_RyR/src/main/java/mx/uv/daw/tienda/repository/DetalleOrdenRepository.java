package mx.uv.daw.tienda.repository;

import mx.uv.daw.tienda.model.DetalleOrden;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DetalleOrdenRepository extends JpaRepository<DetalleOrden, DetalleOrden.DetalleOrdenId> {

    // Busca todos los detalles asociados a una orden específica.
    List<DetalleOrden> findByOrden_Id(Long ordenId);
}