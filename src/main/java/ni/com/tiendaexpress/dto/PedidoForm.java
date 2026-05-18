package ni.com.tiendaexpress.dto;

import jakarta.validation.constraints.*;

/**
 * Objeto de transferencia para el formulario de pedido.
 * Separa los datos del formulario de la entidad Pedido.
 */
public class PedidoForm {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 150, message = "El nombre debe tener entre 2 y 150 caracteres")
    private String nombreCliente;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe ingresar un correo valido")
    private String correo;

    @NotNull(message = "Debe seleccionar un producto")
    private Long productoId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad minima es 1")
    @Max(value = 100, message = "La cantidad maxima es 100")
    private Integer cantidad;

    @Size(max = 500, message = "El comentario no debe exceder 500 caracteres")
    private String comentario;

    public PedidoForm() {}

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
}
