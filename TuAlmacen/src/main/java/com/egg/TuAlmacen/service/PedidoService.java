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
import com.egg.TuAlmacen.enums.Rubro;
import com.egg.TuAlmacen.error.ErrorService;
import com.egg.TuAlmacen.formato.Ventas;
import com.egg.TuAlmacen.repositorio.PedidoRepositorio;
import com.egg.TuAlmacen.repositorio.ProductoRepositorio;
import java.util.ArrayList;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepositorio pedidoRepositorio;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private NotificacionMail notificacionMail;

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

        validar(productos, cantidades, estado);

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

    @Transactional
    public void registrarPedido(List<Producto> productos, List<Integer> cantidades, Estado estado, Usuario usuario) throws ErrorService {

        validar(productos, cantidades, estado);

        Pedido pedido = new Pedido();
        pedido.setCantidad(cantidades);
        pedido.setProductos(productos);
        pedido.setFecha(new Date());
        pedido.setPrecioTotal(this.calcularTotal(productos, cantidades));
        pedido.setEstado(estado);

        pedido.setUsuario(usuario);
        pedidoRepositorio.save(pedido);

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

        validar(productos, cantidades, estado);

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

    public boolean verificarCantidades(Producto producto, Integer cantidad) {
        if (producto.getCantidad() < cantidad) {
            return false;
        }
        return true;
    }

    public void verificar(Pedido pe) {
        boolean stock = true;
        for (int i = 0; i < pe.getProductos().size(); i++) {
            if (pe.getCantidad().get(i) > pe.getProductos().get(i).getCantidad()) {
                stock = false;
                break;
            }
        }
        if (!stock) {
            try {
                this.anular(pe);
            } catch (Exception ex) {
                Logger.getLogger(PedidoService.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                for (int i = 0; i < pe.getProductos().size(); i++) {
                    Producto pr = pe.getProductos().get(i);
                    pr.setCantidad(pr.getCantidad() - pe.getCantidad().get(i));
                    productoService.modificarProducto(pr.getId(), pr.getNombre(), pr.getPrecioCompra(), pr.getCantidad(), pr.getPrecioVenta(), pr.getDescripcion(), null, pr.getRubro());
                }
                this.confirmar(pe);
            } catch (Exception ex) {
                Logger.getLogger(PedidoService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public Pedido carrito(String id) {
        return pedidoRepositorio.carrito(id);
    }

    @Transactional
    public void miCarrito(Usuario usuario, Producto producto, Integer cantidad) throws ErrorService {
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
    public void agregar(Pedido pedido, Producto producto, Integer cantidad) throws ErrorService {
        if (this.verificarCantidades(producto, cantidad)) {
            boolean x = false;

            List<Producto> productos = pedido.getProductos();
            List<Integer> cantidades = pedido.getCantidad();

            if (!productos.isEmpty() && productos != null) {
                for (int i = 0; i < productos.size(); i++) {
                    if (productos.get(i).equals(producto)) {
                        x = true;
                        if (this.verificarCantidades(producto, cantidad + cantidades.get(i))) {
                            cantidades.set(i, cantidad + cantidades.get(i));
                        } else {
                            throw new ErrorService("las cantidades superan al stock");
                        }
                        break;
                    }
                }
            }

            if (!x) {
                productos.add(producto);
                cantidades.add(cantidad);
            }

            pedido.setProductos(productos);
            pedido.setCantidad(cantidades);

            pedidoRepositorio.save(pedido);
        } else {
            throw new ErrorService("las cantidades superan al stock");
        }
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

        notificacionMail.enviar("Su pedido fue enviado y se encuentra pendiente de aprobacion ", "Tu Almacen", pedido.getUsuario().getEmail());

        pedidoRepositorio.save(pedido);

    }

    @Transactional
    public void anular(Pedido p) throws Exception {
        try {
            p.setEstado(Estado.ANULADO);
            pedidoRepositorio.save(p);
            notificacionMail.enviar("Su pedido se ha anulado ", "Tu Almacen", p.getUsuario().getEmail());
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    @Transactional
    public void confirmar(Pedido p) throws Exception {
        try {
            p.setEstado(Estado.CONFIRMADO);
            pedidoRepositorio.save(p);
            notificacionMail.enviar("Su pedido fue confirmado y el monto a pagar es $ " + p.getPrecioTotal() + " gracias por su compra. ", "Tu Almacen", p.getUsuario().getEmail());
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    @Transactional
    public List<Pedido> listarPedidosPorEstado(String idUsuario, Estado estado) {
        return pedidoRepositorio.pedidosPorEstado(idUsuario, estado);
    }

    @Transactional
    public List<Pedido> listarPedidosPorEstado(Estado estado) {
        return pedidoRepositorio.pedidosPorEstado(estado);
    }

    public void validar(List<Producto> productos, List<Integer> cantidades, Estado estado) throws ErrorService {

        if (productos == null || productos.isEmpty()) {

            throw new ErrorService("La lista de productos no puede estar vacía");
        }
        if (cantidades == null || cantidades.isEmpty()) {

            throw new ErrorService("La lista de cantidades no puede estar vacía");
        }

        if (estado == null) {

            throw new ErrorService("El estado del pedido no puede ser nulo");
        }

    }

    public List mispedidos(String idUsuario) {
        return pedidoRepositorio.mispedidos(idUsuario);
    }

    public List<Ventas> masVendidos() throws ErrorService {
        List<Ventas> masVendidos = new ArrayList<>();
        List<Producto> productos = productoService.listarProducto();
        List<Pedido> confirmados = pedidoRepositorio.pedidosPorEstado(Estado.CONFIRMADO);
        for (Producto p : productos) {
            Ventas v = new Ventas();
            v.setProducto(p);
            v.setVendidos(0);
            for (Pedido pedido : confirmados) {
                if (pedido.getProductos().contains(p)) {
                    v.setVendidos(v.getVendidos() + pedido.getCantidad().get(pedido.getProductos().indexOf(p)));
                }
            }
            masVendidos.add(v);
        }
	for(int i=0;i<masVendidos.size();i++){
		for(int j=i+1;j<masVendidos.size();j++){
			Ventas actual = masVendidos.get(i);
			Ventas siguiente = masVendidos.get(j); 
			if(actual.getVendidos()<siguiente.getVendidos()){
				masVendidos.set(i,siguiente);
				masVendidos.set(j,actual);
			}
		}
	}
        return masVendidos;
    }

}
