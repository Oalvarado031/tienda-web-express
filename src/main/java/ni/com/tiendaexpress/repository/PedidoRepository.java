package ni.com.tiendaexpress.repository;

import ni.com.tiendaexpress.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
