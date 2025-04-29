import java.io.Serializable;
import java.sql.*;

public class Envio implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idEnvio;
    private int rOrden;
    private int rDireccion;
    private String estado;
    private java.sql.Timestamp fechaEnvio;
    private java.sql.Date fechaEntregaEstimada;

    public Envio() {}

    public Envio(int idEnvio, int rOrden, int rDireccion, String estado, java.sql.Timestamp fechaEnvio, java.sql.Date fechaEntregaEstimada) {
        this.idEnvio = idEnvio;
        this.rOrden = rOrden;
        this.rDireccion = rDireccion;
        this.estado = estado;
        this.fechaEnvio = fechaEnvio;
        this.fechaEntregaEstimada = fechaEntregaEstimada;
    }

    public int getIdEnvio() { return idEnvio; }
    public void setIdEnvio(int idEnvio) { this.idEnvio = idEnvio; }

    public int getROrden() { return rOrden; }
    public void setROrden(int rOrden) { this.rOrden = rOrden; }

    public int getRDireccion() { return rDireccion; }
    public void setRDireccion(int rDireccion) { this.rDireccion = rDireccion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public java.sql.Timestamp getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(java.sql.Timestamp fechaEnvio) { this.fechaEnvio = fechaEnvio; }

    public java.sql.Date getFechaEntregaEstimada() { return fechaEntregaEstimada; }
    public void setFechaEntregaEstimada(java.sql.Date fechaEntregaEstimada) { this.fechaEntregaEstimada = fechaEntregaEstimada; }

}