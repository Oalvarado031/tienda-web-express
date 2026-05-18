-- ============================================================
-- Tienda Web Express - Script de creacion de BD PostgreSQL
-- Ejecutar UNA SOLA VEZ antes de levantar la aplicacion.
-- ============================================================

-- Si necesitas crear la BD desde cero:
-- CREATE DATABASE tienda_web_express
--     WITH OWNER = postgres ENCODING = 'UTF8' CONNECTION LIMIT = -1;

-- Conectarse a la BD tienda_web_express antes de ejecutar lo siguiente.

DROP TABLE IF EXISTS detalle_pedido;
DROP TABLE IF EXISTS pedido;
DROP TABLE IF EXISTS producto;
DROP TABLE IF EXISTS categoria;

CREATE TABLE categoria (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE producto (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    precio NUMERIC(10,2) NOT NULL CHECK (precio >= 0),
    imagen VARCHAR(255),
    stock INTEGER NOT NULL DEFAULT 0 CHECK (stock >= 0),
    categoria_id BIGINT NOT NULL,
    CONSTRAINT fk_producto_categoria
        FOREIGN KEY (categoria_id) REFERENCES categoria(id)
        ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE pedido (
    id BIGSERIAL PRIMARY KEY,
    nombre_cliente VARCHAR(150) NOT NULL,
    correo VARCHAR(150) NOT NULL,
    comentario TEXT,
    fecha_pedido TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total NUMERIC(10,2) NOT NULL DEFAULT 0 CHECK (total >= 0)
);

CREATE TABLE detalle_pedido (
    id BIGSERIAL PRIMARY KEY,
    pedido_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    cantidad INTEGER NOT NULL CHECK (cantidad > 0),
    precio_unitario NUMERIC(10,2) NOT NULL CHECK (precio_unitario >= 0),
    subtotal NUMERIC(10,2) NOT NULL CHECK (subtotal >= 0),
    CONSTRAINT fk_detalle_pedido FOREIGN KEY (pedido_id) REFERENCES pedido(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_detalle_producto FOREIGN KEY (producto_id) REFERENCES producto(id)
        ON DELETE RESTRICT ON UPDATE CASCADE
);

-- Datos iniciales (tematica boutique)
INSERT INTO categoria (nombre) VALUES
('Vestidos'),
('Blusas'),
('Pantalones'),
('Accesorios');

INSERT INTO producto (nombre, descripcion, precio, imagen, stock, categoria_id) VALUES
('Vestido floral verano', 'Vestido midi con estampado floral, tela fresca ideal para clima calido. Disponible en tallas S, M, L.', 850.00, 'vestido-floral.jpg', 12, 1),
('Vestido negro elegante', 'Pieza atemporal para ocasiones especiales. Corte favorecedor y tela de alta calidad.', 1200.00, 'vestido-negro.jpg', 8, 1),
('Blusa de seda', 'Blusa suelta en seda satinada, perfecta para combinar con jeans o falda.', 550.00, 'blusa-seda.jpg', 20, 2),
('Blusa bordada artesanal', 'Blusa de algodon con bordados a mano. Hecha por artesanas nicaraguenses.', 480.00, 'blusa-bordada.jpg', 15, 2),
('Jeans tiro alto', 'Pantalon de mezclilla con tiro alto, corte recto y bolsillos funcionales.', 720.00, 'jeans.jpg', 18, 3),
('Pantalon palazzo', 'Pantalon ancho de tela fluida, comodo y elegante para el dia a dia.', 650.00, 'palazzo.jpg', 10, 3),
('Bolso de mano cuero', 'Bolso pequeno de cuero genuino con asa de cadena dorada.', 950.00, 'bolso.jpg', 6, 4),
('Pañuelo de seda', 'Pañuelo cuadrado de seda con estampado exclusivo. Versatil para multiples ocasiones.', 320.00, 'panuelo.jpg', 25, 4);
