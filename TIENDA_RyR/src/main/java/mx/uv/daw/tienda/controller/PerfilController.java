package mx.uv.daw.tienda.controller;

import mx.uv.daw.tienda.model.Orden;
import mx.uv.daw.tienda.model.DetalleOrden;
import org.springframework.web.bind.annotation.PathVariable;
import mx.uv.daw.tienda.model.Usuario;
import mx.uv.daw.tienda.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PerfilController {

    private final UsuarioService usuarioService;
    private final DireccionService direccionService;
    private final OrdenService ordenService;
    private final DetalleOrdenService detalleOrdenService;
    private final ReseniaService reseniaService;

    public PerfilController(UsuarioService usuarioService,
                            DireccionService direccionService,
                            OrdenService ordenService,
                            DetalleOrdenService detalleOrdenService,
                            ReseniaService reseniaService) {
        this.usuarioService      = usuarioService;
        this.direccionService    = direccionService;
        this.ordenService        = ordenService;
        this.detalleOrdenService = detalleOrdenService;
        this.reseniaService      = reseniaService;
    }

    @GetMapping("/perfil")
    public String perfil(Model model, Authentication auth) {
        String email = auth.getName();
        Usuario u = usuarioService.buscarPorEmail(email).orElseThrow();
        model.addAttribute("usuario", u);
        return "PerfilUser/perfil";
    }

    @GetMapping("/perfil/direcciones")
    public String verDirecciones(Model model, Authentication auth) {
        String email = auth.getName();
        model.addAttribute("direcciones",
                direccionService.listarPorUsuarioEmail(email));
        return "PerfilUser/direcciones";
    }

    @GetMapping("/perfil/ordenes")
    public String verOrdenes(Model model, Authentication auth) {
        String email = auth.getName();
        var ordenes = ordenService.listarPorUsuarioEmail(email);
        model.addAttribute("ordenes", ordenes);
        return "OrdenesUser/ordenes";
    }

    @GetMapping("/perfil/ordenes/{id}/detalle")
    public String verDetalleOrden(@PathVariable Long id, Model model) {
        var detalles = detalleOrdenService.listarPorOrdenId(id);
        model.addAttribute("detalles", detalles);
        return "OrdenesUser/ordenes_detalle";
    }

    @GetMapping("/perfil/resenas")
    public String verResenas(Model model, Authentication auth) {
        String email = auth.getName();
        model.addAttribute("resenas",
                reseniaService.listarPorUsuarioEmail(email));
        return "PerfilUser/resenas";
    }
}
