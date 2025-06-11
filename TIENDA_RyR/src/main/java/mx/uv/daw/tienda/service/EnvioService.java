package mx.uv.daw.tienda.service;

import mx.uv.daw.tienda.model.Envio;

public interface EnvioService {
    Envio guardar(Envio envio);
    Envio buscarPorOrdenId(Long ordenId);
    Envio buscarPorId(Long id);
}
