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
import com.egg.TuAlmacen.entidad.Pedido;
import com.egg.TuAlmacen.entidad.Producto;
import com.egg.TuAlmacen.entidad.Usuario;
import com.egg.TuAlmacen.error.ErrorService;
import com.egg.TuAlmacen.service.ComentarioService;
import com.egg.TuAlmacen.service.PedidoService;
import com.egg.TuAlmacen.service.ProductoService;

@Controller
public class ComentarioUsuarioController {

    @Autowired
    private ComentarioService comentarioService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private HttpSession session;

    @PreAuthorize("hasRole('ROLE_USUARIO')||hasRole('ROLE_ADMIN')")
    @GetMapping("/comentarioproducto/{idproducto}")
    public String comentariosproducto(ModelMap modelo, @PathVariable String idproducto) throws ErrorService {

        Producto producto = productoService.buscarPorId(idproducto);
        modelo.put("producto", producto);

        List<Comentario> comentario = comentarioService.comentarioPorProducto(idproducto);

        modelo.put("comentarios", comentario);

        Integer largoCarrito = 0;
        Usuario u = (Usuario) session.getAttribute("usuariosession");
        Pedido p = pedidoService.carrito(u.getId());
        if (p != null) {
            largoCarrito = p.getProductos().size();
        }
        modelo.put("largocarrito", largoCarrito);

        List<Producto> productos = productoService.buscarPorRubro(producto.getRubro());
        Integer index = productos.indexOf(producto);
        Integer size = productos.size();

        if (index > 0) {
            Producto anterior = productos.get(index - 1);
            modelo.put("anterior", anterior);
        }

        if (index < size - 1) {
            Producto siguiente = productos.get(index + 1);
            modelo.put("siguiente", siguiente);
        }

        return "producto.html";
    }

    @PostMapping("/buscar")
    public String buscar(ModelMap modelo, @RequestParam(required=false) String producto) throws ErrorService{
        Producto p = productoService.buscarPorNombre(producto);
        if (p!=null){
            return this.comentariosproducto(modelo, p.getId());
        }else{
            return "redirect:/inicio";
        }
    }
    
    @PostMapping("/comentar")
    public String comentar(ModelMap modelo,
            HttpSession session,
            @RequestParam String comentario,
            @RequestParam String idProducto,
            @RequestParam String idUsuario) throws ErrorService {

        try {

            comentarioService.registrarComentario(comentario, idProducto, idUsuario);

            modelo.put("mensaje", "Se ha realizado el comentario exitosamente");

        } catch (ErrorService e) {
            modelo.addAttribute("error", e.getMessage());
            return this.comentariosproducto(modelo, idProducto);
        }
        return this.comentariosproducto(modelo, idProducto);

    }

    @PostMapping("/editar")
    public String editar(ModelMap modelo,
            HttpSession session,
            @RequestParam String id,
            @RequestParam String comentario,
            @RequestParam String idProducto,
            @RequestParam String idUsuario) throws ErrorService {

        try {

            comentarioService.modificarComentario(id, comentario, idProducto, idUsuario);

            modelo.put("mensaje", "Se ha modificado el comentario exitosamente");

        } catch (ErrorService e) {
            modelo.addAttribute("error", e.getMessage());
            return this.comentariosproducto(modelo, idProducto);
        }
        return this.comentariosproducto(modelo, idProducto);

    }

    @PostMapping("/borrar")
    public String borrar(ModelMap modelo,
            HttpSession session,
            @RequestParam String id) throws ErrorService {

        Comentario comentario = comentarioService.buscarPorId(id);
        try {
            comentarioService.eliminarComentario(id);
            modelo.put("mensaje", "Se ha eliminado el comentario exitosamente");

        } catch (ErrorService e) {
            modelo.addAttribute("error", e.getMessage());
            return this.comentariosproducto(modelo, comentario.getProducto().getId());
        }
        return this.comentariosproducto(modelo, comentario.getProducto().getId());

    }

}
