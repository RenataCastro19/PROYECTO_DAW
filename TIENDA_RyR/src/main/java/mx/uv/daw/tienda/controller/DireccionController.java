// src/main/java/mx/uv/daw/tienda/controller/DireccionController.java
package mx.uv.daw.tienda.controller;

import mx.uv.daw.tienda.model.Direccion;
import mx.uv.daw.tienda.service.DireccionService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/direccion")
public class DireccionController {

    private final DireccionService direccionService;

    public DireccionController(DireccionService direccionService) {
        this.direccionService = direccionService;
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("direccion", new Direccion());
        return "DireccionUser/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Direccion direccion,
                          Authentication authentication) {
        String email = authentication.getName();
        direccionService.guardar(direccion, email);
        return "redirect:/perfil";
    }
}
