// src/main/java/mx/uv/daw/tienda/service/DireccionServiceImpl.java
package mx.uv.daw.tienda.service;

import mx.uv.daw.tienda.model.Direccion;
import mx.uv.daw.tienda.model.Usuario;
import mx.uv.daw.tienda.repository.DireccionRepository;
import mx.uv.daw.tienda.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class DireccionServiceImpl implements DireccionService {

    private final DireccionRepository direccionRepo;
    private final UsuarioRepository usuarioRepo;

    public DireccionServiceImpl(DireccionRepository direccionRepo,
                                UsuarioRepository usuarioRepo) {
        this.direccionRepo = direccionRepo;
        this.usuarioRepo = usuarioRepo;
    }

    @Override
    @Transactional
    public Direccion guardar(Direccion direccion, String email) {
        Usuario usuario = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        direccion.setUsuario(usuario);
        return direccionRepo.save(direccion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Direccion> listarPorUsuarioEmail(String email) {
        Usuario usuario = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return direccionRepo.findAll().stream()
                .filter(d -> d.getUsuario().getId().equals(usuario.getId()))
                .toList();
    }
}
