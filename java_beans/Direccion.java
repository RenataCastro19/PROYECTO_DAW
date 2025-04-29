import java.io.Serializable;

public class Direccion implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idDireccion;
    private int rUsuario;
    private String calle;
    private String ciudad;
    private String estado;
    private String codigoPostal;

    public Direccion() {}

    public Direccion(int idDireccion, int rUsuario, String calle, String ciudad, String estado, String codigoPostal) {
        this.idDireccion = idDireccion;
        this.rUsuario = rUsuario;
        this.calle = calle;
        this.ciudad = ciudad;
        this.estado = estado;
        this.codigoPostal = codigoPostal;
    }

    public int getIdDireccion() { return idDireccion; }
    public void setIdDireccion(int idDireccion) { this.idDireccion = idDireccion; }

    public int getRUsuario() { return rUsuario; }
    public void setRUsuario(int rUsuario) { this.rUsuario = rUsuario; }

    public String getCalle() { return calle; }
    public void setCalle(String calle) { this.calle = calle; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getCodigoPostal() { return codigoPostal; }
    public void setCodigoPostal(String codigoPostal) { this.codigoPostal = codigoPostal; }

}