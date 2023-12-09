
package com.prueba.servicios;

import com.prueba.Excepcion.MyExcepcion;
import com.prueba.entidades.Imagen;
import com.prueba.entidades.Usuario;
import com.prueba.enumeracion.Rol;
import com.prueba.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioServicio implements UserDetailsService{
    
    
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    
    @Autowired
    private ImagenServicios imagenServicio;
    
    @Transactional
    public void registrar(MultipartFile archivo,String nombre , String email , String password , String password2) throws MyExcepcion{
           
        validar(nombre ,email ,password ,password2);
        
        Usuario usuario = new Usuario();
        
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setPasswork(new BCryptPasswordEncoder().encode(password));
        
        usuario.setRol(Rol.USER);
        
        Imagen imagen = imagenServicio.guardar(archivo);
        
        usuario.setImagen(imagen);
        
        usuarioRepositorio.save(usuario);
    
    }
    
        @Transactional
    public void actualizar(MultipartFile archivo,String idUsuario,String nombre , String email , String password , String password2) throws MyExcepcion{
           
        validar(nombre ,email ,password ,password2);
        
        Optional<Usuario> respuesta = usuarioRepositorio.findById(idUsuario);
        
        if(respuesta.isPresent()){
        
         Usuario usuario = respuesta.get();
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setPasswork(new BCryptPasswordEncoder().encode(password));
        
        usuario.setRol(Rol.USER);
        
        String idImagen = null;
        
        if(usuario.getImagen() != null){
        
            idImagen = usuario.getImagen().getId();
        
        }
        
        Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
        
        usuario.setImagen(imagen);
        
        usuarioRepositorio.save(usuario);
    
        }
        
       
        
    }
    private void validar(String nombre , String email , String password , String password2) throws MyExcepcion{

     if(nombre.isEmpty() || nombre==null){
         throw new MyExcepcion("El nombre no puede estar nulo o vacio !!");
     }
       if(email.isEmpty() || email==null){
         throw new MyExcepcion("El email no puede estar nulo o vacio !!");
     }
         if(password.isEmpty() || password==null || password.length() <= 5){
         throw new MyExcepcion("La contraseña no puede estar nulo o vacio  y debe tener mas 5 digitos!!");
     }
        if(!password.equals(password2)){
         throw new MyExcepcion("La contraseña  ingresado deben ser iguales!!");
     }
    
}

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        
      
       Usuario usuario = usuarioRepositorio.buscarPorEmail(email);

        
        if(usuario != null){
        
            List<GrantedAuthority> permisos = new ArrayList();
            
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_"+usuario.getRol().toString());
            
            permisos.add(p);
            
            
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            
            HttpSession session = attr.getRequest().getSession(true);
            
            session.setAttribute("usuariosesion",usuario);
            
            return new User(usuario.getEmail(),usuario.getPasswork(),permisos);
        
        }else{
        
          return null;
        }
    }

    public Usuario getOne(String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}