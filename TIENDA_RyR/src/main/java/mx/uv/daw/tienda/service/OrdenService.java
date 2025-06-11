package mx.uv.daw.tienda.service;

import mx.uv.daw.tienda.model.Orden;
import mx.uv.daw.tienda.model.Envio;
import java.util.List;

public interface OrdenService {
    Orden crearOrdenDesdeCarrito(String email, Long direccionId);
    List<Orden> listarPorUsuarioEmail(String email);
    List<Orden> listarTodas();
    void eliminar(Long id);
    List<Orden> listarPorEstadoEnvio(Envio.EstadoEnvio estadoEnvio);
    Orden buscarPorId(Long id);
}
