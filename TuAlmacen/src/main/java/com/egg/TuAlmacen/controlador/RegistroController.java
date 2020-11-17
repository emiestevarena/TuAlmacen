/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.TuAlmacen.controlador;

import com.egg.TuAlmacen.entidad.*;
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
    
    //@Autowired
    //private UsuarioService usuarioService
    
    @Autowired
    public Rol rol;
    
    @GetMapping("/registro_cliente")
    public String registroCliente(ModelMap modelo,@RequestParam(required=false) String error, @RequestParam(required=false) String OK){
        if(error!=null&& !error.isEmpty()) modelo.put("error",error);
        if(OK!=null&& !OK.isEmpty()) modelo.put("OK",OK);
        return "registro_cliente.html";
    }
    
    @GetMapping("/registro_admin")
    public String registroAdmin(ModelMap modelo,@RequestParam(required=false) String error, @RequestParam(required=false) String OK){
        if(error!=null&& !error.isEmpty()) modelo.put("error",error);
        if(OK!=null&& !OK.isEmpty()) modelo.put("OK",OK);
        return "registro_admin.html";
    }
    @PostMapping("/registrar_cliente")
    public String registrarCliente(ModelMap modelo,
                                   @RequestParam(required=true) String nombre,
                                   @RequestParam(required=true) String password,
                                   @RequestParam(required=true) String email){
        try{
            //usuarioService.alta(nombre,password,email,rol.USUARIO);
            modelo.put("OK","alta exitosa");
        }catch(Exception ex){
            modelo.put("error",ex.getMessage());
        }
        return "redirect:/registro_cliente";
    }
    
    @PostMapping("/registrar_admin")
    public String registrarAdmin(ModelMap modelo,
                                   @RequestParam(required=true) String nombre,
                                   @RequestParam(required=true) String password,
                                   @RequestParam(required=true) String email){
        try{
            //usuarioService.alta(nombre,password,email,rol.ADMIN);
            modelo.put("OK","alta exitosa");
        }catch(Exception ex){
            modelo.put("error",ex.getMessage());
        }
        return "redirect:/registro_admin";
    }
}
