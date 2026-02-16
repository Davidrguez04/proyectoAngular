package maximacarga.com.controladores;

import java.sql.Date;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import maximacarga.com.entidades.Usuario;
import maximacarga.com.servicios.UsuarioServicio;

/**
 * Clase controladora del usuario
 */
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class UsuarioControlador {

    private final UsuarioServicio usuarioServicio;

    @Autowired
    private PasswordEncoder contraseniaMetodo;

    private final long EXPIRATION_TIME = 3_600_000; // 1h
    private final String SECRET_KEY = "altair_006!";

    @Autowired
    public UsuarioControlador(UsuarioServicio usuarioServicio) {
        this.usuarioServicio = usuarioServicio;
    }

    /**
     * Genera un token JWT para autenticación.
     * Incluye email como subject y fecha de expiración.
     */
    private String generateToken(Usuario usuario) {
        long now = System.currentTimeMillis();
        Date expirationDate = new Date(now + EXPIRATION_TIME);

        return JWT.create()
                .withSubject(usuario.getCorreoElectronico())
                .withIssuedAt(new Date(now))
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }
    
    /**
     * Obtiene un usuario por su ID.
     */
    @GetMapping("/usuarios/{id}")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable Long id) {

        Usuario usuario = usuarioServicio.buscarPorId(id);

        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(usuario);
    }
    
    
    /**
     * Registra un nuevo usuario.
     * - Lo crea como inactivo.
     * - Genera token de activación válido 24h.
     */
    @PostMapping({"/usuarios/registrarUsuario", "/usuarios/registroUsuario"})
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        try {
            System.out.println("📩 Petición recibida en registro: " + usuario);

            // 1) Usuario inactivo hasta activar por email
            usuario.setActivo(false);

            // 2) Generar token de activación
            String tokenActivacion = UUID.randomUUID().toString();
            usuario.setTokenActivacion(tokenActivacion);

            // 3) Expiración del token (24h)
            Instant fechaExpiracion = Instant.now().plusSeconds(86_400);
            usuario.setFechaExpiracionTokenActivacion(fechaExpiracion);

            // 4) Guardar
            Usuario guardado = usuarioServicio.crearUsuario(usuario);

            // 5) Devolver 201 con el usuario (incluye token y expiración)
            return ResponseEntity.status(HttpStatus.CREATED).body(guardado);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    
    
    /**
     * Activa la cuenta de un usuario mediante token.
     * Verifica que el token exista y no esté expirado.
     */
    @PutMapping("/usuarios/activarCuenta")
    public ResponseEntity<String> activarCuenta(@RequestParam("token") String token) {
        Optional<Usuario> usuarioOpt = usuarioServicio.buscarPorTokenActivacion(token);

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token inválido.");
        }

        Usuario usuario = usuarioOpt.get();

        if (usuario.getFechaExpiracionTokenActivacion() == null
                || usuario.getFechaExpiracionTokenActivacion().isBefore(Instant.now())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token expirado.");
        }

        usuario.setActivo(true);
        usuario.setTokenActivacion(null);
        usuario.setFechaExpiracionTokenActivacion(null);

        usuarioServicio.actualizarUsuario(usuario);

        return ResponseEntity.ok("Cuenta activada correctamente.");
    }

    
    
    /**
     * Elimina un usuario por ID.
     */
    @DeleteMapping("/usuarios/eliminar/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().body("El ID del usuario es requerido");
        }

        boolean eliminado = usuarioServicio.eliminarUsuarioPorId(id);

        if (eliminado) {
            return ResponseEntity.ok("Usuario eliminado correctamente");
        } else {
            return ResponseEntity.internalServerError().body("Error al eliminar el usuario");
        }
    }

    
    /**
     * Actualiza datos básicos del usuario (actualización parcial).
     */
    @PutMapping("/usuarios/actualizarUsuario")
    public ResponseEntity<String> actualizarUsuario(@RequestBody Usuario usuarioActualizado) {
        if (usuarioActualizado.getIdUsuario() == null) {
            return ResponseEntity.badRequest().body("El ID del usuario es requerido");
        }

        Usuario usuarioEnBD = usuarioServicio.buscarPorId(usuarioActualizado.getIdUsuario());
        if (usuarioEnBD == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        // Actualizaciones parciales
        if (usuarioActualizado.getNombreUsuario() != null && !usuarioActualizado.getNombreUsuario().trim().isEmpty()) {
            usuarioEnBD.setNombreUsuario(usuarioActualizado.getNombreUsuario().trim());
        }
        if (usuarioActualizado.getApellidosUsuario() != null && !usuarioActualizado.getApellidosUsuario().trim().isEmpty()) {
            usuarioEnBD.setApellidosUsuario(usuarioActualizado.getApellidosUsuario().trim());
        }
        if (usuarioActualizado.getFchNacUsu() != null) {
            usuarioEnBD.setFchNacUsu(usuarioActualizado.getFchNacUsu());
        }

        usuarioServicio.actualizarUsuario(usuarioEnBD);
        return ResponseEntity.ok("Usuario actualizado correctamente");
    }

    
    /**
     * Realiza el login del usuario.
     * Valida credenciales y devuelve un token JWT si son correctas.
     */
    @PostMapping("/usuarios/login")
    public Map<String, String> login(@RequestBody Usuario loginRequest) {
        String email = loginRequest.getCorreoElectronico();
        String rawPassword = loginRequest.getContrasena();

       
        Optional<Usuario> usuario = usuarioServicio.buscarPorCorreoElectronico(email);

        if (usuario.isPresent() && contraseniaMetodo.matches(rawPassword, usuario.get().getContrasena())) {
            String token = generateToken(usuario.get());
            return Map.of("token", token);
        } else {
            return Map.of("error", "Credenciales incorrectas");
        }
    }
    
    
    /**
     * Obtiene los detalles de un usuario por su email.
     */
    @GetMapping("/usuarios/detalles")
	public ResponseEntity<Usuario> obtenerDetallesUsuario(@RequestParam("email") String email) {
	    Optional<Usuario> usuarioOptional = usuarioServicio.buscarPorCorreoElectronico(email);
	    if (usuarioOptional.isPresent()) {
	        return ResponseEntity.ok(usuarioOptional.get());
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}
    
    
    /**
     * Lista todos los usuarios registrados.
     */
    @GetMapping("/usuarios/listar")
    public ResponseEntity<?> listarUsuarios() {
        try {
            return ResponseEntity.ok(usuarioServicio.listarUsuarios());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al listar usuarios");
        }
    }
    

    /**
     * Devuelve la foto de perfil del usuario.
     */
    @GetMapping("/usuarios/{id}/foto")
    public ResponseEntity<byte[]> obtenerFoto(@PathVariable Long id) {

        Usuario usuario = usuarioServicio.buscarPorId(id);

        if (usuario == null || usuario.getFoto() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header("Content-Type", "image/jpeg")
                .body(usuario.getFoto());
    }
    
    
    /**
     * Permite subir o actualizar la foto de perfil del usuario.
     */
    @PutMapping("/usuarios/subirFoto/{id}")
    public ResponseEntity<?> subirFoto(@PathVariable Long id,
                                       @RequestBody byte[] imagen) {

        Usuario usuario = usuarioServicio.buscarPorId(id);

        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }

        usuario.setFoto(imagen);
        usuarioServicio.actualizarUsuario(usuario);

        return ResponseEntity.ok("Foto actualizada");
    }
    
    
    /**
     * Genera un token para recuperación de contraseña.
     */
    @PostMapping("/usuarios/recuperar")
    public ResponseEntity<String> recuperar(@RequestParam String correoElectronico) {

        boolean generado =
                usuarioServicio.generarTokenRecuperacion(correoElectronico);

        if (!generado) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok("Token generado");
    }

    
    /**
     * Obtiene el token de recuperación asociado a un correo.
     */
    @GetMapping("/usuarios/tokenRecuperacion")
    public ResponseEntity<String> obtenerToken(@RequestParam String correoElectronico) {

        String token =
                usuarioServicio.obtenerTokenRecuperacion(correoElectronico);

        if (token == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(token);
    }
    
    
    /**
     * Restablece la contraseña utilizando un token válido.
     */
    @PutMapping("/usuarios/restablecerContrasenia")
    public ResponseEntity<String> restablecer(@RequestBody Map<String, String> body) {

        String tokenRecuperacion = body.get("tokenRecuperacion");
        String nuevaContrasenia = body.get("nuevaContrasenia");

        boolean ok = usuarioServicio.restablecerContrasenia(
                tokenRecuperacion,
                nuevaContrasenia,
                contraseniaMetodo
        );

        if (!ok) {
            return ResponseEntity.badRequest()
                    .body("Token inválido o expirado");
        }

        return ResponseEntity.ok("Contraseña actualizada");
    }

    
}
