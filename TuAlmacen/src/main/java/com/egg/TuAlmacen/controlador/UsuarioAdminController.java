/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.TuAlmacen.controlador;

import com.egg.TuAlmacen.entidad.Usuario;
import com.egg.TuAlmacen.enums.Rol;
import com.egg.TuAlmacen.error.ErrorService;
import com.egg.TuAlmacen.service.UsuarioService;
import java.util.List;
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
 * @author emiliano
 */
@Controller
@RequestMapping("/")
public class UsuarioAdminController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/usuarios")
    public String usuarios(ModelMap modelo,
                           @RequestParam(required=false) String error,
                           @RequestParam(required=false) String ok){
        List<Usuario> usuarios = usuarioService.findAll();
        modelo.put("usuarios",usuarios);
        if(error!=null){modelo.put("error", error);}
        if(ok!=null){modelo.put("ok", ok);}
        return "usuarios.html";
    }
    
    @PostMapping("/altausuario")
    public String altaUsuario(ModelMap modelo,
                              @RequestParam(required=true) String usuario,
                              @RequestParam(required=true) String email,
                              @RequestParam(required=true) String password,
                              @RequestParam(required=true) String repetir,
                              @RequestParam(required=true) String rol)throws ErrorService{
        try{
            if(rol.equals("usuario")){ 
                usuarioService.registrarUsuario(usuario, email, password, repetir, Rol.USUARIO);
            }else{
                usuarioService.registrarUsuario(usuario, email, password, repetir, Rol.ADMIN);}
            modelo.put("ok", "alta exitosa");
        }catch(ErrorService ex){
            modelo.put("error",ex.getMessage());
        }
        return "redirect:/usuarios";
    }
    
    @PostMapping("/modificarusuario")
    public String modificarUsuario(ModelMap modelo,
                              @RequestParam(required=true) String id,
                              @RequestParam(required=false) String usuario,
                              @RequestParam(required=false) String email,
                              @RequestParam(required=false) String password,
                              @RequestParam(required=false) String repetir,
                              @RequestParam(required=false) String rol)throws ErrorService{
        try{
            Usuario u = usuarioService.buscarPorId(id);
            if(usuario!=null&&!usuario.isEmpty()){u.setUsuario(usuario);}
            if(email!=null&&!email.isEmpty()){u.setEmail(email);}
            if(password!=null&&!password.isEmpty()){u.setPassword(password);}
            if(rol.equals("usuario")){u.setRol(Rol.USUARIO);}
            if(rol.equals("admin")){u.setRol(Rol.ADMIN);}
            if(repetir!=null && !repetir.isEmpty() && repetir!=password){throw new ErrorService("las contraseñas no coinciden");}
            usuarioService.modificarUsuario(u);
            
            modelo.put("ok", "modificación exitosa");
        }catch(ErrorService ex){
            modelo.put("error",ex.getMessage());
        }
        return "redirect:/usuarios";
    }
    
    @PostMapping("/bajausuario")
    public String bajaUsuario(ModelMap modelo,@RequestParam(required=true) String id)throws ErrorService{
        try{
            usuarioService.eliminarUsuario(id);
            modelo.put("ok", "baja exitosa");
        }catch(ErrorService ex){
              modelo.put("error",ex.getMessage());
        }
        return "redirect:/usuarios";
    }
}
