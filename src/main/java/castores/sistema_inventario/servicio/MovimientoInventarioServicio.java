package castores.sistema_inventario.servicio;

import castores.sistema_inventario.modelo.MovimientoInventario;
import castores.sistema_inventario.enums.TipoMovimiento;
import castores.sistema_inventario.repositorio.MovimientoInventarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovimientoInventarioServicio implements InterfaceMovimientoInventarioServicio {

    @Autowired
    private MovimientoInventarioRepositorio repositorio;

    @Override
    public MovimientoInventario guardarMovimiento(MovimientoInventario movimiento) {
        return repositorio.save(movimiento);
    }

    @Override
    public List<MovimientoInventario> obtenerTodos() {
        return repositorio.findAll();
    }

    @Override
    public List<MovimientoInventario> obtenerPorTipo(TipoMovimiento tipoMovimiento) {
        return repositorio.findByTipoMovimiento(tipoMovimiento);
    }

    @Override
    public List<MovimientoInventario> obtenerPorUsuarioId(Long usuarioId) {
        return repositorio.findByUsuarioId(usuarioId);
    }

    @Override
    public List<MovimientoInventario> obtenerPorProductoId(Long productoId) {
        return repositorio.findByProductoId(productoId);
    }
}