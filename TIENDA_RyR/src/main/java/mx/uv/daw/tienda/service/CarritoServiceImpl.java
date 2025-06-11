package mx.uv.daw.tienda.service;

import mx.uv.daw.tienda.model.Carrito;
import mx.uv.daw.tienda.model.Producto;
import mx.uv.daw.tienda.model.RolUsuario;
import mx.uv.daw.tienda.model.Usuario;
import mx.uv.daw.tienda.repository.CarritoRepository;
import mx.uv.daw.tienda.repository.ProductoRepository;
import mx.uv.daw.tienda.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
//implementación concreta de la interfaz. Aquí está el código real que hace las operaciones
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



    @Override
    @Transactional // Indica que este método hace cambios en la base de datos.
    public void agregarItem(String email, Long idProducto, int cantidad) {
        //busca email y id , si no los encuentra lanza excepcion
        Usuario usr = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Producto prod = productoRepo.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Calcula cuánto habría en el carrito tras esta operación
        Carrito.CarritoId key = new Carrito.CarritoId(usr.getId(), prod.getId());
        Optional<Carrito> existente = carritoRepo.findById(key);
        int actualEnCarrito = existente.map(Carrito::getCantidad).orElse(0);
        int totalDeseado = actualEnCarrito + cantidad;

        // Valida contra el stock disponible
        if (totalDeseado > prod.getStock()) {
            throw new IllegalArgumentException(
                    "No puedes agregar más de " + prod.getStock() + " unidades de \""
                            + prod.getNombre() + "\""
            );
        }

        // Si cabe, lo guardamos o actualizamos
        if (existente.isPresent()) {
            Carrito c = existente.get();
            c.setCantidad(totalDeseado);
            carritoRepo.save(c);
        } else {
            Carrito c = new Carrito(usr, prod, cantidad, Carrito.EstadoCarrito.activo);
            carritoRepo.save(c);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Carrito> listarItemsActivos(String email) {
        // Devuelve la lista de productos activos en el carrito del usuario.
        return carritoRepo.findByUsuario_EmailAndEstado(email, Carrito.EstadoCarrito.activo);
    }

    @Override
    @Transactional
    public void eliminarItem(String email, Long idProducto) {
        Usuario usr = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Carrito.CarritoId key = new Carrito.CarritoId(usr.getId(), idProducto);
        // Elimina el producto específico del carrito del usuario.
        carritoRepo.deleteById(key);
    }

    @Override
    @Transactional
    public void actualizarCantidad(String email, Long idProducto, int cantidad) {
        Usuario usr = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Producto prod = productoRepo.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
// Valida que la cantidad solicitada no supere el stock.
        if (cantidad > prod.getStock()) {
            throw new IllegalArgumentException(
                    "No puedes actualizar a más de "
                            + prod.getStock()
                            + " unidades de \""
                            + prod.getNombre()
                            + "\""
            );
        }

        Carrito.CarritoId key = new Carrito.CarritoId(usr.getId(), idProducto);
        Optional<Carrito> op = carritoRepo.findById(key);
        // Si el item existe, actualiza cantidad o elimina si cantidad < 1.
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
        // Elimina todos los productos activos en el carrito del usuario.
        carritoRepo.deleteAllByUsuario_EmailAndEstado(email, Carrito.EstadoCarrito.activo
        );
    }

    @Override
    @Transactional(readOnly = true)
    public boolean esUsuarioAdmin(String email) {
        Optional<Usuario> usuarioOpt = usuarioRepo.findByEmail(email);
        if (!usuarioOpt.isPresent()) {
            return false;
        }
        Usuario usuario = usuarioOpt.get();
        // Comprueba si el usuario tiene rol de administrador.

        return usuario.getRolUsuario() == mx.uv.daw.tienda.model.RolUsuario.ADMIN;
    }

}
