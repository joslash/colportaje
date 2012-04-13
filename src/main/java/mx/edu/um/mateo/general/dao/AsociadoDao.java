/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.general.dao;

import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.general.model.Asociado;
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
public class AsociadoDao {
   private static final Logger log = LoggerFactory.getLogger(AsociadoDao.class);
    @Autowired
    private SessionFactory sessionFactory;
    
    public AsociadoDao () {
        log.info("Se ha creado una nueva AsociadoDao");
    }
    
    
    
    private Session currentSession () {
        return sessionFactory.getCurrentSession();
    }
    
    
    
    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de asociado con params {}", params);
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
        
        Criteria criteria = currentSession().createCriteria(Asociado.class);
        Criteria countCriteria = currentSession().createCriteria(Asociado.class);
        
        if (params.containsKey(Constantes.CONTAINSKEY_FILTRO)) {
            String filtro = (String) params.get(Constantes.CONTAINSKEY_FILTRO);
            filtro = "%" + filtro + "%";
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("nombre", filtro));
            propiedades.add(Restrictions.ilike("clave", filtro));
            propiedades.add(Restrictions.ilike("direccion", filtro));
            propiedades.add(Restrictions.ilike("telefono", filtro));
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
        params.put(Constantes.CONTAINSKEY_ASOCIADOS, criteria.list());
        countCriteria.setProjection(Projections.rowCount());
        params.put(Constantes.CONTAINSKEY_CANTIDAD, (Long) countCriteria.list().get(0));

        return params;
    }

    public Asociado obtiene(Long id) {
        log.debug("Obtiene cuenta de asociado con id = {}", id);
        Asociado asociado = (Asociado) currentSession().get(Asociado.class, id);
        return asociado;
    }

    public Asociado crea(Asociado asociado) {
        log.debug("Creando cuenta de asociado : {}", asociado);
        currentSession().save(asociado);
        currentSession().flush();
        return asociado;
    }

     public Asociado actualiza(Asociado asociado) {
        log.debug("Actualizando cuenta de asociado {}", asociado);
        
        //trae el objeto de la DB 
        Asociado nueva = (Asociado)currentSession().get(Asociado.class, asociado.getId());
        //actualiza el objeto
        BeanUtils.copyProperties(asociado, nueva);
        //lo guarda en la BD
        
        currentSession().update(nueva);
        currentSession().flush();
        return nueva;
    }

    public String elimina(Long id) throws UltimoException {
        log.debug("Eliminando cuenta de asociado con id {}", id);
        Asociado asociado = obtiene(id);
        currentSession().delete(asociado);
        currentSession().flush();
        String nombre = asociado.getNombre();
        return nombre;
    }
    
  }


