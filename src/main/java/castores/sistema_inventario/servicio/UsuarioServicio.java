package castores.sistema_inventario.servicio;

import castores.sistema_inventario.modelo.Usuario;
import castores.sistema_inventario.enums.TipoRol;
import castores.sistema_inventario.repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServicio implements InterfaceUsuarioServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Override
    public Usuario autenticar(String correo, String contrasena) {
        Usuario usuario = usuarioRepositorio.findByCorreoAndContrasena(correo, contrasena);

        if (usuario == null || !usuario.getEstatus()) {
            return null;
        }

        return usuario;
    }

    // ADMINISTRADOR y ALMACENISTA
    @Override
    public boolean puedeVerInventario(Usuario usuario) {
       return usuario != null && usuario.getEstatus();
    }

    // Solo ADMINISTRADOR
    @Override
    public boolean puedeAgregarProductos(Usuario usuario) {
        return usuario != null && usuario.getEstatus() && usuario.getRol() == TipoRol.ADMINISTRADOR;
    }

    // Solo ADMINISTRADOR
    @Override
    public boolean puedeAumentarInventario(Usuario usuario) {
        return usuario != null && usuario.getEstatus() && usuario.getRol() == TipoRol.ADMINISTRADOR;
    }

    // Solo ADMINISTRADOR
    @Override
    public boolean puedeDarDeBajaReactivar(Usuario usuario) {
        return usuario != null && usuario.getEstatus() && usuario.getRol() == TipoRol.ADMINISTRADOR;
    }

    // Solo ALMACENISTA
    @Override
    public boolean puedeVerModuloSalida(Usuario usuario) {
        return usuario != null && usuario.getEstatus() && usuario.getRol() == TipoRol.ALMACENISTA;
    }

    // Solo ALMACENISTA
    @Override
    public boolean puedeSacarInventario(Usuario usuario) {
        return usuario != null && usuario.getEstatus() && usuario.getRol() == TipoRol.ALMACENISTA;
    }

    // Solo ADMINISTRADOR
    @Override
    public boolean puedeVerHistorial(Usuario usuario) {
        return usuario != null && usuario.getEstatus() && usuario.getRol() == TipoRol.ADMINISTRADOR;
    }

    @Override
    public Usuario buscarPorId(Long id) {
        return usuarioRepositorio.findById(id).orElse(null);
    }

    @Override
    public void guardarUsuario(Usuario usuario) {
        usuarioRepositorio.save(usuario);
    }
}
