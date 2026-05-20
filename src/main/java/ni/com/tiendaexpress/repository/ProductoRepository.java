package ni.com.tiendaexpress.repository;

import ni.com.tiendaexpress.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // === Queries derivadas ===

    List<Producto> findByCategoriaId(Long categoriaId);

    List<Producto> findTop4ByOrderByIdDesc();

    // Busqueda por nombre: ignora mayusculas/minusculas y acepta coincidencias parciales
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Consulta personalizada con JPQL que combina busqueda por nombre y filtro por categoria.
     * - Si 'nombre' es null o vacio, no se filtra por nombre.
     * - Si 'categoriaId' es null, no se filtra por categoria.
     * - Si ambos son null/vacio, retorna todos los productos.
     */
    @Query("SELECT p FROM Producto p WHERE " +
            "(:nombre IS NULL OR :nombre = '' OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) " +
            "AND (:categoriaId IS NULL OR p.categoria.id = :categoriaId) " +
            "ORDER BY p.id ASC")
    List<Producto> buscarPorNombreYCategoria(
            @Param("nombre") String nombre,
            @Param("categoriaId") Long categoriaId
    );
}