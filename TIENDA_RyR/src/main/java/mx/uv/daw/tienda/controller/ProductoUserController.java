package mx.uv.daw.tienda.controller;

import mx.uv.daw.tienda.model.Producto;
import mx.uv.daw.tienda.model.Resenia;
import mx.uv.daw.tienda.model.Usuario;
import mx.uv.daw.tienda.model.Carrito;
import mx.uv.daw.tienda.service.ProductoService;
import mx.uv.daw.tienda.service.ReseniaService;
import mx.uv.daw.tienda.service.UsuarioService;
import mx.uv.daw.tienda.service.CarritoService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Collections;

@Controller
@RequestMapping("/producto")
public class ProductoUserController {

    private final ProductoService productoService;
    private final ReseniaService reseniaService;
    private final UsuarioService usuarioService;
    private final CarritoService carritoService;

    public ProductoUserController(ProductoService productoService,
                                  ReseniaService reseniaService,
                                  UsuarioService usuarioService,
                                  CarritoService carritoService) {
        this.productoService = productoService;
        this.reseniaService = reseniaService;
        this.usuarioService = usuarioService;
        this.carritoService = carritoService;
    }

    @ModelAttribute("carrito")
    public List<Carrito> obtenerCarrito(Principal principal) {
        if (principal == null) {
            return Collections.emptyList();
        }
        return carritoService.listarItemsActivos(principal.getName());
    }

    @GetMapping("/{id}")
    public String verDetalle(@PathVariable Long id, Model model) {
        Producto p = productoService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + id));
        model.addAttribute("producto", p);

        List<Resenia> resenias = reseniaService.listarPorProducto(id);
        model.addAttribute("resenias", resenias);

        double mediaRaw = resenias.stream()
                .mapToInt(Resenia::getCalificacion)
                .average()
                .orElse(0.0);
        double media = Math.round(mediaRaw * 10) / 10.0;
        model.addAttribute("valoracionMedia", media);

        model.addAttribute("nuevaResenia", new Resenia());
        return "ProductoUser/Producto";
    }

    @PostMapping("/{id}/resenia")
    @PreAuthorize("hasRole('CLIENTE')")
    public String enviarResenia(@PathVariable Long id,
                                @RequestParam int calificacion,
                                @RequestParam String comentario,
                                Principal principal,
                                RedirectAttributes attrs) {
        Usuario u = usuarioService.buscarPorEmail(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + principal.getName()));
        Producto p = productoService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + id));

        Resenia nueva = new Resenia();
        nueva.setUsuario(u);
        nueva.setProducto(p);
        nueva.setCalificacion(calificacion);
        nueva.setComentario(comentario);
        nueva.setFechaResenia(LocalDateTime.now());

        reseniaService.guardar(nueva);

        attrs.addFlashAttribute("msgExito","¡Gracias por tu reseña!");
        return "redirect:/producto/" + id;
    }

}
