package castores.sistema_inventario.servicio;

import castores.sistema_inventario.modelo.Producto;
import castores.sistema_inventario.repositorio.ProductoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoServicio implements InterfaceProductoServicio{

    @Autowired
    private ProductoRepositorio productoRepositorio;

    @Override
    public List<Producto> listarProductos() {
        return this.productoRepositorio.findAll();
    }

    @Override
    public Producto buscarProductosPorId(Long idProducto) {
        return this.productoRepositorio.findById(idProducto).orElse(null);
    }

    @Override
    public void guardarProductos(Producto producto) {
        this.productoRepositorio.save(producto);
    }

    @Override
    public void eliminarProductoPorId(Long idProducto) {
        this.productoRepositorio.deleteById(idProducto);
    }

    // REQUERRIMIENTO: poder agregar un nuevo producto con cantidad inicial 0
    @Override
    public Producto agreggarNuevoProducto(Producto producto) {
        producto.setCantidadActual(0);
        producto.setActivo(true);
        return this.productoRepositorio.save(producto);
    }

    // REQUERIMIENTO: poder aumentar el stock de un producto
    // REQUERIMIENTO: Al intentar disminuier la cantidad se mostrara un mensaje de error
    @Override
    public boolean aumentarInventario(Long idProducto, int cantidad) {
        try {
            if (cantidad <= 0) {
                throw new IllegalArgumentException("No esta permitido disminuir la cantidad de un producto");
            }

            Producto producto = buscarProductosPorId(idProducto);
            if (producto == null) {
                return false;
            }

            producto.aumentarInventario(cantidad);
            guardarProductos(producto);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // REQUERIMIENTO: poder dar de baja un producto
    @Override
    public boolean darBajaProducto(Long idProducto) {
        try {
            Producto producto = buscarProductosPorId(idProducto);
            if (producto == null) {
                return false;
            }

            producto.darDeBaja();
            guardarProductos(producto);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // REQUERIMIENTO: poder activar un producto que se encuentra inactivo
    @Override
    public boolean activarProducto(Long idProducto) {
        try {
            Producto producto = buscarProductosPorId(idProducto);
            if (producto == null) {
                return false;
            }

            producto.reactivar();
            guardarProductos(producto);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean cambiarEstatusProducto(Long idProducto) {
        try {
            Producto producto = buscarProductosPorId(idProducto);
            if (producto == null) {
                return false;
            }

            if(producto.getActivo()){
                producto.darDeBaja();
            }else {
                producto.reactivar();
            }

            guardarProductos(producto);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    // REQUERIMIENTO: poder ver los productos activos e inactivos
    @Override
    public List<Producto> listarProductosActivos() {
        return this.productoRepositorio.finByActivoTrue();
    }
    @Override
    public List<Producto> listarProductosInactivos() {
        return this.productoRepositorio.findByActivoFalse();
    }

// REQUERIMIENTO: Solo poder ver los productos activos (SALIDA)
    @Override
    public List<Producto> listarProductosActivosConStock() {
        return this.productoRepositorio.findByProductoActivoConStock();
    }

    // REQUERIMIENTO: No poder sacar una cantidad mayor a la disponible, al intentar mostrar mensaje de error
    @Override
    public boolean validarStockDisponible(Long idProducto, int cantidad) {
        Producto producto = buscarProductosPorId(idProducto);
        return producto != null && producto.getActivo() && producto.getCantidadActual() > cantidad;
    }

    @Override
    public boolean reducirInventario(Long idProducto, int cantidad) {
        try {
            Producto producto = buscarProductosPorId(idProducto);
            if (producto == null || !producto.getActivo()) {
                return false;
            }

            producto.disminuirInventario(cantidad);
            guardarProductos(producto);
            return true;
        }catch (IllegalArgumentException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
