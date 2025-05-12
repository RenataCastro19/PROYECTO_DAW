package mx.uv.daw.tienda.service;

import mx.uv.daw.tienda.model.Orden;
import java.util.List;

public interface OrdenService {
    List<Orden> listarPorUsuarioEmail(String email);
}
