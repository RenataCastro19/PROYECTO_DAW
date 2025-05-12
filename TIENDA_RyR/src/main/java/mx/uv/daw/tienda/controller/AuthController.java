package mx.uv.daw.tienda.controller;

import jakarta.validation.Valid;
import mx.uv.daw.tienda.model.Usuario;
import mx.uv.daw.tienda.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuario/register";
    }


    @PostMapping("/register")
    public String doRegister(
            @Valid @ModelAttribute("usuario") Usuario usuario,
            BindingResult br) {
        if (!usuario.getContrasenia().equals(usuario.getConfirmPassword())) {
            br.rejectValue("confirmPassword", "", "Las contraseñas no coinciden");
        }
        if (br.hasErrors()) {
            return "usuario/register";
        }
        try {
            usuarioService.registrarCliente(usuario);
        } catch (Exception e) {
            br.rejectValue("email", "", e.getMessage());
            return "usuario/register";
        }
        return "redirect:/login?registered";
    }

    @GetMapping("/login")
    public String showLogin(
            @RequestParam(value = "error",      required = false) String error,
            @RequestParam(value = "logout",     required = false) String logout,
            @RequestParam(value = "registered", required = false) String registered,
            Model model) {

        if (error     != null) model.addAttribute("error",      "Credenciales inválidas");
        if (logout    != null) model.addAttribute("logout",     "Has cerrado sesión");
        if (registered!= null) model.addAttribute("registered", "Registro exitoso, inicia sesión");

        return "usuario/login";
    }
}
