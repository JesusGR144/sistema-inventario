package castores.sistema_inventario.controlador;

import castores.sistema_inventario.modelo.Usuario;
import castores.sistema_inventario.repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@CrossOrigin(origins = {"http://localhost:4200", "http://127.0.0.1:5500"})
@RestController
@RequestMapping("/inventario-app")
public class AuthController {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> loginData) {
        String correo = loginData.get("correo");
        String contrasena = loginData.get("contrasena");

        Optional<Usuario> usuarioOpt = usuarioRepositorio.findByCorreo(correo);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (passwordEncoder.matches(contrasena, usuario.getContrasena())) {
                return Map.of(
                        "id", usuario.getIdUsuario(),
                        "rol", usuario.getRol().name()
                );
            }
        }
        throw new RuntimeException("Credenciales inválidas");
    }
}
