/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.general.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 *
 * @author gibrandemetrioo
 */
@Entity
@Table(name = "asociados")
public class Asociado implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @Column(nullable = false, length = 65)
    private String clave;
    @Size(min = 10, max = 12)
    @Column(length = 12)
    private String telefono;
    @Column(nullable = false, length = 23)
    private String status;
    @Column(length = 200)
    private String calle;
    @Column(length = 200)
    private String colonia;
    @Column(length = 200)
    private String municipio;
    private String nombre;
    private String apellidop;
    private String apellidom;
    private String username;

    public Asociado() {
    }

    public Asociado(String clave, String telefono, String status, String calle, String colonia, String municipio) {
        this.clave = clave;
        this.telefono = telefono;
        this.status = status;
        this.calle = calle;
        this.colonia = colonia;
        this.municipio = municipio;
    }

    public Asociado(Long id, String username, String nombre, String apellidop, String apellidom, String status, String clave, String telefono, String calle, String colonia, String municipio){
    this.id=id;
    this.username=username;
    this.nombre=nombre;
    this.apellidop=apellidop;
    this.apellidom=apellidom;
    this.status=status;
    this.clave=clave;
    this.telefono=telefono;
    this.calle = calle;
    this.colonia = colonia;
    this.municipio = municipio;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getApellidom() {
        return apellidom;
    }

    public void setApellidom(String apellidom) {
        this.apellidom = apellidom;
    }

    public String getApellidop() {
        return apellidop;
    }

    public void setApellidop(String apellidop) {
        this.apellidop = apellidop;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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