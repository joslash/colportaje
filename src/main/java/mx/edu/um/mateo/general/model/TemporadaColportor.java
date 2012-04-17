/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.general.model;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
/**
 *
 * @author gibrandemetrioo
 */
@Entity
@Table (name = "temporadacolportores")
public class TemporadaColportor implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @Temporal(TemporalType.DATE)
    @Column(nullable = false, name = "fecha")
    private Date fecha;
    @NotNull
    @Column(nullable = false, length = 56, name ="status")
    private String status;
    @NotBlank
    @Column(nullable = false, length = 65)
    private String objetivo;
    @NotBlank
    @Column(nullable = false, length = 300)
    private String observacion;
    @ManyToOne
    private Colportor colportor;
    @ManyToOne
    private Asociacion asociacion;
    @ManyToOne
    private Asociado asociado;
    @ManyToOne
    private Temporada temporada;
    @ManyToOne
    private Union union;

    public TemporadaColportor() {
    }
    
    public TemporadaColportor(Colportor colportor,Asociacion asociacion,Asociado asociado,Temporada temporada,Union union){
        this.colportor = colportor;
        this.asociacion = asociacion;
        this.asociado = asociado;
        this.temporada = temporada;
        this.union = union;
        
    }
    public TemporadaColportor(String status,String objetivo, String observacion){
        this.status = status;
        this.objetivo = objetivo;
        this.observacion = observacion;
        Date f = new Date();
        this.fecha = f;
    }

    public Asociacion getAsociacion() {
        return asociacion;
    }

    public void setAsociacion(Asociacion asociacion) {
        this.asociacion = asociacion;
    }

    public Asociado getAsociado() {
        return asociado;
    }

    public void setAsociado(Asociado asociado) {
        this.asociado = asociado;
    }

    public Colportor getColporto() {
        return colportor;
    }

    public void setColporto(Colportor colportor) {
        this.colportor = colportor;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Temporada getTemporada() {
        return temporada;
    }

    public void setTemporada(Temporada temporada) {
        this.temporada = temporada;
    }

    public Union getUnion() {
        return union;
    }

    public void setUnion(Union union) {
        this.union = union;
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
        final TemporadaColportor other = (TemporadaColportor) obj;
        if (!Objects.equals(this.status, other.status)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.status);
        return hash;
    }

    @Override
    public String toString() {
        return "TemporadaColportor{" + "status=" + status + '}';
    }
    
    
    
}