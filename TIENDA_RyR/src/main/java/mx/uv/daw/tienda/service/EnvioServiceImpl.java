package mx.uv.daw.tienda.service;

import mx.uv.daw.tienda.model.Envio;
import mx.uv.daw.tienda.repository.EnvioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EnvioServiceImpl implements EnvioService {
    private final EnvioRepository envioRepository;

    public EnvioServiceImpl(EnvioRepository envioRepository) {
        this.envioRepository = envioRepository;
    }

    @Override
    @Transactional
    public Envio guardar(Envio envio) {
        return envioRepository.save(envio);
    }

    @Override
    @Transactional(readOnly = true)
    public Envio buscarPorOrdenId(Long ordenId) {
        return envioRepository.findByOrden_Id(ordenId);
    }

    @Override
    @Transactional(readOnly = true)
    public Envio buscarPorId(Long id) {
        return envioRepository.findById(id).orElse(null);
    }
} 