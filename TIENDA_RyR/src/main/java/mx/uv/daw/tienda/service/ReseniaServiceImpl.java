package mx.uv.daw.tienda.service;
import org.springframework.transaction.annotation.Transactional;

import mx.uv.daw.tienda.model.Resenia;
import mx.uv.daw.tienda.repository.ReseniaRepository;
import mx.uv.daw.tienda.service.ReseniaService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReseniaServiceImpl implements ReseniaService {
    private final ReseniaRepository repo;

    public ReseniaServiceImpl(ReseniaRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Resenia> listarPorProducto(Long productoId) {
        return repo.findByProductoId(productoId);
    }

    @Override
    public Resenia guardar(Resenia resenia) {
        return repo.save(resenia);
    }
    @Override
    @Transactional(readOnly = true)
    public List<Resenia> listarPorUsuarioEmail(String email) {
        return repo.findByUsuario_Email(email);
    }

}
