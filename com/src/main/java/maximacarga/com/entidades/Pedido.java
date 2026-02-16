package maximacarga.com.entidades;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Clase pedido con elementos necesarios de pedidos
 */
@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    /**
     * Fecha de creación del pedido.
     * (Mantengo el nombre "fecha" para no romper tu código actual)
     */
    @Column(nullable = false)
    private LocalDateTime fecha;

    /**
     * Fecha en la que el pedido se entrega (solo cuando estado = ENTREGADO).
     * Debe permitir null.
     */
    @Column(name = "fecha_entrega")
    private LocalDateTime fechaEntrega;

    /**
     * Subtotal del pedido (suma de subtotales de líneas).
     */
    @Column(nullable = false)
    private BigDecimal subtotal;
    
   
    /**
     * Total del pedido (subtotal +/- gastos/envío/descuentos si aplica).
     */
    @Column(nullable = false)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPedido estado;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoLinea> lineas = new ArrayList<>();

    public enum EstadoPedido {
        EN_PREPARACION,
        ENVIADO,
        ENTREGADO,
        CANCELADO
    }

    // Getters y setters

    public Long getId() {
        return id;
    }

    public Long getUsuarioId() {
       return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
    
    

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public LocalDateTime getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDateTime fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public List<PedidoLinea> getLineas() {
        return lineas;
    }

    public void setLineas(List<PedidoLinea> lineas) {
        this.lineas = lineas;
    }
}
