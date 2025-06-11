package mx.uv.daw.tienda.repository;

import mx.uv.daw.tienda.model.Envio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnvioRepository extends JpaRepository<Envio, Long> {
    Envio findByOrden_Id(Long ordenId);
}
