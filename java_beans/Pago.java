import java.io.Serializable;
import java.sql.*;

public class Pago implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idPago;
    private int rOrden;
    private double montoTotal;
    private java.sql.Timestamp fechaPago;
    private String comprobantePago;
    private String estadoPago;

    public Pago() {}

    public Pago(int idPago, int rOrden, double montoTotal, java.sql.Timestamp fechaPago, String comprobantePago, String estadoPago) {
        this.idPago = idPago;
        this.rOrden = rOrden;
        this.montoTotal = montoTotal;
        this.fechaPago = fechaPago;
        this.comprobantePago = comprobantePago;
        this.estadoPago = estadoPago;
    }

    public int getIdPago() { return idPago; }
    public void setIdPago(int idPago) { this.idPago = idPago; }

    public int getROrden() { return rOrden; }
    public void setROrden(int rOrden) { this.rOrden = rOrden; }

    public double getMontoTotal() { return montoTotal; }
    public void setMontoTotal(double montoTotal) { this.montoTotal = montoTotal; }

    public java.sql.Timestamp getFechaPago() { return fechaPago; }
    public void setFechaPago(java.sql.Timestamp fechaPago) { this.fechaPago = fechaPago; }

    public String getComprobantePago() { return comprobantePago; }
    public void setComprobantePago(String comprobantePago) { this.comprobantePago = comprobantePago; }

    public String getEstadoPago() { return estadoPago; }
    public void setEstadoPago(String estadoPago) { this.estadoPago = estadoPago; }

}