package com.egg.TuAlmacen.controlador;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.egg.TuAlmacen.entidad.Comentario;
import com.egg.TuAlmacen.entidad.Producto;
import com.egg.TuAlmacen.error.ErrorService;
import com.egg.TuAlmacen.service.ComentarioService;
import com.egg.TuAlmacen.service.ProductoService;

@Controller
public class ComentarioUsuarioController {

	@Autowired
	private ComentarioService comentarioService;
	
	@Autowired
	private ProductoService productoService;
	
	@PreAuthorize("hasRole('ROLE_USUARIO')")
	@GetMapping("/comentarioproducto/{idproducto}")
	public String comentariosproducto(ModelMap modelo,@PathVariable String idproducto) throws ErrorService{

				Producto producto = productoService.buscarPorId(idproducto);
				modelo.put("producto", producto);
				
				List<Comentario> comentario = comentarioService.comentarioPorProducto(idproducto);
				
				modelo.put("comentario", comentario);
		        return "comentarios.html";
		    }
	
	
	
	@PreAuthorize("hasRole('ROLE_USUARIO')")
	@PostMapping("/comentar")
	public String comentar(ModelMap modelo,
			HttpSession session,
			@RequestParam String comentario,
			@RequestParam String idProducto,
			@RequestParam String idUsuario) {
		
		
		try {			
			
			comentarioService.registrarComentario(comentario, idProducto, idUsuario);
				
			modelo.put("mensaje", "Se ha realizado el comentario exitosamente");
			
		}catch(ErrorService e) {
			modelo.addAttribute("error", e.getMessage());
			return "redirect:/productos";
		}
		return "redirect:/productos";
		
	}
	
	 
	 @PreAuthorize("hasRole('ROLE_USUARIO')")
		@PostMapping("/editar")
		public String editar(ModelMap modelo,
				HttpSession session,
				@RequestParam String id,
				@RequestParam String comentario,
				@RequestParam String idProducto,
				@RequestParam String idUsuario) {
			
			
			try {
				
				comentarioService.modificarComentario(id, comentario, idProducto, idUsuario);
				
		
				modelo.put("mensaje", "Se ha modificado el comentario exitosamente");
				
			}catch(ErrorService e) {
				modelo.addAttribute("error", e.getMessage());
				return "redirect:/productos";
			}
			return "redirect:/productos";
			
		}
	 
	 @PreAuthorize("hasRole('ROLE_USUARIO')")
		@PostMapping("/borrar")
		public String borrar(ModelMap modelo,
				HttpSession session,
				@RequestParam String id) {
			
			
			try {			
				comentarioService.eliminarComentario(id);	
				modelo.put("mensaje", "Se ha eliminado el comentario exitosamente");
				
			}catch(ErrorService e) {
				modelo.addAttribute("error", e.getMessage());
				return "redirect:/productos";
			}
			return "redirect:/productos";
			
		}
	
}
