package com.egg.TuAlmacen.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egg.TuAlmacen.entidad.Comentario;
import com.egg.TuAlmacen.entidad.Foto;
import com.egg.TuAlmacen.entidad.Producto;
import com.egg.TuAlmacen.entidad.Usuario;
import com.egg.TuAlmacen.enums.Rubro;
import com.egg.TuAlmacen.error.ErrorService;
import com.egg.TuAlmacen.repositorio.ComentarioRepositorio;
import com.egg.TuAlmacen.repositorio.ProductoRepositorio;
import com.egg.TuAlmacen.repositorio.UsuarioRepositorio;


@Service
public class ComentarioService {
	@Autowired
	private ComentarioRepositorio comentarioRepositorio;
	@Autowired
	private ProductoRepositorio productoRepositorio;
	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	
	
	
	
    public Comentario buscarPorId(String id) {
		
		return comentarioRepositorio.getOne(id);
	}
	
	public List<Comentario>findAll(){
		
		return comentarioRepositorio.findAll();
		
	}
	
	@Transactional
	public void registrarComentario(String comentario,String idProducto, String idUsuario
			) throws ErrorService {
		
		 Producto producto = productoRepositorio.getOne(idProducto);       
	     Usuario usuario = usuarioRepositorio.getOne(idUsuario);
		 Comentario c = new Comentario();
		
		c.setComentario(comentario);
		c.setProducto(producto);
		c.setUsuario(usuario);

		comentarioRepositorio.save(c);
		
		
	}
	
	@Transactional
	public void modificarComentario(String id,String comentario,String idProducto, String idUsuario) throws ErrorService {
		
		Producto producto = productoRepositorio.getOne(idProducto);       
	     Usuario usuario = usuarioRepositorio.getOne(idUsuario);
		
		Optional<Comentario> respuesta = comentarioRepositorio.findById(id);
		
		if(respuesta.isPresent()) {
			
			Comentario c = respuesta.get();
			
			c.setComentario(comentario);
			c.setProducto(producto);
			c.setUsuario(usuario);
			
	
			comentarioRepositorio.save(c);
			
			
		}else {
			throw new ErrorService("No se ha encontrado el comentario solicitado");
		}
		
	}
	
	@Transactional
	public void eliminarComentario(String id) throws ErrorService {
		
		Optional<Comentario> respuesta = comentarioRepositorio.findById(id);
		
		if(respuesta.isPresent()) {
			
			Comentario c = respuesta.get();
			
			comentarioRepositorio.delete(c);
			
		}else {
			throw new ErrorService("No se ha encontrado el comentario solicitado");
		}
	}

	public void validar(String comentario ) throws ErrorService {
		
		if(comentario == null || comentario.isEmpty()) {
			
			throw new ErrorService("El comentario no puede ser nulo");
		}	
		
	}
}
