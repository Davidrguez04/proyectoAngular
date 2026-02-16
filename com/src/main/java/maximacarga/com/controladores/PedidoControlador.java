package maximacarga.com.controladores;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import maximacarga.com.entidades.Pedido;
import maximacarga.com.servicios.PedidoServicio;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoControlador {

    private final PedidoServicio pedidoServicio;

    // Constructor que inyecta el servicio de pedidos
    public PedidoControlador(PedidoServicio pedidoServicio) {
        this.pedidoServicio = pedidoServicio;
    }

    /**
     * Método que sirve para crear un nuevo pedido para un usuario.
     * Recibe el id del usuario y un mapa con los productos del carrito.
     *
     * @param idUsuario ID del usuario que realiza el pedido.
     * @param carrito Mapa con producto y cantidad.
     * @return 200 OK si el pedido se crea correctamente.
     */
    @PostMapping("/{idUsuario}")
    public ResponseEntity<Void> crearPedido(@PathVariable Long idUsuario,
                                            @RequestBody Map<String, Integer> carrito) {
        pedidoServicio.crearPedido(idUsuario, carrito);
        return ResponseEntity.ok().build();
    }


    /**
     * Método que sirve para obtener una lista con todos los pedidos.
     *
     * @return Lista de pedidos registrados en el sistema.
     */
    @GetMapping
    public List<Pedido> listarPedidos() {
        return pedidoServicio.listarTodos();
    }


    /**
     * Método que sirve para obtener todos los pedidos de un usuario concreto.
     *
     * @param idUsuario ID del usuario.
     * @return Lista de pedidos asociados a ese usuario.
     */
    @GetMapping("/usuario/{idUsuario}")
    public List<Pedido> listarPedidosPorUsuario(@PathVariable Long idUsuario) {
        return pedidoServicio.listarPorUsuario(idUsuario);
    }
    

    /**
     * Método que sirve para obtener el detalle de un pedido específico.
     *
     * @param idPedido ID del pedido.
     * @return Pedido encontrado.
     */
    @GetMapping("/{idPedido}")
    public Pedido obtenerPedido(@PathVariable Long idPedido) {
        return pedidoServicio.obtenerPorId(idPedido);
    }

    /**
     * Método que permite cambiar el estado de un pedido.
     * Ejemplo: PENDIENTE, ENVIADO, ENTREGADO.
     *
     * @param idPedido ID del pedido.
     * @param estado Nuevo estado del pedido.
     * @return Pedido actualizado.
     */
    @PutMapping("/{idPedido}/estado")
    public Pedido cambiarEstado(@PathVariable Long idPedido,
                                @RequestParam Pedido.EstadoPedido estado) {
        return pedidoServicio.cambiarEstado(idPedido, estado);
    }
    
    /**
     * Método que sirve para eliminar un pedido por su ID.
     *
     * @param idPedido ID del pedido a eliminar.
     * @return 200 OK si se elimina correctamente.
     */
    @DeleteMapping("/{idPedido}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable Long idPedido) {

        pedidoServicio.eliminarPedido(idPedido);

        return ResponseEntity.ok().build();
    }
    
}
