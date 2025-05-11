package mx.uv.daw.tienda.service;

import jakarta.validation.ValidationException;
import mx.uv.daw.tienda.dto.RegistroForm;
import mx.uv.daw.tienda.model.RolUsuario;
import mx.uv.daw.tienda.model.Usuario;
import mx.uv.daw.tienda.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Transactional
    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Transactional
    public void registrarCliente(RegistroForm form) {
        // Validaciones
        if (usuarioRepository.findByEmail(form.getEmail()).isPresent()) {
            throw new ValidationException("Ya existe un usuario con ese email");
        }
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            throw new ValidationException("Las contraseñas no coinciden");
        }

        // Construcción de la entidad Usuario con campos independientes
        Usuario u = new Usuario();
        u.setNombre(form.getNombre());
        u.setApPat(form.getApPat());
        u.setApMat(form.getApMat());
        u.setEmail(form.getEmail());
        u.setContrasenia(passwordEncoder.encode(form.getPassword()));
        u.setRolUsuario(RolUsuario.CLIENTE);

        usuarioRepository.save(u);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        Usuario u = usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Usuario no encontrado: " + email)
                );

        return User.builder()
                .username(u.getEmail())
                .password(u.getContrasenia())
                .roles(u.getRolUsuario().name())
                .build();
    }
}
