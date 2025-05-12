package mx.uv.daw.tienda.service;

import mx.uv.daw.tienda.model.Resenia;
import java.util.List;

public interface ReseniaService {
    List<Resenia> listarPorProducto(Long productoId);
    Resenia guardar(Resenia resenia);
    List<Resenia> listarPorUsuarioEmail(String email);

}
