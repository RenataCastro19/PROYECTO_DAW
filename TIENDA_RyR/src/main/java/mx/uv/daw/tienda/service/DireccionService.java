package mx.uv.daw.tienda.service;

import mx.uv.daw.tienda.model.Direccion;
import java.util.List;
import java.util.Optional;

public interface DireccionService {
    // Guarda o actualiza una dirección para un usuario dado su email.
    Direccion guardar(Direccion direccion, String email);

    // Lista todas las direcciones asociadas a un usuario por su email.
    List<Direccion> listarPorUsuarioEmail(String email);

    // Busca una dirección por su ID.
    Optional<Direccion> buscarPorId(Long id);

    // Elimina una dirección por su ID.
    void eliminar(Long id);

    // Verifica si una dirección pertenece a un usuario por email.
    boolean perteneceAUsuario(Long direccionId, String email);
}
