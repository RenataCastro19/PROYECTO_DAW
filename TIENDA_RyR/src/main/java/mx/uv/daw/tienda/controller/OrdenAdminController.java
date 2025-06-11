package mx.uv.daw.tienda.controller;

import mx.uv.daw.tienda.model.Orden;
import mx.uv.daw.tienda.model.DetalleOrden;
import mx.uv.daw.tienda.service.OrdenService;
import mx.uv.daw.tienda.model.Envio;
import mx.uv.daw.tienda.service.EnvioService;
import mx.uv.daw.tienda.service.DetalleOrdenService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin/ordenes")
@PreAuthorize("hasRole('ADMIN')")
public class OrdenAdminController {
    private final OrdenService ordenService;
    private final EnvioService envioService;
    private final DetalleOrdenService detalleOrdenService;

    public OrdenAdminController(OrdenService ordenService,
                                EnvioService envioService,
                                DetalleOrdenService detalleOrdenService) {
        this.ordenService = ordenService;
        this.envioService = envioService;
        this.detalleOrdenService = detalleOrdenService;
    }
    // Método para listar órdenes con filtro opcional por estado de envío
    @GetMapping
    public String listarOrdenes(
            @RequestParam(name = "estadoEnvio", required = false) String estadoEnvio,
            Model model) {

        List<Orden> ordenes;
        // Si se recibió un estado de envío, se filtran las órdenes por ese estado
        if (estadoEnvio != null && !estadoEnvio.isEmpty()) {
            try {
                // Convierte el string a un valor del enum EstadoEnvio (en minúscula)
                Envio.EstadoEnvio estado = Envio.EstadoEnvio.valueOf(estadoEnvio.toLowerCase());
                ordenes = ordenService.listarPorEstadoEnvio(estado);
            } catch (IllegalArgumentException e) {
                // Si el estado es inválido, se listan todas las órdenes
                ordenes = ordenService.listarTodas();
            }
        } else {
            // Si no se recibió estado, se listan todas
            ordenes = ordenService.listarTodas();
        }
// Se crea un mapa para asociar cada orden con su respectivo envío
        Map<Long, Envio> enviosPorOrden = new HashMap<>();
        for (Orden orden : ordenes) {
            Envio envio = envioService.buscarPorOrdenId(orden.getId());
            enviosPorOrden.put(orden.getId(), envio);
        }
// Se agregan datos al modelo para enviarlos a la vista
        model.addAttribute("ordenes", ordenes);
        model.addAttribute("enviosPorOrden", enviosPorOrden);
        model.addAttribute("estadoEnvio", estadoEnvio);
        // Se muestra la vista de la lista de órdenes del administrador
        return "ordenesAdmin/lista";
    }

    @GetMapping("/{id}")
    // Método para ver el detalle de una orden específica
    public String verOrden(@PathVariable Long id, Model model) {
        Orden orden = ordenService.buscarPorId(id);
        Envio envio = envioService.buscarPorOrdenId(id);
        List<DetalleOrden> detalles = detalleOrdenService.buscarPorOrdenId(id);
// Se agregan los datos al modelo para mostrarlos en la vista de detalle
        model.addAttribute("orden", orden);
        model.addAttribute("envio", envio);
        model.addAttribute("detalles", detalles);
        model.addAttribute("estadosEnvio", Envio.EstadoEnvio.values());
        // Se muestra la vista de detalle de la orde
        return "ordenesAdmin/detalle";
    }
    // Método para actualizar el estado de envío de una orden específica
    @PostMapping("/{id}/actualizar-envio")
    public String actualizarEstadoEnvio(
            @PathVariable Long id,
            @RequestParam Envio.EstadoEnvio nuevoEstado) {
// Busca el envío asociado a la orden
        Envio envio = envioService.buscarPorOrdenId(id);
        // Cambia el estado del envío y lo guarda
        envio.setEstado(nuevoEstado);
        envioService.guardar(envio);
// Redirige de nuevo al detalle de la orden
        return "redirect:/admin/ordenes/" + id;
    }
} 