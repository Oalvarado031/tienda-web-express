package ni.com.tiendaexpress.repository;

import ni.com.tiendaexpress.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
