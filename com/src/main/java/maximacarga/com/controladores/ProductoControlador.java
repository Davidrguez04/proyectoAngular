package maximacarga.com.controladores;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import maximacarga.com.entidades.Producto;
import maximacarga.com.servicios.ProductoServicio;

@RestController
@RequestMapping("/api/productos")
public class ProductoControlador {

    private final ProductoServicio productoServicio;

    // Constructor que inyecta el servicio de productos
    public ProductoControlador(ProductoServicio productoServicio) {
        this.productoServicio = productoServicio;
    }

    /**
     * Método que sirve para crear un nuevo producto.
     * Valida que nombre, precio y stock sean correctos antes de guardarlo.
     *
     * @param producto Producto recibido en el body de la petición.
     * @return 200 OK si se crea correctamente,
     *         400 Bad Request si hay datos inválidos.
     */
    @PostMapping
    public ResponseEntity<String> crearProducto(@RequestBody Producto producto) {

        if (producto.getNombre() == null || producto.getNombre().isBlank()) {
            return ResponseEntity.badRequest().body("El nombre es obligatorio");
        }

        if (producto.getPrecio() == null || producto.getPrecio() < 0) {
            return ResponseEntity.badRequest().body("El precio no es válido");
        }

        if (producto.getStock() == null || producto.getStock() < 0) {
            return ResponseEntity.badRequest().body("El stock no es válido");
        }

        productoServicio.crearProducto(producto);
        return ResponseEntity.ok("success");
    }
    /**
     * Método que sirve para eliminar un producto por su ID.
     *
     * @param id ID del producto.
     * @return 200 OK si se elimina correctamente,
     *         500 si ocurre un error.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarProducto(@PathVariable Long id) {
        boolean eliminado = productoServicio.eliminarProductoPorId(id);
        if (eliminado) {
            return ResponseEntity.ok("success");
        } else {
            return ResponseEntity.internalServerError().body("error");
        }
    }

    /**
     * Método que sirve para obtener una lista con todos los productos.
     *
     * @return Lista de productos disponibles.
     */
    @GetMapping
    public List<Producto> listarProductos() {
        return productoServicio.listarProductos();
    }
    
    /**
     * Método que sirve para obtener la imagen de un producto.
     *
     * @param id ID del producto.
     * @return Imagen en formato byte[] o 404 si no existe.
     */
    @GetMapping("/{id}/imagen")
    public ResponseEntity<byte[]> verImagen(@PathVariable Long id) {
        Producto p = productoServicio.obtenerPorId(id); // crea este método si no lo tienes
        if (p == null || p.getImagenProducto() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
            .header("Content-Type", "image/jpeg") // o image/png si subes png
            .body(p.getImagenProducto());
    }

    /**
     * Método que sirve para obtener un producto por su ID.
     *
     * @param id ID del producto.
     * @return Producto encontrado.
     */
    @GetMapping("/{id}")
    public Producto obtenerProductoPorId(@PathVariable Long id) {

        return productoServicio.obtenerPorId(id);
    }
    
    /**
     * Método que permite actualizar los datos de un producto existente.
     *
     * @param id ID del producto.
     * @param productoActualizado Nuevos datos del producto.
     * @return Producto actualizado.
     */
    @PutMapping("/{id}")
    public Producto actualizarProducto(@PathVariable Long id,
                                        @RequestBody Producto productoActualizado) {

        return productoServicio.actualizarProducto(id, productoActualizado);
    }
    }
    
