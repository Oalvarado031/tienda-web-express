package ni.com.tiendaexpress.service;

import ni.com.tiendaexpress.dto.PedidoForm;
import ni.com.tiendaexpress.model.DetallePedido;
import ni.com.tiendaexpress.model.Pedido;
import ni.com.tiendaexpress.model.Producto;
import ni.com.tiendaexpress.repository.PedidoRepository;
import ni.com.tiendaexpress.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;

    public PedidoService(PedidoRepository pedidoRepository, ProductoRepository productoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
    }

    /**
     * Crea un pedido a partir del formulario, calcula subtotal y total,
     * y guarda todo en cascada (pedido + detalle_pedido).
     */
    @Transactional
    public Pedido crearPedido(PedidoForm form) {
        Producto producto = productoRepository.findById(form.getProductoId())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + form.getProductoId()));

        BigDecimal precioUnitario = producto.getPrecio();
        BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(form.getCantidad()));

        Pedido pedido = new Pedido();
        pedido.setNombreCliente(form.getNombreCliente());
        pedido.setCorreo(form.getCorreo());
        pedido.setComentario(form.getComentario());
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setTotal(subtotal);

        DetallePedido detalle = new DetallePedido();
        detalle.setProducto(producto);
        detalle.setCantidad(form.getCantidad());
        detalle.setPrecioUnitario(precioUnitario);
        detalle.setSubtotal(subtotal);

        pedido.agregarDetalle(detalle);

        return pedidoRepository.save(pedido);
    }
}
