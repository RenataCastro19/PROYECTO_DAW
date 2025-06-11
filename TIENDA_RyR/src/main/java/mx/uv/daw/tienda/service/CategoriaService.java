package mx.uv.daw.tienda.service;

import mx.uv.daw.tienda.model.Categoria;
import mx.uv.daw.tienda.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
//NO NECEISTA INTERFAZ , PQ es un servicio más simple
@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }
     // Listar todas las categorías

    @Transactional(readOnly = true)
    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    /**
     * Buscar una categoría por su ID
     */
    @Transactional(readOnly = true)
    public Optional<Categoria> buscarPorId(Long id) {
        return categoriaRepository.findById(id);
    }

    /**
     * Crear o actualizar una categoría
     */
    @Transactional
    public Categoria guardar(Categoria categoria) {
        // 1. Busca si ya hay una categoría con ese nombre
        Optional<Categoria> existente = categoriaRepository.findByNombre(categoria.getNombre());
        if (existente.isPresent()
                && !existente.get().getId().equals(categoria.getId())) {
            // si existe y es distinta al registro actual manda el error
            throw new IllegalArgumentException("Ya existe una categoría con ese nombre.");
        }
        return categoriaRepository.save(categoria);
    }
}
