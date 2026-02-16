package maximacarga.com.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import maximacarga.com.entidades.Pedido;



/**
 * IOnterfaz JPA para la entidad Pedido.
 * 
 * Proporciona:
 * - Operaciones CRUD básicas (heredadas de JpaRepository)
 * - Métodos personalizados de consulta
 */
public interface PedidoRepositorio extends JpaRepository<Pedido, Long> {
	
	 /**
     * Devuelve todos los pedidos asociados a un usuario concreto.
     * 
     * Spring Data genera automáticamente la consulta
     * a partir del nombre del método.
     *
     * @param usuarioId ID del usuario.
     * @return Lista de pedidos del usuario.
     */
	List<Pedido> findAllByUsuarioId(Long usuarioId);
}
