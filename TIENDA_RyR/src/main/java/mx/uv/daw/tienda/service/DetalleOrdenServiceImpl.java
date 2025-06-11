package mx.uv.daw.tienda.service;

import mx.uv.daw.tienda.model.DetalleOrden;
import mx.uv.daw.tienda.repository.DetalleOrdenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class DetalleOrdenServiceImpl implements DetalleOrdenService {

    private final DetalleOrdenRepository detalleOrdenRepo;

    public DetalleOrdenServiceImpl(DetalleOrdenRepository detalleOrdenRepo) {
        this.detalleOrdenRepo = detalleOrdenRepo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleOrden> buscarPorOrdenId(Long ordenId) {
        List<DetalleOrden> detalles = detalleOrdenRepo.findByOrden_Id(ordenId);
        // Inicializar los productos
        detalles.forEach(detalle -> {
            detalle.getProducto().getNombre();
            if (detalle.getProducto().getCategoria() != null) {
                detalle.getProducto().getCategoria().getNombre();
            }
            detalle.getProducto().getImagen();
        });
        return detalles;
    }
}