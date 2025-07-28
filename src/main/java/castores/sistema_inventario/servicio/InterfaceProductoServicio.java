package castores.sistema_inventario.servicio;

import castores.sistema_inventario.modelo.Producto;
import java.util.List;

public interface InterfaceProductoServicio {

    //Metodos para manejar productos
    List<Producto> listarProductos();
    Producto buscarProductosPorId(Long idProducto);
    void guardarProductos(Producto producto);

    // REQUERRIMIENTO: poder agregar un nuevo producto al inventario
    Producto agreggarNuevoProducto(Producto producto);

    // REQUERIMIENTO: poder aumentar el stock de un producto
    boolean aumentarInventario(Long idProducto, int cantidad);

    //REQUERIMIENTO: poder dar de baja un producto y poder activarlo nuevamente
    boolean darBajaProducto(Long idProducto);
    boolean activarProducto(Long idProducto);
    boolean cambiarEstatusProducto(Long idProducto);

    //REQUERIMIENTO: poder ver los productos activos e inactivos
    List<Producto> listarProductosActivos();
    List<Producto> listarProductosInactivos();

    //REQUERIMIENTO: Solo poder ver los productos activos
    List<Producto> listarProductosActivosConStock();

    //REQUERIMIENTO: No poder sacar una cantidad mayor a la disponible
    boolean validarStockDisponible(Long idProducto, int cantidad);
    boolean reducirInventario(Long idProducto, int cantidad);

}
