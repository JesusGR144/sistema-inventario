package castores.sistema_inventario.servicio;

import castores.sistema_inventario.modelo.Producto;
import castores.sistema_inventario.modelo.MovimientoInventario;
import castores.sistema_inventario.modelo.Usuario;
import castores.sistema_inventario.enums.TipoMovimiento;
import castores.sistema_inventario.repositorio.ProductoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductoServicio implements InterfaceProductoServicio{

    @Autowired
    private ProductoRepositorio productoRepositorio;

    @Autowired
    private InterfaceMovimientoInventarioServicio movimientoInventarioServicio;

    @Autowired
    private UsuarioServicio usuarioServicio;

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

    // REQUERRIMIENTO: poder agregar un nuevo producto con cantidad inicial 0
    @Override
    @Transactional
    public Producto agreggarNuevoProducto(Producto producto) {
        producto.setCantidadActual(0);
        producto.setActivo(true);
        return this.productoRepositorio.save(producto);
    }

    // Sobrecarga para incluir usuario al crear producto
    @Transactional
    public Producto agreggarNuevoProducto(Producto producto, Long usuarioId) {
        producto.setCantidadActual(0);
        producto.setActivo(true);
        Producto productoGuardado = this.productoRepositorio.save(producto);

        // Solo crear movimiento si hay cantidad inicial
        if (producto.getCantidadActual() > 0) {
            crearMovimientoInventario(productoGuardado, usuarioId,
                    producto.getCantidadActual(), TipoMovimiento.ENTRADA);
        }

        return productoGuardado;
    }

    // REQUERIMIENTO: poder aumentar el stock de un producto
    @Override
    @Transactional
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

    // Sobrecarga para incluir usuario
    @Transactional
    public boolean aumentarInventario(Long idProducto, int cantidad, Long usuarioId) {
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

            // CREAR REGISTRO EN HISTORIAL
            crearMovimientoInventario(producto, usuarioId, cantidad, TipoMovimiento.ENTRADA);

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
        return this.productoRepositorio.findByActivoTrue();
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

    // REQUERIMIENTO: No poder sacar una cantidad mayor a la disponible
    @Override
    public boolean validarStockDisponible(Long idProducto, int cantidad) {
        Producto producto = buscarProductosPorId(idProducto);
        return producto != null && producto.getActivo() && producto.getCantidadActual() >= cantidad;
    }

    @Override
    @Transactional
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

    // Sobrecarga para incluir usuario
    @Transactional
    public boolean reducirInventario(Long idProducto, int cantidad, Long usuarioId) {
        try {
            Producto producto = buscarProductosPorId(idProducto);
            if (producto == null || !producto.getActivo()) {
                return false;
            }

            producto.disminuirInventario(cantidad);
            guardarProductos(producto);

            // CREAR REGISTRO EN HISTORIAL
            crearMovimientoInventario(producto, usuarioId, cantidad, TipoMovimiento.SALIDA);

            return true;
        }catch (IllegalArgumentException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    // MÉTODO AUXILIAR PARA CREAR MOVIMIENTOS DE INVENTARIO
    private void crearMovimientoInventario(Producto producto, Long usuarioId,
                                           Integer cantidad, TipoMovimiento tipo) {
        try {
            Usuario usuario = usuarioServicio.buscarPorId(usuarioId);
            if (usuario != null) {
                MovimientoInventario movimiento = new MovimientoInventario();
                movimiento.setProducto(producto);
                movimiento.setUsuario(usuario);
                movimiento.setCantidad(cantidad);
                movimiento.setTipoMovimiento(tipo);
                movimiento.setFechaHora(LocalDateTime.now());

                movimientoInventarioServicio.guardarMovimiento(movimiento);
            }
        } catch (Exception e) {
            // Log del error, pero no fallar la operación principal
            System.err.println("Error al crear movimiento de inventario: " + e.getMessage());
        }
    }
}