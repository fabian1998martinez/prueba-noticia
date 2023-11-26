
package com.prueba.controladores;

import com.prueba.Excepcion.MyExcepcion;
import com.prueba.entidades.Noticia;
import com.prueba.servicios.NoticiaServicios;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller

@RequestMapping("/Noticia")
public class NoticiaControlador {
    @Autowired
    private NoticiaServicios noticiaServicio;
    
    @GetMapping("/registrar")
    public String registrar(ModelMap modelo ){
          List<Noticia> noticias = noticiaServicio.listaNoticia();
          
          modelo.addAttribute("noticias", noticias);
         
         return "form_noticia.html";
    }
    
    @PostMapping("/registro")
    public String registro(@RequestParam(required=false) String titulo,
            @RequestParam(required=false) String cuerpo,
            @RequestParam(required=false) String id
            ,ModelMap modelo
            ){
        try {
            noticiaServicio.crearNoticia(titulo, cuerpo);
            modelo.put("exito","La noticia fue cargada exitosamente");
        } catch (MyExcepcion ex) {
            List<Noticia> noticias = noticiaServicio.listaNoticia();

            modelo.addAttribute("noticias", noticias);
            modelo.put("error", ex.getMessage());
            Logger.getLogger(NoticiaControlador.class.getName()).log(Level.SEVERE, null, ex);
            return "form_noticia.html";
        }
        
         return "index.html";            
    }
    
    @GetMapping("/lista")
    public String Listar(ModelMap modelo){
    
        List<Noticia> lista = noticiaServicio.listaNoticia();
        modelo.addAttribute("lista", lista);
        return "Listados.html";
    }
    
    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable String id,ModelMap modelo){
           
        modelo.put("noticia", noticiaServicio.getOne(id));
        
        return "noticia_actualizar.html";
    }

    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable String id,
            @RequestParam String titulo,
            @RequestParam String cuerpo,
            ModelMap modelo) {

        try {
            noticiaServicio.modificarNoticia(id, titulo, cuerpo);
            return "redirect:../lista";
        } catch (MyExcepcion ex) {
            Logger.getLogger(NoticiaControlador.class.getName()).log(Level.SEVERE, null, ex);
            modelo.put("error", ex.getMessage());
            return "noticia_actualizar.html";
        }
    }
    
}
