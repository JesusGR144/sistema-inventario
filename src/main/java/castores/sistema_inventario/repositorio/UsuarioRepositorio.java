package castores.sistema_inventario.repositorio;

import castores.sistema_inventario.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
    Usuario findByCorreoAndContrasena(String corre, String contrasena);
}
