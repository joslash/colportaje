/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.general.dao;

import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.general.model.Asociacion;
import mx.edu.um.mateo.general.utils.UltimoException;
import mx.um.edu.mateo.Constantes;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author gibrandemetrioo
 */
@Repository
@Transactional
public class AsociacionDao {
    
    private static final Logger log = LoggerFactory.getLogger(AsociacionDao.class);
    @Autowired
    private SessionFactory sessionFactory;
    
    public AsociacionDao () {
        log.info("Se ha creado una nueva AsociacionDao");
    }
    
    
    
    private Session currentSession () {
        return sessionFactory.getCurrentSession();
    }
    
    
    
    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de asociacion con params {}", params);
        if (params == null) {
            params = new HashMap<>();
        }

        if (!params.containsKey(Constantes.CONTAINSKEY_MAX)) {
            params.put(Constantes.CONTAINSKEY_MAX, 10);
        } else {
            params.put(Constantes.CONTAINSKEY_MAX, Math.min((Integer) params.get(Constantes.CONTAINSKEY_MAX), 100));
        }

        if (params.containsKey(Constantes.CONTAINSKEY_PAGINA)) {
            Long pagina = (Long) params.get(Constantes.CONTAINSKEY_PAGINA);
            Long offset = (pagina - 1) * (Integer) params.get(Constantes.CONTAINSKEY_MAX);
            params.put(Constantes.CONTAINSKEY_OFFSET, offset.intValue());
        }

        if (!params.containsKey(Constantes.CONTAINSKEY_OFFSET)) {
            params.put(Constantes.CONTAINSKEY_OFFSET, 0);
        }
        
        Criteria criteria = currentSession().createCriteria(Asociacion.class);
        Criteria countCriteria = currentSession().createCriteria(Asociacion.class);
        
        if (params.containsKey(Constantes.CONTAINSKEY_FILTRO)) {
            String filtro = (String) params.get(Constantes.CONTAINSKEY_FILTRO);
            filtro = "%" + filtro + "%";
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("nombre", filtro));
            propiedades.add(Restrictions.ilike("status", filtro));
           
           
            criteria.add(propiedades);
            countCriteria.add(propiedades);
        }

        if (params.containsKey(Constantes.CONTAINSKEY_ORDER)) {
            String campo = (String) params.get(Constantes.CONTAINSKEY_ORDER);
            if (params.get(Constantes.CONTAINSKEY_SORT).equals(Constantes.CONTAINSKEY_SORT)) {
                criteria.addOrder(Order.desc(campo));
            } else {
                criteria.addOrder(Order.asc(campo));
            }
        }

        if (!params.containsKey(Constantes.CONTAINSKEY_REPORTE)) {
            criteria.setFirstResult((Integer) params.get(Constantes.CONTAINSKEY_OFFSET));
            criteria.setMaxResults((Integer) params.get(Constantes.CONTAINSKEY_MAX));
        }
        params.put(Constantes.CONTAINSKEY_ASOCIACIONES, criteria.list());
        countCriteria.setProjection(Projections.rowCount());
        params.put(Constantes.CONTAINSKEY_CANTIDAD, (Long) countCriteria.list().get(0));

        return params;
    }

    public Asociacion obtiene(Long id) {
        log.debug("Obtiene cuenta de asociacion con id = {}", id);
        Asociacion asociacion = (Asociacion) currentSession().get(Asociacion.class, id);
        return asociacion;
    }

    public Asociacion crea(Asociacion asociacion) {
        log.debug("Creando cuenta de asociacion : {}", asociacion);
        currentSession().save(asociacion);
        currentSession().flush();
        return asociacion;
    }

     public Asociacion actualiza(Asociacion asociacion) {
        log.debug("Actualizando cuenta de asociacion {}", asociacion);
        
        //trae el objeto de la DB 
        Asociacion nueva = (Asociacion)currentSession().get(Asociacion.class, asociacion.getId());
        //actualiza el objeto
        BeanUtils.copyProperties(asociacion, nueva);
        //lo guarda en la BD
        
        currentSession().update(nueva);
        currentSession().flush();
        return nueva;
    }

    public String elimina(Long id) throws UltimoException {
        log.debug("Eliminando cuenta de asociacion con id {}", id);
        Asociacion asociacion = obtiene(id);
        currentSession().delete(asociacion);
        currentSession().flush();
        String nombre = asociacion.getNombre();
        return nombre;
    }
    
  }



