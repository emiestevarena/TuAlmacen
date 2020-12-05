/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.TuAlmacen.controlador;


import com.egg.TuAlmacen.enums.Rol;
import com.egg.TuAlmacen.error.ErrorService;
import com.egg.TuAlmacen.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
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
public class RegistroController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @GetMapping("/registro_cliente")
    public String registroCliente(ModelMap modelo){
        return "registro_cliente.html";
    }
    
    @GetMapping("/registro_admin")
    public String registroAdmin(ModelMap modelo){
        return "registro_admin.html";
    }
    
    @PostMapping("/registrar_cliente")
    public String registrarCliente(ModelMap modelo,
                                   @RequestParam(required=true) String usuario,
                                   @RequestParam(required=true) String password,
                                   @RequestParam(required=true) String email,
                                   @RequestParam(required=true) String password_confirmation) throws ErrorService{
        try{
            usuarioService.registrarUsuario(usuario,email,password,password_confirmation,Rol.USUARIO);
//            modelo.put("ok","alta exitosa");
        }catch(ErrorService ex){
            modelo.put("error",ex.getMessage());
            return this.registroCliente(modelo);
        }
        return "redirect:/login?ok=alta exitosa";
    }
    
    @PostMapping("/registrar_admin")
    public String registrarAdmin(ModelMap modelo,
                                   @RequestParam(required=true) String nombre,
                                   @RequestParam(required=true) String password,
                                   @RequestParam(required=true) String email,
                                   @RequestParam(required=true) String password_confirmation)throws ErrorService{
        try{
             usuarioService.registrarUsuario(nombre,email,password,password_confirmation,Rol.ADMIN);
        }catch(ErrorService ex){
            modelo.put("error",ex.getMessage());
            return this.registroAdmin(modelo);
        }
        return "redirect:/login?ok=alta exitosa";
    }
}
