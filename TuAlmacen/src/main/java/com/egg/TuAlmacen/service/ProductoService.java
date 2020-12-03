package com.egg.TuAlmacen.service;

import com.egg.TuAlmacen.entidad.Comentario;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.egg.TuAlmacen.entidad.Foto;
import com.egg.TuAlmacen.entidad.Pedido;
import com.egg.TuAlmacen.entidad.Producto;
import com.egg.TuAlmacen.enums.Rubro;
import com.egg.TuAlmacen.error.ErrorService;
import com.egg.TuAlmacen.repositorio.FotoRepositorio;
import com.egg.TuAlmacen.repositorio.ProductoRepositorio;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepositorio productoRepositorio;
    @Autowired
    private FotoRepositorio fotoRepositorio;
    @Autowired
    private PedidoService pedidoService;
    @Autowired
    private ComentarioService comentarioService;
    @Autowired
    private FotoService fotoService;

    public Producto buscarPorId(String id) {

        return productoRepositorio.getOne(id);
    }

    public List<Producto> findAll() {

        return productoRepositorio.findAll();

    }
    
    public Producto buscarPorNombre(String nombre){
        return productoRepositorio.buscarPorNombre(nombre);
    }
    
    public List<Producto> buscarPorRubro(Rubro rubro){
        return productoRepositorio.buscarPorRubro(rubro);
    };

    @Transactional
    public List<Producto> listarProducto() throws ErrorService {

        List<Producto> respuesta = productoRepositorio.findAll();

        if (respuesta != null) {

            return respuesta;

        } else {
            throw new ErrorService("No se encontro productos");
        }

    }

    @Transactional
    public void registrarProducto(String nombre, Double precioCompra, Integer cantidad, Double precioVenta, String descripcion,
            MultipartFile archivo, Rubro rubro) throws ErrorService {

        validar(nombre, precioCompra, cantidad, precioVenta, descripcion, rubro);

        Producto producto = new Producto();

        producto.setNombre(nombre);
        producto.setPrecioCompra(precioCompra);
        producto.setCantidad(cantidad);
        producto.setPrecioVenta(precioVenta);
        producto.setDescripcion(descripcion);
        if (archivo != null) {
            Foto foto = fotoService.guardar(archivo);
            producto.setFoto(foto);
        }
        producto.setRubro(rubro);

        productoRepositorio.save(producto);

    }

    @Transactional
    public void modificarProducto(String id, String nombre, Double precioCompra, Integer cantidad, Double precioVenta, String descripcion,
            MultipartFile archivo, Rubro rubro) throws ErrorService {

        validar(nombre, precioCompra, cantidad, precioVenta, descripcion, rubro);

        Optional<Producto> respuesta = productoRepositorio.findById(id);

        if (respuesta.isPresent()) {

            Producto producto = respuesta.get();

            producto.setNombre(nombre);
            producto.setPrecioCompra(precioCompra);
            producto.setCantidad(cantidad);
            producto.setPrecioVenta(precioVenta);
            producto.setDescripcion(descripcion);
            if (archivo != null) {
                Foto foto = fotoService.guardar(archivo);
                producto.setFoto(foto);
            }
            producto.setRubro(rubro);

            productoRepositorio.save(producto);

        } else {
            throw new ErrorService("No se ha encontrado el producto solicitado");
        }

    }

    @Transactional
    public void eliminarProducto(String id) throws ErrorService {

        Optional<Producto> respuesta = productoRepositorio.findById(id);

        if (respuesta.isPresent()) {

            Producto producto = respuesta.get();
                    
            List<Pedido> pedidos = pedidoService.pedidosPorProducto(producto);
            
            for (Pedido pedido : pedidos) {
                pedidoService.quitar(pedido, producto);
            }
            
            List<Comentario> comentarios = comentarioService.listarComentariosPorProducto(id);
            
            for (Comentario comentario : comentarios) {
                comentarioService.eliminarComentario(comentario.getId());
            }
            
            fotoRepositorio.delete(producto.getFoto());
            productoRepositorio.delete(producto);

        } else {
            throw new ErrorService("No se ha encontrado el producto solicitado");
        }
    }

    public List<Producto> listarProductosPorRubro(String rubro) {
        return productoRepositorio.buscarPorRubro(Rubro.valueOf(rubro));
    }

    public List<Producto> listarSeis() {
        return productoRepositorio.buscarPrimeroRubro();
    }

    public void validar(String nombre, Double precioCompra, Integer cantidad, Double precioVenta, String descripcion,
            Rubro rubro) throws ErrorService {

        if (nombre == null || nombre.isEmpty()) {

            throw new ErrorService("El nombre no puede ser nulo");
        }

        if (precioCompra == null || precioCompra < 0) {

            throw new ErrorService("El precio de compra no puede ser nulo");
        }

        if (cantidad == null || cantidad < 0) {

            throw new ErrorService("La cantidad no puede ser nula");
        }

        if (precioVenta == null || precioVenta < 0) {

            throw new ErrorService("El precio de venta no puede ser nulo");
        }

        if (descripcion == null || descripcion.isEmpty()) {

            throw new ErrorService("La descripción no puede ser nula");
        }

        if (rubro == null) {

            throw new ErrorService("Debe indicar a que rubro pertenece el producto");
        }
    }

}
