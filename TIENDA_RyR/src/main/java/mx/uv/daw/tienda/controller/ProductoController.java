package mx.uv.daw.tienda.controller;

import jakarta.validation.Valid;
import mx.uv.daw.tienda.model.Producto;
import mx.uv.daw.tienda.service.ProductoService;
import mx.uv.daw.tienda.service.CategoriaService;
import mx.uv.daw.tienda.service.MaterialService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador MVC para la entidad Producto.
 * Maneja rutas para listar, crear, editar y guardar productos.
 */
@Controller
@RequestMapping("/productosAdmin")
public class ProductoController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;
    private final MaterialService materialService;

    /**
     * Inyección de dependencias de los servicios necesarios.
     */
    public ProductoController(ProductoService productoService,
                              CategoriaService categoriaService,
                              MaterialService materialService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
        this.materialService = materialService;
    }

    /**
     * GET /productosAdmin
     * Muestra el listado de todos los productos.
     */
    @GetMapping
    public String listarProductos(Model model) {
        List<Producto> productos = productoService.listarTodos();
        model.addAttribute("productos", productos);
        return "productosAdmin/lista";  // templates/productosAdmin/lista.html
    }

    /**
     * GET /productosAdmin/nuevo
     * Muestra el formulario para crear un nuevo producto,
     * incluyendo listas de categorías y materiales.
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model) {
        // Objeto vacío para enlazar el formulario
        model.addAttribute("producto", new Producto());
        // Listas de opciones para selects
        model.addAttribute("categorias", categoriaService.listarTodas());
        model.addAttribute("materiales", materialService.listarTodas());
        return "productosAdmin/formulario";
    }

    /**
     * POST /productosAdmin/guardar
     * Valida y guarda el producto (creación o edición).
     * Si hay errores de validación o nombre duplicado, vuelve al formulario.
     */
    @PostMapping("/guardar")
    public String guardarProducto(
            @Valid @ModelAttribute("producto") Producto producto,
            BindingResult result,
            Model model) {

        // 1) Si hay errores de validación, rebobina selects y vuelve al form
        if (result.hasErrors()) {
            model.addAttribute("categorias", categoriaService.listarTodas());
            model.addAttribute("materiales", materialService.listarTodas());
            return "productosAdmin/formulario";
        }

        try {
            productoService.guardar(producto);
            return "redirect:/productosAdmin";
        } catch (IllegalArgumentException | DataIntegrityViolationException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("categorias", categoriaService.listarTodas());
            model.addAttribute("materiales", materialService.listarTodas());
            return "productosAdmin/formulario";
        }
    }

    /**
     * GET /productosAdmin/editar/{id}
     * Carga los datos de un producto existente en el formulario para edición.
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Optional<Producto> opt = productoService.buscarPorId(id);
        if (opt.isPresent()) {
            model.addAttribute("producto", opt.get());
            // Rebobina listas de selects para el formulario
            model.addAttribute("categorias", categoriaService.listarTodas());
            model.addAttribute("materiales", materialService.listarTodas());
            return "productosAdmin/formulario";
        }
        // Si no existe, redirige al listado
        return "redirect:/productosAdmin";
    }
}
