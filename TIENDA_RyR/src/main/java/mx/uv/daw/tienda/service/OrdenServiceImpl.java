package mx.uv.daw.tienda.service;

import mx.uv.daw.tienda.model.*;
import mx.uv.daw.tienda.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrdenServiceImpl implements OrdenService {

    private final OrdenRepository ordenRepo;
    private final UsuarioRepository usuarioRepo;
    private final CarritoService carritoService;
    private final DireccionService direccionService;
    private final DetalleOrdenRepository detalleOrdenRepo;
    private final EnvioService envioService;
    private final ProductoService productoService;

    public OrdenServiceImpl(OrdenRepository ordenRepo,
                            UsuarioRepository usuarioRepo,
                            CarritoService carritoService,
                            DireccionService direccionService,
                            DetalleOrdenRepository detalleOrdenRepo,
                            EnvioService envioService,
                            ProductoService productoService) {
        this.ordenRepo = ordenRepo;
        this.usuarioRepo = usuarioRepo;
        this.carritoService = carritoService;
        this.direccionService = direccionService;
        this.detalleOrdenRepo = detalleOrdenRepo;
        this.envioService = envioService;
        this.productoService = productoService;
    }

    @Override
    @Transactional
    public Orden crearOrdenDesdeCarrito(String email, Long direccionId) {
        Usuario usuario = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Carrito> itemsCarrito = carritoService.listarItemsActivos(email);
        if (itemsCarrito.isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }

        // Calcular total
        BigDecimal total = itemsCarrito.stream()
                .map(item -> item.getProducto().getPrecio()
                        .multiply(BigDecimal.valueOf(item.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Crear la orden
        Orden orden = new Orden();
        orden.setUsuario(usuario);
        orden.setFecha(LocalDateTime.now());
        orden.setTotalOrden(total);
        orden.setUsuarioModifico(usuario);
        orden.setFechaModificacion(LocalDateTime.now());

        orden = ordenRepo.save(orden);

        // Crear los detalles de la orden
        for (Carrito item : itemsCarrito) {
            DetalleOrden detalle = new DetalleOrden();
            detalle.setId(new DetalleOrden.DetalleOrdenId(orden.getId(), item.getProducto().getId()));
            detalle.setOrden(orden);
            detalle.setProducto(item.getProducto());
            detalle.setCantidadItems(item.getCantidad());
            detalle.setPrecioUnitario(item.getProducto().getPrecio());
            detalleOrdenRepo.save(detalle);

            // Actualizar stock del producto
            productoService.reducirStock(item.getProducto().getId(), item.getCantidad());
        }

        // Crear el envío asociado a la orden
        Direccion direccion = direccionService.buscarPorId(direccionId)
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada"));
        Envio envio = new Envio();
        envio.setOrden(orden);
        envio.setDireccion(direccion);
        envio.setEstado(Envio.EstadoEnvio.pendiente);
        LocalDateTime fechaEnvio = orden.getFecha().plusDays(3);
        envio.setFechaEnvio(fechaEnvio);
        envio.setFechaEntregaEstimada(fechaEnvio.toLocalDate().plusDays(2));
        envioService.guardar(envio);

        // Vaciar el carrito
        carritoService.vaciarCarrito(email);

        return orden;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Orden> listarPorUsuarioEmail(String email) {
        return ordenRepo.findByUsuario_EmailOrderByFechaDesc(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Orden> listarTodas() {
        return ordenRepo.findAllByOrderByFechaDesc();
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        ordenRepo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Orden> listarPorEstadoEnvio(Envio.EstadoEnvio estadoEnvio) {
        return ordenRepo.findByEnvio_Estado(estadoEnvio);
    }

    @Override
    @Transactional(readOnly = true)
    public Orden buscarPorId(Long id) {
        return ordenRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
    }
}
