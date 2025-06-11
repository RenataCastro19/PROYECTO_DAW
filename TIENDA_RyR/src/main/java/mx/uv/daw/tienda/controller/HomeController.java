package mx.uv.daw.tienda.controller;

import mx.uv.daw.tienda.model.Carrito;
import mx.uv.daw.tienda.model.Categoria;
import mx.uv.daw.tienda.model.Producto;
import mx.uv.daw.tienda.service.CarritoService;
import mx.uv.daw.tienda.service.CategoriaService;
import mx.uv.daw.tienda.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

@Controller
public class HomeController {
    // Servicios que serán usados para obtener datos de productos, categorías y carrito
    private final ProductoService productoService;
    private final CategoriaService categoriaService;
    private final CarritoService carritoService;

    public HomeController(ProductoService productoService,
                          CategoriaService categoriaService,
                          CarritoService carritoService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
        this.carritoService = carritoService;
    }
    // Este método se ejecuta automáticamente antes de cada petición para agregar al modelo la lista de categorías
    @ModelAttribute("categorias")
    public List<Categoria> listarCategorias() {
        return categoriaService.listarTodas();
    }

    // Este método agrega automáticamente el carrito del usuario autenticado al modelo
    @ModelAttribute("carrito")
    public List<Carrito> obtenerCarrito(Principal principal) {
        if (principal == null) {
            // Si el usuario no ha iniciado sesión, se devuelve una lista vacía
            return Collections.emptyList();
        }
        // Si el usuario está autenticado, se obtiene su carrito activo
        return carritoService.listarItemsActivos(principal.getName());
    }

    @GetMapping({"/", "/index", "/tienda"})
    public String home(
            @RequestParam(name = "search", required = false) String termino,
            @RequestParam(name = "categoria", required = false) Long categoriaId,
            Model model
    ) {
        List<Producto> productos;
// Si se seleccionó una categoría, se filtran los productos por esa categoría
        if (categoriaId != null) {
            productos = productoService.listarPorCategoria(categoriaId);
            model.addAttribute("categoriaSeleccionada", categoriaService.buscarPorId(categoriaId).orElse(null));
            // Si se ingresó un término de búsqueda, se buscan los productos que coincidan
        } else if (termino != null && !termino.trim().isEmpty()) {
            productos = productoService.buscar(termino);
            model.addAttribute("search", termino);
            // Si no se seleccionó nada, se listan todos los productos
        } else {
            productos = productoService.listarTodos();
        }
// Se agregan atributos al modelo para usarlos en la vista (HTML)
        model.addAttribute("mensaje", "RyR joyería");
        model.addAttribute("totalProductos", productoService.listarTodos().size());
        model.addAttribute("totalCategorias", categoriaService.listarTodas().size());
        model.addAttribute("productos", productos);
// Devuelve la vista llamada "index.html"
        return "index";
    }
}