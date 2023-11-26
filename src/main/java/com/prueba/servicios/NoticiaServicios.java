
package com.prueba.servicios;

import com.prueba.Excepcion.MyExcepcion;
import com.prueba.entidades.Noticia;
import com.prueba.repositorios.NoticiaRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoticiaServicios {
    
    @Autowired
    private NoticiaRepositorio noticiaRepositorio;
    
    @Transactional
    public void crearNoticia(String titulo,String cuerpo) throws MyExcepcion{
      validarDatos(titulo, cuerpo);
              
      Noticia noticia = new Noticia();
      
      noticia.setTitulo(titulo);
      noticia.setCuerpo(cuerpo);
      noticia.setAlta(true);
      
      noticiaRepositorio.save(noticia);
       }
    public void modificarNoticia(String id,String titulo,String cuerpo) throws MyExcepcion{
         validarDatos(titulo, cuerpo);
         
        Optional<Noticia> respuesta = noticiaRepositorio.findById(id);
        if(respuesta.isPresent()){
          Noticia noticia = respuesta.get();
          
          noticia.setTitulo(titulo);
          noticia.setCuerpo(cuerpo);
      
          noticiaRepositorio.save(noticia);
          
        }
        
    }
    
    public void validarDatos(String titulo,String cuerpo) throws MyExcepcion {
       if(titulo == null || titulo.isEmpty()){
          throw new MyExcepcion("El titulo no puede estar vacio o nulo");
       }
       if(cuerpo == null || cuerpo.isEmpty()){
         throw new MyExcepcion("El cuerpo no puede estar vacio o nulo");
       }
    
    }
    
    public void darBaja(String id){
         Optional<Noticia> opcion = noticiaRepositorio.findById(id);
         if(opcion.isPresent()){
            Noticia noticia = opcion.get();
             noticia.setAlta(false);
         }
    }
    
    public Noticia getOne(String id){
       return noticiaRepositorio.getOne(id);
    }
    
    public List<Noticia> listaNoticia(){
   
     List<Noticia> listaDeNoticia = new ArrayList();
           
     listaDeNoticia = noticiaRepositorio.findAll();
     
     return listaDeNoticia;
     
}
}
   