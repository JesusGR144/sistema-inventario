package castores.sistema_inventario.servicio;

import castores.sistema_inventario.modelo.Producto;
import java.util.List;

public interface InterfaceProductoServicio {
    List<Producto> listarProductos();

    Producto buscarProductosPorId(Long idProducto);

    void guardarProductos(Producto producto);

    void eliminarProductoPorId(Long idProducto);
}
