package mx.uv.daw.tienda.service;

import mx.uv.daw.tienda.model.Producto;
import mx.uv.daw.tienda.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio que encapsula la lógica de negocio para la entidad Producto.
 * Proporciona métodos para operaciones CRUD y validación de nombres duplicados.
 */
@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    /**
     * Inyección de dependencia del repositorio de Producto.
     *
     * @param productoRepository Repositorio JPA para Producto
     */
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    /**
     * Obtiene la lista de todos los productos.
     *
     * @return Lista de productos
     */
    @Transactional(readOnly = true)
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    /**
     * Busca un producto por su identificador.
     *
     * @param id Identificador del producto
     * @return Optional con el producto si existe, o vacío si no
     */
    @Transactional(readOnly = true)
    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }

    /**
     * Crea o actualiza un producto.
     * Valida que no exista otro producto con el mismo nombre.
     *
     * @param producto Producto a guardar
     * @return Producto guardado con su ID
     * @throws IllegalArgumentException si ya existe otro producto con el mismo nombre
     */
    @Transactional
    public Producto guardar(Producto producto) {
        Optional<Producto> existente = productoRepository.findByNombre(producto.getNombre());
        if (existente.isPresent() && !existente.get().getId().equals(producto.getId())) {
            throw new IllegalArgumentException("Ya existe un producto con ese nombre.");
        }
        return productoRepository.save(producto);
    }
}
