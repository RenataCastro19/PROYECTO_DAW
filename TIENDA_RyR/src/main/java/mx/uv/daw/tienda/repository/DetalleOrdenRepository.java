package mx.uv.daw.tienda.repository;

import mx.uv.daw.tienda.model.DetalleOrden;
import mx.uv.daw.tienda.model.DetalleOrden.DetalleOrdenId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DetalleOrdenRepository extends JpaRepository<DetalleOrden, DetalleOrdenId> {
    List<DetalleOrden> findByOrden_Id(Long ordenId);
}
