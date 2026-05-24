package ni.com.tiendaexpress.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Representa un item del carrito de compras.
 * Guarda un snapshot de los datos del producto (no depende de la sesion de Hibernate).
 */
public class ItemCarrito implements Serializable {

    private Long productoId;
    private String nombre;
    private BigDecimal precio;
    private String imagen;
    private Integer cantidad;

    public ItemCarrito() {}

    public ItemCarrito(Producto producto, Integer cantidad) {
        this.productoId = producto.getId();
        this.nombre = producto.getNombre();
        this.precio = producto.getPrecio();
        this.imagen = producto.getImagen();
        this.cantidad = cantidad;
    }

    /** Subtotal calculado: precio unitario * cantidad */
    public BigDecimal getSubtotal() {
        if (precio == null || cantidad == null) return BigDecimal.ZERO;
        return precio.multiply(BigDecimal.valueOf(cantidad));
    }

    // === Getters & Setters ===

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemCarrito)) return false;
        ItemCarrito that = (ItemCarrito) o;
        return Objects.equals(productoId, that.productoId);
    }

    @Override
    public int hashCode() { return Objects.hash(productoId); }
}