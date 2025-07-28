package castores.sistema_inventario.controlador;

import castores.sistema_inventario.enums.TipoMovimiento;
import castores.sistema_inventario.modelo.MovimientoInventario;
import castores.sistema_inventario.servicio.InterfaceMovimientoInventarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("inventario-app/movimientos-inventario")
@CrossOrigin(value = "http://localhost:4200") // Puerto por default de Angular
public class MovimientoInventarioControlador {

    @Autowired
    private InterfaceMovimientoInventarioServicio servicio;

    @PostMapping
    public ResponseEntity<?> crearMivimiento(@RequestBody MovimientoInventario movimiento) {
        try {
            MovimientoInventario creado = servicio.guardarMovimiento(movimiento);
            return ResponseEntity.ok(creado);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Error al crear el movimiento"));
        }
    }

    @GetMapping
    public ResponseEntity<?> listarTodos() {
        try {
            List<MovimientoInventario> lista = servicio.obtenerTodos();
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error al obtener los movimientos"));
        }
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<?> listarPorTipo(@PathVariable TipoMovimiento tipo) {
        try {
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
