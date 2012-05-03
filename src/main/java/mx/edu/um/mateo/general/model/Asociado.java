/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.general.model;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import mx.edu.um.mateo.general.model.TipoAsociado;
/**
 *
 * @author gibrandemetrioo
 */

@Entity

@Table (name = "asociados")
public  class Asociado  implements Serializable{
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @NotBlank
    @Column(nullable = false, length = 65 ,unique=true)
    private String clave;
    @Column(length = 15)
    private String telefono;
    @NotNull
    @Column(nullable = false, length = 23, name ="status")
    private String status;
    @NotNull
    @Column(length = 200)
    private String calle;
    @NotNull
    @Column(length = 200)
    private String colonia;
    @NotNull
    @Column(length = 200)
    private String municipio;
    
    
    
    public Asociado() {
    }

    public Asociado( String clave,  String telefono, String status,String calle,String colonia,String municipio) {
        this.clave = clave;
        this.telefono = telefono;
        this.status = status;
        this.calle = calle;
        this.colonia = colonia;
        this.municipio = municipio;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
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


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

   
    

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Asociado other = (Asociado) obj;
        if (!Objects.equals(this.status, other.status)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.status);
        return hash;
    }

    @Override
    public String toString() {
        return "Asociado{" + "clave=" + clave + ", telefono=" + telefono + ", status=" + status + ", calle=" + calle + ", colonia=" + colonia + ", municipio=" + municipio + '}';
    }

    

      
}