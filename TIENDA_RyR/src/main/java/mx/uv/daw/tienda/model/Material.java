package mx.uv.daw.tienda.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Entidad JPA que representa el catálogo de materiales en la base de datos.
 * Cada instancia se mapea a una fila de la tabla 'material'.
 */
@Entity
@Table(name = "material")
public class Material {

    /**
     * Identificador único del material. Clave primaria generada automáticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_material")
    private Long id;

    /**
     * Nombre descriptivo del material.
     * - No puede estar vacío.
     * - Máximo 100 caracteres.
     * - Debe ser único en la tabla (constraint UNIQUE en BD).
     */
    @Column(
            name     = "nombre_material",
            nullable = false,
            length   = 100,
            unique   = true
    )
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "Máximo 100 caracteres")
    private String nombre;

    /**
     * Constructor sin argumentos requerido por JPA.
     */
    public Material() {
    }

    /**
     * Constructor de conveniencia para instanciar un material con nombre.
     *
     * @param nombre Nombre del material
     */
    public Material(String nombre) {
        this.nombre = nombre;
    }

    // Getters y setters
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
}
