package com.egg.TuAlmacen.controlador;

import com.egg.TuAlmacen.entidad.Pedido;
import com.egg.TuAlmacen.entidad.Producto;
import com.egg.TuAlmacen.entidad.Usuario;
import com.egg.TuAlmacen.enums.Rubro;
import com.egg.TuAlmacen.error.ErrorService;
import com.egg.TuAlmacen.service.PedidoService;
import com.egg.TuAlmacen.service.ProductoService;
import com.egg.TuAlmacen.service.UsuarioService;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author octav
 */
@Controller
@RequestMapping("/")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private ProductoService productoService;
    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private HttpSession session;

    @PreAuthorize("hasRole('ROLE_USUARIO')||hasRole('ROLE_ADMIN')")
    @GetMapping("/inicio")
    public String inicio(ModelMap modelo, @RequestParam(required = false) String rubro) {
        
        try{
        List<Producto> productos;

        Set<Rubro> rubros = EnumSet.allOf(Rubro.class);
        modelo.put("rubros", rubros);

        if (rubro != null) {
            productos = productoService.listarProductosPorRubro(rubro);
        } else {
            productos = productoService.listarProducto();
        }

        modelo.put("productos", productos);
        
        Integer largoCarrito = 0;
        Usuario u = (Usuario) session.getAttribute("usuariosession");
        Pedido p = pedidoService.carrito(u.getId());
        if (p != null) {
            largoCarrito = p.getProductos().size();
        }
        modelo.put("largocarrito", largoCarrito);
        }catch(ErrorService ex){
            modelo.put("error", ex.getMessage());
            return "inicio.html";
        }
        return "inicio.html";
    }

    @PostMapping("/agregar")
    public String agregar(ModelMap modelo, @RequestParam String idUsuario, @RequestParam String id, @RequestParam Integer cantidad) throws ErrorService {

        try{
        System.out.println("AASDGFBQALWIEGASDFJHADF");

        Pedido pedido = pedidoService.carrito(idUsuario);
        System.out.println("BUSCO CARRITO");

        Producto producto = productoService.buscarPorId(id);

        if (pedido == null) {
            System.out.println("ADENTRO DEL IF NULL PEDIDO");
            pedidoService.miCarrito(usuarioService.buscarPorId(idUsuario), producto, cantidad);
        } else {

            System.out.println("ELSE DEL IF NULL PEDIDO");

            pedidoService.agregar(pedido, producto, cantidad);
        }
        }catch(ErrorService ex){
            modelo.put("error", ex.getMessage());
            return inicio(modelo, null);
        }
        return "redirect:/inicio";
    }
    
    @PreAuthorize("hasRole('ROLE_USUARIO')||hasRole('ROLE_ADMIN')")
    @GetMapping("/miperfil")
    public String miPerfil(ModelMap modelo) {

        Usuario u = (Usuario) session.getAttribute("usuariosession");
        Pedido pedido = pedidoService.carrito(u.getId());
        
        modelo.put("usuario", u);

        Integer largoCarrito = 0;
      
        if (pedido != null) {
            largoCarrito = pedido.getProductos().size();
        }
        modelo.put("largocarrito", largoCarrito);
        
        return "miperfil.html";

    }

    @PostMapping("/modificarperfil")
    public String registro(ModelMap modelo,
            HttpSession session,
            @RequestParam String id,
            @RequestParam String usuario,
            @RequestParam String password,
            @RequestParam String repetir,
            @RequestParam String email,
            @RequestParam String rol) throws ErrorService {

        try {

            Usuario usu = (Usuario) session.getAttribute("usuariosession");

            usuarioService.modificarUsuario(id, usuario, email, password, repetir, usu.getRol());

            usu = usuarioService.buscarPorId(id);

            session.setAttribute("usuariosession", usu);
            modelo.put("usuario", usu);

        } catch (ErrorService ex) {

            Usuario usu = (Usuario) session.getAttribute("usuariosession");

            modelo.addAttribute("usuario", usu);
            modelo.put("error", ex.getMessage());
            modelo.put("id", id);
            modelo.put("usuario", usuario);
            modelo.put("password", password);
            modelo.put("repetir", repetir);
            modelo.put("rol", rol);

            return "modificarperfil.html";

        }

        return "inicio.html";

    }

    @PostMapping("/bajaperfil")
    public String bajaperfil(ModelMap modelo,
            @RequestParam String id, HttpSession session) {

        try {

            Usuario usu = (Usuario) session.getAttribute("usuariosession");

            usuarioService.eliminarUsuario(usu.getId());

            modelo.put("mensaje", "Ha eliminado exitosamente");

        } catch (ErrorService e) {
            modelo.addAttribute("error", e.getMessage());

            return "redirec:/miperfil";
        }

        return "redirect:/logout";

    }

}
