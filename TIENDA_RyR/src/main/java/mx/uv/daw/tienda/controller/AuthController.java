package mx.uv.daw.tienda.controller;

import jakarta.validation.Valid;
import mx.uv.daw.tienda.dto.RegistroForm;
import mx.uv.daw.tienda.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador encargado de manejar la autenticación y el registro de usuarios.
 * Proporciona endpoints para mostrar formularios de login y registro,
 * así como para procesar los datos enviados por el usuario.
 */
@Controller
public class AuthController {

    private final UsuarioService usuarioService;

    /**
     * Inyecta el servicio de usuarios para delegar la lógica de negocio.
     *
     * @param usuarioService servicio que maneja operaciones de usuario
     */
    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * GET /register
     * Muestra la página de registro con un objeto RegistroForm vacío.
     *
     * @param model modelo usado por Thymeleaf para renderizar la vista
     * @return nombre de la plantilla Thymeleaf para el registro
     */
    @GetMapping("/register")
    public String showRegister(Model model) {
        // Agrega un DTO para enlazar los datos del formulario
        model.addAttribute("form", new RegistroForm());
        return "usuario/register";
    }

    /**
     * POST /register
     * Procesa el formulario de registro; valida campos y maneja errores.
     *
     * @param form DTO con los datos ingresados
     * @param br resultado de la validación de Bean Validation
     * @return redirección o re-render de la vista de registro en caso de errores
     */
    @PostMapping("/register")
    public String doRegister(
            @Valid @ModelAttribute("form") RegistroForm form,
            BindingResult br) {
        // Si hay errores de validación (longitud, formato, etc.), mostrar form
        if (br.hasErrors()) {
            return "usuario/register";
        }
        try {
            // Llama al servicio para crear el usuario; puede lanzar excepción
            usuarioService.registrarCliente(form);
        } catch (Exception e) {
            // Rechaza el valor del email con el mensaje devuelto por el servicio
            br.rejectValue("email", "", e.getMessage());
            return "usuario/register";
        }
        // Si todo va bien, redirige al login con flag 'registered'
        return "redirect:/login?registered";
    }

    /**
     * GET /login
     * Muestra la página de login y procesa posibles parámetros de estado.
     *
     * @param error     mensaje de error por credenciales inválidas
     * @param logout    mensaje de éxito tras logout
     * @param registered mensaje de confirmación de registro
     * @param model     modelo usado en la vista
     * @return nombre de la plantilla Thymeleaf para login
     */
    @GetMapping("/login")
    public String showLogin(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            @RequestParam(value = "registered", required = false) String registered,
            Model model) {

        // Agrega flags al modelo para mostrar alertas condicionales
        if (error != null)      model.addAttribute("error", "Credenciales inválidas");
        if (logout != null)     model.addAttribute("logout", "Has cerrado sesión");
        if (registered != null) model.addAttribute("registered", "Registro exitoso, inicia sesión");

        return "usuario/login";
    }
}