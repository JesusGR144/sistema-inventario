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
@RequestMapping("inventario-app")
@CrossOrigin(value = "http://localhost:4200")
public class UsuarioControlador {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioControlador.class);

    @Autowired
    private UsuarioServicio usuarioServicio;

    //ENPOINT para LOGIN
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        try {
            String correo = credenciales.get("correo");
            String contrasena = credenciales.get("contrasena");

            if (correo == null || contrasena == null || correo.trim().isEmpty() || contrasena.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Correo y contraseña son obligatorios"));
            }
            Usuario usuario = usuarioServicio.autenticar(correo, contrasena);

            if (usuario == null) {
                logger.warn("Login fallido para correo {}", correo);
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Credenciales inv{alidas o usuario inactivo"));
            }

            logger.info("Login exitoso para usuario; {} - Rol: {}", usuario.getNombre(), usuario.getRol());

            return ResponseEntity.ok(Map.of(
                    "mensaje", "Login exitoso",
                    "usuario", Map.of(
                            "id", usuario.getIdUsuario(),
                            "nombre", usuario.getNombre(),
                            "correo", usuario.getCorreo(),
                            "rol", usuario.getRol().toString()
                    )
            ));
        } catch (Exception e) {
            logger.error("Error en login: ", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }
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
    public ResponseEntity<?> obtenerPermisos(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioServicio.buscarPorId(id);

            if (usuario == null){
                return  ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(Map.of(
                   "puedeVerInventario", usuarioServicio.puedeAumentarInventario(usuario),
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
