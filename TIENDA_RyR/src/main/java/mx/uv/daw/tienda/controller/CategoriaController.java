package mx.uv.daw.tienda.controller;

import jakarta.validation.Valid;
import mx.uv.daw.tienda.model.Categoria;
import mx.uv.daw.tienda.service.CategoriaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controlador MVC para gestionar operaciones CRUD de la entidad Categoria.
 */
@Controller
@RequestMapping("/categoriasAdmin")
public class CategoriaController {

    private final CategoriaService categoriaService;

    /**
     * Inyección de dependencia del servicio de Categorias.
     */
    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    /**
     * 1. Listar todas las categorías.
     *    Metodo GET para mostrar la lista de categorias.
     */
    @GetMapping
    public String listarCategorias(Model model) {
        // Obtiene la lista desde el servicio y la añade al modelo
        model.addAttribute("categorias", categoriaService.listarTodas());
        return "categoriasAdmin/lista";
    }

    /**
     * 2. Mostrar formulario para crear una nueva categoría.
     *    Metodo GET que prepara un objeto Categoria vacío.
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model) {
        // Añade un objeto vacío al modelo para enlazar en el formulario
        model.addAttribute("categoria", new Categoria());
        return "categoriasAdmin/formulario";
    }

    /**
     * 3. Procesar la creación o edición de una categoría.
     *    Metodo POST que recibe los datos del formulario.
     */
    @PostMapping("/guardar")
    public String guardarCategoria(
            @Valid @ModelAttribute("categoria") Categoria categoria,
            BindingResult result,
            Model model) {

        // 3.1 Validación de Bean Validation (@NotBlank, @Size, etc.)
        if (result.hasErrors()) {
            // Si hay errores, retorna al formulario mostrando los mensajes
            return "categoriasAdmin/formulario";
        }

        // 3.2 Intento de guardado con manejo de duplicados
        try {
            categoriaService.guardar(categoria);
            // Si todo va bien, redirige al listado de categorías
            return "redirect:/categoriasAdmin";
        } catch (IllegalArgumentException e) {
            // Captura error de nombre duplicado y lo añade al modelo
            model.addAttribute("error", e.getMessage());
            // Retorna al mismo formulario para mostrar el mensaje global
            return "categoriasAdmin/formulario";
        }
    }

    /**
     * 4. Mostrar formulario de edición de una categoría existente.
     *    Metodo GET que carga la categoría por id.
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        // Busca la categoria en el servicio por su identificador
        Optional<Categoria> opt = categoriaService.buscarPorId(id);
        if (opt.isPresent()) {
            // Si existe, la añade al modelo y muestra el formulario
            model.addAttribute("categoria", opt.get());
            return "categoriasAdmin/formulario";
        }
        // Si no existe, redirige al listado de categorías
        return "redirect:/categoriasAdmin";
    }
}
