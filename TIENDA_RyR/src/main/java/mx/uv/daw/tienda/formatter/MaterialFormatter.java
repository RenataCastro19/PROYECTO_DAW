package mx.uv.daw.tienda.formatter;

import mx.uv.daw.tienda.model.Material;
import mx.uv.daw.tienda.service.MaterialService;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

//Convierte entre String (ID) y objeto Material en formularios HTML.
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
     * Convierte un ID (String) en un objeto Material.
     * Usado al recibir datos de un formulario .
     * parse significa analizar y convertir un texto en otro tipo de dato o estructura
     * throws indicar que un método puede lanzar una excepción (un error controlado)
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
     * Convierte un objeto Material en su ID (String).
     * Usado al mostrar el valor seleccionado en el formulario.
     */
    @Override
    public String print(Material object, Locale locale) {
        // Si existe el objeto, devolvemos su ID, de lo contrario cadena vacía
        return (object != null) ? object.getId().toString() : "";
    }
}
