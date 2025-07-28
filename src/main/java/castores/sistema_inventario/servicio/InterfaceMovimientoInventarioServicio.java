package castores.sistema_inventario.servicio;

import castores.sistema_inventario.enums.TipoMovimiento;
import castores.sistema_inventario.modelo.MovimientoInventario;

import java.util.List;

public interface InterfaceMovimientoInventarioServicio {
    MovimientoInventario guardarMovimiento(MovimientoInventario movimiento);
    List<MovimientoInventario> obtenerTodos();
    List<MovimientoInventario> obtenerPorTipo(TipoMovimiento tipoMovimiento);
    List<MovimientoInventario> obtenerPorUsuarioId(Long usuarioId);
    List<MovimientoInventario> obtenerPorProductoId(Long productoId);
}
