package mx.uv.daw.tienda.service;

import jakarta.validation.ValidationException;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder    = passwordEncoder;
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

    /**
     * Registra un nuevo cliente usando directamente la entidad Usuario.
     * Valida unicidad de email y coincidencia de contraseñas.
     */
    @Transactional
    public void registrarCliente(Usuario u) {
        logger.info("Starting registration for user: {}", u.getEmail());

        if (usuarioRepository.findByEmail(u.getEmail()).isPresent()) {
            logger.warn("User already exists with email: {}", u.getEmail());
            throw new ValidationException("Ya existe un usuario con ese email");
        }
        if (!u.getContrasenia().equals(u.getConfirmPassword())) {
            logger.warn("Password mismatch for user: {}", u.getEmail());
            throw new ValidationException("Las contraseñas no coinciden");
        }

        try {
            String encodedPassword = passwordEncoder.encode(u.getContrasenia());
            logger.debug("Password encoded successfully for user: {}", u.getEmail());

            u.setContrasenia(encodedPassword);
            u.setRolUsuario(RolUsuario.CLIENTE);

            Usuario savedUser = usuarioRepository.save(u);
            logger.info("Successfully registered user: {} with role: {}", savedUser.getEmail(), savedUser.getRolUsuario());
        } catch (Exception e) {
            logger.error("Error during user registration: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Registra un nuevo administrador. Este método solo debe ser llamado por usuarios ADMIN existentes.
     */
    @Transactional
    public void registrarAdmin(Usuario u) {
        if (usuarioRepository.findByEmail(u.getEmail()).isPresent()) {
            throw new ValidationException("Ya existe un usuario con ese email");
        }
        if (!u.getContrasenia().equals(u.getConfirmPassword())) {
            throw new ValidationException("Las contraseñas no coinciden");
        }

        u.setContrasenia(passwordEncoder.encode(u.getContrasenia()));
        u.setRolUsuario(RolUsuario.ADMIN);
        usuarioRepository.save(u);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        logger.debug("Loading user by email: {}", email);
        Usuario u = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.warn("User not found with email: {}", email);
                    return new UsernameNotFoundException("Usuario no encontrado: " + email);
                });

        UserDetails userDetails = User.builder()
                .username(u.getEmail())
                .password(u.getContrasenia())
                .authorities("ROLE_" + u.getRolUsuario().name())
                .build();

        logger.debug("Loaded user: {} with role: {}", email, u.getRolUsuario());
        return userDetails;
    }
}
