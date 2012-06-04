/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.general.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List; 
import java.util.Map;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.general.model.Asociacion;
import mx.edu.um.mateo.general.model.Asociado;
import mx.edu.um.mateo.general.model.Colportor;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
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

    public AsociadoDao() {
        log.info("Se ha creado una nueva AsociadoDao");
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de asociado con params {}", params);
              if (params == null) {
            params = new HashMap<>();
        }

        if (!params.containsKey("max")) {
            params.put("max", 10);
        } else {
            params.put("max", Math.min((Integer) params.get("max"), 100));
        }

        if (params.containsKey("pagina")) {
            Long pagina = (Long) params.get("pagina");
            Long offset = (pagina - 1) * (Integer) params.get("max");
            params.put("offset", offset.intValue());
        }

        if (!params.containsKey("offset")) {
            params.put("offset", 0);
        }

        if (!params.containsKey("asociacion")) {
            params.put("asociados", new ArrayList());
            params.put("cantidad", 0L);

            return params;
        }
        
        Criteria criteria = currentSession().createCriteria(Asociado.class);
        Criteria countCriteria = currentSession().createCriteria(Asociado.class);

        if (params.containsKey("asociacion")) {
            log.debug("valor de asociacion"+params.get("asociacion"));
            criteria.createCriteria("asociacion").add(Restrictions.eq("id",((Asociacion)params.get("asociacion")).getId()));
            countCriteria.createCriteria("asociacion").add(Restrictions.eq("id",((Asociacion)params.get("asociacion")).getId()));
        }

        if (params.containsKey("filtro")) {
            String filtro = (String) params.get("filtro");
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("username", filtro, MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("nombre", filtro, MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("apellido", filtro, MatchMode.ANYWHERE));
            criteria.add(propiedades);
            countCriteria.add(propiedades);
        }

        if (params.containsKey("order")) {
            String campo = (String) params.get("order");
            if (params.get("sort").equals("desc")) {
                criteria.addOrder(Order.desc(campo));
            } else {
                criteria.addOrder(Order.asc(campo));
            }
        }

        if (!params.containsKey("reporte")) {
            criteria.setFirstResult((Integer) params.get("offset"));
            criteria.setMaxResults((Integer) params.get("max"));
        }
        params.put("asociados", criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }

    public Asociado obtiene(Long id) {
        log.debug("Obtiene cuenta de asociado con id = {}", id);
        Asociado asociado = (Asociado) currentSession().get(Asociado.class, id);
        return asociado;
    }
    
    public Object obtienePorUsuario(Long id) {
        log.debug("Obtiene cuenta de asociado con id = {}", id);
        String hql = "SELECT "
                + "u.id, a.status, a.clave, a.telefono, a.calle, a.colonia, a.municipio "
                + "FROM "
                + "usuarios u, roles r, usuarios_roles ur, asociados a "
                + "WHERE "
                + "u.asociado_id = :id AND ur.rol_id = r.id AND r.authority = 'ROLE_ASO'";
        Query query = currentSession().createSQLQuery(hql);
        query.setLong("id", id);
        return query.uniqueResult();
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
        Asociado nueva = (Asociado) currentSession().get(Asociado.class, asociado.getId());
        //actualiza el objeto
        BeanUtils.copyProperties(asociado, nueva);
        //lo guarda en la BD
        currentSession().update(nueva);
        currentSession().flush();
        return nueva;
    }

    public String elimina(Long id) {
        log.debug("Eliminando cuenta de asociado con id {}", id);
        Asociado asociado = obtiene(id);
        currentSession().delete(asociado);
        currentSession().flush();
        String clave = asociado.getClave();
        return clave;
    }
}
