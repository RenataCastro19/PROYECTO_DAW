package mx.uv.daw.tienda.repository;

import mx.uv.daw.tienda.model.Orden;
import mx.uv.daw.tienda.model.Envio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdenRepository extends JpaRepository<Orden, Long> {
    List<Orden> findByUsuario_EmailOrderByFechaDesc(String email);
    List<Orden> findAllByOrderByFechaDesc();

    // Para el caso de estado específico (no nulo)
    List<Orden> findByEnvio_EstadoOrderByFechaDesc(Envio.EstadoEnvio estadoEnvio);

    // Para cuando se necesiten todas las órdenes independientemente del estado
    default List<Orden> findByEnvio_Estado(Envio.EstadoEnvio estadoEnvio) {
        if (estadoEnvio == null) {
            return findAllByOrderByFechaDesc();
        } else {
            return findByEnvio_EstadoOrderByFechaDesc(estadoEnvio);
        }
    }
}
