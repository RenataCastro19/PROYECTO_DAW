package mx.uv.daw.tienda.repository;

import mx.uv.daw.tienda.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Devuelve Optional<Usuario> en lugar de Usuario para evitar nulls
    Optional<Usuario> findByEmail(String email);
}
