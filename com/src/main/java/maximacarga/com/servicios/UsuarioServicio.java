package maximacarga.com.servicios;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import maximacarga.com.entidades.Usuario;
import maximacarga.com.repositorios.UsuarioRepositorio;

/**
 * Servicio que gestiona la lógica de negocio relacionada con los usuarios.
 * Incluye registro, actualización, eliminación y recuperación de contraseña.
 */
@Service
public class UsuarioServicio {

    private final UsuarioRepositorio usuarioRepositorio;

    public UsuarioServicio(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    /**
     * Crea un nuevo usuario en la base de datos.
     * Se ejecuta dentro de una transacción.
     */
    @Transactional
    public Usuario crearUsuario(Usuario usuario) {
        return usuarioRepositorio.save(usuario);
    }

    /**
     * Actualiza un usuario existente.
     */
    @Transactional
    public Usuario actualizarUsuario(Usuario usuario) {
        return usuarioRepositorio.save(usuario);
    }

    /**
     * Busca un usuario por su ID.
     * Devuelve null si no existe.
     */
    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return usuarioRepositorio.findById(id).orElse(null);
    }


    /**
     * Elimina un usuario por su ID.
     * Devuelve true si se elimina correctamente.
     */
    @Transactional
    public boolean eliminarUsuarioPorId(Long id) {
        if (usuarioRepositorio.existsById(id)) {
            usuarioRepositorio.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Busca un usuario por su token de activación.
     */
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorTokenActivacion(String token) {
        return usuarioRepositorio.findByTokenActivacion(token);
    }
    
    /**
     * Busca un usuario por su correo electrónico.
     */
    public Optional<Usuario> buscarPorCorreoElectronico(String email) {
        return usuarioRepositorio.findByCorreoElectronico(email);
    }
    
    /**
     * Devuelve la lista completa de usuarios registrados.
     */
    public List<Usuario> listarUsuarios() {
        return usuarioRepositorio.findAll();
    }
    
    
    /**
     * Genera un token de recuperación de contraseña.
     * El token se guarda junto con la fecha de generación.
     */
    @Transactional
    public boolean generarTokenRecuperacion(String correoElectronico) {

        Optional<Usuario> optional = usuarioRepositorio
                .findByCorreoElectronico(correoElectronico);

        if (optional.isEmpty()) {
            return false;
        }

        Usuario usuario = optional.get();

        String token = UUID.randomUUID().toString();

        usuario.setTokenRecuperacion(token);
        usuario.setFechaTokenRecuperacion(LocalDateTime.now());

        usuarioRepositorio.save(usuario);

        return true;
    }

    /**
     * Obtiene el token de recuperación asociado a un correo.
     */
    @Transactional(readOnly = true)
    public String obtenerTokenRecuperacion(String correoElectronico) {

        Optional<Usuario> optional = usuarioRepositorio
                .findByCorreoElectronico(correoElectronico);

        if (optional.isEmpty()) {
            return null;
        }

        return optional.get().getTokenRecuperacion();
    }

    /**
     * Restablece la contraseña utilizando un token válido.
     * El token expira una hora después de su creación.
     */
    @Transactional
    public boolean restablecerContrasenia(String tokenRecuperacion,
                                          String nuevaContrasenia,
                                          PasswordEncoder encoder) {

        Optional<Usuario> optional =
                usuarioRepositorio.findByTokenRecuperacion(tokenRecuperacion);

        if (optional.isEmpty()) {
            return false;
        }

        Usuario usuario = optional.get();

        //  Expira a la hora
        if (usuario.getFechaTokenRecuperacion() == null ||
            usuario.getFechaTokenRecuperacion()
                   .isBefore(LocalDateTime.now().minusHours(1))) {
            return false;
        }

        usuario.setContrasena(encoder.encode(nuevaContrasenia));
        usuario.setTokenRecuperacion(null);
        usuario.setFechaTokenRecuperacion(null);

        usuarioRepositorio.save(usuario);

        return true;
    }

  
}
