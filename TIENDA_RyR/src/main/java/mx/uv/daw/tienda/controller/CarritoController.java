package mx.uv.daw.tienda.controller;

import mx.uv.daw.tienda.model.Carrito;
import mx.uv.daw.tienda.service.CarritoService;
import mx.uv.daw.tienda.service.ProductoService;
import mx.uv.daw.tienda.service.CategoriaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/carrito")
public class CarritoController {

    private final CarritoService carritoService;
    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    public CarritoController(CarritoService carritoService,
                             ProductoService productoService,
                             CategoriaService categoriaService) {
        this.carritoService  = carritoService;
        this.productoService = productoService;
        this.categoriaService = categoriaService;
    }

    @GetMapping("/agregar/{idProducto}")
    public String agregar(
            @PathVariable Long idProducto,
            @RequestParam(defaultValue = "1") int cantidad,
            Principal principal,
            RedirectAttributes flash
    ) {
        try {
            // Comprobar si el usuario es admin
            if (isAdmin(principal)) {
                flash.addFlashAttribute("error", "Los administradores no pueden realizar compras");
                return "redirect:/admin/dashboard";
            }

            carritoService.agregarItem(principal.getName(), idProducto, cantidad);
        } catch (IllegalArgumentException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/carrito/ver";
    }

    @GetMapping("/eliminar/{idProducto}")
    public String eliminar(@PathVariable Long idProducto,
                           Principal principal,
                           RedirectAttributes flash) {
        // Comprobar si el usuario es admin
        if (isAdmin(principal)) {
            flash.addFlashAttribute("error", "Los administradores no pueden realizar compras");
            return "redirect:/admin/dashboard";
        }

        carritoService.eliminarItem(principal.getName(), idProducto);
        return "redirect:/carrito/ver";
    }

    @PostMapping("/actualizar")
    public String actualizar(
            @RequestParam Long idProducto,
            @RequestParam int cantidad,
            Principal principal,
            RedirectAttributes flash
    ) {
        try {
            // Comprobar si el usuario es admin
            if (isAdmin(principal)) {
                flash.addFlashAttribute("error", "Los administradores no pueden realizar compras");
                return "redirect:/admin/dashboard";
            }

            carritoService.actualizarCantidad(principal.getName(), idProducto, cantidad);
            flash.addFlashAttribute("success", "Cantidad actualizada");
        } catch (IllegalArgumentException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/carrito/ver";
    }

    @GetMapping("/ver")
    public String verCarrito(Model model, Principal principal,
                             RedirectAttributes flash) {
        // Comprobar si el usuario es admin
        if (isAdmin(principal)) {
            flash.addFlashAttribute("error", "Los administradores no pueden realizar compras");
            return "redirect:/admin/dashboard";
        }

        String email = principal.getName();
        List<Carrito> items = carritoService.listarItemsActivos(email);
        model.addAttribute("items", items);
        model.addAttribute("categorias", categoriaService.listarTodas());
        model.addAttribute("carrito", items);

        BigDecimal total = items.stream()
                .map(i -> i.getProducto().getPrecio()
                        .multiply(BigDecimal.valueOf(i.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("total", total);

        return "CarritoUser/carrito";
    }

    @GetMapping("/pagar")
    public String pagar(Model model, Principal principal,
                        @ModelAttribute("montoTotal") BigDecimal montoTotal,
                        @ModelAttribute("ordenId") Long ordenId,
                        RedirectAttributes flash) {
        // Comprobar si el usuario es admin
        if (isAdmin(principal)) {
            flash.addFlashAttribute("error", "Los administradores no pueden realizar compras");
            return "redirect:/admin/dashboard";
        }

        String email = principal.getName();
        List<Carrito> items = carritoService.listarItemsActivos(email);

        // 1) Descontar stock de cada producto
        for (Carrito item : items) {
            productoService.reducirStock(
                    item.getProducto().getId(),
                    item.getCantidad()
            );
        }

        // 2) Vaciar el carrito
        carritoService.vaciarCarrito(email);

        // 3) Generar comprobante y datos de pago
        String comprobante = UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
        LocalDateTime fechaPago = LocalDateTime.now();
        String fechaPagoStr = fechaPago.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        String estadoPago = "CONFIRMADO";

        // 4) Calcular fecha estimada de entrega (5 días después)
        String fechaEntregaStr = fechaPago.plusDays(5)
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        // Agregar atributos al modelo
        model.addAttribute("categorias", categoriaService.listarTodas());
        model.addAttribute("carrito", items);
        model.addAttribute("total", montoTotal);
        model.addAttribute("comprobante", comprobante);
        model.addAttribute("fechaPago", fechaPagoStr);
        model.addAttribute("fechaEntrega", fechaEntregaStr);
        model.addAttribute("estadoPago", estadoPago);
        model.addAttribute("ordenId", ordenId);

        return "CarritoUser/pago";
    }

    // Agregar método auxiliar para comprobar si el usuario es admin
    private boolean isAdmin(Principal principal) {
        String email = principal.getName();
        return carritoService.esUsuarioAdmin(email);
    }
}
