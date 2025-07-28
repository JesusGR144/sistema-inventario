package castores.sistema_inventario.repositorio;

import castores.sistema_inventario.modelo.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductoRepositorio extends JpaRepository <Producto, Long> {

    // Para poder visializar los productos activos y no activos
    List<Producto> finByActivoTrue();
    List<Producto> findByActivoFalse();

    // Para solo ver los productos activos con stock (SALIDA)
    @Query("SELECT p FROM Producto p WHERE p.activo = true AND p.cantidadActual > 0")
    List<Producto> findByProductoActivoConStock();
}
