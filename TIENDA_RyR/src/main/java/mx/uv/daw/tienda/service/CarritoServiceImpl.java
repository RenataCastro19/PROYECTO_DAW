// src/main/java/mx/uv/daw/tienda/service/CarritoServiceImpl.java
package mx.uv.daw.tienda.service;

import mx.uv.daw.tienda.model.Carrito;
import mx.uv.daw.tienda.model.Producto;
import mx.uv.daw.tienda.model.Usuario;
import mx.uv.daw.tienda.repository.CarritoRepository;
import mx.uv.daw.tienda.repository.ProductoRepository;
import mx.uv.daw.tienda.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CarritoServiceImpl implements CarritoService {

    private final CarritoRepository carritoRepo;
    private final UsuarioRepository usuarioRepo;
    private final ProductoRepository productoRepo;

    public CarritoServiceImpl(CarritoRepository carritoRepo,
                              UsuarioRepository usuarioRepo,
                              ProductoRepository productoRepo) {
        this.carritoRepo   = carritoRepo;
        this.usuarioRepo   = usuarioRepo;
        this.productoRepo  = productoRepo;
    }

//    @Override
//    @Transactional
//    public void agregarItem(String email, Long idProducto) {
//        agregarItem(email, idProducto, 1);
//    }

    @Override
    @Transactional
    public void agregarItem(String email, Long idProducto, int cantidad) {
        Usuario usr = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Producto prod = productoRepo.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Carrito.CarritoId key = new Carrito.CarritoId(usr.getId(), prod.getId());
        Optional<Carrito> existente = carritoRepo.findById(key);

        if (existente.isPresent()) {
            Carrito c = existente.get();
            c.setCantidad(c.getCantidad() + cantidad);
            carritoRepo.save(c);
        } else {
            Carrito c = new Carrito(usr, prod, cantidad, Carrito.EstadoCarrito.activo);
            carritoRepo.save(c);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Carrito> listarItemsActivos(String email) {
        return carritoRepo.findByUsuario_EmailAndEstado(email, Carrito.EstadoCarrito.activo);
    }

    @Override
    @Transactional
    public void eliminarItem(String email, Long idProducto) {
        Usuario usr = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Carrito.CarritoId key = new Carrito.CarritoId(usr.getId(), idProducto);
        carritoRepo.deleteById(key);
    }

    @Override
    @Transactional
    public void actualizarCantidad(String email, Long idProducto, int cantidad) {
        Usuario usr = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Carrito.CarritoId key = new Carrito.CarritoId(usr.getId(), idProducto);
        Optional<Carrito> op = carritoRepo.findById(key);

        if (op.isPresent()) {
            Carrito c = op.get();
            if (cantidad < 1) {
                carritoRepo.delete(c);
            } else {
                c.setCantidad(cantidad);
                carritoRepo.save(c);
            }
        } else {
            throw new RuntimeException("Item no existe en carrito");
        }
    }
    @Override
    @Transactional
    public void vaciarCarrito(String email) {
        carritoRepo.deleteAllByUsuario_EmailAndEstado(
                email, Carrito.EstadoCarrito.activo
        );
    }

}
