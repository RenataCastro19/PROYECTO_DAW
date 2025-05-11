package mx.uv.daw.tienda.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Entidad JPA que representa la tabla 'categoria' en la base de datos.
 * Cada instancia mapea una fila de la tabla con su identificador y nombre.
 */
@Entity
@Table(name = "categoria")
public class Categoria {

    /**
     * Clave primaria auto-generada para cada categoría.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Long id;

    /**
     * Nombre descriptivo de la categoría.
     * - No puede estar vacío ni nulo.
     * - Longitud máxima de 100 caracteres.
     * - Debe ser único en la tabla (constraint UNIQUE en BD).
     */
    @Column(
            name = "nombre_categoria",
            nullable = false,
            length = 100,
            unique = true
    )
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "Máximo 100 caracteres")
    private String nombre;

    /**
     * Constructor sin argumentos requerido por JPA.
     */
    public Categoria() {
    }

    /**
     * Constructor de conveniencia para crear una categoría con nombre.
     * @param nombre Nombre de la categoría
     */
    public Categoria(String nombre) {
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
