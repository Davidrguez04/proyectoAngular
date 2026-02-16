package maximacarga.com.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import maximacarga.com.entidades.Producto;


/**
 * Repositorio JPA para la entidad Producto.
 * 
 * Proporciona acceso a la base de datos para operaciones
 * relacionadas con productos.
 */
@Repository
public interface ProductoRepositorio extends JpaRepository<Producto, Long> {
}
