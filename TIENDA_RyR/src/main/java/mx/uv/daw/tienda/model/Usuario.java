package mx.uv.daw.tienda.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

// Interface para validación condicional de contraseña
interface PasswordValidationGroup {}

@Entity
@Table(
        name = "usuario",
        uniqueConstraints = @UniqueConstraint(columnNames = "email")
)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "El apellido paterno es obligatorio")
    @Size(max = 100, message = "El apellido paterno no puede exceder los 100 caracteres")
    @Column(name = "ap_pat", nullable = false, length = 100)
    private String apPat;

    @NotBlank(message = "El apellido materno es obligatorio")
    @Size(max = 100, message = "El apellido materno no puede exceder los 100 caracteres")
    @Column(name = "ap_mat", nullable = false, length = 100)
    private String apMat;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    @Size(max = 100, message = "El email no puede exceder los 100 caracteres")
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "contrasenia", nullable = false)
    private String contrasenia;

    @Transient
    @NotBlank(message = "La contraseña es obligatoria", groups = PasswordValidationGroup.class)
    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres", groups = PasswordValidationGroup.class)
    private String rawPassword;

    @Transient
    @NotBlank(message = "Debe confirmar la contraseña", groups = PasswordValidationGroup.class)
    @Size(min = 6, max = 100, message = "La confirmación de contraseña debe tener entre 6 y 100 caracteres", groups = PasswordValidationGroup.class)
    private String confirmPassword;

    @Column(name = "rol_usuario", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private RolUsuario rolUsuario;

    public Usuario() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApPat() { return apPat; }
    public void setApPat(String apPat) { this.apPat = apPat; }

    public String getApMat() { return apMat; }
    public void setApMat(String apMat) { this.apMat = apMat; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContrasenia() { return contrasenia; }
    public void setContrasenia(String contrasenia) { this.contrasenia = contrasenia; }

    public String getRawPassword() { return rawPassword; }
    public void setRawPassword(String rawPassword) {
        this.rawPassword = rawPassword;
        if (StringUtils.hasText(rawPassword)) {
            this.contrasenia = rawPassword;
        }
    }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public RolUsuario getRolUsuario() { return rolUsuario; }
    public void setRolUsuario(RolUsuario rolUsuario) {
        this.rolUsuario = rolUsuario;
    }
}
