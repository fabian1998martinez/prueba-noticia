    
package com.prueba.controladores;

import com.prueba.Excepcion.MyExcepcion;
import com.prueba.entidades.Usuario;
import com.prueba.servicios.UsuarioServicio;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping("/")
public class PortalControlador {
    
    @Autowired
    private UsuarioServicio usuarioServicio;
    
    @GetMapping("/")
          public String index() {
                  return "index.html";
                      }
    @GetMapping("/registrar")
          public String registrar() {
                  return "registrar.html";
                      }   
          
    @PostMapping("/registro")
    public String registro(@RequestParam String nombre,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String password2,
            @RequestParam MultipartFile archivo,
            ModelMap modelo ){
    
        try {
            usuarioServicio.registrar(archivo,nombre, email, password, password2);
            
            modelo.put("exito","Usuario registrado exitosamente!!");
            
            return "index.html";
        } catch (MyExcepcion ex) {
            Logger.getLogger(PortalControlador.class.getName()).log(Level.SEVERE, null, ex);
             modelo.put("error",ex.getMessage());
             modelo.put("nombre", nombre);
             modelo.put("email", email);
             return "registrar.html";
             
        }
    }
          
     @GetMapping("/login")
          public String login(@RequestParam (required = false) String error, ModelMap modelo ) {
              
              if(error != null){
                modelo.put("error","Usuario o Contraseña invalidos !");
               
              }
                  return "login.html";
          }
      
     @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
     @GetMapping("/inicio")
           public String inicio( HttpSession session){
               
               Usuario logeado = (Usuario) session.getAttribute("usuariosesion");
               
               if(logeado.getRol().toString().equals("ADMIN")){
                  
                   return "redirect:/admin/dashboard";
               
               }
           
           return "inicio.html";
           
           } 
           
      @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
     @GetMapping("/perfil")
      public String perfil(ModelMap modelo,HttpSession session){
         
          Usuario usuario = (Usuario) session.getAttribute("usuariosesion");
          
          modelo.put("usuario", usuario);
      
          return "usuario_modificar.html";
      }
      
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')") 
    @PostMapping("/perfil/{id}")
    public String actualizar(MultipartFile archivo,@PathVariable String id , @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String password2,
            ModelMap modelo ){
            
             try {
               
                 usuarioServicio.actualizar(archivo,id, nombre, email, password, password2);
                 
                 modelo.put("exito", "Usuario Actualizado Correctamente");
                 
                 return "inicio.html";
           } catch (Exception e) {
               modelo.put("error", e.getMessage());
               modelo.put("nombre",nombre);
                 modelo.put("email",email);
                 return "usuario_modificar.html";
               
           }
             
    
    }
}
