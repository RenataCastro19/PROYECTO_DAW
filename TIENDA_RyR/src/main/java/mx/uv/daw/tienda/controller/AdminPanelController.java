package mx.uv.daw.tienda.controller;

import jakarta.validation.Valid;
import mx.uv.daw.tienda.model.Usuario;
import mx.uv.daw.tienda.service.UsuarioService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BeanPropertyBindingResult;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')") // Solo usuarios con rol ADMIN pueden acceder a este controlador
public class AdminPanelController {

    private static final Logger logger = LoggerFactory.getLogger(AdminPanelController.class);
    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

    // Constructor con inyección de dependencias
    public AdminPanelController(UsuarioService usuarioService, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    // Mostrar la vista del panel de administración
    @GetMapping("/dashboard")
    public String mostrarPanel() {
        return "admin/dashboard";
    }

    // Mostrar formulario para editar perfil del administrador
    @GetMapping("/perfil/editar")
    public String mostrarFormularioEditar(Model model, Authentication auth) {
        String email = auth.getName(); // Obtener correo del usuario autenticado
        logger.info("Cargando perfil para editar del usuario: {}", email);

        // Buscar el usuario por correo
        Usuario usuario = usuarioService.buscarPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Almacenar la contraseña actual en un campo oculto
        String currentPassword = usuario.getContrasenia();

        // Limpiar campos sensibles antes de mostrar el formulario
        usuario.setContrasenia("");
        usuario.setConfirmPassword("");

        model.addAttribute("usuario", usuario);
        model.addAttribute("currentPassword", currentPassword);
        return "admin/perfil-editar";
    }

    // Procesar el formulario de edición de perfil
    @PostMapping("/perfil/editar")
    public String actualizarPerfil(
            @Valid Usuario usuario, // Validar los campos del usuario
            BindingResult result,   // Resultado de la validación
            Model model,
            @RequestParam("currentPassword") String currentPassword, // Contraseña actual oculta
            RedirectAttributes redirectAttributes, // Mensajes flash
            Authentication auth) {

        logger.info("Intentando actualizar perfil para usuario ID: {}", usuario.getId());

        // Verificar si se desea cambiar la contraseña
        boolean isChangingPassword = StringUtils.hasText(usuario.getRawPassword());

        if (!isChangingPassword) {
            // Si no cambia la contraseña, se mantiene la actual
            usuario.setContrasenia(currentPassword);
        } else {
            // Validar confirmación de la nueva contraseña
            if (!usuario.getRawPassword().equals(usuario.getConfirmPassword())) {
                result.rejectValue("confirmPassword", "", "Las contraseñas no coinciden");
                model.addAttribute("usuario", usuario);
                return "admin/perfil-editar";
            }
            // Codificar la nueva contraseña
            usuario.setContrasenia(passwordEncoder.encode(usuario.getRawPassword()));
        }

        // Verificar si hubo errores de validación
        if (result.hasErrors()) {
            logger.error("Errores de validación: {}", result.getAllErrors());
            model.addAttribute("usuario", usuario);
            return "admin/perfil-editar";
        }

        try {
            // Obtener usuario autenticado actual
            String email = auth.getName();
            Usuario usuarioActual = usuarioService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            logger.info("Usuario actual encontrado con ID: {}", usuarioActual.getId());

            // Conservar el rol del usuario
            usuario.setRolUsuario(usuarioActual.getRolUsuario());

            // Guardar cambios en el usuario
            usuarioService.guardar(usuario);
            logger.info("Perfil actualizado exitosamente para usuario ID: {}", usuario.getId());

            // Mostrar mensaje de éxito y redirigir al dashboard
            redirectAttributes.addFlashAttribute("success", "Perfil actualizado exitosamente");
            return "redirect:/admin/dashboard";
        } catch (Exception e) {
            logger.error("Error al actualizar perfil: {}", e.getMessage(), e);
            result.rejectValue("email", "", e.getMessage());
            model.addAttribute("usuario", usuario);
            return "admin/perfil-editar";
        }
    }
}
