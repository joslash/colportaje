/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.general.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.*;
import javax.validation.constraints.NotNull;


/**
 *
 * @author wilbert
 */
@Entity
@Table(name = "colportores")
public class Colportor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
  
    @NotNull
    @Column(nullable = false, length = 15)
    private String tipoDeColportor;
    @Column(length = 20)
    private String matricula;
    @NotNull
    @Column(nullable = false, length = 2, name = "status")
    private String status;
    @NotNull
    @Column(unique = true, nullable = false, length = 65)
    private String clave;
    @Temporal(TemporalType.DATE)
    @Column(nullable = false, name = "fecha")
    private Date fechaDeNacimiento;
    @Column(length = 200)
    private String calle;
    @Column(length = 200)
    private String colonia;
    @Column(length = 200)
    private String municipio;
    @Column(length = 15)
    private String telefono;
   
    /*
     * DE AQUI private Set<TipoColportor> tipoColportor = new
     * HashSet<TipoColportor>();      *
     * @ElementCollection @Enumerated(EnumType.STRING) public Set<TipoColportor>
     * getTipoColportors() { return tipoColportor; }      *
     * public void setTipoColportors(Set<TipoColportor> tipoColportor) {
     * this.tipoColportor = tipoColportor; }
     */

    public Colportor() {
    }

    public Colportor(String tipoDeColportor,String matricula,String status, String clave, String calle, String colonia, String municipio,String telefono) {
        this.tipoDeColportor=tipoDeColportor;
        this.matricula=matricula;
        this.status = status;
        this.clave = clave;
        this.fechaDeNacimiento = new Date();
        this.calle = calle;
        this.colonia = colonia;
        this.municipio = municipio;
        this.telefono = telefono;
    
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public Date getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(Date fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
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

    public String getTipoDeColportor() {
        return tipoDeColportor;
    }

    public void setTipoDeColportor(String tipoDeColportor) {
        this.tipoDeColportor = tipoDeColportor;
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
        if (!Objects.equals(this.tipoDeColportor, other.tipoDeColportor)) {
            return false;
        }
        if (!Objects.equals(this.matricula, other.matricula)) {
            return false;
        }
        if (!Objects.equals(this.status, other.status)) {
            return false;
        }
        if (!Objects.equals(this.clave, other.clave)) {
            return false;
        }
        if (!Objects.equals(this.calle, other.calle)) {
            return false;
        }
        if (!Objects.equals(this.colonia, other.colonia)) {
            return false;
        }
        if (!Objects.equals(this.municipio, other.municipio)) {
            return false;
        }
        if (!Objects.equals(this.telefono, other.telefono)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.tipoDeColportor);
        hash = 41 * hash + Objects.hashCode(this.matricula);
        hash = 41 * hash + Objects.hashCode(this.status);
        hash = 41 * hash + Objects.hashCode(this.clave);
        hash = 41 * hash + Objects.hashCode(this.calle);
        hash = 41 * hash + Objects.hashCode(this.colonia);
        hash = 41 * hash + Objects.hashCode(this.municipio);
        hash = 41 * hash + Objects.hashCode(this.telefono);
        return hash;
    }

    @Override
    public String toString() {
        return "Colportor{" + "tipoDeColportor=" + tipoDeColportor + ", matricula=" + matricula + ", status=" + status + ", clave=" + clave + ", calle=" + calle + ", colonia=" + colonia + ", municipio=" + municipio + ", telefono=" + telefono + '}';
    }

    

   
    
    
}
