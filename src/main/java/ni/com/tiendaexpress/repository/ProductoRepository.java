package ni.com.tiendaexpress.repository;

import ni.com.tiendaexpress.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Spring Data genera la consulta automaticamente por el nombre del metodo
    List<Producto> findByCategoriaId(Long categoriaId);

    List<Producto> findTop4ByOrderByIdDesc();
}
