package mx.uv.daw.tienda.formatter;

import mx.uv.daw.tienda.model.Categoria;
import mx.uv.daw.tienda.service.CategoriaService;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

/**
 * Formatter personalizado para la entidad Categoria.
 * Permite convertir automáticamente entre el ID (String) recibido desde
 * un formulario HTML y la entidad Categoria correspondiente.
 */
@Component
public class CategoriaFormatter implements Formatter<Categoria> {

    /** Servicio para acceder a las operaciones de Categoria. */
    private final CategoriaService service;

    /**
     * Inyecta el servicio de categoria para poder buscar por ID.
     *
     * @param service servicio con lógica de negocio para Categoria
     */
    public CategoriaFormatter(CategoriaService service) {
        this.service = service;
    }

    /**
     * Convierte el texto (normalmente el valor de un <select> o input) a una
     * instancia de Categoria buscándola por su ID.
     *
     * @param text   el valor recibido en la petición (String que representa el ID)
     * @param locale la configuración regional (no usada aquí)
     * @return la Categoria correspondiente al ID, o null si el texto está vacío
     * @throws ParseException si no se encuentra ninguna Categoria con ese ID
     */
    @Override
    public Categoria parse(String text, Locale locale) throws ParseException {
        // Si el valor viene vacío, devolvemos null (campo opcional)
        if (text == null || text.isEmpty()) {
            return null;
        }
        // Intentamos buscar la Categoria por su ID
        return service.buscarPorId(Long.valueOf(text))
                .orElseThrow(() ->
                        new ParseException("Categoría no encontrada: " + text, 0)
                );
    }

    /**
     * Convierte una instancia de Categoria a su representación String (el ID).
     * Utilizado al renderizar formularios para rellenar automáticamente el valor.
     *
     * @param object la Categoria a convertir
     * @param locale la configuración regional (no usada aquí)
     * @return el ID de la Categoria como String, o cadena vacía si es null
     */
    @Override
    public String print(Categoria object, Locale locale) {
        // Si la categoría no es null, devolvemos su ID en formato String
        return (object != null) ? object.getId().toString() : "";
    }
}
