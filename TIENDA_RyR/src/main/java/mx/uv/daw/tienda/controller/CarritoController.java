package mx.uv.daw.tienda.controller;

import mx.uv.daw.tienda.model.Carrito;
import mx.uv.daw.tienda.service.CarritoService;
import mx.uv.daw.tienda.service.ProductoService;
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
    private final ProductoService productoService;  // ← inyecta

    public CarritoController(CarritoService carritoService,
                             ProductoService productoService) {
        this.carritoService  = carritoService;
        this.productoService = productoService;     // ← asigna
    }

    @GetMapping("/agregar/{idProducto}")
    public String agregar(
            @PathVariable Long idProducto,
            @RequestParam(defaultValue = "1") int cantidad,
            Principal principal,
            RedirectAttributes flash
    ) {
        try {
            carritoService.agregarItem(principal.getName(), idProducto, cantidad);
        } catch (IllegalArgumentException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/carrito/ver";
    }

    @GetMapping("/eliminar/{idProducto}")
    public String eliminar(@PathVariable Long idProducto,
                           Principal principal) {
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
            carritoService.actualizarCantidad(principal.getName(), idProducto, cantidad);
            flash.addFlashAttribute("success", "Cantidad actualizada");
        } catch (IllegalArgumentException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/carrito/ver";
    }

    @GetMapping("/ver")
    public String verCarrito(Model model, Principal principal) {
        List<Carrito> items = carritoService.listarItemsActivos(principal.getName());
        model.addAttribute("items", items);

        BigDecimal total = items.stream()
                .map(i -> i.getProducto().getPrecio()
                        .multiply(BigDecimal.valueOf(i.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("total", total);

        return "CarritoUser/carrito";
    }

    @GetMapping("/pagar")
    public String pagar(Model model, Principal principal) {
        List<Carrito> items = carritoService.listarItemsActivos(principal.getName());
        BigDecimal total = items.stream()
                .map(i -> i.getProducto().getPrecio()
                        .multiply(BigDecimal.valueOf(i.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 1) Descontar stock de cada producto
        for (Carrito item : items) {
            productoService.reducirStock(
                    item.getProducto().getId(),
                    item.getCantidad()
            );
        }

        // 2) Vaciar el carrito
        carritoService.vaciarCarrito(principal.getName());

        // 3) Generar comprobante y datos de pago
        String comprobante = UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
        String fechaPagoStr = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        String estadoPago = "CONFIRMADO";

        model.addAttribute("items", items);
        model.addAttribute("total", total);
        model.addAttribute("comprobante", comprobante);
        model.addAttribute("fechaPago", fechaPagoStr);
        model.addAttribute("estadoPago", estadoPago);

        return "CarritoUser/pago";
    }
}
