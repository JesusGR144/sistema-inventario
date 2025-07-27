package castores.sistema_inventario.controlador;

import castores.sistema_inventario.modelo.Producto;
import castores.sistema_inventario.servicio.ProductoServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("inventario-app") //http://localhost:8080/inventario-app
@CrossOrigin(value = "http://localhost:4200") //¨Puerto por default de Angular
public class ProductoControlador {
    private static final Logger logger = LoggerFactory.getLogger(ProductoControlador.class);

    @Autowired
    private ProductoServicio productoServicio;

    @GetMapping("/productos") //http:localhost:8080/inventario-app/productos
    public List<Producto> obtenerProductos() {
        List<Producto> productos = this.productoServicio.listarProductos();

        logger.info("Productos obtenidos");
        productos.forEach(producto -> logger.info(producto.toString()));
        return productos;
    }
}
