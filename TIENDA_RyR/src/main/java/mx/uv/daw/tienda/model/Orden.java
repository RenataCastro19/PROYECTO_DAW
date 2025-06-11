package mx.uv.daw.tienda.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orden")
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_orden")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "r_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "total_orden", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalOrden;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "r_usuario_modifico") // puede quedar NULL
    private Usuario usuarioModifico;

    @Column(name = "fecha_modificacion", nullable = false)
    private LocalDateTime fechaModificacion;

    @OneToOne(mappedBy = "orden", fetch = FetchType.LAZY)
    private Envio envio;

    public Orden() {
    }

    public Orden(Usuario usuario, LocalDateTime fecha, BigDecimal totalOrden, Usuario usuarioModifico, LocalDateTime fechaModificacion) {
        this.usuario = usuario;
        this.fecha = fecha;
        this.totalOrden = totalOrden;
        this.usuarioModifico = usuarioModifico;
        this.fechaModificacion = fechaModificacion;
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

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getTotalOrden() {
        return totalOrden;
    }

    public void setTotalOrden(BigDecimal totalOrden) {
        this.totalOrden = totalOrden;
    }

    public Usuario getUsuarioModifico() {
        return usuarioModifico;
    }

    public void setUsuarioModifico(Usuario usuarioModifico) {
        this.usuarioModifico = usuarioModifico;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Envio getEnvio() {
        return envio;
    }

    public void setEnvio(Envio envio) {
        this.envio = envio;
    }
}
