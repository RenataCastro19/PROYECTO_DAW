package mx.uv.daw.tienda.formatter;

import mx.uv.daw.tienda.model.Categoria;
import mx.uv.daw.tienda.service.CategoriaService;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;
// ayuda a enlazar datos de formularios HTML con objetos de tu modelo
/**
 * Formatter personalizado para la entidad Categoria.
 * Permite convertir automáticamente entre el ID (String) recibido desde
 * un formulario HTML y la entidad Categoria correspondiente.
 */
@Component
public class CategoriaFormatter implements Formatter<Categoria> {

    // Servicio para acceder a las operaciones de Categoria.
    private final CategoriaService service;

    public CategoriaFormatter(CategoriaService service) {
        this.service = service;
    }

    /**
     * Convierte un ID (String) a un objeto Categoria.
     * Usado cuando se envía un formulario con un <select>.
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
     * Convierte un objeto Categoria a su ID en texto.
     * Usado para mostrar el valor seleccionado en un formulario.
     */

    @Override
    public String print(Categoria object, Locale locale) {
        // Si la categoría no es null, devolvemos su ID en formato String
        return (object != null) ? object.getId().toString() : "";
    }
}
