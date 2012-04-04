/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.general.model;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Email;

/**
 *
 * @author wilbert
 */
@Entity
@Table(name="colportores")
public class Colportor implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @NotNull
    @Column(nullable = false, length = 64)
    private String nombre;
    @NotNull
    @Column(nullable = false, length = 2, name = "status")
    private String status;
    @NotNull
    @Column(unique = true, nullable = false, length = 64)
    private String clave;
    @Column(length = 500)
    private String direccion;
    @Email
    @Column(length = 128)
    private String correo;
    @Column(length = 25)
    private String telefono;
  /*DE AQUI  
    private Set<TipoColportor> tipoColportor = new HashSet<TipoColportor>();  
  
@ElementCollection 
@Enumerated(EnumType.STRING)  
public Set<TipoColportor> getTipoColportors() {  
    return tipoColportor;  
}      
  
public void setTipoColportors(Set<TipoColportor> tipoColportor) {  
    this.tipoColportor = tipoColportor;  
}  
 */      
    public Colportor() {
    }
      
      public Colportor(String nombre, String status, String clave,String direccion,String correo, String telefono){
          this.nombre = nombre;
          this.status = status;
          this.clave  = clave;
          this.direccion = direccion;
          this.correo = correo;
          this.telefono = telefono;
      }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

   

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Colportor other = (Colportor) obj;
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.nombre);
        return hash;
    }

    @Override
    public String toString() {
        return "Colportor{" + "nombre=" + nombre + ", status=" + status + ", clave=" + clave + ", direccion=" + direccion + ", correo=" + correo + ", telefono=" + telefono + '}';
    }



      
}
