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

import edu.egg.entidades.Editorial;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Controller;

@Controller
public class ComentarioAdminController {
    
    @Autowired
    private ComentarioService comentarioService;
    

    @Autowired
    private HttpSession session;
    
    @GetMapping("/comentarios")
    public String comentarios(ModelMap modelo) throws ErrorService{

		List<Comentario> comentario = comentarioService.listarComentario();
		
		modelo.put("comentario", comentario);
        return "comentarios.html";
    }
   
    @PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/bajacomentario")
	public String bajaproducto(ModelMap modelo,
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/modificarcomentario")
	public String modificacionproducto(ModelMap modelo,
			HttpSession session,
			@RequestParam String id,@RequestParam String comentario,@RequestParam String idProducto,@RequestParam String idUsuario) {
		
		
		try {
			
			comentarioService.modificarComentario(id, comentario, idProducto, idUsuario);
			
	
			modelo.put("mensaje", "Se ha modificado el comentario exitosamente");
			
		}catch(ErrorService e) {
			modelo.addAttribute("error", e.getMessage());
			return "redirect:/productos";
		}
		return "redirect:/productos";
		
	}
	   
}
