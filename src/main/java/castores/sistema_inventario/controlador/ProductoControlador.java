package castores.sistema_inventario.controlador;

import castores.sistema_inventario.enums.TipoMovimiento;
import castores.sistema_inventario.modelo.MovimientoInventario;
import castores.sistema_inventario.modelo.Producto;
import castores.sistema_inventario.modelo.Usuario;
import castores.sistema_inventario.servicio.InterfaceMovimientoInventarioServicio;
import castores.sistema_inventario.servicio.ProductoServicio;
import castores.sistema_inventario.servicio.UsuarioServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("inventario-app") //http://localhost:8080/inventario-app
//@CrossOrigin(value = "http://localhost:4200") // Puerto por default de Angular
@CrossOrigin(origins = {"http://localhost:4200", "http://127.0.0.1:5500"})
public class ProductoControlador {
    private static final Logger logger = LoggerFactory.getLogger(ProductoControlador.class);

    @Autowired
    private ProductoServicio productoServicio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private InterfaceMovimientoInventarioServicio movimientoServicio;

    // REQUERIMIENTO: Ver modulo inventario - CUALQUIER USUARIO
    @GetMapping("/productos") //http:localhost:8080/inventario-app/productos
    public ResponseEntity<?> obtenerProductos(@RequestParam Long usuarioId) {
        try {
            Usuario usuario = usuarioServicio.buscarPorId(usuarioId);

            if (usuario == null || !usuarioServicio.puedeVerInventario(usuario)) {
                return ResponseEntity.status(403)
                        .body(Map.of("error", "No tienes permiso para ver el inventario"));
            }

            List<Producto> productos = productoServicio.listarProductos();
            logger.info("Inventario consultado por el usuario: {}", usuario.getNombre());
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            logger.error("Error al obtener productos: ", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }

    //REQUERIMIENTO: Agregar un nuevo producto - ADMINISTRADOR
    @PostMapping("/productos")
    public ResponseEntity<?> agregarProducto(@RequestBody Producto producto, @RequestParam Long usuarioId) {
        try {
            Usuario usuario = usuarioServicio.buscarPorId(usuarioId);

            if (usuario == null || !usuarioServicio.puedeAgregarProductos(usuario)) {
                return ResponseEntity.status(403)
                        .body(Map.of("error", "No tienes permiso para agregar productos"));
            }

            if (producto.getTalla() == null || producto.getColor() == null || producto.getTipo() == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Talla, color y tipo son obligatorios"));
            }

            if (producto.getPrecio() == null || producto.getPrecio().doubleValue() <= 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "El precio debe ser un valor positivo"));
            }

            Producto nuevoProducto = productoServicio.agreggarNuevoProducto(producto);
            logger.info("Producto agregado: {} por el usuario: {}", nuevoProducto.getNombre(), usuario.getNombre());
            return ResponseEntity.ok(Map.of("mensaje", "Producto agregado exitosamente", "producto", nuevoProducto));

        } catch (Exception e) {
            logger.error("Error al agregar producto: ", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }

    // REQUERIMIENTO: Aumentar inventario - ADMINISTRADOR
    @PutMapping("/productos/{id}/entrada")
    public ResponseEntity<?> aumentarInventario(@PathVariable Long id, @RequestParam int cantidad, @RequestParam Long usuarioId) {
        try {
            Usuario usuario = usuarioServicio.buscarPorId(usuarioId);

            if (usuario == null || !usuarioServicio.puedeAumentarInventario(usuario)) {
                return ResponseEntity.status(403)
                        .body(Map.of("error", "No tienes permiso para aumentar el inventario"));
            }

            Producto producto = productoServicio.buscarProductosPorId(id);
            if (producto == null) {
                return ResponseEntity.notFound().build();
            }

            //REQUERIMIENTO: No esta permitido disminuir la cantidad de un producto
            if (cantidad <= 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "No esta permitido disminuir el inventario"));
            }

            boolean exito = productoServicio.aumentarInventario(id, cantidad, usuarioId);

            if (exito) {
                Producto productoActualizado = productoServicio.buscarProductosPorId(id);
                logger.info("Inventario aumentado: {} unidades de {} por usuario {}", cantidad, producto.getNombre(), usuario.getNombre());

                return ResponseEntity.ok(Map.of(
                        "mensaje", "Inventario aumentado exitosamente",
                        "producto", productoActualizado,
                        "cantidadActual", cantidad
                ));
            } else {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Error al aumentar inventario"));
            }
        } catch (Exception e) {
            logger.error("Error al aumentar inventario: ", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error interno del servidor"));
        }

    }

    // REQUERIMIENTO: Dar de baja/reactivar producto - ADMINISTRADOR
    @PutMapping("/productos/{id}/baja")
    public ResponseEntity<?> darBajaProducto(@PathVariable Long id, @RequestParam Long usuarioId) {
        try {
            Usuario usuario = usuarioServicio.buscarPorId(usuarioId);

            if (usuario == null || !usuarioServicio.puedeDarDeBajaReactivar(usuario)) {
                return ResponseEntity.status(403)
                        .body(Map.of("error", "No tienes permisos para dar de baja o reactivar productos"));
            }

            Producto producto = productoServicio.buscarProductosPorId(id);

            if (producto == null) {
                return ResponseEntity.notFound().build();
            }

            boolean exito = productoServicio.darBajaProducto(id);

            if (exito) {
                logger.info("Producto dado de baja: {} por el usuario: {}", producto.getNombre(), usuario.getNombre());
                return ResponseEntity.ok(Map.of("mensaje", "Producto dado de baja exitosamente",
                        "producto", productoServicio.buscarProductosPorId(id)));
            } else {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Error al dar de baja el producto"));
            }

        } catch (Exception e) {
            logger.error("Error al dar de baja el producto: ", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }

    @PutMapping("/productos/{id}/reactivar")
    public ResponseEntity<?> reactivarProducto(@PathVariable Long id, @RequestParam Long usuarioId) {
        try {
            Usuario usuario = usuarioServicio.buscarPorId(usuarioId);

            if (usuario == null || !usuarioServicio.puedeDarDeBajaReactivar(usuario)) {
                return ResponseEntity.status(403)
                        .body(Map.of("error", "No tienes permisos para reactivar productos"));
            }

            Producto producto = productoServicio.buscarProductosPorId(id);

            if (producto == null) {
                return ResponseEntity.notFound().build();
            }

            boolean exito = productoServicio.activarProducto(id);

            if (exito) {
                logger.info("Producto reactivado: {} por el usuario: {}", producto.getNombre(), usuario.getNombre());
                return ResponseEntity.ok(Map.of(
                        "mensaje", "Producto reactivado exitosamente",
                        "producto", productoServicio.buscarProductosPorId(id)));
            } else {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Error al reactivar el producto"));
            }

        } catch (Exception e) {
            logger.error("Error al reactivar el producto: ", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }

    //REQUERIMIENTO: Ver productos activos e inactivos
    @GetMapping("/productos/activos")
    public ResponseEntity<?> obtenerProductosActivos(@RequestParam Long usuarioId) {
        try {
            Usuario usuario = usuarioServicio.buscarPorId(usuarioId);

            if (usuario == null || !usuarioServicio.puedeVerInventario(usuario)) {
                return ResponseEntity.status(403)
                        .body(Map.of("error", "No tienes permisos para ver productos activos"));
            }

            List<Producto> productosActivos = productoServicio.listarProductosActivos();
            logger.info("Productos activos consultados por el usuario: {}", usuario.getNombre());
            return ResponseEntity.ok(productosActivos);
        }catch (Exception e) {
            logger.error("Error al obtener productos activos: ", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }

    @GetMapping("/productos/inactivos")
    public ResponseEntity<?> obtenerProductosInactivos(@RequestParam Long usuarioId) {
        try {
            Usuario usuario = usuarioServicio.buscarPorId(usuarioId);

            if (usuario == null || !usuarioServicio.puedeVerInventario(usuario)) {
                return ResponseEntity.status(403)
                        .body(Map.of("error", "No tienes permisos para ver productos inactivos"));
            }

            List<Producto> productosInactivos = productoServicio.listarProductosInactivos();
            logger.info("Productos inactivos consultados por el usuario: {}", usuario.getNombre());
            return ResponseEntity.ok(productosInactivos);
        } catch(Exception e) {
            logger.error("Error al obtener productos inactivos: ", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error","Error interno del servidor"));
        }
    }

    // REQUERIMIENTO: Solo poder ver los productos activos (SALIDA) - ALMACENISTA
    @GetMapping("/productos/salidas")
    public ResponseEntity<?> obtenerProductosParaSalida(@RequestParam Long usuarioId) {
        try {
            Usuario usuario = usuarioServicio.buscarPorId(usuarioId);

            if (usuario == null || !usuarioServicio.puedeVerModuloSalida(usuario)) {
                return ResponseEntity.status(403)
                        .body(Map.of("error","No tienes permisos ver modulo de salidas"));
            }

            List<Producto> productosParaSalida = productoServicio.listarProductosActivosConStock();
            logger.info("Productos para salida consultador por el usuario: {}", usuario.getNombre());
            return ResponseEntity.ok(productosParaSalida);
        }catch (Exception e) {
            logger.error("Error al obtener productos para salida: ", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }

    // REQUERIMIENTO: Sacar un producto del inventario - ALMACENISTA
    @PutMapping("/productos/{id}/salida")
    public ResponseEntity<?> sacarInventario(@PathVariable Long id, @RequestParam int cantidad, @RequestParam Long usuarioId) {
        try {
            Usuario usuario = usuarioServicio.buscarPorId(usuarioId);

            if (usuario == null || !usuarioServicio.puedeSacarInventario(usuario)) {
                return ResponseEntity.status(403)
                        .body(Map.of("error", "No tienes permiso para sacar inventario"));
            }

            Producto producto = productoServicio.buscarProductosPorId(id);

            if (producto == null) {
                return ResponseEntity.notFound().build();
            }

            if (!producto.getActivo()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error","No es posible sacar inventario de un producto inactivo"));
            }

            if (cantidad <= 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "La cantidad debe ser mayor a 0"));
            }

            // REQUERIMIENTO: No es posible sacar una cantidad mayor a la disponible, mensaje de error
            if  (!productoServicio.validarStockDisponible(id, cantidad)) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "No hay suficiente stock disponible. Stock actual: " + producto.getCantidadActual()));
            }

            boolean exito = productoServicio.reducirInventario(id, cantidad, usuarioId);

            if (exito) {
                Producto productoActualizado = productoServicio.buscarProductosPorId(id);
                logger.info("Salida de inventario: {} unidades de {} por el usuario: {}", cantidad, producto.getNombre(), usuario.getNombre());

                return ResponseEntity.ok(Map.of("mensaje", "Inventario reducido exitosamente",
                        "producto", productoActualizado,
                        "CantidadRetirada", cantidad));
            } else {
                return ResponseEntity.badRequest()
                        .body(Map.of("error","Error al reducir inventario"));
            }
        } catch(Exception e) {
            logger.error("Error en salida de inventario", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }

    @PutMapping("/productos/{id}/estatus")
    public ResponseEntity<?> cambiarEstatusProducto(@PathVariable Long id, @RequestParam Long usuarioId) {
        try {
            Usuario usuario = usuarioServicio.buscarPorId(usuarioId);

            if (usuario == null || !usuarioServicio.puedeDarDeBajaReactivar(usuario)) {
                return ResponseEntity.status(403)
                        .body(Map.of("error", "No tienes permisos para cambiar el estatus del producto"));
            }

            Producto producto = productoServicio.buscarProductosPorId(id);
            if (producto == null) {
                return ResponseEntity.notFound().build();
            }

            boolean exito = productoServicio.cambiarEstatusProducto(id);

            if (exito) {
                logger.info("Estatus cambiado para producto: {} por el usuario: {}", producto.getNombre(), usuario.getNombre());
                return ResponseEntity.ok(Map.of(
                        "mensaje", "Estatus del producto cambiado exitosamente",
                        "producto", productoServicio.buscarProductosPorId(id)
                ));
            } else {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Error al cambiar el estatus del producto"));
            }
        } catch (Exception e) {
            logger.error("Error al cambiar estatus del producto: ", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }
}