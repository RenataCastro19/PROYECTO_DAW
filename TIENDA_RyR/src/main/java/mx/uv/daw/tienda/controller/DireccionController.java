// src/main/java/mx/uv/daw/tienda/controller/DireccionController.java
package mx.uv.daw.tienda.controller;

import mx.uv.daw.tienda.model.Direccion;
import mx.uv.daw.tienda.service.DireccionService;
import mx.uv.daw.tienda.service.CategoriaService;
import mx.uv.daw.tienda.service.CarritoService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/direccion")
public class DireccionController {

    private final DireccionService direccionService;
    private final CategoriaService categoriaService;
    private final CarritoService carritoService;

    public DireccionController(DireccionService direccionService,
                               CategoriaService categoriaService,
                               CarritoService carritoService) {
        this.direccionService = direccionService;
        this.categoriaService = categoriaService;
        this.carritoService = carritoService;
    }

    @GetMapping("/nueva")
    public String nueva(Model model,
                        @RequestParam(required = false) String redirect,
                        Authentication auth) {
        model.addAttribute("direccion", new Direccion());
        model.addAttribute("redirect", redirect);
        model.addAttribute("categorias", categoriaService.listarTodas());
        model.addAttribute("carrito", carritoService.listarItemsActivos(auth.getName()));
        return "DireccionUser/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Direccion direccion,
                          @RequestParam(required = false) String redirect,
                          Authentication authentication) {
        String email = authentication.getName();
        direccionService.guardar(direccion, email);

        if (redirect != null && !redirect.isEmpty()) {
            return "redirect:" + redirect;
        }
        return "redirect:/perfil";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id,
                         Model model,
                         Authentication auth,
                         RedirectAttributes flash) {
        String email = auth.getName();

        // Verificar que la dirección pertenece al usuario
        if (!direccionService.perteneceAUsuario(id, email)) {
            flash.addFlashAttribute("error", "No tienes permiso para editar esta dirección");
            return "redirect:/perfil/direcciones";
        }

        // Buscar la dirección
        var direccion = direccionService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada"));

        model.addAttribute("direccion", direccion);
        model.addAttribute("categorias", categoriaService.listarTodas());
        model.addAttribute("carrito", carritoService.listarItemsActivos(email));

        return "DireccionUser/form";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id,
                           Authentication auth,
                           RedirectAttributes flash) {
        String email = auth.getName();

        // Verificar que la dirección pertenece al usuario
        if (!direccionService.perteneceAUsuario(id, email)) {
            flash.addFlashAttribute("error", "No tienes permiso para eliminar esta dirección");
            return "redirect:/perfil/direcciones";
        }

        try {
            direccionService.eliminar(id);
            flash.addFlashAttribute("success", "Dirección eliminada correctamente");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al eliminar la dirección");
        }

        return "redirect:/perfil/direcciones";
    }
}
