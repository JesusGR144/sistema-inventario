package castores.sistema_inventario.servicio;

import castores.sistema_inventario.modelo.Usuario;

public interface InterfaceUsuarioServicio {
    // Método para autenticación
    Usuario autenticar(String correo, String contrasena);

    // Método para validacion de permisos
    boolean puedeVerInventario(Usuario usuario);
    boolean puedeAgregarProductos(Usuario usuario);
    boolean puedeAumentarInventario(Usuario usuario);
    boolean puedeDarDeBajaReactivar(Usuario usuario);
    boolean puedeVerModuloSalida(Usuario usuario);
    boolean puedeSacarInventario(Usuario usuario);
    boolean puedeVerHistorial(Usuario usuario);

    // Métods CRUD básicos
    Usuario buscarPorId(Long id);
    void guardarUsuario(Usuario usuario);
}
