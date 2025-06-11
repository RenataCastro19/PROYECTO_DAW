package mx.uv.daw.tienda.service;

import mx.uv.daw.tienda.model.Material;
import mx.uv.daw.tienda.repository.MaterialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MaterialService {

    private final MaterialRepository materialRepository;
    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    @Transactional(readOnly = true)
    public List<Material> listarTodas() {
        return materialRepository.findAll();
    }


    @Transactional(readOnly = true)
    public Optional<Material> buscarPorId(Long id) {
        return materialRepository.findById(id);//retorna Optional con el Material si existe, o vacío si no
    }


    @Transactional
    public Material guardar(Material material) {
        Optional<Material> existente = materialRepository.findByNombre(material.getNombre());
        if (existente.isPresent() && !existente.get().getId().equals(material.getId())) {
            throw new IllegalArgumentException("Ya existe un material con ese nombre.");
        }
        return materialRepository.save(material);//Material guardado con ID asignado
    }
}
