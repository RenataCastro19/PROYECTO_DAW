package mx.uv.daw.tienda.service;

import mx.uv.daw.tienda.model.DetalleOrden;
import mx.uv.daw.tienda.repository.DetalleOrdenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class DetalleOrdenServiceImpl implements DetalleOrdenService {

    private final DetalleOrdenRepository repo;

    public DetalleOrdenServiceImpl(DetalleOrdenRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleOrden> listarPorOrdenId(Long ordenId) {
        return repo.findByOrden_Id(ordenId);
    }
}
