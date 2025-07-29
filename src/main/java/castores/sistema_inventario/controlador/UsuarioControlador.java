package castores.sistema_inventario.controlador;

import castores.sistema_inventario.modelo.Usuario;
import castores.sistema_inventario.servicio.UsuarioServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/inventario-app")
//@CrossOrigin(value = "http://localhost:4200") // Puerto por default de Angular
@CrossOrigin(origins = {"http://localhost:4200", "http://127.0.0.1:5500"})
public class UsuarioControlador {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioControlador.class);

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/usuario/{id}")
    public ResponseEntity<?> obtenerUsuario(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioServicio.buscarPorId(id);

            if (usuario == null) {
                return  ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(Map.of(
                    "id", usuario.getIdUsuario(),
                    "nombre", usuario.getNombre(),
                    "correo", usuario.getCorreo(),
                    "rol", usuario.getRol().toString(),
                    "estatus", usuario.getEstatus()
            ));
        }catch (Exception e) {
            logger.error("Error al obtener usuario: ", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }

    // ENPOINT para verificar permisos
    @GetMapping("/usuario/{id}/permisos")
    public ResponseEntity<?> obtenerPermisos(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioServicio.buscarPorId(id);

            if (usuario == null){
                return  ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(Map.of(
                   "puedeVerInventario", usuarioServicio.puedeVerInventario(usuario),
                    "puedeAgregarProductos", usuarioServicio.puedeAgregarProductos(usuario),
                    "puedeAumentarInventario", usuarioServicio.puedeAumentarInventario(usuario),
                    "puedeDarDeBajaReactivar", usuarioServicio.puedeDarDeBajaReactivar(usuario),
                    "puedeVerModuloSalida", usuarioServicio.puedeVerModuloSalida(usuario),
                    "puedeSacarInventario", usuarioServicio.puedeSacarInventario(usuario),
                    "puedeVerHistorial", usuarioServicio.puedeVerHistorial(usuario)
            ));
        } catch (Exception e) {
            logger.error("Error al obtener permisos: ", e);
            return  ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }
}
