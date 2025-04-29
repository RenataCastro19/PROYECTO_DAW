import java.io.Serializable;

public class Producto implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idProducto;
    private String nombreProducto;
    private String descripcionProducto;
    private double precioProducto;
    private String imagenProducto;
    private int stock;
    private int rMaterial;
    private int rCategoria;

    public Producto() {}

    public Producto(int idProducto, String nombreProducto, String descripcionProducto, double precioProducto, String imagenProducto, int stock, int rMaterial, int rCategoria) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.descripcionProducto = descripcionProducto;
        this.precioProducto = precioProducto;
        this.imagenProducto = imagenProducto;
        this.stock = stock;
        this.rMaterial = rMaterial;
        this.rCategoria = rCategoria;
    }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public String getDescripcionProducto() { return descripcionProducto; }
    public void setDescripcionProducto(String descripcionProducto) { this.descripcionProducto = descripcionProducto; }

    public double getPrecioProducto() { return precioProducto; }
    public void setPrecioProducto(double precioProducto) { this.precioProducto = precioProducto; }

    public String getImagenProducto() { return imagenProducto; }
    public void setImagenProducto(String imagenProducto) { this.imagenProducto = imagenProducto; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public int getRMaterial() { return rMaterial; }
    public void setRMaterial(int rMaterial) { this.rMaterial = rMaterial; }

    public int getRCategoria() { return rCategoria; }
    public void setRCategoria(int rCategoria) { this.rCategoria = rCategoria; }

}