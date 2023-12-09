
package com.prueba.repositorios;

import com.prueba.entidades.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ImagenRepositorio extends JpaRepository<Imagen, String> {
    
}
