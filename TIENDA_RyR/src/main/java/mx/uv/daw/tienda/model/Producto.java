package mx.uv.daw.tienda.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;

/**
 * Entidad JPA que representa la tabla 'producto' en la base de datos.
 * Cada objeto Producto mapea una fila con sus atributos y relaciones a Material y Categoria.
 */
@Entity
@Table(
        name = "producto",
        uniqueConstraints = @UniqueConstraint(columnNames = "nombre_producto")
)
public class Producto {

    /**
     * Identificador único del producto, auto-generado.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long id;

    /**
     * Nombre del producto.
     * - Obligatorio (no blank).
     * - Longitud máxima 100 caracteres.
     * - Debe ser único en la tabla.
     */
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "Máximo 100 caracteres")
    @Column(
            name     = "nombre_producto",
            nullable = false,
            length   = 100,
            unique   = true
    )
    private String nombre;

    /**
     * Descripción detallada del producto.
     * - Obligatoria (no blank).
     * - Longitud máxima 500 caracteres.
     */
    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 500, message = "Máximo 500 caracteres")
    @Column(
            name     = "descripcion_producto",
            nullable = false,
            length   = 500
    )
    private String descripcion;

    /**
     * Precio del producto.
     * - Obligatorio (no null).
     * - Mínimo 0.00.
     * - Hasta 8 dígitos enteros y 2 decimales.
     */
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.00", inclusive = true, message = "El precio no puede ser negativo")
    @Digits(integer = 8, fraction = 2, message = "Formato inválido para el precio")
    @Column(
            name      = "precio_producto",
            nullable  = false,
            precision = 10,
            scale     = 2
    )
    private BigDecimal precio;

    /**
     * URL de la imagen del producto.
     * - Obligatoria.
     * - Debe ser una URL válida (http://… o https://…).
     */
    @NotBlank(message = "La URL de la imagen es obligatoria")
    @URL(protocol = "https", message = "La URL debe comenzar con https")
    @Column(name = "imagen_producto", nullable = false, length = 255)
    private String imagen;

    /**
     * Cantidad en stock disponible.
     * - Obligatorio (no null).
     * - Mínimo 0.
     */
    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(name = "stock", nullable = false)
    private Integer stock;

    /**
     * Relación Many-to-One con la entidad Material. Obligatoria.
     */
    @NotNull(message = "El material es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "r_material", nullable = false)
    private Material material;

    /**
     * Relación Many-to-One con la entidad Categoria. Obligatoria.
     */
    @NotNull(message = "La categoría es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "r_categoria", nullable = false)
    private Categoria categoria;

    /**
     * Constructor sin argumentos requerido por JPA.
     */
    public Producto() {
    }

    /**
     * Constructor completo para crear instancias con todos los atributos.
     *
     * @param nombre       Nombre del producto
     * @param descripcion  Descripción del producto
     * @param precio       Precio del producto
     * @param imagen       Ruta/URL de la imagen
     * @param stock        Cantidad en stock
     * @param material     Material asociado
     * @param categoria    Categoría asociada
     */
    public Producto(String nombre,
                    String descripcion,
                    BigDecimal precio,
                    String imagen,
                    Integer stock,
                    Material material,
                    Categoria categoria) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagen = imagen;
        this.stock = stock;
        this.material = material;
        this.categoria = categoria;
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
