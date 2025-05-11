package mx.uv.daw.tienda.formatter;

import mx.uv.daw.tienda.model.Material;
import mx.uv.daw.tienda.service.MaterialService;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

/**
 * Formatter personalizado para la entidad Material.
 * Convierte automáticamente entre el valor String recibido desde
 * formularios (normalmente un ID) y la instancia correspondiente de Material.
 */
@Component
public class MaterialFormatter implements Formatter<Material> {

    /** Servicio para acceder a la lógica de negocio de Material. */
    private final MaterialService service;

    /**
     * Construye el formateador inyectando el servicio de Material.
     *
     * @param service servicio que permite buscar materiales por ID
     */
    public MaterialFormatter(MaterialService service) {
        this.service = service;
    }

    /**
     * Parsea el texto recibido (ID de Material) a una entidad Material.
     *
     * @param text   cadena que representa el ID del Material
     * @param locale configuración regional (no se usa en este formateador)
     * @return la instancia de Material con el ID dado, o null si el texto está vacío
     * @throws ParseException si no se encuentra un Material con el ID proporcionado
     */
    @Override
    public Material parse(String text, Locale locale) throws ParseException {
        // Si no hay texto, devolvemos null (campo opcional)
        if (text == null || text.isEmpty()) {
            return null;
        }
        // Convertir el texto a Long e intentar recuperar el Material
        return service.buscarPorId(Long.valueOf(text))
                .orElseThrow(() ->
                        new ParseException("Material no encontrado: " + text, 0)
                );
    }

    /**
     * Convierte una instancia de Material a su representación String (el ID).
     *
     * @param object la instancia de Material a convertir
     * @param locale configuración regional (no se usa aquí)
     * @return el ID del Material como String, o cadena vacía si el objeto es null
     */
    @Override
    public String print(Material object, Locale locale) {
        // Si existe el objeto, devolvemos su ID, de lo contrario cadena vacía
        return (object != null) ? object.getId().toString() : "";
    }
}
