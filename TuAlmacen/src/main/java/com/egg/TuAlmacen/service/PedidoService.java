package com.egg.TuAlmacen.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egg.TuAlmacen.entidad.Pedido;
import com.egg.TuAlmacen.entidad.Producto;
import com.egg.TuAlmacen.entidad.Usuario;
import com.egg.TuAlmacen.enums.Estado;
import com.egg.TuAlmacen.error.ErrorService;
import com.egg.TuAlmacen.repositorio.PedidoRepositorio;
import com.egg.TuAlmacen.repositorio.ProductoRepositorio;
import java.util.ArrayList;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepositorio pedidoRepositorio;

    public List<Pedido> pendientes() {
        return pedidoRepositorio.pendientes();
    }

    public Pedido buscarPorId(String id) {

        return pedidoRepositorio.getOne(id);
    }

    public List<Pedido> findAll() {

        return pedidoRepositorio.findAll();

    }

    @Transactional
    public void registrarPedido(List<Producto> productos, List<Integer> cantidades, Date fecha, Estado estado, Usuario usuario) throws ErrorService {

        validar(productos, cantidades, fecha, estado);

        Pedido pedido = new Pedido();
        pedido.setCantidad(cantidades);
        pedido.setProductos(productos);
        pedido.setFecha(new Date());
        pedido.setPrecioTotal(this.calcularTotal(productos, cantidades));
        pedido.setEstado(estado);

        pedido.setUsuario(usuario);
        pedidoRepositorio.save(pedido);

    }

    public Double calcularTotal(List<Producto> productos, List<Integer> cantidad) {
        double total = 0;

        for (int i = 0; i < productos.size(); i++) {
            total += productos.get(i).getPrecioVenta() * cantidad.get(i);
        }
        return total;
    }

    public Date convertirStringADate(String fecha) {

        try {
            DateFormat fechaHora = new SimpleDateFormat("yyyy-MM-dd");
            Date convertido = fechaHora.parse(fecha);
            return convertido;
        } catch (java.text.ParseException ex) {
            Logger.getLogger(PedidoService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Transactional
    public void modificarPedido(String id, List<Producto> productos, List<Integer> cantidades, Estado estado) throws ErrorService {

        validar(productos, cantidades, new Date(), estado);

        Optional<Pedido> respuesta = pedidoRepositorio.findById(id);

        if (respuesta.isPresent()) {

            Pedido pedido = respuesta.get();
            pedido.setCantidad(cantidades);
            pedido.setProductos(productos);

            pedido.setPrecioTotal(this.calcularTotal(productos, cantidades));
            pedido.setEstado(estado);

            pedidoRepositorio.save(pedido);

        }
    }

    @Transactional
    public void eliminarPedido(String id) throws ErrorService {

        Optional<Pedido> respuesta = pedidoRepositorio.findById(id);

        if (respuesta.isPresent()) {

            Pedido pedido = respuesta.get();

            pedidoRepositorio.delete(pedido);

        } else {

            throw new ErrorService("No se encontro el pedido solicitado");
        }
    }

    public void verificarCantidades(List<Producto> productos, List<Integer> cantidades) {
        for (int i = 0; i < productos.size(); i++) {

            if (productos.get(i).getCantidad() <= cantidades.get(i)) {
                cantidades.set(i, productos.get(i).getCantidad());

            }
        }

    }

    public Pedido carrito(String id) {
        return pedidoRepositorio.carrito(id);
    }

    @Transactional
    public void miCarrito(Usuario usuario, Producto producto, Integer cantidad) {
        Pedido p = new Pedido();
        p.setUsuario(usuario);
        p.setEstado(Estado.CARRITO);
        p.setFecha(new Date());
        List<Producto> productos = new ArrayList<Producto>();
        List<Integer> cantidades = new ArrayList<Integer>();
        p.setProductos(productos);
        p.setCantidad(cantidades);
        System.out.println("ANTES DE AGREGAR");
        this.agregar(p, producto, cantidad);
    }

    @Transactional
    public void agregar(Pedido pedido, Producto producto, Integer cantidad) {

        boolean x = false;

        System.out.println("ANTES DEL LIST");
        List<Producto> productos = pedido.getProductos();
        List<Integer> cantidades = pedido.getCantidad();

        if (!productos.isEmpty() && productos != null) {
            System.out.println("ENTRO AL IF DE AGREGAR");
            for (int i = 0; i < productos.size(); i++) {
                if (productos.get(i).equals(producto)) {
                    x = true;
                    cantidades.set(i, cantidad + cantidades.get(i));
                    break;
                }
            }
        }
        
        System.out.println("SALIO DEL IF");
        if (!x) {
            productos.add(producto);
            cantidades.add(cantidad);
        }

        System.out.println("AGREGO PRODUCTO Y CANTIDAD");
        pedido.setProductos(productos);
        pedido.setCantidad(cantidades);

        System.out.println("SETEO LISTAS");
        pedidoRepositorio.save(pedido);
        
        System.out.println("GUARDO PEDIDO");

    }

    @Transactional
    public void quitar(Pedido pedido, Producto producto) {

        List<Producto> productos = pedido.getProductos();
        List<Integer> cantidades = pedido.getCantidad();

        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).equals(producto)) {
                productos.remove(i);
                cantidades.remove(i);
                break;
            }
        }

        pedido.setProductos(productos);
        pedido.setCantidad(cantidades);

        pedidoRepositorio.save(pedido);

    }

    @Transactional
    public void confirmarCarrito(Pedido pedido, Double total) {

        pedido.setFecha(new Date());
        pedido.setPrecioTotal(total);
        pedido.setEstado(Estado.PENDIENTE);

        pedidoRepositorio.save(pedido);

    }

    public void validar(List<Producto> productos, List<Integer> cantidades, Date fecha, Estado estado) throws ErrorService {

        if (productos == null || productos.isEmpty()) {

            throw new ErrorService("La lista de productos no puede estar vacía");
        }
        if (cantidades == null || cantidades.isEmpty()) {

            throw new ErrorService("La lista de cantidades no puede estar vacía");
        }

        if (fecha == null) {

            throw new ErrorService("La fecha no puede ser nula");
        }

        if (estado == null) {

            throw new ErrorService("El estado del pedido no puede ser nulo");
        }
        this.verificarCantidades(productos, cantidades);

    }

}
