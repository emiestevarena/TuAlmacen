package com.egg.TuAlmacen.controlador;

import com.egg.TuAlmacen.service.UsuarioService;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    private HttpSession session;
    
//    @Autowired
//    public Rol rol;
    
    @PreAuthorize("hasRole('ROLE_USUARIO')||hasRole('ROLE_ADMIN')")
    @GetMapping("/inicio")
    public String inicio(ModelMap modelo){
    	
        return "inicio.html";
    }
}
