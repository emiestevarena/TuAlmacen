/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.TuAlmacen.controlador;

import com.egg.TuAlmacen.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author emiliano
 */
@Controller
@RequestMapping("/")
public class IndexController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    //@Autowired
    //private ProductoService productoService
    
    @GetMapping("/")
    public String index(ModelMap modelo){
        Long count = usuarioService.count();
        modelo.put("count",count);
        //List<Producto> productos = productoService.findAll();
        //modelo.put("productos",productos);
        return "index.html";
    }
}
