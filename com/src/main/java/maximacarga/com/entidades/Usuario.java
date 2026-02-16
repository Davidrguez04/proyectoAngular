package maximacarga.com.entidades;

import java.time.Instant;              // <- añadido
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuario")
    private Long idUsuario;

    @Column(name = "nombreUsuario", length = 50)
    private String nombreUsuario;
    
    
    @Column(name = "foto", columnDefinition = "LONGBLOB")
    private byte[] foto;

    @Column(name = "apellidosUsuario", length = 100)
    private String apellidosUsuario;

    @Column(name = "fchNacUsu")
    private LocalDate fchNacUsu;

    @Column(name = "movil", length = 20)
    private String movil;

    @Column(name = "correoElectronico", nullable = false, unique = true, length = 50)
    private String correoElectronico;

    @Column(name = "tipoUsuario", nullable = false, length = 20)
    private String tipoUsuario;

    @Column(name = "contrasena", nullable = false)
    private String contrasena;

    // ===== NUEVOS CAMPOS =====
    @Column(name = "activo", nullable = false)
    private boolean activo = false;
    
    @Column(name = "token_recuperacion")
    private String tokenRecuperacion;

    @Column(name = "fecha_token_recuperacion")
    private LocalDateTime fechaTokenRecuperacion;

    @Column(name = "token_activacion", unique = true, length = 128)
    private String tokenActivacion;

    @Column(name = "fecha_expiracion_token_activacion")
    private Instant fechaExpiracionTokenActivacion;

    public Usuario() {}

    public Usuario(String nombreUsuario, String apellidosUsuario, LocalDate fchNacUsu) {
        this.nombreUsuario = nombreUsuario;
        this.apellidosUsuario = apellidosUsuario;
        this.fchNacUsu = fchNacUsu;
    }

    public Usuario(String movil, String correoElectronico, String tipoUsuario, String contrasena) {
        this.movil = movil;
        this.correoElectronico = correoElectronico;
        this.tipoUsuario = tipoUsuario;
        this.contrasena = contrasena;
    }

    public Usuario(Long idUsuario, String movil, String correoElectronico, String tipoUsuario, String contrasena,
                   String nombreUsuario, String apellidosUsuario, LocalDate fchNacUsu) {
        this.idUsuario = idUsuario;
        this.movil = movil;
        this.correoElectronico = correoElectronico;
        this.tipoUsuario = tipoUsuario;
        this.contrasena = contrasena;
        this.nombreUsuario = nombreUsuario;
        this.apellidosUsuario = apellidosUsuario;
        this.fchNacUsu = fchNacUsu;
    }

    // ===== GETTERS/SETTERS =====
    
    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }
    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public String getApellidosUsuario() { return apellidosUsuario; }
    public void setApellidosUsuario(String apellidosUsuario) { this.apellidosUsuario = apellidosUsuario; }

    public LocalDate getFchNacUsu() { return fchNacUsu; }
    public void setFchNacUsu(LocalDate fchNacUsu) { this.fchNacUsu = fchNacUsu; }

    public String getMovil() { return movil; }
    public void setMovil(String movil) { this.movil = movil; }

    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }

    public String getTipoUsuario() { return tipoUsuario; }
    public void setTipoUsuario(String tipoUsuario) { this.tipoUsuario = tipoUsuario; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    // --- nuevos ---
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public String getTokenActivacion() { return tokenActivacion; }
    public void setTokenActivacion(String tokenActivacion) { this.tokenActivacion = tokenActivacion; }

    public Instant getFechaExpiracionTokenActivacion() { return fechaExpiracionTokenActivacion; }
    public void setFechaExpiracionTokenActivacion(Instant fechaExpiracionTokenActivacion) {
        this.fechaExpiracionTokenActivacion = fechaExpiracionTokenActivacion;
    }

    public String getTokenRecuperacion() {
        return tokenRecuperacion;
    }

    public void setTokenRecuperacion(String tokenRecuperacion) {
        this.tokenRecuperacion = tokenRecuperacion;
    }

    public LocalDateTime getFechaTokenRecuperacion() {
        return fechaTokenRecuperacion;
    }

    public void setFechaTokenRecuperacion(LocalDateTime fechaTokenRecuperacion) {
        this.fechaTokenRecuperacion = fechaTokenRecuperacion;
    }
    
    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", movil='" + movil + '\'' +
                ", correoElectronico='" + correoElectronico + '\'' +
                ", tipoUsuario='" + tipoUsuario + '\'' +
                ", contrasena='" + (contrasena != null ? "****" : null) + '\'' +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", apellidosUsuario='" + apellidosUsuario + '\'' +
                ", fchNacUsu=" + fchNacUsu +
                ", activo=" + activo +
                ", tokenActivacion=" + (tokenActivacion != null ? "***" : null) +
                ", fechaExpiracionTokenActivacion=" + fechaExpiracionTokenActivacion +
                '}';
    }
}
