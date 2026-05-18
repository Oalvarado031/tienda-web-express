package ni.com.tiendaexpress.service;

import ni.com.tiendaexpress.model.Producto;
import ni.com.tiendaexpress.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public List<Producto> destacados() {
        return productoRepository.findTop4ByOrderByIdDesc();
    }

    public List<Producto> filtrarPorCategoria(Long categoriaId) {
        if (categoriaId == null) {
            return listarTodos();
        }
        return productoRepository.findByCategoriaId(categoriaId);
    }

    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }
}
