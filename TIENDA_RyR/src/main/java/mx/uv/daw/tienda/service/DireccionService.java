
package mx.uv.daw.tienda.service;

import mx.uv.daw.tienda.model.Direccion;
import java.util.List;

public interface DireccionService {
    Direccion guardar(Direccion direccion, String email);
    List<Direccion> listarPorUsuarioEmail(String email);
}
