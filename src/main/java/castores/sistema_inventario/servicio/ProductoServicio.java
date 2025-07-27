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
        Producto producto = this.productoRepositorio.findById(idProducto).orElse(null);
        return producto;
    }

    @Override
    public void guardarProductos(Producto producto) {
        this.productoRepositorio.save(producto);
    }

    @Override
    public void eliminarProductoPorId(Long idProducto) {
        this.productoRepositorio.deleteById(idProducto);
    }
}
