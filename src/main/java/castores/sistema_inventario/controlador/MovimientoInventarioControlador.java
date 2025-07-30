package castores.sistema_inventario.controlador;

import castores.sistema_inventario.enums.TipoMovimiento;
import castores.sistema_inventario.modelo.MovimientoInventario;
import castores.sistema_inventario.modelo.Usuario;
import castores.sistema_inventario.servicio.InterfaceMovimientoInventarioServicio;
import castores.sistema_inventario.servicio.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("inventario-app/movimientos-inventario")
//@CrossOrigin(value = "http://localhost:4200") // Puerto por default de Angular
@CrossOrigin(origins = {"http://localhost:4200", "http://127.0.0.1:5500"})
public class MovimientoInventarioControlador {

    @Autowired
    private InterfaceMovimientoInventarioServicio servicio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @PostMapping
    public ResponseEntity<?> crearMovimiento(@RequestBody MovimientoInventario movimiento) {
        // Validaciones básicas antes de guardar
        if (movimiento.getCantidad() == null || movimiento.getCantidad() <= 0) {
            return ResponseEntity.badRequest().body(Map.of("error", "La cantidad debe ser mayor a cero"));
        }
        if (movimiento.getProducto() == null || movimiento.getProducto().getIdProducto() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "El producto es obligatorio"));
        }
        if (movimiento.getUsuario() == null || movimiento.getUsuario().getIdUsuario() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "El usuario es obligatorio"));
        }
        if (movimiento.getTipoMovimiento() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "El tipo de movimiento es obligatorio"));
        }

        // Asignar fecha/hora automáticamente
        movimiento.setFechaHora(java.time.LocalDateTime.now());

        try {
            MovimientoInventario creado = servicio.guardarMovimiento(movimiento);
            return ResponseEntity.ok(creado);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Error al crear el movimiento"));
        }
    }

    @GetMapping
    public ResponseEntity<?> listarTodos(@RequestParam Long usuarioId) {
        try {
            Usuario usuario = usuarioServicio.buscarPorId(usuarioId);

            if (usuario == null || !usuarioServicio.puedeVerHistorial(usuario)) {
                return ResponseEntity.status(403)
                        .body(Map.of("error", "No tienes permisos para ver el historial"));
            }

            List<MovimientoInventario> lista = servicio.obtenerTodos();
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error al obtener los movimientos"));
        }
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<?> listarPorTipo(@PathVariable TipoMovimiento tipo, @RequestParam Long usuarioId) {
        try {
            Usuario usuario = usuarioServicio.buscarPorId(usuarioId);

            if (usuario == null || !usuarioServicio.puedeVerHistorial(usuario)) {
                return ResponseEntity.status(403)
                        .body(Map.of("error", "No tienes permisos para ver el historial"));
            }

            List<MovimientoInventario> lista = servicio.obtenerPorTipo(tipo);
            return  ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error","Error al filtrar por tipo"));
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> listarPorUsuario(@PathVariable Long usuarioId) {
        try {
            List<MovimientoInventario> lista = servicio.obtenerPorUsuarioId(usuarioId);
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error al filtrar por usuario"));
        }
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<?> listarPorProducto(@PathVariable Long productoId) {
        try {
            List<MovimientoInventario> lista = servicio.obtenerPorProductoId(productoId);
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error al filtrar por producto"));
        }
    }
}