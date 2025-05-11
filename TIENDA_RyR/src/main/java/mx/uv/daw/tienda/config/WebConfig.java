package mx.uv.daw.tienda.config;

import mx.uv.daw.tienda.formatter.MaterialFormatter;
import mx.uv.daw.tienda.formatter.CategoriaFormatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración MVC de Spring para registrar formateadores personalizados.
 * Los formateadores permiten convertir automáticamente entre cadenas (IDs en formularios)
 * y objetos de dominio (Material y Categoria) al procesar peticiones.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Formatter para convertir IDs de Material en instancias de Material
    private final MaterialFormatter materialFormatter;
    // Formatter para convertir IDs de Categoria en instancias de Categoria
    private final CategoriaFormatter categoriaFormatter;

    /**
     * Constructor inyecta los formateadores personalizados.
     *
     * @param mf formateador de Material
     * @param cf formateador de Categoria
     */
    public WebConfig(MaterialFormatter mf, CategoriaFormatter cf) {
        this.materialFormatter  = mf;
        this.categoriaFormatter = cf;
    }

    /**
     * Se ejecuta durante el arranque de Spring MVC. Aquí registramos
     * nuestros formateadores en el registry para que Spring MVC los use
     * al enlazar datos de formularios a objetos.
     *
     * @param registry el registro de formateadores de Spring MVC
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        // Registramos materialFormatter para campos de tipo Material
        registry.addFormatter(materialFormatter);
        // Registramos categoriaFormatter para campos de tipo Categoria
        registry.addFormatter(categoriaFormatter);
    }
}
