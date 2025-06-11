package mx.uv.daw.tienda.controller;

import jakarta.validation.Valid;
import mx.uv.daw.tienda.model.Usuario;
import mx.uv.daw.tienda.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//controlador que reguistra nuevos usuarios y muestra la vista del login
@Controller//indica que esta clase es un controlador web para manejar las peticiones http
public class AuthController {

    //Logger para registrar eventos y errores durante la autenticación.
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    //muestra formulario de registroexpli
    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("usuario", new Usuario());//crea objeto usuario
        return "usuario/register";//retorna la vista
    }
    // metodo procesar registro, incia cuando se envia form de registro
    @PostMapping("/register")
    public String doRegister(
            @Valid @ModelAttribute("usuario") Usuario usuario,//validacion de usuario
            BindingResult br) {
        logger.info("Intentando registrar usuario con correo: {}", usuario.getEmail());

        if (!usuario.getContrasenia().equals(usuario.getConfirmPassword())) { //valida contraseñas
            logger.warn("Las contraseñas no coinciden para el usuario: {}", usuario.getEmail());
            br.rejectValue("confirmPassword", "", "Las contraseñas no coinciden");
        }
        if (br.hasErrors()) {
            logger.warn("Las contraseñas no coinciden: {}", br.getAllErrors());
            return "usuario/register";
        }
        try {//intenta registra el usuario
            logger.info("Llamando a usuarioService.registrarCliente para el usuario: {}", usuario.getEmail());
            usuarioService.registrarCliente(usuario);
            logger.info("Usuario registrado exitosamente: {}", usuario.getEmail());
        } catch (Exception e) {
            logger.error("Error al registrar el usuario: {}", e.getMessage(), e);
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
