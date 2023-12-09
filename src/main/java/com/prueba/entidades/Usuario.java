
package com.prueba.entidades;

import com.prueba.enumeracion.Rol;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;


@Data
@Entity
public class Usuario implements Serializable {
    
    @Id
    @GeneratedValue(generator ="uuid")
    @GenericGenerator(name="uuid",strategy="uuid2")
    private String Id;
    
    private String nombre;
    private String email;
    private String passwork;
    
    @OneToOne
    private Imagen imagen;
    
    @Enumerated(EnumType.STRING)
    private Rol rol;

    public Usuario() {
    }


    
    
    
}
