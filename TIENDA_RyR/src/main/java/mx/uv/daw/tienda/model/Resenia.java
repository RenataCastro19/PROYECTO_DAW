package mx.uv.daw.tienda.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "resenia")
public class Resenia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resenia")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "r_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "r_producto", nullable = false)
    private Producto producto;

    @Column(name = "calificacion", nullable = false)
    private Integer calificacion;

    @Column(name = "comentario")
    private String comentario;

    @Column(name = "fecha_resenia", nullable = false)
    private LocalDateTime fechaResenia;

    public Resenia() {}

    public Resenia(Usuario usuario, Producto producto, Integer calificacion, String comentario, LocalDateTime fechaResenia) {
        this.usuario = usuario;
        this.producto = producto;
        this.calificacion = calificacion;
        this.comentario = comentario;
        this.fechaResenia = fechaResenia;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Integer getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Integer calificacion) {
        this.calificacion = calificacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDateTime getFechaResenia() {
        return fechaResenia;
    }

    public void setFechaResenia(LocalDateTime fechaResenia) {
        this.fechaResenia = fechaResenia;
    }
}
