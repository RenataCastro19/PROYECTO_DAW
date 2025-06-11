package mx.uv.daw.tienda.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "r_orden", nullable = false)
    private Orden orden;

    @Column(name = "monto_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoTotal;

    @Column(name = "fecha_pago", nullable = false)
    private LocalDateTime fechaPago;

    @Column(name = "comprobante_pago")
    private String comprobantePago;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_pago", nullable = false, length = 20)
    private EstadoPago estadoPago;

    public Pago() {}

    public Pago(Orden orden, BigDecimal montoTotal, LocalDateTime fechaPago, String comprobantePago, EstadoPago estadoPago) {
        this.orden = orden;
        this.montoTotal = montoTotal;
        this.fechaPago = fechaPago;
        this.comprobantePago = comprobantePago;
        this.estadoPago = estadoPago;
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Orden getOrden() { return orden; }
    public void setOrden(Orden orden) { this.orden = orden; }

    public BigDecimal getMontoTotal() { return montoTotal; }
    public void setMontoTotal(BigDecimal montoTotal) { this.montoTotal = montoTotal; }

    public LocalDateTime getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDateTime fechaPago) { this.fechaPago = fechaPago; }

    public String getComprobantePago() { return comprobantePago; }
    public void setComprobantePago(String comprobantePago) { this.comprobantePago = comprobantePago; }

    public EstadoPago getEstadoPago() { return estadoPago; }
    public void setEstadoPago(EstadoPago estadoPago) { this.estadoPago = estadoPago; }

    public enum EstadoPago {
        pendiente,
        confirmado,
        rechazado
    }
}
