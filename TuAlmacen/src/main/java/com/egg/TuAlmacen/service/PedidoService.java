package com.egg.TuAlmacen.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egg.TuAlmacen.entidad.Pedido;
import com.egg.TuAlmacen.entidad.Producto;
import com.egg.TuAlmacen.enums.Estado;
import com.egg.TuAlmacen.error.ErrorService;
import com.egg.TuAlmacen.repositorio.PedidoRepositorio;



@Service
public class PedidoService {

	@Autowired
	private PedidoRepositorio pedidoRepositorio;
	
	
    public List<Pedido> pendientes(){
        return pedidoRepositorio.pendientes();
    }
	
    public Pedido buscarPorId(String id) {
		
		return pedidoRepositorio.getOne(id);
	}
	
	public List<Pedido>findAll(){
		
		return pedidoRepositorio.findAll();
		
	}
	
	@Transactional
	public void registrarPedido(List<Producto> productos,List<Integer> cantidades,Date fecha,Double precioTotal,Estado estado) throws ErrorService {
		 
		validar(productos,cantidades,fecha,precioTotal,estado);
		
		Pedido pedido = new Pedido();
		pedido.setCantidad(cantidades);
		pedido.setProductos(productos);
		pedido.setFecha(new Date());
		pedido.setPrecioTotal(precioTotal);
		pedido.setEstado(estado);
		
		pedidoRepositorio.save(pedido);
		
	}
	
	@Transactional
	public void modificarPedido(String id,List<Producto> productos,List<Integer> cantidades,Date fecha,Double precioTotal,Estado estado) throws ErrorService {
		
		validar(productos,cantidades,fecha,precioTotal,estado);
		
		Optional<Pedido> respuesta = pedidoRepositorio.findById(id);
		
		if(respuesta.isPresent()) {
			
			Pedido pedido = new Pedido();
			pedido.setCantidad(cantidades);
			pedido.setProductos(productos);
			pedido.setFecha(fecha);
			pedido.setPrecioTotal(precioTotal);
			pedido.setEstado(estado);
			
			pedidoRepositorio.save(pedido);
			
		}
	}
	
	@Transactional
	public void eliminarPedido(String id) throws ErrorService {
		
		Optional<Pedido> respuesta = pedidoRepositorio.findById(id);
		
		if(respuesta.isPresent()) {
			
			Pedido pedido = respuesta.get();
			
			pedidoRepositorio.delete(pedido);
			
		}else {
			
			throw new ErrorService("No se encontro el pedido solicitado");
		}
	}
	
	public void validar(List<Producto> productos,List<Integer> cantidades, Date fecha,Double precioTotal,Estado estado) throws ErrorService {
		
		if(productos == null || productos.isEmpty()) {
			
			throw new ErrorService("La lista de productos no puede estar vacía");
		}
		if(cantidades == null || cantidades.isEmpty()) {
			
			throw new ErrorService("La lista de cantidades no puede estar vacía");
		}

		if(fecha == null) {
			
			throw new ErrorService("La fecha no puede ser nula");
		}
		if(precioTotal == null || precioTotal < 0) {
			
			throw new ErrorService("El precio total no puede ser nulo");
		}
		if(estado == null) {
			
			throw new ErrorService("El estado del pedido no puede ser nulo");
		}
       
	}
	
}
