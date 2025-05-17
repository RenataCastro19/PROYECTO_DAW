package mx.uv.daw.tienda.service;

import mx.uv.daw.tienda.model.Producto;
import mx.uv.daw.tienda.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Transactional(readOnly = true)
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Producto> buscar(String termino) {
        if (termino == null || termino.trim().isEmpty()) {
            return listarTodos();
        }
        return productoRepository
                .findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCase(termino, termino);
    }

    @Transactional(readOnly = true)
    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }

    @Transactional
    public Producto guardar(Producto producto) {
        Optional<Producto> existente = productoRepository.findByNombre(producto.getNombre());
        if (existente.isPresent() && !existente.get().getId().equals(producto.getId())) {
            throw new IllegalArgumentException("Ya existe un producto con ese nombre.");
        }
        return productoRepository.save(producto);
    }

    @Transactional
    public void reducirStock(Long idProducto, int cantidad) {
        Producto p = productoRepository.findById(idProducto)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + idProducto));
        int nuevoStock = p.getStock() - cantidad;
        if (nuevoStock < 0) {
            throw new IllegalStateException("Stock insuficiente para el producto " + idProducto);
        }
        p.setStock(nuevoStock);
        productoRepository.save(p);
    }
}