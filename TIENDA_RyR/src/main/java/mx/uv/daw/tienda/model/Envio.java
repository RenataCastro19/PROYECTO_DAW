package mx.uv.daw.tienda.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "envios")
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_envio")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "r_orden", nullable = false)
    private Orden orden;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "r_direccion", nullable = false)
    private Direccion direccion;

    // Enum en minúsculas
    public enum EstadoEnvio {
        pendiente,
        preparando,
        enviado,
        entregado
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoEnvio estado;

    @Column(name = "fecha_envio", nullable = false)
    private LocalDateTime fechaEnvio;

    @Column(name = "fecha_entrega_estimada")
    private LocalDate fechaEntregaEstimada;

    public Envio() {}

    public Envio(Orden orden, Direccion direccion, EstadoEnvio estado, LocalDateTime fechaEnvio, LocalDate fechaEntregaEstimada) {
        this.orden = orden;
        this.direccion = direccion;
        this.estado = estado;
        this.fechaEnvio = fechaEnvio;
        this.fechaEntregaEstimada = fechaEntregaEstimada;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Orden getOrden() {
        return orden;
    }
    public void setOrden(Orden orden) { this.orden = orden; }

    public Direccion getDireccion() { return direccion; }
    public void setDireccion(Direccion direccion) { this.direccion = direccion; }

    public EstadoEnvio getEstado() { return estado; }
    public void setEstado(EstadoEnvio estado) { this.estado = estado; }

    public LocalDateTime getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(LocalDateTime fechaEnvio) { this.fechaEnvio = fechaEnvio; }

    public LocalDate getFechaEntregaEstimada() { return fechaEntregaEstimada; }
    public void setFechaEntregaEstimada(LocalDate fechaEntregaEstimada) { this.fechaEntregaEstimada = fechaEntregaEstimada; }
}