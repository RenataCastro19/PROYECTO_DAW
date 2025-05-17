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

    @ModelAttribute("categorias")
    public List<Categoria> listarCategorias() {
        return categoriaService.listarTodas();
    }

    @ModelAttribute("carrito")
    public List<Carrito> obtenerCarrito(Principal principal) {
        if (principal == null) {
            return Collections.emptyList();
        }
        return carritoService.listarItemsActivos(principal.getName());
    }

    @GetMapping({"/", "/index", "/tienda"})
    public String home(
            @RequestParam(name = "search", required = false) String termino,
            Model model
    ) {
        model.addAttribute("mensaje", "RyR joyería");
        model.addAttribute("totalProductos", productoService.listarTodos().size());
        model.addAttribute("totalCategorias", categoriaService.listarTodas().size());
        model.addAttribute("productos", productoService.buscar(termino));
        model.addAttribute("search", termino);
        return "index";
    }
}