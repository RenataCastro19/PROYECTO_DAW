package mx.uv.daw.tienda.controller;

import mx.uv.daw.tienda.model.Orden;
import mx.uv.daw.tienda.model.DetalleOrden;
import mx.uv.daw.tienda.model.Usuario;
import mx.uv.daw.tienda.model.Envio;
import mx.uv.daw.tienda.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Controller
public class PerfilController {

    private final UsuarioService usuarioService;
    private final DireccionService direccionService;
    private final OrdenService ordenService;
    private final DetalleOrdenService detalleOrdenService;
    private final ReseniaService reseniaService;
    private final CategoriaService categoriaService;
    private final CarritoService carritoService;
    private final EnvioService envioService;
    // Constructor con todos los servicios necesarios
    public PerfilController(UsuarioService usuarioService,
                            DireccionService direccionService,
                            OrdenService ordenService,
                            DetalleOrdenService detalleOrdenService,
                            ReseniaService reseniaService,
                            CategoriaService categoriaService,
                            CarritoService carritoService,
                            EnvioService envioService) {
        this.usuarioService      = usuarioService;
        this.direccionService    = direccionService;
        this.ordenService        = ordenService;
        this.detalleOrdenService = detalleOrdenService;
        this.reseniaService      = reseniaService;
        this.categoriaService    = categoriaService;
        this.carritoService      = carritoService;
        this.envioService        = envioService;
    }

    @GetMapping("/perfil")
    public String perfil(Model model, Authentication auth) {
        String email = auth.getName();
        Usuario u = usuarioService.buscarPorEmail(email).orElseThrow();
        model.addAttribute("usuario", u); // Enviar datos del usuario al modelo
        model.addAttribute("categorias", categoriaService.listarTodas());// Categorías disponibles
        model.addAttribute("carrito", carritoService.listarItemsActivos(email));// Items activos del carrito
        return "PerfilUser/perfil";
    }
    // Handler que muestra las direcciones del usuario
    @GetMapping("/perfil/direcciones")
    public String verDirecciones(Model model, Authentication auth) {
        String email = auth.getName();
        model.addAttribute("direcciones",
                direccionService.listarPorUsuarioEmail(email));
        model.addAttribute("categorias", categoriaService.listarTodas());
        model.addAttribute("carrito", carritoService.listarItemsActivos(email));
        return "PerfilUser/direcciones";
    }
// muestra las órdenes del usuario
    @GetMapping("/perfil/ordenes")
    public String verOrdenes(Model model, Authentication auth) {
        String email = auth.getName();
        var ordenes = ordenService.listarPorUsuarioEmail(email);

        // Crear mapa de envíos por orden
        Map<Long, Envio> enviosPorOrden = new HashMap<>();
        for (Orden orden : ordenes) {
            Envio envio = envioService.buscarPorOrdenId(orden.getId());
            enviosPorOrden.put(orden.getId(), envio);
        }

        model.addAttribute("ordenes", ordenes);
        model.addAttribute("enviosPorOrden", enviosPorOrden);
        model.addAttribute("categorias", categoriaService.listarTodas());
        model.addAttribute("carrito", carritoService.listarItemsActivos(email));
        return "PerfilUser/ordenes";
    }
//muestra el detalle de una orden específica
    @GetMapping("/perfil/ordenes/{id}/detalle")
    public String verDetalleOrden(@PathVariable Long id, Model model, Authentication auth) {
        var detalles = detalleOrdenService.buscarPorOrdenId(id);// Obtener detalles de la orden
        model.addAttribute("detalles", detalles);
        model.addAttribute("categorias", categoriaService.listarTodas());
        model.addAttribute("carrito", carritoService.listarItemsActivos(auth.getName()));
        return "PerfilUser/ordenes_detalle";
    }
//muestra las reseñas hechas por el usuario
    @GetMapping("/perfil/resenas")
    public String verResenas(Model model, Authentication auth) {
        String email = auth.getName();
        model.addAttribute("resenas",
                reseniaService.listarPorUsuarioEmail(email));
        model.addAttribute("categorias", categoriaService.listarTodas());
        model.addAttribute("carrito", carritoService.listarItemsActivos(email));
        return "PerfilUser/resenas";
    }
}
