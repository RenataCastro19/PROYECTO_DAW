package mx.uv.daw.tienda.controller;

import jakarta.validation.Valid;
import mx.uv.daw.tienda.model.Material;
import mx.uv.daw.tienda.service.MaterialService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controlador MVC para la entidad Material.
 * Maneja rutas para listar, crear, editar y guardar materiales.
 */
@Controller
@RequestMapping("/admin/materiales")
@PreAuthorize("hasRole('ADMIN')")
public class MaterialController {

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    /**
     * Muestra el listado de todos los materiales.
     */
    @GetMapping
    public String listarMateriales(Model model) {
        model.addAttribute("materiales", materialService.listarTodas());
        return "materialesAdmin/lista";  // Thymeleaf buscará templates/materialesAdmin/lista.html
    }

    /**
     * Muestra el formulario vacío para crear un nuevo material.
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("material", new Material());
        return "materialesAdmin/formulario";
    }

    /**
     * Valida y guarda un material (creación o edición).
     * Si hay errores de validación o nombre duplicado, vuelve al formulario.
     */
    @PostMapping("/guardar")
    public String guardarMaterial(
            @Valid @ModelAttribute("material") Material material,
            BindingResult result,
            Model model) {

        // 1) Si hay errores de validación de campos, regresar al formulario
        if (result.hasErrors()) {
            return "materialesAdmin/formulario";
        }

        try {
            // 2) Intentar guardar, puede lanzar IllegalArgumentException por duplicado
            materialService.guardar(material);
            return "redirect:/admin/materiales";
        } catch (IllegalArgumentException e) {
            // 3) Capturar error de nombre duplicado y mostrar mensaje
            model.addAttribute("error", e.getMessage());
            return "materialesAdmin/formulario";
        }
    }

    /**
     * Carga los datos de un material existente en el formulario para edición.
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Optional<Material> opt = materialService.buscarPorId(id);
        if (opt.isPresent()) {
            model.addAttribute("material", opt.get());
            return "materialesAdmin/formulario";
        }
        // Si no existe, redirige al listado
        return "redirect:/admin/materiales";
    }
}
