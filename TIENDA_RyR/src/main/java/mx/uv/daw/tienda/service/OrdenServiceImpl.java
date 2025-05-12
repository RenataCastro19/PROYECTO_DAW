package mx.uv.daw.tienda.service;

import mx.uv.daw.tienda.model.Orden;
import mx.uv.daw.tienda.repository.OrdenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class OrdenServiceImpl implements OrdenService {

    private final OrdenRepository ordenRepo;

    public OrdenServiceImpl(OrdenRepository ordenRepo) {
        this.ordenRepo = ordenRepo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Orden> listarPorUsuarioEmail(String email) {
        return ordenRepo.findByUsuario_EmailOrderByFechaDesc(email);
    }

}
