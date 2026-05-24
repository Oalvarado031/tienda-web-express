package ni.com.tiendaexpress.controller;

import ni.com.tiendaexpress.model.Pedido;
import ni.com.tiendaexpress.service.CarritoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    /** Mostrar el carrito */
    @GetMapping
    public String verCarrito(Model model) {
        model.addAttribute("titulo", "Mi carrito");
        model.addAttribute("items", carritoService.getItems());
        model.addAttribute("total", carritoService.getTotal());
        model.addAttribute("estaVacio", carritoService.estaVacio());
        return "carrito";
    }

    /** Agregar producto al carrito */
    @PostMapping("/agregar")
    public String agregar(@RequestParam Long productoId,
                          @RequestParam(defaultValue = "1") int cantidad,
                          @RequestParam(required = false) String volverA,
                          RedirectAttributes redirect) {
        try {
            carritoService.agregar(productoId, cantidad);
            redirect.addFlashAttribute("mensajeCarrito", "Producto agregado al carrito");
        } catch (IllegalArgumentException ex) {
            redirect.addFlashAttribute("errorCarrito", ex.getMessage());
        }
        // Redirigir a la URL anterior si se proporciono; si no, ir al carrito
        return "redirect:" + (volverA != null && !volverA.isBlank() ? volverA : "/carrito");
    }

    /** Actualizar cantidad de un item */
    @PostMapping("/actualizar")
    public String actualizar(@RequestParam Long productoId,
                             @RequestParam int cantidad) {
        carritoService.actualizarCantidad(productoId, cantidad);
        return "redirect:/carrito";
    }

    /** Eliminar un producto del carrito */
    @PostMapping("/eliminar/{productoId}")
    public String eliminar(@PathVariable Long productoId,
                           RedirectAttributes redirect) {
        carritoService.eliminar(productoId);
        redirect.addFlashAttribute("mensajeCarrito", "Producto eliminado del carrito");
        return "redirect:/carrito";
    }

    /** Vaciar todo el carrito */
    @PostMapping("/vaciar")
    public String vaciar(RedirectAttributes redirect) {
        carritoService.vaciar();
        redirect.addFlashAttribute("mensajeCarrito", "Carrito vaciado");
        return "redirect:/carrito";
    }

    /** Finalizar el pedido: guarda en BD y limpia el carrito */
    @PostMapping("/finalizar")
    public String finalizar(RedirectAttributes redirect) {
        try {
            Pedido pedido = carritoService.finalizarPedido();
            redirect.addFlashAttribute("mensaje",
                    "Pedido #" + pedido.getId() + " confirmado. Total: C$ " + pedido.getTotal());
            return "redirect:/pedido/confirmacion/" + pedido.getId();
        } catch (IllegalStateException ex) {
            redirect.addFlashAttribute("errorCarrito", ex.getMessage());
            return "redirect:/carrito";
        }
    }
}