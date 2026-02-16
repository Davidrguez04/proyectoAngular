package maximacarga.com.servicios;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import maximacarga.com.entidades.Producto;
import maximacarga.com.repositorios.ProductoRepositorio;

/**
 * Servicio que gestiona la lógica de negocio relacionada con los productos.
 * Se encarga de crear, listar, obtener, actualizar y eliminar productos.
 */
@Service
public class ProductoServicio {

    private final ProductoRepositorio productoRepositorio;

    public ProductoServicio(ProductoRepositorio productoRepositorio) {
        this.productoRepositorio = productoRepositorio;
    }

    /**
     * Crea un nuevo producto en la base de datos.
     * Se ejecuta dentro de una transacción.
     *
     * @param producto Producto a guardar.
     * @return Producto guardado.
     */
    @Transactional
    public Producto crearProducto(Producto producto) {
        return productoRepositorio.save(producto);
    }
   
    /**
     * Devuelve todos los productos registrados.
     * Se marca como readOnly para optimizar la consulta.
     *
     * @return Lista de productos.
     */
    @Transactional(readOnly = true)
    public List<Producto> listarProductos() {
        return productoRepositorio.findAll();
    }

    /**
     * Elimina un producto por su ID.
     *
     * @param id ID del producto.
     * @return true si se elimina correctamente,
     *         false si no existe.
     */
    public boolean eliminarProductoPorId(Long id) {
        if (!productoRepositorio.existsById(id)) {
            return false;
        }
        productoRepositorio.deleteById(id);
        return true;
    }
    

    /**
     * Obtiene un producto por su ID.
     *
     * @param id ID del producto.
     * @return Producto encontrado o null si no existe.
     */
    public Producto obtenerPorId(Long id) {
        return productoRepositorio.findById(id).orElse(null);
    }
    
    
    /**
     * Actualiza los datos de un producto existente.
     * Lanza excepción si no se encuentra.
     *
     * @param id ID del producto.
     * @param datos Nuevos datos a actualizar.
     * @return Producto actualizado.
     */
    public Producto actualizarProducto(Long id, Producto datos) {

        Producto producto = productoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        producto.setNombre(datos.getNombre());
        producto.setDescripcion(datos.getDescripcion());
        producto.setPrecio(datos.getPrecio());
        producto.setStock(datos.getStock());
        producto.setImagenProducto(datos.getImagenProducto());

        return productoRepositorio.save(producto);
    }
    
}
