package com.egg.TuAlmacen.controlador;

import com.egg.TuAlmacen.entidad.Producto;
import com.egg.TuAlmacen.entidad.Usuario;
import com.egg.TuAlmacen.error.ErrorService;
import com.egg.TuAlmacen.service.ProductoService;
import com.egg.TuAlmacen.service.UsuarioService;
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
    private HttpSession session;

//    @Autowired
//    public Rol rol;
    @PreAuthorize("hasRole('ROLE_USUARIO')||hasRole('ROLE_ADMIN')")
    @GetMapping("/inicio")
    public String inicio(ModelMap modelo) throws ErrorService {

        List<Producto> productos = productoService.listarProducto();

        modelo.put("productos", productos);

        return "inicio.html";
    }

    @PreAuthorize("hasRole('ROLE_USUARIO')||hasRole('ROLE_ADMIN')")
    @GetMapping("/miperfil")
    public String miPerfil(ModelMap modelo) {

        modelo.put("usuario", session.getAttribute("usuariosession").toString());

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
