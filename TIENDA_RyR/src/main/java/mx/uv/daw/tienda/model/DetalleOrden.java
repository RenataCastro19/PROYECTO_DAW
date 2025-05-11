package mx.uv.daw.tienda.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "detalle_orden")

public class DetalleOrden {

    @Embeddable
    public static class DetalleOrdenId implements Serializable {

        @Column(name = "r_orden")
        private Long ordenId;

        @Column(name = "r_producto")
        private Long productoId;

        public DetalleOrdenId() {}

        public DetalleOrdenId(Long ordenId, Long productoId) {
            this.ordenId = ordenId;
            this.productoId = productoId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DetalleOrdenId that = (DetalleOrdenId) o;
            return Objects.equals(ordenId, that.ordenId) &&
                    Objects.equals(productoId, that.productoId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(ordenId, productoId);
        }

        // getters and setters
        public Long getOrdenId() { return ordenId; }
        public void setOrdenId(Long ordenId) { this.ordenId = ordenId; }
        public Long getProductoId() { return productoId; }
        public void setProductoId(Long productoId) { this.productoId = productoId; }
    }

    @EmbeddedId
    private DetalleOrdenId id;

    @MapsId("ordenId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "r_orden", nullable = false)
    private Orden orden;

    @MapsId("productoId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "r_producto", nullable = false)
    private Producto producto;

    @Column(name = "cantidad_items", nullable = false)
    private Integer cantidadItems;

    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    public DetalleOrden() {}

    public DetalleOrden(Orden orden, Producto producto, Integer cantidadItems, BigDecimal precioUnitario) {
        this.id = new DetalleOrdenId(orden.getId(), producto.getId());
        this.orden = orden;
        this.producto = producto;
        this.cantidadItems = cantidadItems;
        this.precioUnitario = precioUnitario;
    }

    // getters y setters
    public DetalleOrdenId getId() { return id; }
    public void setId(DetalleOrdenId id) { this.id = id; }

    public Orden getOrden() { return orden; }
    public void setOrden(Orden orden) { this.orden = orden; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public Integer getCantidadItems() { return cantidadItems; }
    public void setCantidadItems(Integer cantidadItems) { this.cantidadItems = cantidadItems; }

    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
}
