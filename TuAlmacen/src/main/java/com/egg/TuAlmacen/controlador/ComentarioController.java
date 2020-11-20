package com.egg.TuAlmacen.controlador;

import com.egg.TuAlmacen.entidad.Comentario;
/**
 *
 * @author octav
 */
import com.egg.TuAlmacen.entidad.Pedido;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.egg.TuAlmacen.entidad.Producto;
import com.egg.TuAlmacen.entidad.Usuario;
import com.egg.TuAlmacen.enums.Rubro;
import com.egg.TuAlmacen.error.ErrorService;
import com.egg.TuAlmacen.service.ComentarioService;
import com.egg.TuAlmacen.service.PedidoService;
import com.egg.TuAlmacen.service.ProductoService;
import com.egg.TuAlmacen.service.UsuarioService;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Controller;

@Controller
public class ComentarioController {
    
    @Autowired
    private ComentarioService comentarioService;
    @Autowired
    private ProductoService productoService;
    

    @Autowired
    private HttpSession session;
    
    
    
    @GetMapping("/producto/{id}")
    public String comentarios(ModelMap modelo,@PathVariable String id) throws ErrorService{
    	Producto l = productoService.buscarPorId(id);
        modelo.put("producto", l);
		List<Comentario> comentario = comentarioService.listarComentario();		
		modelo.put("comentario", comentario);
        return "comentarios.html";
    }
    
    
	@PostMapping("/crear-comentario")
	public String registrarComentario(ModelMap modelo,@RequestParam String comentario,@RequestParam String idProducto,@RequestParam String idUsuario) {
		
		try {
			comentarioService.registrarComentario(comentario, idProducto, idUsuario);
		} catch (ErrorService e) {
			modelo.put("error", e.getMessage());
			modelo.put("comentario", comentario);
		
			return "redirect:/crear-comentario";
		}
		return "redirect:/productos";
	}
   
	
	@PostMapping("/modificarComentario")
	public String modificarComentario(ModelMap modelo,@RequestParam String id,@RequestParam String comentario,@RequestParam String idProducto,@RequestParam String idUsuario) {
		
		try {
			comentarioService.modificarComentario(id,comentario, idProducto, idUsuario);
		} catch (ErrorService e) {
			modelo.put("error", e.getMessage());
			modelo.put("comentario", comentario);
		
			return "redirect:/crear-comentario";
		}
		return "redirect:/productos";
	}
    
   
	@PostMapping("/bajacomentario")
	public String bajaproducto(ModelMap modelo,
			HttpSession session,
			@RequestParam String id) {
		
		
		try {
			
			comentarioService.eliminarComentario(id);
			
	
			modelo.put("mensaje", "Se ha eliminado el producto exitosamente");
			
		}catch(ErrorService e) {
			modelo.addAttribute("error", e.getMessage());
			return "redirect:/bajacomentario";
		}
		return "redirect:/productos";
		
	}
	   
}
