package ni.com.tiendaexpress.controller;

import jakarta.validation.Valid;
import ni.com.tiendaexpress.dto.PedidoForm;
import ni.com.tiendaexpress.model.Pedido;
import ni.com.tiendaexpress.model.Producto;
import ni.com.tiendaexpress.service.CategoriaService;
import ni.com.tiendaexpress.service.PedidoService;
import ni.com.tiendaexpress.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class TiendaController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;
    private final PedidoService pedidoService;

    public TiendaController(ProductoService productoService,
                            CategoriaService categoriaService,
                            PedidoService pedidoService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
        this.pedidoService = pedidoService;
    }

    // === 1. PAGINA DE INICIO ===
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("titulo", "Boutique Express");
        model.addAttribute("destacados", productoService.destacados());
        return "index";
    }

    // === 2. CATALOGO DE PRODUCTOS ===
    @GetMapping("/catalogo")
    public String catalogo(@RequestParam(required = false) Long categoriaId, Model model) {
        model.addAttribute("titulo", "Catalogo de productos");
        model.addAttribute("categorias", categoriaService.listarTodas());
        model.addAttribute("productos", productoService.filtrarPorCategoria(categoriaId));
        model.addAttribute("categoriaSeleccionada", categoriaId);
        return "catalogo";
    }

    // === 3. DETALLE DEL PRODUCTO ===
    @GetMapping("/producto/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        Optional<Producto> productoOpt = productoService.buscarPorId(id);
        if (productoOpt.isEmpty()) {
            return "redirect:/catalogo";
        }
        model.addAttribute("titulo", "Detalle del producto");
        model.addAttribute("producto", productoOpt.get());
        return "detalle";
    }

    // === 5. FORMULARIO DE PEDIDO ===
    @GetMapping("/pedido")
    public String mostrarFormulario(@RequestParam(required = false) Long productoId, Model model) {
        PedidoForm form = new PedidoForm();
        if (productoId != null) {
            form.setProductoId(productoId);
            form.setCantidad(1);
        }
        model.addAttribute("titulo", "Solicitar pedido");
        model.addAttribute("pedidoForm", form);
        model.addAttribute("productos", productoService.listarTodos());
        return "pedido";
    }

    @PostMapping("/pedido")
    public String procesarPedido(@Valid @ModelAttribute("pedidoForm") PedidoForm pedidoForm,
                                 BindingResult bindingResult,
                                 Model model,
                                 RedirectAttributes redirect) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("titulo", "Solicitar pedido");
            model.addAttribute("productos", productoService.listarTodos());
            return "pedido";
        }

        Pedido pedido = pedidoService.crearPedido(pedidoForm);
        redirect.addFlashAttribute("mensaje",
                "Pedido #" + pedido.getId() + " creado correctamente. Total: C$" + pedido.getTotal());
        return "redirect:/pedido/confirmacion/" + pedido.getId();
    }

    @GetMapping("/pedido/confirmacion/{id}")
    public String confirmacion(@PathVariable Long id, Model model) {
        model.addAttribute("titulo", "Pedido confirmado");
        model.addAttribute("pedidoId", id);
        return "confirmacion";
    }
}
