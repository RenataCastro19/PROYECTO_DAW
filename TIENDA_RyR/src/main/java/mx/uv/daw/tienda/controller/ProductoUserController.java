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
    // Constructor para inyectar los servicios usando inyección por constructor
    public ProductoUserController(ProductoService productoService,
                                  ReseniaService reseniaService,
                                  UsuarioService usuarioService,
                                  CarritoService carritoService) {
        this.productoService = productoService;
        this.reseniaService = reseniaService;
        this.usuarioService = usuarioService;
        this.carritoService = carritoService;
    }
    // Método que se ejecuta antes de cada handler para añadir al modelo los items activos del carrito del usuario autenticado
    @ModelAttribute("carrito")
    public List<Carrito> obtenerCarrito(Principal principal) {
        // Si no hay usuario autenticado, devuelve lista vacía
        if (principal == null) {
            return Collections.emptyList();
        }
        // Obtiene la lista de items activos del carrito para el usuario logueado (usando su email)
        return carritoService.listarItemsActivos(principal.getName());
    }

    @GetMapping("/{id}")
    public String verDetalle(@PathVariable Long id, Model model) {
        // Busca el producto por ID, lanza excepción si no se encuentra
        Producto p = productoService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + id));
        model.addAttribute("producto", p);// Añade el producto al modelo para la vista
// Obtiene las reseñas asociadas a ese producto
        List<Resenia> resenias = reseniaService.listarPorProducto(id);
        model.addAttribute("resenias", resenias);// Añade las reseñas al modelo

        double mediaRaw = resenias.stream()
                .mapToInt(Resenia::getCalificacion)
                .average()
                .orElse(0.0);
        double media = Math.round(mediaRaw * 10) / 10.0;
        model.addAttribute("valoracionMedia", media);// Añade la media al modelo
        // Añade un objeto vacío de reseña para el formulario de nueva reseña en la vista
        model.addAttribute("nuevaResenia", new Resenia());
        // Devuelve el nombre de la vista(archivo HTML)
        return "ProductoUser/Producto";
    }

    @PostMapping("/{id}/resenia")
    @PreAuthorize("hasRole('CLIENTE')")// Requiere rol CLIENTE para acceder a este método
    public String enviarResenia(@PathVariable Long id,
                                @RequestParam int calificacion,
                                @RequestParam String comentario,
                                Principal principal,
                                RedirectAttributes attrs) {
        // Busca el usuario  por su email
        Usuario u = usuarioService.buscarPorEmail(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + principal.getName()));
        // Busca el producto por ID
        Producto p = productoService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + id));
        // Crea una nueva instancia de Resenia y asigna los datos recibido
        Resenia nueva = new Resenia();
        nueva.setUsuario(u);// Usuario que hizo la reseña
        nueva.setProducto(p);// Producto reseñado
        nueva.setCalificacion(calificacion);// Calificación numérica
        nueva.setComentario(comentario);
        nueva.setFechaResenia(LocalDateTime.now()); // Fecha actual para la reseña

        reseniaService.guardar(nueva); // Guarda la reseña usando el servicio correspondiente

        attrs.addFlashAttribute("msgExito","¡Gracias por tu reseña!");
        return "redirect:/producto/" + id;// Redirige a la página del producto para mostrar el detalle y la nueva reseña
    }

}
