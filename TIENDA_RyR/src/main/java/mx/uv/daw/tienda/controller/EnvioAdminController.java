package mx.uv.daw.tienda.controller;

import mx.uv.daw.tienda.model.Envio;
import mx.uv.daw.tienda.service.EnvioService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin/envios")
@PreAuthorize("hasRole('ADMIN')")
public class EnvioAdminController {
    private final EnvioService envioService;

    public EnvioAdminController(EnvioService envioService) {
        this.envioService = envioService;
    }

    @GetMapping("/{id}")
    public String verEnvio(@PathVariable Long id, Model model) {
        Envio envio = envioService.buscarPorId(id);
        if (envio == null) {
            return "redirect:/admin/ordenes";
        }
        model.addAttribute("envio", envio);
        return "enviosAdmin/detalle";
    }

    @PostMapping("/{id}/actualizar-estado")
    public String actualizarEstado(
            @PathVariable Long id,
            @RequestParam("estado") Envio.EstadoEnvio estado,
            @RequestParam("fechaEnvio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaEnvio,
            @RequestParam("fechaEntregaEstimada") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaEntregaEstimada,
            RedirectAttributes redirectAttributes) {
        Envio envio = envioService.buscarPorId(id);
        envio.setEstado(estado);
        envio.setFechaEnvio(fechaEnvio);
        envio.setFechaEntregaEstimada(fechaEntregaEstimada);
        envioService.guardar(envio);
        redirectAttributes.addFlashAttribute("success", "Estado y fechas actualizados con éxito");
        return "redirect:/admin/envios/" + id;
    }
} 