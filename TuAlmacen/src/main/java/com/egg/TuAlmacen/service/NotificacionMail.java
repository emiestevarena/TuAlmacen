package com.egg.TuAlmacen.service;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.egg.TuAlmacen.entidad.Usuario;



@Service
public class NotificacionMail {

    @Autowired
    private JavaMailSender mailSender;

  
    
    @Async("enviar")
    public void enviar(String cuerpo,String titulo, String mail){
         
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setFrom("tualmacenegg@gmail.com");
        mensaje.setTo(mail);
        mensaje.setSubject(titulo);
        mensaje.setText(cuerpo);
        mailSender.send(mensaje);
    }
    @Async("enviar")
    public void enviarr(SimpleMailMessage mensaje){
        mailSender.send(mensaje);
    }
    public void constructResetTokenEmail(String contextPath, Locale locale, String token, Usuario usuario) {
    	
        String url = contextPath + "/user/changePassword/" + token;
        String message = "Para cambiar su contrasena ingrese al siguiente link";
        
        this.enviar(message+" "+url, "Resetar contrasena", usuario.getEmail());   
        
        //return constructEmail("Reset Password", message + " \r\n" + url, usuario);
    }
     
    private SimpleMailMessage constructEmail(String subject, String body, Usuario usuario) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(usuario.getEmail());
        email.setFrom("tualmacenegg@gmail.com");
        return email;
    }
        
}

