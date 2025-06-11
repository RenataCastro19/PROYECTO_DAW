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

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);//imprimir mensaje en consola
    //dependencias
    private final UsuarioRepository usuarioRepository;//acceso a bd
    private final PasswordEncoder passwordEncoder;//encripta contra


    //contructor para la inyeccion de dependencias
    public UsuarioService(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder    = passwordEncoder;
    }
//transactional - sig que el metodo se ejecuta dentro de una transaccion de bd
    @Transactional(readOnly = true)
    //READ
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
    //CREATE - UPDATE
    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Transactional
    //DELETE
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

        if (usuarioRepository.findByEmail(u.getEmail()).isPresent()) {//verifica si existe el email
            logger.warn("User already exists with email: {}", u.getEmail());
            throw new ValidationException("Ya existe un usuario con ese email");
        }
        if (!u.getContrasenia().equals(u.getConfirmPassword())) {//verifica que las contraseñas coincidan
            logger.warn("Password mismatch for user: {}", u.getEmail());
            throw new ValidationException("Las contraseñas no coinciden");
        }

        try {
            String encodedPassword = passwordEncoder.encode(u.getContrasenia());//encripta contra
            logger.debug("Password encoded successfully for user: {}", u.getEmail());
            //asigna si contra al usuario
            u.setContrasenia(encodedPassword);
            u.setRolUsuario(RolUsuario.CLIENTE);
            //guarda al usuaio en la bd
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
    // Buscar un usuario por su email y devolver los datos necesarios para autenticarlo.
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        logger.debug("Loading user by email: {}", email);
        //busca en bd  usuario con el email que el usuario ingresó en el login
        Usuario u = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.warn("User not found with email: {}", email);
                    return new UsernameNotFoundException("Usuario no encontrado: " + email);
                });
        //crea un objeto UserDetails que Spring Security usa para validar la autenticación.
        UserDetails userDetails = User.builder()
                .username(u.getEmail())
                .password(u.getContrasenia())
                .authorities("ROLE_" + u.getRolUsuario().name())
                .build();

        logger.debug("Loaded user: {} with role: {}", email, u.getRolUsuario());
            //retorna usuario autenticado
        return userDetails;
    }
}
