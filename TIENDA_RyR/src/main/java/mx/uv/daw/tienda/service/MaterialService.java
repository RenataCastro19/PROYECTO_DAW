package mx.uv.daw.tienda.service;

import mx.uv.daw.tienda.model.Material;
import mx.uv.daw.tienda.repository.MaterialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio que encapsula la lógica de negocio relacionada con la entidad Material.
 * Gestiona operaciones de lectura, creación, actualización y eliminación.
 */
@Service
public class MaterialService {

    private final MaterialRepository materialRepository;

    /**
     * Inyección de dependencia del repositorio de Material.
     *
     * @param materialRepository repositorio JPA para Material
     */
    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    /**
     * Obtiene la lista de todos los materiales.
     * Transacción de solo lectura para optimización.
     *
     * @return lista de Material
     */
    @Transactional(readOnly = true)
    public List<Material> listarTodas() {
        return materialRepository.findAll();
    }

    /**
     * Busca un material por su identificador.
     * Transacción de solo lectura para optimización.
     *
     * @param id identificador del material
     * @return Optional con el Material si existe, o vacío si no
     */
    @Transactional(readOnly = true)
    public Optional<Material> buscarPorId(Long id) {
        return materialRepository.findById(id);
    }

    /**
     * Crea o actualiza un material.
     * Valida que no exista otro material con el mismo nombre.
     *
     * @param material objeto Material a guardar
     * @return Material guardado con ID asignado
     * @throws IllegalArgumentException si ya existe un material con el mismo nombre
     */
    @Transactional
    public Material guardar(Material material) {
        Optional<Material> existente = materialRepository.findByNombre(material.getNombre());
        if (existente.isPresent() && !existente.get().getId().equals(material.getId())) {
            throw new IllegalArgumentException("Ya existe un material con ese nombre.");
        }
        return materialRepository.save(material);
    }
}
