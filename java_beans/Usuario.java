import java.io.Serializable;

public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idUsuario;
    private String nombre;
    private String apPat;
    private String apMat;
    private String calle;
    private String ciudad;
    private String estado;
    private String codigoPostal;
    private String email;
    private String contrasenia;
    private String rolUsuario;

    public Usuario() {}

    public Usuario(int idUsuario, String nombre, String apPat, String apMat, String calle, String ciudad, String estado, String codigoPostal, String email, String contrasenia, String rolUsuario) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apPat = apPat;
        this.apMat = apMat;
        this.calle = calle;
        this.ciudad = ciudad;
        this.estado = estado;
        this.codigoPostal = codigoPostal;
        this.email = email;
        this.contrasenia = contrasenia;
        this.rolUsuario = rolUsuario;
    }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApPat() { return apPat; }
    public void setApPat(String apPat) { this.apPat = apPat; }

    public String getApMat() { return apMat; }
    public void setApMat(String apMat) { this.apMat = apMat; }

    public String getCalle() { return calle; }
    public void setCalle(String calle) { this.calle = calle; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getCodigoPostal() { return codigoPostal; }
    public void setCodigoPostal(String codigoPostal) { this.codigoPostal = codigoPostal; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContrasenia() { return contrasenia; }
    public void setContrasenia(String contrasenia) { this.contrasenia = contrasenia; }

    public String getRolUsuario() { return rolUsuario; }
    public void setRolUsuario(String rolUsuario) { this.rolUsuario = rolUsuario; }

}