package mx.uv.daw.tienda.controller;

import jakarta.validation.Valid;
import mx.uv.daw.tienda.model.Usuario;
import mx.uv.daw.tienda.service.UsuarioService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/usuarios")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUsuarioController {

    private final UsuarioService usuarioService;

    public AdminUsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/nuevo-admin")
    public String mostrarFormularioNuevoAdmin(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuariosAdmin/form-admin";
    }

    @PostMapping("/nuevo-admin")
    public String registrarNuevoAdmin(
            @Valid @ModelAttribute Usuario usuario,
            BindingResult br) {
        if (!usuario.getContrasenia().equals(usuario.getConfirmPassword())) {
            br.rejectValue("confirmPassword", "", "Las contraseñas no coinciden");
        }
        if (br.hasErrors()) {
            return "usuariosAdmin/form-admin";
        }
        try {
            usuarioService.registrarAdmin(usuario);
        } catch (Exception e) {
            br.rejectValue("email", "", e.getMessage());
            return "usuariosAdmin/form-admin";
        }
        return "redirect:/admin/usuarios?adminCreado";
    }

    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "usuariosAdmin/lista";
    }
}