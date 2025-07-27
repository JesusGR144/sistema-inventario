package castores.sistema_inventario.repositorio;

import castores.sistema_inventario.modelo.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepositorio extends JpaRepository <Producto, Long> {
}
