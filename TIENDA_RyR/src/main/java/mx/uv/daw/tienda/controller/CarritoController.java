package mx.uv.daw.tienda.controller;

import mx.uv.daw.tienda.model.Carrito;
import mx.uv.daw.tienda.service.CarritoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @GetMapping("/agregar/{idProducto}")
    public String agregar(
            @PathVariable Long idProducto,
            @RequestParam(defaultValue = "1") int cantidad,
            Principal principal) {
        carritoService.agregarItem(principal.getName(), idProducto, cantidad);
        return "redirect:/carrito/ver";
    }

    @GetMapping("/eliminar/{idProducto}")
    public String eliminar(
            @PathVariable Long idProducto,
            Principal principal) {
        carritoService.eliminarItem(principal.getName(), idProducto);
        return "redirect:/carrito/ver";
    }

    @PostMapping("/actualizar")
    public String actualizar(
            @RequestParam Long idProducto,
            @RequestParam int cantidad,
            Principal principal) {
        carritoService.actualizarCantidad(principal.getName(), idProducto, cantidad);
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

        carritoService.vaciarCarrito(principal.getName());

        return "CarritoUser/pago";
    }
}
