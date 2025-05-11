package mx.uv.daw.tienda.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

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

    @Column(name = "nombre",     nullable = false, length = 100)
    private String nombre;

    @Column(name = "ap_pat",     nullable = false, length = 100)
    private String apPat;

    @Column(name = "ap_mat",     nullable = false, length = 100)
    private String apMat;

    @Column(name = "email",      nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "contrasenia", nullable = false)
    private String contrasenia;

    @Column(name = "rol_usuario", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private RolUsuario rolUsuario;

    public Usuario() { }

    // getters y setters

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApPat() {
        return apPat;
    }
    public void setApPat(String apPat) {
        this.apPat = apPat;
    }

    public String getApMat() {
        return apMat;
    }
    public void setApMat(String apMat) {
        this.apMat = apMat;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasenia() {
        return contrasenia;
    }
    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public RolUsuario getRolUsuario() {
        return rolUsuario;
    }
    public void setRolUsuario(RolUsuario rolUsuario) {
        this.rolUsuario = rolUsuario;
    }
}
