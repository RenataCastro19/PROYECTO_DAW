package mx.uv.daw.tienda.controller;

import mx.uv.daw.tienda.model.Producto;
import mx.uv.daw.tienda.service.ProductoService;
import mx.uv.daw.tienda.service.CategoriaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    public HomeController(ProductoService productoService,
                          CategoriaService categoriaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
    }

    @GetMapping({"/", "/index", "/tienda"})
    public String home(Model model) {
        // Tu mensaje y totales
        model.addAttribute("mensaje", "¡Hola de nuevo!");
        int totalProductos   = productoService.listarTodos().size();
        int totalCategorias  = categoriaService.listarTodas().size();
        model.addAttribute("totalProductos", totalProductos);
        model.addAttribute("totalCategorias", totalCategorias);

        // **Aquí es donde faltaba**: la lista de productos
        List<Producto> productos = productoService.listarTodos();
        model.addAttribute("productos", productos);

        return "index";
    }
}
