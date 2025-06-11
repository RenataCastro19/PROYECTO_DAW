package mx.uv.daw.tienda.service;

import mx.uv.daw.tienda.model.Direccion;
import mx.uv.daw.tienda.model.Usuario;
import mx.uv.daw.tienda.repository.DireccionRepository;
import mx.uv.daw.tienda.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class DireccionServiceImpl implements DireccionService {

    private final DireccionRepository direccionRepo;
    private final UsuarioRepository usuarioRepo;

    public DireccionServiceImpl(DireccionRepository direccionRepo,
                                UsuarioRepository usuarioRepo) {
        this.direccionRepo = direccionRepo;
        this.usuarioRepo = usuarioRepo;
    }
//Guarda o actualiza una dirección, asociándola al usuario con el email dado.
    @Override
    @Transactional
    public Direccion guardar(Direccion direccion, String email) {
        Usuario usuario = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        direccion.setUsuario(usuario);// asigna el usuario a la dirección
        return direccionRepo.save(direccion);
    }
//Lista todas las direcciones asociadas a un usuario
    @Override
    @Transactional(readOnly = true)
    public List<Direccion> listarPorUsuarioEmail(String email) {
        Usuario usuario = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return direccionRepo.findAll().stream()
                .filter(d -> d.getUsuario().getId().equals(usuario.getId()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Direccion> buscarPorId(Long id) {
        return direccionRepo.findById(id);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        direccionRepo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean perteneceAUsuario(Long direccionId, String email) {
        Optional<Direccion> direccion = buscarPorId(direccionId);
        if (direccion.isEmpty()) {
            return false;
        }

        Usuario usuario = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
// Compara el ID del usuario dueño de la dirección con el email recibido
        return direccion.get().getUsuario().getId().equals(usuario.getId());
    }
}
