package maximacarga.com.servicios;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import maximacarga.com.entidades.Pedido;
import maximacarga.com.entidades.Pedido.EstadoPedido;
import maximacarga.com.entidades.PedidoLinea;
import maximacarga.com.entidades.Producto;
import maximacarga.com.entidades.Usuario;
import maximacarga.com.repositorios.PedidoRepositorio;
import maximacarga.com.repositorios.ProductoRepositorio;
import maximacarga.com.repositorios.UsuarioRepositorio;

/**
 * Servicio que gestiona la lógica de negocio relacionada con los pedidos.
 * Se encarga de crear, listar, actualizar estado y eliminar pedidos.
 */
@Service
public class PedidoServicio {

    private final PedidoRepositorio pedidoRepo;
    private final UsuarioRepositorio usuarioRepo;
    private final ProductoRepositorio productoRepo;
    

    public PedidoServicio(PedidoRepositorio pedidoRepo,
                          UsuarioRepositorio usuarioRepo,
                          ProductoRepositorio productoRepo) {
        this.pedidoRepo = pedidoRepo;
        this.usuarioRepo = usuarioRepo;
        this.productoRepo = productoRepo;
    }

    /**
     * Crea un nuevo pedido a partir del carrito de un usuario.
     * - Valida que el carrito no esté vacío.
     * - Calcula subtotal y total.
     * - Genera líneas de pedido.
     * - Guarda el pedido en base de datos.
     */
    public void crearPedido(Long idUsuario, Map<String, Integer> carrito) {

        if (carrito == null || carrito.isEmpty()) {
            throw new IllegalArgumentException("El carrito está vacío");
        }

        Usuario usuario = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Pedido pedido = new Pedido();
        pedido.setUsuarioId(usuario.getIdUsuario());
        
        pedido.setFecha(LocalDateTime.now());
        pedido.setFechaEntrega(null);
        pedido.setEstado(EstadoPedido.EN_PREPARACION);

        BigDecimal subtotalPedido = BigDecimal.ZERO;

        for (Map.Entry<String, Integer> entry : carrito.entrySet()) {
            Long idProducto = Long.valueOf(entry.getKey());
            Integer cantidad = entry.getValue();

            if (cantidad == null || cantidad <= 0) {
                throw new IllegalArgumentException("Cantidad inválida para el producto " + idProducto);
            }

            Producto producto = productoRepo.findById(idProducto)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + idProducto));

            BigDecimal precioUnit = BigDecimal.valueOf(producto.getPrecio());
            BigDecimal subtotalLinea = precioUnit.multiply(BigDecimal.valueOf(cantidad));

            PedidoLinea linea = new PedidoLinea();
            linea.setPedido(pedido);
            linea.setProductoId(producto.getIdProducto());
            linea.setNombreProducto(producto.getNombre());
            linea.setPrecioUnitario(precioUnit);
            linea.setCantidad(cantidad);
            linea.setSubtotal(subtotalLinea); 

            pedido.getLineas().add(linea);
            subtotalPedido = subtotalPedido.add(subtotalLinea);
        }

        pedido.setSubtotal(subtotalPedido);
        pedido.setTotal(subtotalPedido);

        pedidoRepo.save(pedido);
    }

    /**
     * Devuelve todos los pedidos del sistema.
     * Fuerza la carga de las líneas para evitar problemas de Lazy Loading.
     */
    public List<Pedido> listarTodos() {

        List<Pedido> pedidos = pedidoRepo.findAll();

        // FORZAR CARGA DE LAS LÍNEAS
        pedidos.forEach(p -> p.getLineas().size());

        return pedidos;
    }
    
    

    /**
     * Devuelve todos los pedidos asociados a un usuario concreto.
     */
    public List<Pedido> listarPorUsuario(Long idUsuario) {

        List<Pedido> pedidos = pedidoRepo.findAllByUsuarioId(idUsuario);

        // 🔥 FORZAR CARGA DE LAS LÍNEAS
        pedidos.forEach(p -> p.getLineas().size());

        return pedidos;
    }

    /**
     * Obtiene un pedido por su ID.
     */
    public Pedido obtenerPorId(Long idPedido) {
        return pedidoRepo.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }

    /**
     * Cambia el estado de un pedido.
     * - No permite cambios si ya está ENTREGADO o CANCELADO.
     * - Si pasa a ENTREGADO, fija la fecha de entrega.
     */
    public Pedido cambiarEstado(Long idPedido, EstadoPedido nuevoEstado) {

        Pedido pedido = pedidoRepo.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        EstadoPedido estadoActual = pedido.getEstado();

        // No permitir cambios si ya está finalizado
        if (estadoActual == EstadoPedido.ENTREGADO || estadoActual == EstadoPedido.CANCELADO) {
            throw new IllegalStateException("El pedido ya está finalizado");
        }

        // Si pasa a ENTREGADO, fijar fecha de entrega
        if (nuevoEstado == EstadoPedido.ENTREGADO) {
            pedido.setFechaEntrega(LocalDateTime.now());
        }

        pedido.setEstado(nuevoEstado);
        return pedidoRepo.save(pedido);
    }
    
    /**
     * Actualiza ciertos datos de un pedido existente.
     * Nota: En esta entidad se guarda usuarioId, no objeto Usuario completo.
     */
    public Pedido actualizarPedido(Long id, Pedido datos) {

        Pedido pedido = pedidoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        pedido.setFecha(datos.getFecha());
        pedido.setTotal(datos.getTotal());
        // ⚠ IMPORTANTE: en tu entidad NO existe setUsuario(...)
        // Tú guardas usuarioId, no Usuario completo
        pedido.setUsuarioId(datos.getUsuarioId());

        return pedidoRepo.save(pedido);
    }
    
    

    /**
     * Elimina un pedido por su ID.
     * Lanza excepción si no existe.
     */
    public void eliminarPedido(Long idPedido) {

        if (!pedidoRepo.existsById(idPedido)) {
            throw new RuntimeException("Pedido no encontrado");
        }

        pedidoRepo.deleteById(idPedido);
    }
}
