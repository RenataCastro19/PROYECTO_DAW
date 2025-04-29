import java.io.Serializable;

public class DetalleOrden implements Serializable {
    private static final long serialVersionUID = 1L;

    private int rOrden;
    private int rProducto;
    private int cantidadItems;
    private double precioUnitario;

    public DetalleOrden() {}

    public DetalleOrden(int rOrden, int rProducto, int cantidadItems, double precioUnitario) {
        this.rOrden = rOrden;
        this.rProducto = rProducto;
        this.cantidadItems = cantidadItems;
        this.precioUnitario = precioUnitario;
    }

    public int getROrden() { return rOrden; }
    public void setROrden(int rOrden) { this.rOrden = rOrden; }

    public int getRProducto() { return rProducto; }
    public void setRProducto(int rProducto) { this.rProducto = rProducto; }

    public int getCantidadItems() { return cantidadItems; }
    public void setCantidadItems(int cantidadItems) { this.cantidadItems = cantidadItems; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

}