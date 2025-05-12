package mx.uv.daw.tienda.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "carrito")
public class Carrito {

    @Embeddable
    public static class CarritoId implements Serializable {

        @Column(name = "r_usuario")
        private Long usuarioId;

        @Column(name = "r_producto")
        private Long productoId;

        public CarritoId() {}

        public CarritoId(Long usuarioId, Long productoId) {
            this.usuarioId = usuarioId;
            this.productoId = productoId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CarritoId that = (CarritoId) o;
            return Objects.equals(usuarioId, that.usuarioId) &&
                    Objects.equals(productoId, that.productoId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(usuarioId, productoId);
        }

        public Long getUsuarioId() { return usuarioId; }
        public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
        public Long getProductoId() { return productoId; }
        public void setProductoId(Long productoId) { this.productoId = productoId; }
    }

    @EmbeddedId
    private CarritoId id;

    @MapsId("usuarioId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "r_usuario", nullable = false)
    private Usuario usuario;

    @MapsId("productoId")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "r_producto", nullable = false)
    private Producto producto;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoCarrito estado;

    public Carrito() {}

    public Carrito(Usuario usuario, Producto producto, Integer cantidad, EstadoCarrito estado) {
        this.id = new CarritoId(usuario.getId(), producto.getId());
        this.usuario = usuario;
        this.producto = producto;
        this.cantidad = cantidad;
        this.estado = estado;
    }

    public CarritoId getId() { return id; }
    public void setId(CarritoId id) { this.id = id; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public EstadoCarrito getEstado() { return estado; }
    public void setEstado(EstadoCarrito estado) { this.estado = estado; }

    public enum EstadoCarrito {
        activo,
        comprado
    }
}
