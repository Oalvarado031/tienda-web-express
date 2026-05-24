package ni.com.tiendaexpress.service;

import ni.com.tiendaexpress.model.*;
import ni.com.tiendaexpress.repository.PedidoRepository;
import ni.com.tiendaexpress.repository.ProductoRepository;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.SessionScope;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Servicio del carrito de compras.
 * @SessionScope: cada usuario tiene su propio carrito que vive durante su sesion HTTP.
 */
@Service
@SessionScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CarritoService implements Serializable {

    private final ProductoRepository productoRepository;
    private final PedidoRepository pedidoRepository;

    // Map<productoId, ItemCarrito> para evitar duplicados
    private final Map<Long, ItemCarrito> items = new LinkedHashMap<>();

    public CarritoService(ProductoRepository productoRepository,
                          PedidoRepository pedidoRepository) {
        this.productoRepository = productoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    /** Agrega un producto al carrito; si ya existe, suma la cantidad. */
    public void agregar(Long productoId, int cantidad) {
        if (cantidad <= 0) cantidad = 1;

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + productoId));

        ItemCarrito existente = items.get(productoId);
        if (existente != null) {
            existente.setCantidad(existente.getCantidad() + cantidad);
        } else {
            items.put(productoId, new ItemCarrito(producto, cantidad));
        }
    }

    /** Cambia la cantidad de un item (minimo 1). */
    public void actualizarCantidad(Long productoId, int cantidad) {
        ItemCarrito item = items.get(productoId);
        if (item != null) {
            item.setCantidad(Math.max(1, cantidad));
        }
    }

    /** Elimina un item del carrito. */
    public void eliminar(Long productoId) {
        items.remove(productoId);
    }

    /** Vacia todo el carrito. */
    public void vaciar() {
        items.clear();
    }

    public List<ItemCarrito> getItems() {
        return new ArrayList<>(items.values());
    }

    public boolean estaVacio() {
        return items.isEmpty();
    }

    /** Cantidad total de unidades (para el badge del navbar). */
    public int getCantidadTotal() {
        return items.values().stream()
                .mapToInt(ItemCarrito::getCantidad)
                .sum();
    }

    /** Total general del carrito (suma de todos los subtotales). */
    public BigDecimal getTotal() {
        return items.values().stream()
                .map(ItemCarrito::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Finaliza el pedido: persiste en BD (pedido + detalle_pedido) y vacia el carrito.
     * Usa datos genericos de cliente.
     */
    @Transactional
    public Pedido finalizarPedido() {
        if (items.isEmpty()) {
            throw new IllegalStateException("El carrito esta vacio");
        }

        Pedido pedido = new Pedido();
        pedido.setNombreCliente("Cliente Boutique Express");
        pedido.setCorreo("cliente@boutique.express");
        pedido.setComentario("Pedido generado desde el carrito de compras");
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setTotal(getTotal());

        // Crear un DetallePedido por cada item
        for (ItemCarrito item : items.values()) {
            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new IllegalStateException(
                            "Producto del carrito no existe: " + item.getProductoId()));

            DetallePedido detalle = new DetallePedido();
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(item.getPrecio());
            detalle.setSubtotal(item.getSubtotal());

            pedido.agregarDetalle(detalle);
        }

        Pedido guardado = pedidoRepository.save(pedido);
        vaciar();
        return guardado;
    }
}