/* =========================================================
   Tienda Web Express - app.js
   Funcionalidades:
   1) Filtro de productos por categoria
   2) Contador simple de productos seleccionados (carrito)
   3) Mostrar / ocultar descripcion
   4) Validacion basica de formulario
   5) Mensaje emergente (toast) al agregar producto
   ========================================================= */

document.addEventListener("DOMContentLoaded", () => {
    inicializarContadorCarrito();
    inicializarFiltroCategorias();
    inicializarBotonesAgregar();
    inicializarToggleDescripcion();
    inicializarValidacionFormulario();
});

/* ---------- 1) Contador de carrito (persistente en sesion) ---------- */
function obtenerCarrito() {
    try {
        return parseInt(sessionStorage.getItem("carritoCount") || "0", 10);
    } catch (e) { return 0; }
}

function guardarCarrito(valor) {
    try { sessionStorage.setItem("carritoCount", valor); } catch (e) { /* ignorar */ }
}

function inicializarContadorCarrito() {
    const span = document.getElementById("contadorCarrito");
    if (span) span.textContent = obtenerCarrito();
}

function incrementarCarrito() {
    const nuevo = obtenerCarrito() + 1;
    guardarCarrito(nuevo);
    const span = document.getElementById("contadorCarrito");
    if (span) {
        span.textContent = nuevo;
        span.style.transform = "scale(1.3)";
        setTimeout(() => { span.style.transform = "scale(1)"; }, 200);
    }
}

/* ---------- 2) Filtro por categoria ----------
   Filtra del lado del cliente sin recargar.
   Las tarjetas tienen data-categoria con el ID de la categoria.
*/
function inicializarFiltroCategorias() {
    const chips = document.querySelectorAll(".chip");
    const tarjetas = document.querySelectorAll(".grid-productos .tarjeta");

    if (chips.length === 0 || tarjetas.length === 0) return;

    chips.forEach(chip => {
        chip.addEventListener("click", () => {
            // Marcar chip activo
            chips.forEach(c => c.classList.remove("chip-activo"));
            chip.classList.add("chip-activo");

            const cat = chip.dataset.categoria;

            tarjetas.forEach(tarjeta => {
                if (cat === "todas" || tarjeta.dataset.categoria === cat) {
                    tarjeta.style.display = "";
                } else {
                    tarjeta.style.display = "none";
                }
            });
        });
    });
}

/* ---------- 3) Botones "Agregar al carrito" + Toast ---------- */
function inicializarBotonesAgregar() {
    const botones = document.querySelectorAll(".btn-agregar");
    botones.forEach(btn => {
        btn.addEventListener("click", (e) => {
            e.preventDefault();
            if (btn.disabled) return;
            const nombre = btn.dataset.nombre || "Producto";
            incrementarCarrito();
            mostrarToast(`Agregado: ${nombre}`);
        });
    });
}

function mostrarToast(mensaje) {
    let toast = document.getElementById("toast");
    if (!toast) {
        toast = document.createElement("div");
        toast.id = "toast";
        toast.className = "toast";
        document.body.appendChild(toast);
    }
    toast.textContent = mensaje;
    toast.classList.add("mostrar");
    clearTimeout(toast._timer);
    toast._timer = setTimeout(() => {
        toast.classList.remove("mostrar");
    }, 2500);
}

/* ---------- 4) Mostrar / ocultar descripcion (vista detalle) ---------- */
function inicializarToggleDescripcion() {
    const btn = document.getElementById("btnToggleDesc");
    const desc = document.getElementById("descripcionProducto");
    if (!btn || !desc) return;

    btn.addEventListener("click", () => {
        const oculta = desc.style.display === "none";
        desc.style.display = oculta ? "" : "none";
        btn.textContent = oculta ? "Ocultar descripcion" : "Mostrar descripcion";
    });
}

/* ---------- 5) Validacion del formulario de pedido ---------- */
function inicializarValidacionFormulario() {
    const form = document.getElementById("formPedido");
    if (!form) return;

    form.addEventListener("submit", (e) => {
        let valido = true;
        limpiarErrores(form);

        const nombre = form.querySelector("#nombreCliente");
        if (!nombre.value.trim() || nombre.value.trim().length < 2) {
            marcarError(nombre, "Ingresa tu nombre (minimo 2 caracteres)");
            valido = false;
        }

        const correo = form.querySelector("#correo");
        const regexCorreo = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!regexCorreo.test(correo.value.trim())) {
            marcarError(correo, "Ingresa un correo valido");
            valido = false;
        }

        const producto = form.querySelector("#productoId");
        if (!producto.value) {
            marcarError(producto, "Selecciona un producto");
            valido = false;
        }

        const cantidad = form.querySelector("#cantidad");
        const c = parseInt(cantidad.value, 10);
        if (isNaN(c) || c < 1 || c > 100) {
            marcarError(cantidad, "La cantidad debe estar entre 1 y 100");
            valido = false;
        }

        if (!valido) {
            e.preventDefault();
            mostrarToast("Revisa los campos del formulario");
        }
    });
}

function marcarError(input, mensaje) {
    input.classList.add("input-error");
    const small = document.querySelector(`[data-error-for="${input.id}"]`);
    if (small) small.textContent = mensaje;
}

function limpiarErrores(form) {
    form.querySelectorAll(".input-error").forEach(i => i.classList.remove("input-error"));
    form.querySelectorAll(".error-js").forEach(s => s.textContent = "");
}
