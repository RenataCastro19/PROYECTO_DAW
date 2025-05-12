package mx.uv.daw.tienda.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

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

    @NotBlank @Size(max = 100)
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @NotBlank @Size(max = 100)
    @Column(name = "ap_pat", nullable = false, length = 100)
    private String apPat;

    @NotBlank @Size(max = 100)
    @Column(name = "ap_mat", nullable = false, length = 100)
    private String apMat;

    @NotBlank @Email @Size(max = 100)
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @NotBlank @Size(min = 6, max = 100)
    @Column(name = "contrasenia", nullable = false)
    private String contrasenia;

    /** campo transitorio para confirmar contraseña en el formulario **/
    @Transient
    @NotBlank @Size(min = 6, max = 100)
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

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public RolUsuario getRolUsuario() { return rolUsuario; }
    public void setRolUsuario(RolUsuario rolUsuario) {
        this.rolUsuario = rolUsuario;
    }
}
