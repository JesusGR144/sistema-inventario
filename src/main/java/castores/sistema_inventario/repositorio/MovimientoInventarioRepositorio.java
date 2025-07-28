package castores.sistema_inventario.repositorio;

import castores.sistema_inventario.enums.TipoMovimiento;
import castores.sistema_inventario.modelo.MovimientoInventario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimientoInventarioRepositorio extends JpaRepository<MovimientoInventario, Long> {
    List<MovimientoInventario> findByTipoMovimiento(TipoMovimiento tipoMovimiento);
    List<MovimientoInventario> findByUsuarioId(Long usuarioId);
    List<MovimientoInventario> findByProductoId(Long productoId);
}
