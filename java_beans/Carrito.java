import java.io.Serializable;

public class Carrito implements Serializable {
    private static final long serialVersionUID = 1L;

    private int rUsuario;
    private int rProducto;
    private int cantidad;
    private String estado;

    public Carrito() {}

    public Carrito(int rUsuario, int rProducto, int cantidad, String estado) {
        this.rUsuario = rUsuario;
        this.rProducto = rProducto;
        this.cantidad = cantidad;
        this.estado = estado;
    }

    public int getRUsuario() { return rUsuario; }
    public void setRUsuario(int rUsuario) { this.rUsuario = rUsuario; }

    public int getRProducto() { return rProducto; }
    public void setRProducto(int rProducto) { this.rProducto = rProducto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

}