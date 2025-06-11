package mx.uv.daw.tienda.controller;

import mx.uv.daw.tienda.model.*;
import mx.uv.daw.tienda.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.math.BigDecimal;

@Controller
@RequestMapping("/pedido")
public class PedidoController {

    private final DireccionService direccionService;
    private final CarritoService carritoService;
    private final CategoriaService categoriaService;
    private final OrdenService ordenService;

    public PedidoController(DireccionService direccionService,
                            CarritoService carritoService,
                            CategoriaService categoriaService,
                            OrdenService ordenService) {
        this.direccionService = direccionService;
        this.carritoService = carritoService;
        this.categoriaService = categoriaService;
        this.ordenService = ordenService;
    }

    @GetMapping("/direccion_pedido")
    public String seleccionarDireccion(Model model, Principal principal) {
        String email = principal.getName();
        List<Direccion> direcciones = direccionService.listarPorUsuarioEmail(email);

        if (direcciones.isEmpty()) {
            return "redirect:/direccion/nueva?redirect=/pedido/direccion_pedido";
        }

        model.addAttribute("direcciones", direcciones);
        model.addAttribute("categorias", categoriaService.listarTodas());
        model.addAttribute("carrito", carritoService.listarItemsActivos(email));
        return "pedido/direccion_pedido";
    }

    @PostMapping("/direccion")
    public String procesarDireccion(
            @RequestParam Long direccionId,
            Principal principal,
            Model model,
            RedirectAttributes flash
    ) {
        if (direccionId == null) {
            model.addAttribute("direcciones",
                    direccionService.listarPorUsuarioEmail(principal.getName()));
            model.addAttribute("error", "Debes seleccionar una dirección");
            return "pedido/direccion_pedido";
        }

        return "redirect:/pedido/confirmacion?direccionId=" + direccionId;
    }

    @GetMapping("/confirmacion")
    public String confirmarPedido(@RequestParam Long direccionId,
                                  Principal principal,
                                  Model model) {
        String email = principal.getName();

        // Obtener items del carrito
        List<Carrito> itemsCarrito = carritoService.listarItemsActivos(email);
        if (itemsCarrito.isEmpty()) {
            return "redirect:/carrito/ver";
        }

        // Calcular total
        BigDecimal total = itemsCarrito.stream()
                .map(item -> item.getProducto().getPrecio()
                        .multiply(BigDecimal.valueOf(item.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Obtener la dirección seleccionada
        Direccion direccion = direccionService.buscarPorId(direccionId)
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada"));

        model.addAttribute("items", itemsCarrito);
        model.addAttribute("total", total);
        model.addAttribute("direccion", direccion);
        model.addAttribute("categorias", categoriaService.listarTodas());
        model.addAttribute("carrito", itemsCarrito);

        return "pedido/confirmacion";
    }

    @PostMapping("/pagar")
    public String procesarPago(@RequestParam Long direccionId,
                               Principal principal,
                               RedirectAttributes flash) {
        try {
            String email = principal.getName();

            // Obtener items y calcular total antes de crear la orden
            List<Carrito> itemsCarrito = carritoService.listarItemsActivos(email);
            BigDecimal total = itemsCarrito.stream()
                    .map(item -> item.getProducto().getPrecio()
                            .multiply(BigDecimal.valueOf(item.getCantidad())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Crear la orden
            Orden orden = ordenService.crearOrdenDesdeCarrito(email, direccionId);

            // Pasar el total a la página de pago
            flash.addFlashAttribute("montoTotal", total);
            // Pasar el ID de la orden al comprobante
            flash.addFlashAttribute("ordenId", orden.getId());

            return "redirect:/carrito/pagar";
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al procesar el pago: " + e.getMessage());
            return "redirect:/pedido/confirmacion?direccionId=" + direccionId;
        }
    }
}
