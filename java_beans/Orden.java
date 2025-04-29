import java.io.Serializable;
import java.sql.*;

public class Orden implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idOrden;
    private int rUsuario;
    private java.sql.Timestamp fecha;
    private double totalOrden;
    private int rUsuarioModifico;
    private java.sql.Timestamp fechaModificacion;

    public Orden() {}

    public Orden(int idOrden, int rUsuario, java.sql.Timestamp fecha, double totalOrden, int rUsuarioModifico, java.sql.Timestamp fechaModificacion) {
        this.idOrden = idOrden;
        this.rUsuario = rUsuario;
        this.fecha = fecha;
        this.totalOrden = totalOrden;
        this.rUsuarioModifico = rUsuarioModifico;
        this.fechaModificacion = fechaModificacion;
    }

    public int getIdOrden() { return idOrden; }
    public void setIdOrden(int idOrden) { this.idOrden = idOrden; }

    public int getRUsuario() { return rUsuario; }
    public void setRUsuario(int rUsuario) { this.rUsuario = rUsuario; }

    public java.sql.Timestamp getFecha() { return fecha; }
    public void setFecha(java.sql.Timestamp fecha) { this.fecha = fecha; }

    public double getTotalOrden() { return totalOrden; }
    public void setTotalOrden(double totalOrden) { this.totalOrden = totalOrden; }

    public int getRUsuarioModifico() { return rUsuarioModifico; }
    public void setRUsuarioModifico(int rUsuarioModifico) { this.rUsuarioModifico = rUsuarioModifico; }

    public java.sql.Timestamp getFechaModificacion() { return fechaModificacion; }
    public void setFechaModificacion(java.sql.Timestamp fechaModificacion) { this.fechaModificacion = fechaModificacion; }

}