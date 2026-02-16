package maximacarga.com.repositorios;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import maximacarga.com.entidades.Usuario;

/**
 * Interfaz JPA para la entidad Usuario.
 * 
 * Gestiona el acceso a datos de los usuarios.
 * Hereda operaciones CRUD básicas desde JpaRepository.
 */
@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {

	  /**
     * Busca un usuario por su correo electrónico.
     * 
     * IMPORTANTE: El nombre del método debe coincidir
     * con el atributo de la entidad (correoElectronico).
     *
     * @param correoElectronico Email del usuario.
     * @return Optional con el usuario si existe.
     */
    Optional<Usuario> findByCorreoElectronico(String correoElectronico);

    /**
     * Busca un usuario por su token de activación.
     * Se utiliza durante el proceso de activación de cuenta.
     *
     * @param tokenActivacion Token enviado por email.
     * @return Optional con el usuario asociado.
     */
    Optional<Usuario> findByTokenActivacion(String tokenActivacion);
    
    /**
     * Busca un usuario por su token de recuperación.
     * Se usa para restablecer contraseña.
     *
     * @param tokenRecuperacion Token temporal.
     * @return Optional con el usuario asociado.
     */
    Optional<Usuario> findByTokenRecuperacion(String tokenRecuperacion);
    
    
}
