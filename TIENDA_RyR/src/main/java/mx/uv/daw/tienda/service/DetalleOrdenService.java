package mx.uv.daw.tienda.service;

import mx.uv.daw.tienda.model.DetalleOrden;
import java.util.List;

public interface DetalleOrdenService {
    List<DetalleOrden> buscarPorOrdenId(Long ordenId);
}