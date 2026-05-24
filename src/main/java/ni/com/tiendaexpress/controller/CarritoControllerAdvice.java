package ni.com.tiendaexpress.controller;

import ni.com.tiendaexpress.service.CarritoService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Inyecta el numero de items del carrito en TODAS las vistas automaticamente.
 * Asi el navbar puede mostrar el badge sincronizado en cualquier pagina.
 */
@ControllerAdvice
public class CarritoControllerAdvice {

    private final CarritoService carritoService;

    public CarritoControllerAdvice(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @ModelAttribute("totalItemsCarrito")
    public int totalItemsCarrito() {
        return carritoService.getCantidadTotal();
    }
}