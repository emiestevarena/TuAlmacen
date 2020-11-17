package com.egg.TuAlmacen.controlador;

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
 * @author octav
 */
@Controller
@RequestMapping("/")
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String login(ModelMap modelo, @RequestParam(required=false) String error) {
        if (!error.isEmpty() && error != null) {
            modelo.put("error", error);
        }
        return "login.html";
    }
    
}
