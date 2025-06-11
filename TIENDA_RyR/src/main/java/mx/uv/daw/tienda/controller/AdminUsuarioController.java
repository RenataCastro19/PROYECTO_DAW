package mx.uv.daw.tienda.controller;

import jakarta.validation.Valid;
import mx.uv.daw.tienda.model.Usuario;
import mx.uv.daw.tienda.service.DireccionService;
import mx.uv.daw.tienda.service.OrdenService;
import mx.uv.daw.tienda.service.UsuarioService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
// Clase controlador que maneja las rutas relacionadas con usuarios en la administración
@Controller
@RequestMapping("/admin/usuarios")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUsuarioController {
    //dependencias
    private final UsuarioService usuarioService;
    private final DireccionService direccionService;
    private final OrdenService ordenService;
    private final PasswordEncoder passwordEncoder;
    // Constructor para inyección de dependencias por Spring
    public AdminUsuarioController(UsuarioService usuarioService, DireccionService direccionService, OrdenService ordenService, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.direccionService = direccionService;
        this.ordenService = ordenService;
        this.passwordEncoder = passwordEncoder;
    }
    // Mostrar formulario para crear un nuevo administrador
    @GetMapping("/nuevo-admin")
    public String mostrarFormularioNuevoAdmin(Model model) {
        model.addAttribute("usuario", new Usuario());// Se añade un objeto Usuario vacío para el formulario
        return "usuariosAdmin/form-admin";// Devuelve la vista HTML para el formulario
    }
    // Procesar datos enviados para registrar un nuevo administrador
    @PostMapping("/nuevo-admin")
    public String registrarNuevoAdmin(
            @Valid @ModelAttribute Usuario usuario,// Se valida automáticamente el objeto usuario
            BindingResult br) {
        if (!usuario.getContrasenia().equals(usuario.getConfirmPassword())) {//validar que las contrañas coincidan
            br.rejectValue("confirmPassword", "", "Las contraseñas no coinciden");
        }
        //si hay error con la validadcion regresa al form
        if (br.hasErrors()) {
            return "usuariosAdmin/form-admin";
        }
        try {
            usuarioService.registrarAdmin(usuario);//se intenta registrar el usuario
        } catch (Exception e) {
            br.rejectValue("email", "", e.getMessage());
            return "usuariosAdmin/form-admin";
        }
        //si hay error con el email se regresa al form
        return "redirect:/admin/usuarios?adminCreado";
    }

    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "usuariosAdmin/lista";
    }

    @GetMapping("/{id}")
    public String verPerfilUsuario(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id).orElse(null);
        if (usuario == null) return "redirect:/admin/usuarios?notfound";
        model.addAttribute("usuario", usuario);
        model.addAttribute("direcciones", direccionService.listarPorUsuarioEmail(usuario.getEmail()));
        model.addAttribute("ordenes", ordenService.listarPorUsuarioEmail(usuario.getEmail()));
        return "usuariosAdmin/perfil";
    }

    @GetMapping("/{id}/editar")
    public String editarPerfilUsuario(@PathVariable Long id, Model model) {//recibe un id
        Usuario usuario = usuarioService.buscarPorId(id).orElse(null);
        if (usuario == null) return "redirect:/admin/usuarios?notfound";//busca usuario con id
        model.addAttribute("usuario", usuario);//si existe muestr los daatos
        model.addAttribute("direcciones", direccionService.listarPorUsuarioEmail(usuario.getEmail()));
        model.addAttribute("ordenes", ordenService.listarPorUsuarioEmail(usuario.getEmail()));
        return "usuariosAdmin/editar";
    }

    @PostMapping("/{id}/editar")
    public String actualizarPerfilUsuario(@PathVariable Long id, @ModelAttribute Usuario usuario, RedirectAttributes redirectAttributes) {
        Usuario original = usuarioService.buscarPorId(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        // Si el admin quiere cambiar la contraseña
        if (usuario.getRawPassword() != null && !usuario.getRawPassword().isEmpty()) {
            // Validar confirmación
            if (!usuario.getRawPassword().equals(usuario.getConfirmPassword())) {
                redirectAttributes.addFlashAttribute("error", "Las contraseñas no coinciden");
                return "redirect:/admin/usuarios/" + id + "/editar";
            }
            // Codificar y asignar la nueva contraseña
            usuario.setContrasenia(passwordEncoder.encode(usuario.getRawPassword()));
        } else {
            // Mantener la contraseña actual
            usuario.setContrasenia(original.getContrasenia());
        }
        usuario.setId(id);
        usuarioService.guardar(usuario);
        redirectAttributes.addFlashAttribute("success", "Perfil actualizado con éxito");
        return "redirect:/admin/usuarios/" + id;
    }

    @PostMapping("/direcciones/{id}/eliminar")
    public String eliminarDireccion(@PathVariable Long id, @RequestParam(required = false) Long usuarioId, RedirectAttributes redirectAttributes) {
        direccionService.eliminar(id);
        redirectAttributes.addFlashAttribute("success", "Dirección eliminada correctamente");
        if (usuarioId != null) {
            return "redirect:/admin/usuarios/" + usuarioId + "/editar";
        }
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/ordenes/{id}/eliminar")
    public String eliminarOrden(@PathVariable Long id, @RequestParam(required = false) Long usuarioId, RedirectAttributes redirectAttributes) {
        ordenService.eliminar(id);
        redirectAttributes.addFlashAttribute("success", "Orden eliminada correctamente");
        if (usuarioId != null) {
            return "redirect:/admin/usuarios/" + usuarioId + "/editar";
        }
        return "redirect:/admin/usuarios";
    }
}